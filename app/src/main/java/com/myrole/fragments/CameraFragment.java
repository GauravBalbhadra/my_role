package com.myrole.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.myrole.MainHandler;
import com.myrole.R;
import com.myrole.adapter.MySpeedListAdapter;
import com.myrole.customData.CameraVideoButton;
import com.myrole.databinding.FragmentCameraBinding;
import com.myrole.fragments.viewmodel.CameraViewModel;
import com.myrole.fragments.viewmodel.SharedViewModel;
import com.myrole.listeners.onDialogAction;
import com.myrole.progressBar.ProgressBarListener;
import com.myrole.progressBar.SegmentedProgressBar;
import com.myrole.utilities.TrimSongEvent;
import com.myrole.utilities.TrimSongTask;
import com.picker.gallery.view.PickerActivity;
import com.v9kmedia.v9krecorder.V9kRecorder;
import com.v9kmedia.v9krecorder.utils.V9krecorderutil;
import com.v9kmedia.v9kview.CameraGLView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CameraFragment extends Fragment implements View.OnClickListener, onDialogAction {

    private static final boolean DEBUG = true;	// TODO set false on release

    public static final String TAG = "CameraFragment";

    private boolean recordingEnabled;
    private boolean pauseEnabled;

    private int recordingStatus;
    private ProgressDialog progressDialog;

    private V9kRecorder mRecorder;

    private static final int RECORDING_OFF = 0;
    private static final int RECORDING_ON = 1;
    private static final int RECORDING_RESUMED = 2;
    private static final int RECORDING_PAUSE = 3;

    private static final int REQUEST_VIDEO_PERMISSIONS = 1;

    private static final String[] VIDEO_PERMISSIONS = new String[]{

            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE

    };

    private static int MAX_HEIGHT = 720;
    private static int MAX_WIDTH = 1280;

    private CameraGLView mCameraGLView;

    private ConstraintLayout mCancelDoneButton;

    private RecyclerView mSpeedRecyclerView;

    private FragmentCameraBinding fragmentCameraBinding;

    private MySpeedListAdapter mSpeedAdapter;

    private static final int CAMERA_PERMISSION_CODE = 100;

    private CameraVideoButton mCameraRecordingBtn;
    private LinearLayout mLinearSpeedContainer;
    private SegmentedProgressBar mProgressBar;

    private TextView mGallery;

    private CameraViewModel mCameraViewModel;
    private SharedViewModel mSharedViewModel;

    private OnBackPressedCallback callback;

    private MainHandler mHandler;
    private float mSecondsOfVideo;

    private NavController mNavController;
    private LinearLayout mFlashLayout;
    private LinearLayout mFlipLayout;
    private LinearLayout mSpeedLayout;
    private LinearLayout mTimerLayout;
    private LinearLayout mSelectMusicLayout;
    private TextView mSelectMusicText;
    private AppCompatImageView mCancelButton;
    private boolean prepareSongToPlay;
    private boolean mIsPlaying;

    private SimpleExoPlayer mPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(DEBUG) Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(recordingStatus == RECORDING_ON || recordingStatus == RECORDING_RESUMED || recordingStatus == RECORDING_PAUSE) {
                    showAlertDialog();
                } else {
                    back();
                }
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void back() {
       callback.setEnabled(false);
        requireActivity().getOnBackPressedDispatcher().onBackPressed();
    }

    private void showAlertDialog() {
        new AlertDialogFragment(this, "back").show(getParentFragmentManager(), "discardAlert");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(DEBUG) Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);
        prepareSongToPlay = false;

        if(!hasPermissionsGranted(VIDEO_PERMISSIONS)){
            requestVideoPermissions();
        }

        init();

        mSharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        mSharedViewModel.getSelectedSongName().removeObservers(this);
        mSharedViewModel.getSelectedSongName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged called");
                mSelectMusicText.setText(s);
                prepareSongToPlay = true;
            }

        });

        initializeProgressBar();
        initRecycler();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(DEBUG) Log.d(TAG, "onCreateView: ");
        fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false);
        return fragmentCameraBinding.getRoot();
    }

    @Override
    public void onResume() {
        if(DEBUG) Log.d(TAG, "onResume: ");
        super.onResume();
        mCameraGLView.onResume();
        transparentStatusAndNavigation();
        hideSystemUi();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        if(DEBUG) Log.d(TAG, "onPause: ");
        mCameraGLView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {

        if(DEBUG) Log.d(TAG, "onDestroyView: ");

        if(mRecorder != null) {
            stopRecording();
        }

        recordingEnabled = false;
        pauseEnabled = true;
        recordingStatus = RECORDING_OFF;
        progressDialog = null;

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        if(DEBUG) Log.d(TAG, "onDestroy: ");

        recordingEnabled = false;
        pauseEnabled = true;
        recordingStatus = RECORDING_OFF;

        super.onDestroy();
    }

    private void init() {

        setHeightWidth();

        fragmentCameraBinding.selectMusicLayout.setSelected(true);

        mSelectMusicText = fragmentCameraBinding.selectMusicText;
        mLinearSpeedContainer = fragmentCameraBinding.linearSpeedContainer;
        mCameraRecordingBtn =  fragmentCameraBinding.ivRecord;
        mCancelDoneButton = fragmentCameraBinding.cancelDoneIcon;
        //mCancelButton = fragmentCameraBinding.cancelIcon;
        mGallery = fragmentCameraBinding.tvGallery;
        mFlashLayout = fragmentCameraBinding.flashLayout;
        mFlipLayout = fragmentCameraBinding.flipLayout;
        mSpeedLayout = fragmentCameraBinding.speedLayout;
        mTimerLayout = fragmentCameraBinding.timerLayout;
        mSelectMusicLayout = fragmentCameraBinding.selectMusicLayout;
        mCameraRecordingBtn.enableVideoRecording(true);
        mCameraRecordingBtn.enablePhotoTaking(false);
        mCameraGLView = fragmentCameraBinding.cameraGlview;

        mCameraGLView.setScaleMode(3);
        mCameraGLView.setVideoSize(MAX_WIDTH, MAX_HEIGHT);

        fragmentCameraBinding.flashLayout.setOnClickListener(this);
        fragmentCameraBinding.flipLayout.setOnClickListener(this);
        fragmentCameraBinding.ivClose.setOnClickListener(this);
        fragmentCameraBinding.speedLayout.setOnClickListener(this);
        fragmentCameraBinding.timerLayout.setOnClickListener(this);
        fragmentCameraBinding.ivCloseSpeedContainer.setOnClickListener(this);
        fragmentCameraBinding.tvGallery.setOnClickListener(this);
        fragmentCameraBinding.selectMusicLayout.setOnClickListener(this);
        fragmentCameraBinding.doneIcon.setOnClickListener(this);
        //mCancelButton.setOnClickListener(this);

        mCameraRecordingBtn.setActionListener(new CameraVideoButton.ActionListener() {

            @Override
            public void onResumeRecord() {

            }

            @Override
            public void onPauseRecord() {

            }

            @Override
            public void onStartRecord() {

                showCancel(false);
                hideUi(true);
                updateProgress();
                mProgressBar.resume();

                if(prepareSongToPlay) {

                    if(mIsPlaying) {
                        resumeSong();
                    } else {
                        playSong();
                    }
                }

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        startRecording();
                    }
                });

            }

            @Override
            public void onEndRecord() {
                hideUi(false);
                showCancel(true);
                mProgressBar.pause();
                mProgressBar.addDivider();
                updateProgress();

                if(prepareSongToPlay) {
                    pauseSong();
                }

                pauseRecording();
            }

            @Override
            public void onDurationTooShortError() {
                Toast.makeText(getActivity(), "Duration is too short", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSingleTap() {
                //Toast.makeText(getActivity(), "E", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled() {
                //Toast.makeText(getActivity(), "End rg", Toast.LENGTH_SHORT).show();
            }

        });

        setTimerForRecording();

        mHandler = new MainHandler(getActivity(), this, mCameraGLView);

    }

    public void initializeProgressBar(){

        mProgressBar = fragmentCameraBinding.myProgressBar;

        mProgressBar.enableAutoProgressView(15000);
        mProgressBar.setDividerColor(Color.WHITE);
        mProgressBar.setDividerEnabled(true);
        mProgressBar.setDividerWidth(4);
        mProgressBar.setShader(new int[]{0xFFFF2633, 0xFFFF2633, 0xFFFF2633}); //pinkish red

        mProgressBar.SetListener(new ProgressBarListener() {

            long sec_passed = 0L;
            boolean onced = false;

            @Override
            public void TimeinMill(long mills) {

                if(DEBUG) Log.d(TAG, mills + " sec passed");

                if(!onced) {
                    if(mills >= 15000) {
                        //TODO start_stop_recording()
                        if(DEBUG) Log.d(TAG, "15sec time reached");
                        stopSong();
                        stopRecording();
                        onced = true;
                    }
                    if(DEBUG) Log.d(TAG, "once");
                }
            }
        });
    }

    private void hideUi(boolean shouldHide) {

        if(shouldHide) {
            mGallery.setVisibility(View.INVISIBLE);
            mFlashLayout.setVisibility(View.INVISIBLE);
            mSpeedLayout.setVisibility(View.INVISIBLE);
            mTimerLayout.setVisibility(View.INVISIBLE);
            mFlipLayout.setVisibility(View.INVISIBLE);
            mSelectMusicLayout.setVisibility(View.INVISIBLE);
            //mMixUp.setVisibility(View.INVISIBLE);
        } else {
            mGallery.setVisibility(View.VISIBLE);
            mFlashLayout.setVisibility(View.VISIBLE);
            mSpeedLayout.setVisibility(View.VISIBLE);
            mTimerLayout.setVisibility(View.VISIBLE);
            mFlipLayout.setVisibility(View.VISIBLE);
            //mSelectMusicLayout.setVisibility(View.VISIBLE);
            //mMixUp.setVisibility(View.VISIBLE);
        }

    }

    private void startRecording() {

        if(DEBUG) Log.d(TAG, "startRecording:");

        if(recordingEnabled)
            recordingEnabled = false;

        if(pauseEnabled)
            pauseEnabled = false;

        if(DEBUG) Log.d(TAG, "changePauseState to " + false);

        mCameraGLView.changePauseState(pauseEnabled);

        if(!recordingEnabled) {
            switch(recordingStatus) {
                case RECORDING_OFF:
                    if(DEBUG) Log.d(TAG, "RECORDING_OFF: starting now");

                    mRecorder = new V9kRecorder("movie", MAX_WIDTH, MAX_HEIGHT, mHandler);

                    mRecorder.startRecording();

                    recordingEnabled = mRecorder.isRecording();

                    recordingStatus = RECORDING_ON;

                    if(DEBUG) Log.d(TAG, "recordingEnabled: " + recordingEnabled);

                    break;

                case RECORDING_RESUMED:
                    if(DEBUG) Log.d(TAG, "RECORDING_RESUMED: resuming now");
                    mHandler.onResumed(mRecorder.getVideoEncoder());
                    recordingStatus = RECORDING_ON;
                    break;

                case RECORDING_ON:
                    if(DEBUG) Log.d(TAG, "RECORDING_ON: resuming now after pause");
                    mRecorder.resumeRecording();
                    break;

                default:
                    throw new RuntimeException("unknown status " + recordingStatus);
            }
        } else {
            switch (recordingStatus) {
                case RECORDING_ON:
                case RECORDING_RESUMED:
                    // stop recording
                    if(DEBUG) Log.d(TAG, "STOP recording");
                    mRecorder.stopRecording();
                    recordingStatus = RECORDING_OFF;
                    break;
                case RECORDING_OFF:
                    // yay
                    break;
                default:
                    throw new RuntimeException("unknown status " + recordingStatus);
            }
        }

    }

    private void pauseRecording() {
        if(DEBUG) Log.d(TAG, "pauseRecording: ");
        if(mRecorder != null) {
            mRecorder.pauseRecording();
            mCameraGLView.changePauseState(!pauseEnabled);
        }
    }

    private void stopRecording() {
        if(DEBUG) Log.d(TAG, "stopRecording: ");

        startProgress();

        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(prepareSongToPlay) {
                    Log.d(TAG, "trim selected song");
                    new TrimSongTask().execute(0.0f, (mPlayer.getCurrentPosition()/1000.0f), V9krecorderutil.mediaMap.get("remoteaac"), V9krecorderutil.createStreamPath(Environment.DIRECTORY_MOVIES, "trimaacaudio"));
                    return;
                }
                if(mRecorder != null) {
                    mRecorder.stopRecording();
                    mSecondsOfVideo = 0.0f;
                    mRecorder = null;
                }
                endProgress();
                mNavController.navigate(CameraFragmentDirections.navigateToPlaybackFragment(null));
            }
        }.start();

    }

    private void resetRecorder() {
        if(DEBUG) Log.d(TAG, "resetRecorder: ");

        if(mRecorder != null) {
            mRecorder.resetRecorder();
        }

        mCancelDoneButton.setVisibility(View.INVISIBLE);
        mGallery.setVisibility(View.VISIBLE);
        recordingStatus = RECORDING_OFF;
        mSecondsOfVideo = 0.0f;
        recordingEnabled = false;
        pauseEnabled = false;
        mProgressBar.reset();
    }

    @Subscribe
    public void onTrimEvent(TrimSongEvent event) {

        if(DEBUG) Log.d(TAG, "onTrimEvent: " + event.getResponsePath());
        final String path = event.getResponsePath();
        if(mRecorder != null) {
            mRecorder.stopRecording();
            mSharedViewModel.getSelectedSongName().postValue(null);
            mSecondsOfVideo = 0.0f;
            mRecorder = null;
        }
        endProgress();
        mNavController.navigate(CameraFragmentDirections.navigateToPlaybackFragment(null));
    }

    private void setTimerForRecording() {
        mCameraRecordingBtn.setVideoDuration(15000);
    }

    public void updateBufferStatus(long durationUsec) {
        mSecondsOfVideo = (durationUsec / 1000000.0f);
        updateProgress();
    }


    public void bufferFilledUp(int status) {

    }

    private void startProgress() {
        progressDialog = new ProgressDialog(getContext(), R.style.MyTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing video");
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
    }

    private void endProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void updateProgress() {

        int secondOfVideo = (int)mSecondsOfVideo;

        //mProgressBar.setProgress(secondOfVideo);

        if(DEBUG) Log.d(TAG, "secondsOfVideo" + secondOfVideo);
        if(DEBUG) Log.d(TAG, "mSecondsOfVideo" + mSecondsOfVideo);

        //boolean wantEnabled = (mVideoEncoder != null) && !mFileSaveInProgress;
        boolean wantVisible = (mSecondsOfVideo >= 3);

        if (mCancelDoneButton.isEnabled() == wantVisible) {
            if(DEBUG) Log.d(TAG, "mCancelDoneButton.isEnabled()" + mCancelDoneButton.isEnabled());
            if(DEBUG) Log.d(TAG, "wantVisible" + wantVisible);
            mCancelDoneButton.setVisibility(View.VISIBLE);
            mGallery.setVisibility(View.INVISIBLE);
        }
    }

    private void showCancel(boolean shouldShow) {
        if(shouldShow) {
            //mCancelButton.setVisibility(View.VISIBLE);
        } else {
            //mCancelButton.setVisibility(View.INVISIBLE);
        }
    }

    private void initRecycler() {
        mSpeedRecyclerView = fragmentCameraBinding.speedRecycler;
        mSpeedAdapter = new MySpeedListAdapter();
        mSpeedRecyclerView.setHasFixedSize(true);
        mSpeedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSpeedRecyclerView.setAdapter(mSpeedAdapter);
    }

    private boolean hasPermissionsGranted(String[] permissions)
    {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions)
    {
        for (String permission : permissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            }
        }
        return false;
    }

    private void requestVideoPermissions() {
        if(shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)) {
            // confirm dialog
            if(DEBUG) Log.d(TAG, "requestVideoPermissions");
            requestPermissions(VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        } else {
            requestPermissions(VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        }
    }

    private void openGallery() {
        Intent i = new Intent(getActivity(), PickerActivity.class);
        i.putExtra("IMAGES_LIMIT", 1);
        i.putExtra("VIDEOS_LIMIT", 1);
        i.putExtra("REQUEST_RESULT_CODE", 123);
        startActivityForResult(i, 101);
    }

    public void playSong() {

        final String song = V9krecorderutil.mediaMap.get("remoteaac");

        Log.d(TAG, "playing download song: " + song);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();

        mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireActivity(),
                Util.getUserAgent(getContext(), "TikTok"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(song));

        mPlayer.prepare(videoSource);

        mPlayer.setPlayWhenReady(true);

        mIsPlaying = true;

    }

    private void pauseSong() {
        mPlayer.setPlayWhenReady(false);
    }

    private void resumeSong() {
        mPlayer.setPlayWhenReady(true);
    }

    public void stopSong() {

        if(mPlayer!=null){
            mPlayer.setPlayWhenReady(false);
            mPlayer.release();
            mIsPlaying = false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_VIDEO_PERMISSIONS: {
                if (grantResults.length == VIDEO_PERMISSIONS.length) {
                    for (int result : grantResults) {
                        if(result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "You must grand permission!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "You must grand permission!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flip_layout:
                mCameraGLView.switchCamera();
                break;
            case R.id.iv_close:
                getActivity().finish();
                break;
            case R.id.speed_layout:
            case R.id.ivCloseSpeedContainer:
            case R.id.timer_layout:
            case R.id.flash_layout:
                Toast.makeText(getContext(), "Under development", Toast.LENGTH_SHORT).show();
                break;
            case R.id.select_music_layout:
                mNavController.navigate(CameraFragmentDirections.navigateToSongPickerSubgraph().setIsFromHome(true).setShowIndex(0));
                //startActivity(new Intent(getActivity(), MixupFragment.class).putExtra("show", 1));
                break;
            //startActivity(new Intent(getActivity(), MixupFragment.class).putExtra("show", 0)
                        //.putExtra("home_type", true));
            case R.id.tvGallery:
                //Toast.makeText(getContext(), "Gallery clicked", Toast.LENGTH_SHORT).show();
                openGallery();
                break;
           /*case R.id.tvTemplates:
                Toast.makeText(getContext(), "Template clicked", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.done_icon:
                stopSong();
                stopRecording();
                break;
//            case R.id.cancel_icon:
//                new AlertDialogFragment(this, "delete").show(getParentFragmentManager(), "deleteAlert");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(DEBUG) Log.d(TAG, "onActivityResult from PickerActivity: ");
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 101) {
            V9krecorderutil.mediaMap.put("video", data.getStringExtra("MEDIA"));
            //startActivity(new Intent(getActivity(), EditSoundFragment.class));
            mNavController.navigate(CameraFragmentDirections.navigateToPlaybackFragment(data.getStringExtra("MEDIA")));
        }

    }

    private void setHeightWidth() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int realWidth;
        int realHeight;

        if (Build.VERSION.SDK_INT >= 17) {
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            realWidth = realMetrics.widthPixels;
            realHeight = realMetrics.heightPixels;

        } else {
            realWidth = display.getWidth();
            realHeight = display.getHeight();
        }

        MAX_HEIGHT = realHeight;
        MAX_WIDTH = realWidth;
    }

    private void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            getActivity().getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void hideSystemUi() {

        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.KEEP_SCREEN_ON
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onExitAction() {
        callback.setEnabled(false);
        requireActivity().getOnBackPressedDispatcher().onBackPressed();
    }

    @Override
    public void onCancelAction() {

    }

    @Override
    public void onReshootAction() {
        //TODO restart the recorder
        resetRecorder();
    }

    public void onDelete() {
        //TODO delete last recorded movie
    }
}