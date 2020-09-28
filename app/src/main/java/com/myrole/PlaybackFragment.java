package com.myrole;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.myrole.databinding.FragmentPlaybackBinding;
import com.myrole.fragments.AlertDialogFragment;
import com.myrole.fragments.viewmodel.SharedViewModel;
import com.myrole.listeners.onDialogAction;
import com.myrole.utilities.SongCodec;
import com.myrole.utilities.SongCodec.SongDecodeListener;
import com.myrole.utilities.TrimSongEvent;
import com.v9kmedia.v9krecorder.utils.V9krecorderutil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class PlaybackFragment extends Fragment implements View.OnClickListener, onDialogAction {

    private static final String TAG = "PlaybackFragment";
    private static final Boolean DEBUG = true;

    private FragmentPlaybackBinding fragmentPlaybackBinding;
    private OnBackPressedCallback callback;
    private NavController mNavController;

    private MediaSource videoSource;
    private MediaSource audioSource;

    private PlayerView mPlayerView;

    private SimpleExoPlayer mVideoPlayer;
    private SimpleExoPlayer mAudioPlayer;

    private SharedViewModel mModel;

    private int currentWindow = 0;
    private long recordDuration = 0;
    private long playbackPosition = 0;

    private long duration;

    private boolean playWhenReady = true;


    private String selectedAudioPath;

    //private IjkMediaPlayer mAudioPlayer;
    //private IjkMediaPlayer mVideoPlayer;

    private boolean prepareSongToPlay = false;

   // private SimpleExoPlayer mPlayer;

    private PlayerView mPlayerSurfaceView;

    private boolean mFinish;

    private boolean mIsPlaying;

    private String pcmPath;
    private String aacPath;
    private String mVideoGalleryPath;

    public PlaybackFragment() {
        if(DEBUG) Log.d(TAG, "audio: " + V9krecorderutil.mediaMap.get("audio"));
        if(DEBUG) Log.d(TAG, "audio: " + V9krecorderutil.mediaMap.get("video"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                showAlertDialog();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPlaybackBinding = FragmentPlaybackBinding.inflate(inflater, container, false);
        return fragmentPlaybackBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);

        mVideoGalleryPath = PlaybackFragmentArgs.fromBundle(getArguments()).getGalleryVideoPath();

        mPlayerView = fragmentPlaybackBinding.playbackView;

        init();

        if(prepareSongToPlay) {
            if(mIsPlaying) {
                resumeSong();
            } else {
                playSong();
            }
        }

        mModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        mModel.getSelectedSongPath().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "selected path: " + s);
                prepareSongToPlay = true;
            }
        });

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

    private void showAlertDialog() {
        new AlertDialogFragment(this, "back").show(getParentFragmentManager(), "discardAlert");
    }

    @Override
    public void onExitAction() {
        callback.setEnabled(false);
        requireActivity().getViewModelStore().clear();
        requireActivity().getOnBackPressedDispatcher().onBackPressed();
    }

    @Override
    public void onCancelAction() {

    }

    @Override
    public void onReshootAction() {

    }

    private void init() {

        mPlayerSurfaceView = fragmentPlaybackBinding.playbackView;

        fragmentPlaybackBinding.addMusicLayout.setOnClickListener(this);
        fragmentPlaybackBinding.volumeLayout.setOnClickListener(this);

        fragmentPlaybackBinding.backIcon.setOnClickListener(this);
        fragmentPlaybackBinding.nextIcon.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_music_layout:
                mNavController.navigate(PlaybackFragmentDirections.navigateToSongPickerSubgraph().setIsFromHome(false).setShowIndex(0).setDuration(mVideoPlayer.getDuration()));
                break;
            case R.id.volume_layout:
                Toast.makeText(getContext(), "volume layout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.next_icon:
                mNavController.navigate(PlaybackFragmentDirections.navigateToPostRecordingFragment());
                break;
            case R.id.back_icon:
                mNavController.popBackStack();
        }

    }

//    private void pause() {
//        if (mVideoPlayer != null) {
//            mVideoPlayer.pause();
//        }
//        if (mAudioPlayer != null) {
//            mAudioPlayer.pause();
//        }
//    }

    private void prepareMusic(MediaSource audioSource) {
        createAudioPlayer(audioSource);
    }

//    private void release() {
//        if (mVideoPlayer != null) {
//            mVideoPlayer.stop();
//            mVideoPlayer.release();
//            mVideoPlayer = null;
//        }
//        if (mAudioPlayer != null) {
//            mAudioPlayer.stop();
//            mAudioPlayer.release();
//            mAudioPlayer = null;
//        }
//    }
//
//    private void resume() {
//        if (mVideoPlayer != null) {
//            mVideoPlayer.start();
//            mFinish = false;
//        }
//        if (mAudioPlayer != null) {
//            mAudioPlayer.seekTo(mVideoPlayer.getCurrentPosition());
//            mAudioPlayer.start();
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: PlayBackFragment");
        if(DEBUG) Log.d(TAG, "audio source in onStart(): " + this.audioSource);
        if (Util.SDK_INT > 23) {
            initializeVideoPlayer();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: PlayBackFragment");
        hideSystemUi();
        if(DEBUG) Log.d(TAG, "audio source in onResume(): " + this.audioSource);
        if ((Util.SDK_INT <= 23 || mVideoPlayer == null)) {
            initializeVideoPlayer();
        }
        if (mIsPlaying) {
            //resume();
            resumeSong();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: PlayBackFragment");
        if (Util.SDK_INT > 23) {
            Log.d(TAG, "releasePlayer: PlayBackFragment");
            releasePlayer();
        }
        if (!mFinish) {
            //pause();
            pauseSong();
            mIsPlaying = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: PlayBackFragment");
        if (Util.SDK_INT > 23) {
            Log.d(TAG, "releasePlayer: PlayBackFragment");
            releasePlayer();
        }
        EventBus.getDefault().unregister(this);
    }

    private void releasePlayer() {
        if (mVideoPlayer != null) {
            playbackPosition = mVideoPlayer.getCurrentPosition();
            currentWindow = mVideoPlayer.getCurrentWindowIndex();
            playWhenReady = mVideoPlayer.getPlayWhenReady();
            mVideoPlayer.stop();
            mVideoPlayer.release();

            if(mAudioPlayer != null) {
                mAudioPlayer.stop();
                mAudioPlayer.release();
                mAudioPlayer = null;
            }
            mVideoPlayer = null;
        }
    }

    @Subscribe
    public void onTrimEvent(TrimSongEvent event) {

        if(DEBUG) Log.d(TAG, "onTrimEvent: " + event.getResponsePath());

        final String path = event.getResponsePath();

        audioSource = buildMediaSource(Uri.fromFile(new File(path)));

        prepareMusic(audioSource);

//        AsyncTask.execute(new Runnable() {
//            public void run() {
//                startDecoding(path);
//            }
//        });

    }

    public void startDecoding(String audioSong) {

        pcmPath = V9krecorderutil.createStreamPath(Environment.DIRECTORY_MOVIES, "rawaudio");

        aacPath = V9krecorderutil.createStreamPath(Environment.DIRECTORY_MOVIES, "audio");

        SongCodec.getPCMFromSong(audioSong, pcmPath, new SongDecodeListener() {
            public void decodeFail() {

            }
            public void decodeOver() {
                SongCodec.PcmToAudio(pcmPath, aacPath, new SongDecodeListener() {
                    public void decodeFail() {
                        Log.e(TAG, "decode fail");
                    }

                    public void decodeOver() {
                        Log.e(TAG, "decode over");
                    }
                });
            }
        });
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {

        public void onClick(View view) {

            int id = view.getId();

            if (id == R.id.add_music) {
                addMusic();
            } else if (id == R.id.next_icon) {
                loadPostFragment();
            }
        }

    };

    private void loadPostFragment() {

    }

    private void addMusic() {

    }

    public void playSong() {

        final String song = V9krecorderutil.mediaMap.get("remoteaac");

        Log.d(TAG, "playing download song: " + song);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();

        mAudioPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(requireActivity(),
                Util.getUserAgent(getContext(), "TikTok"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(song));

        mAudioPlayer.prepare(videoSource);
        mAudioPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        mAudioPlayer.setPlayWhenReady(true);

        mIsPlaying = true;

    }

    private void pauseSong() {
        if(mAudioPlayer != null)
            mAudioPlayer.setPlayWhenReady(false);
    }

    private void resumeSong() {
        if(mAudioPlayer != null)
            mAudioPlayer.setPlayWhenReady(true);
    }

    public void stopSong() {
        if(mVideoPlayer !=null){
            mAudioPlayer.setPlayWhenReady(false);
            mAudioPlayer.release();
            mIsPlaying = false;
        }
    }

    private void createAudioPlayer(MediaSource audioSource) {
        if(DEBUG) Log.d(TAG, "createAudioPlayer: ");

        mVideoPlayer.setVolume(0.0f);

        mAudioPlayer = ExoPlayerFactory.newSimpleInstance(
                requireActivity(),
                new DefaultRenderersFactory(requireActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        if(DEBUG) Log.d(TAG, "audio source in prepareMusic()" + audioSource);



        mAudioPlayer.seekTo(currentWindow, playbackPosition);

        mAudioPlayer.prepare(audioSource);
        mAudioPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        mAudioPlayer.setPlayWhenReady(playWhenReady);
    }

    private void initializeVideoPlayer() {

        mVideoPlayer = ExoPlayerFactory.newSimpleInstance(
                requireActivity(),
                new DefaultRenderersFactory(requireActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());


        mVideoPlayer.addVideoListener(new VideoListener() {

                public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRation)
                {
                    ViewGroup.LayoutParams params = mPlayerView.getLayoutParams();
                    int currentwidth = mPlayerView.getWidth();


                    params.width = currentwidth;
                    params.height = (int) ((float) height / width * currentwidth);

                    mPlayerView.requestLayout();
                }

                public void onRenderedFirstFrame()
                {

                }

            }
        );

        mVideoPlayer.addListener(new Player.EventListener() {

                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == SimpleExoPlayer.STATE_READY) {
                        duration = mVideoPlayer.getDuration();
                    }
                }

                public void onRepeatModeChanged(int repeatMode) {

                }

                public void onPlayerError(ExoPlaybackException error) {

                }

                public void onSeekProcessed() {

                }

                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                public void onPositionDiscontinuity(int reason) {

                }

                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                public void onLoadingChanged(boolean isLoading) {

                }
            }
        );

        mPlayerView.setPlayer(mVideoPlayer);

        mVideoPlayer.seekTo(currentWindow, playbackPosition);

        if(mVideoGalleryPath == null) {
            mModel.getSelectedSongName().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    mVideoPlayer.prepare(
                            buildMediaSource(
                                    Uri.parse(V9krecorderutil.mediaMap.get("trimaacaudio")),
                                    Uri.parse(V9krecorderutil.mediaMap.get("video"))
                            )
                    );
                }
            });

                mVideoPlayer.prepare(
                        buildMediaSource(
                                Uri.parse(V9krecorderutil.mediaMap.get("audio")),
                                Uri.parse(V9krecorderutil.mediaMap.get("video"))
                        )
                );

        } else {
            mVideoPlayer.prepare(buildMediaSource(Uri.parse(mVideoGalleryPath)));
        }

        mVideoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
        mVideoPlayer.setPlayWhenReady(playWhenReady);

    }

    private MediaSource buildMediaSource(Uri audioSource, Uri videoSource)
    {
        // Create audio data source factory.
        MediaSource audio = new ProgressiveMediaSource.Factory(new FileDataSourceFactory()).createMediaSource(audioSource);

        // Create video data source factory.
        MediaSource video = new ProgressiveMediaSource.Factory(new FileDataSourceFactory()).createMediaSource(videoSource);

        if(DEBUG) Log.d(TAG, "mVideoStream: " + video);
        if(DEBUG) Log.d(TAG, "mAudioStream: " + audio);

        // return a new MergingMediaSource
        return new MergingMediaSource(audio, video);
    }

    private MediaSource buildMediaSource(Uri source)
    {
        // Create audio data source factory.
        return new ProgressiveMediaSource.Factory(new FileDataSourceFactory()).createMediaSource(source);

    }


}