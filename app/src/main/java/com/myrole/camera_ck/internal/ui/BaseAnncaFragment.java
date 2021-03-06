package com.myrole.camera_ck.internal.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaActionSound;
import android.os.Build;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.myrole.R;
import com.myrole.camera_ck.CameraFragmentApi;
import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.configuration.ConfigurationProvider;
import com.myrole.camera_ck.configuration.ConfigurationProviderImpl;
import com.myrole.camera_ck.internal.controller.CameraController;
import com.myrole.camera_ck.internal.controller.impl.Camera1Controller;
import com.myrole.camera_ck.internal.controller.impl.Camera2Controller;
import com.myrole.camera_ck.internal.controller.view.CameraView;
import com.myrole.camera_ck.internal.enums.Camera;
import com.myrole.camera_ck.internal.enums.Flash;
import com.myrole.camera_ck.internal.enums.MediaAction;
import com.myrole.camera_ck.internal.enums.Record;
import com.myrole.camera_ck.internal.timer.CountdownTask;
import com.myrole.camera_ck.internal.timer.TimerTask;
import com.myrole.camera_ck.internal.timer.TimerTaskBase;
import com.myrole.camera_ck.internal.ui.model.PhotoQualityOption;
import com.myrole.camera_ck.internal.ui.model.VideoQualityOption;
import com.myrole.camera_ck.internal.ui.view.AspectFrameLayout;
import com.myrole.camera_ck.internal.utils.CameraHelper;
import com.myrole.camera_ck.internal.utils.ImageParameters;
import com.myrole.camera_ck.internal.utils.Size;
import com.myrole.camera_ck.internal.utils.Utils;
import com.myrole.camera_ck.listeners.CameraFragmentControlsListener;
import com.myrole.camera_ck.listeners.CameraFragmentResultListener;
import com.myrole.camera_ck.listeners.CameraFragmentStateListener;
import com.myrole.camera_ck.listeners.CameraFragmentVideoRecordTextListener;

import java.io.File;

import static com.myrole.camera_ck.configuration.Configuration.IMAGE_INFO;

//import com.myrole.camera_ck.R;

/*
 * Created by memfis on 12/1/16.
 * Updated by Florent37
 */

public abstract class BaseAnncaFragment<CameraId> extends Fragment implements CameraFragmentApi {

    public static final String ARG_CONFIGURATION = "configuration";
    public static final int MIN_VERSION_ICECREAM = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;

    protected CharSequence[] videoQualities;
    protected CharSequence[] photoQualities;
    protected AspectFrameLayout previewContainer;
   /* private View topCoverView;
    private View btnCoverView;*/

    protected ConfigurationProvider configurationProvider;
    @Configuration.MediaQuality
    protected int newQuality = -1;
    private Configuration configuration;
    private SensorManager sensorManager = null;
    private CameraController cameraController;
    private AlertDialog settingsDialog;
    private CameraFragmentControlsListener cameraFragmentControlsListener;
    private CameraFragmentVideoRecordTextListener cameraFragmentVideoRecordTextListener;

    private ImageParameters mImageParameters;


    final TimerTaskBase.Callback timerCallBack = new TimerTaskBase.Callback() {
        @Override
        public void setText(String text) {
            if (cameraFragmentVideoRecordTextListener != null) {
                cameraFragmentVideoRecordTextListener.setRecordDurationText(text);
            }
        }

        @Override
        public void setTextVisible(boolean visible) {
            if (cameraFragmentVideoRecordTextListener != null) {
                cameraFragmentVideoRecordTextListener.setRecordDurationTextVisible(visible);
            }
        }
    };
    private CameraFragmentStateListener cameraFragmentStateListener;
    @Flash.FlashMode
    private int currentFlashMode = Flash.FLASH_AUTO;
    @Camera.CameraType
    private int currentCameraType = Camera.CAMERA_TYPE_REAR;
    @MediaAction.MediaActionState
    private int currentMediaActionState = MediaAction.ACTION_PHOTO;
    @Record.RecordState
    private int currentRecordState = Record.TAKE_PHOTO_STATE;
    private String mediaFilePath;
    private FileObserver fileObserver;
    private long maxVideoFileSize = 0;
    private TimerTaskBase countDownTimer;
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (this) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    if (sensorEvent.values[0] < 4 && sensorEvent.values[0] > -4) {
                        if (sensorEvent.values[1] > 0) {
                            // UP
                            configurationProvider.setSensorPosition(Configuration.SENSOR_POSITION_UP);
                            configurationProvider.setDegrees(configurationProvider.getDeviceDefaultOrientation() == Configuration.ORIENTATION_PORTRAIT ? 0 : 90);
                        } else if (sensorEvent.values[1] < 0) {
                            // UP SIDE DOWN
                            configurationProvider.setSensorPosition(Configuration.SENSOR_POSITION_UP_SIDE_DOWN);
                            configurationProvider.setDegrees(configurationProvider.getDeviceDefaultOrientation() == Configuration.ORIENTATION_PORTRAIT ? 180 : 270);
                        }
                    } else if (sensorEvent.values[1] < 4 && sensorEvent.values[1] > -4) {
                        if (sensorEvent.values[0] > 0) {
                            // LEFT
                            configurationProvider.setSensorPosition(Configuration.SENSOR_POSITION_LEFT);
                            configurationProvider.setDegrees(configurationProvider.getDeviceDefaultOrientation() == Configuration.ORIENTATION_PORTRAIT ? 90 : 180);
                        } else if (sensorEvent.values[0] < 0) {
                            // RIGHT
                            configurationProvider.setSensorPosition(Configuration.SENSOR_POSITION_RIGHT);
                            configurationProvider.setDegrees(configurationProvider.getDeviceDefaultOrientation() == Configuration.ORIENTATION_PORTRAIT ? 270 : 0);
                        }
                    }
                    onScreenRotation(configurationProvider.getDegrees());
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    private CameraFragmentResultListener cameraFragmentResultListener;

    protected static Fragment newInstance(Fragment fragment, Configuration configuration) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIGURATION, configuration);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View decorView = ((Activity) container.getContext()).getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > MIN_VERSION_ICECREAM) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        return inflater.inflate(R.layout.generic_camera_layout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            configuration = (Configuration) arguments.getSerializable(ARG_CONFIGURATION);
        }
        this.configurationProvider = new ConfigurationProviderImpl();
        this.configurationProvider.setupWithAnnaConfiguration(configuration);

        this.sensorManager = (SensorManager) getContext().getSystemService(Activity.SENSOR_SERVICE);

        final CameraView cameraView = new CameraView() {

            @Override
            public void updateCameraPreview(Size size, View cameraPreview) {
                if (cameraFragmentControlsListener != null) {
                    cameraFragmentControlsListener.unLockControls();
                    cameraFragmentControlsListener.allowRecord(true);
                }

                setCameraPreview(cameraPreview, size);
            }

            @Override
            public void updateUiForMediaAction(@Configuration.MediaAction int mediaAction) {

            }

            @Override
            public void updateCameraSwitcher(int numberOfCameras) {
                if (cameraFragmentControlsListener != null) {
                    cameraFragmentControlsListener.allowCameraSwitching(numberOfCameras > 1);
                }
            }

            @Override
            public void onPhotoTaken(byte[] bytes, CameraFragmentResultListener callback) {
                final String filePath = cameraController.getOutputFile().toString();
                if (cameraFragmentResultListener != null) {
                    cameraFragmentResultListener.onPhotoTaken(bytes, filePath);
                }
                if (callback != null) {
                    callback.onPhotoTaken(bytes, filePath);
                }
            }

            @Override
            public void onVideoRecordStart(int width, int height) {
                final File outputFile = cameraController.getOutputFile();
                onStartVideoRecord(outputFile);
            }

            @Override
            public void onVideoRecordStop(@Nullable CameraFragmentResultListener callback) {
                //BaseAnncaFragment.this.onStopVideoRecord(callback);
            }

            @Override
            public void releaseCameraPreview() {
                clearCameraPreview();
            }
        };

        if (CameraHelper.hasCamera2(getContext())) {
            cameraController = new Camera2Controller(getContext(), cameraView, configurationProvider);
        } else {
            cameraController = new Camera1Controller(getContext(), cameraView, configurationProvider);
        }
        cameraController.onCreate(savedInstanceState);

        //onProcessBundle
        currentMediaActionState = configurationProvider.getMediaAction() == Configuration.MEDIA_ACTION_VIDEO ?
                MediaAction.ACTION_VIDEO : MediaAction.ACTION_PHOTO;

        if (savedInstanceState == null) {
            mImageParameters = new ImageParameters();
        } else {

            mImageParameters = savedInstanceState.getParcelable(IMAGE_INFO);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        previewContainer = (AspectFrameLayout) view.findViewById(R.id.previewContainer);
        /*topCoverView = view.findViewById(R.id.cover_top_view);
        btnCoverView = view.findViewById(R.id.cover_bottom_view);*/

        final int defaultOrientation = Utils.getDeviceDefaultOrientation(getContext());
        switch (defaultOrientation) {
            case android.content.res.Configuration.ORIENTATION_LANDSCAPE:
                mImageParameters.mIsPortrait = false;
                configurationProvider.setDeviceDefaultOrientation(Configuration.ORIENTATION_LANDSCAPE);
                break;
            default:
                mImageParameters.mIsPortrait = true;
                configurationProvider.setDeviceDefaultOrientation(Configuration.ORIENTATION_PORTRAIT);
                break;
        }

        //setSqaureCamera(savedInstanceState);

        switch (configurationProvider.getFlashMode()) {
            case Configuration.FLASH_MODE_AUTO:
                setFlashMode(Flash.FLASH_AUTO);
                break;
            case Configuration.FLASH_MODE_ON:
                setFlashMode(Flash.FLASH_ON);
                break;
            case Configuration.FLASH_MODE_OFF:
                setFlashMode(Flash.FLASH_OFF);
                break;
        }

        if (cameraFragmentControlsListener != null) {
            setMaxVideoDuration(configurationProvider.getVideoDuration());
            setMaxVideoFileSize(configurationProvider.getVideoFileSize());
        }

        setCameraTypeFrontBack(configurationProvider.getCameraFace());

        notifyListeners();

    }

   /* private void setSqaureCamera(Bundle savedInstanceState)
    {
        if (savedInstanceState == null) {
            ViewTreeObserver observer = previewContainer.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mImageParameters.mPreviewWidth = previewContainer.getWidth();
                    mImageParameters.mPreviewHeight = previewContainer.getHeight();

                    mImageParameters.mCoverWidth = mImageParameters.mCoverHeight
                            = mImageParameters.calculateCoverWidthHeight();

//                    Log.d(TAG, "parameters: " + mImageParameters.getStringValues());
//                    Log.d(TAG, "cover height " + topCoverView.getHeight());
                    resizeTopAndBtmCover(topCoverView, btnCoverView);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        previewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        previewContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        } else {
            if (mImageParameters.isPortrait()) {
                topCoverView.getLayoutParams().height = mImageParameters.mCoverHeight;
                btnCoverView.getLayoutParams().height = mImageParameters.mCoverHeight;
            } else {
                topCoverView.getLayoutParams().width = mImageParameters.mCoverWidth;
                btnCoverView.getLayoutParams().width = mImageParameters.mCoverWidth;
            }
        }
    }

    private void resizeTopAndBtmCover( final View topCover, final View bottomCover) {
        ResizeAnimation resizeTopAnimation
                = new ResizeAnimation(topCover, mImageParameters);
        resizeTopAnimation.setDuration(800);
        resizeTopAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        topCover.startAnimation(resizeTopAnimation);

        ResizeAnimation resizeBtmAnimation
                = new ResizeAnimation(bottomCover, mImageParameters);
        resizeBtmAnimation.setDuration(800);
        resizeBtmAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        bottomCover.startAnimation(resizeBtmAnimation);
    }*/

    public void notifyListeners() {
        onFlashModeChanged();
        onActionPhotoVideoChanged();
        onCameraTypeFrontBackChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void takePhotoOrCaptureVideo(final CameraFragmentResultListener resultListener) {
        switch (currentMediaActionState) {
            case MediaAction.ACTION_PHOTO:
                takePhoto(resultListener);
                break;
            case MediaAction.ACTION_VIDEO:
                switch (currentRecordState) {
                    case Record.RECORD_IN_PROGRESS_STATE:
                        stopRecording(resultListener);
                        break;
                    default:
                        startRecording();
                        break;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        cameraController.onResume();
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        if (cameraFragmentControlsListener != null) {
            cameraFragmentControlsListener.lockControls();
            cameraFragmentControlsListener.allowRecord(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        cameraController.onPause();
        sensorManager.unregisterListener(sensorEventListener);

        if (cameraFragmentControlsListener != null) {
            cameraFragmentControlsListener.lockControls();
            cameraFragmentControlsListener.allowRecord(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cameraController.onDestroy();
    }

    protected void setMaxVideoFileSize(long maxVideoFileSize) {
        this.maxVideoFileSize = maxVideoFileSize;
    }

    protected void setMaxVideoDuration(int maxVideoDurationInMillis) {
        if (maxVideoDurationInMillis > 0) {
            this.countDownTimer = new CountdownTask(timerCallBack, maxVideoDurationInMillis);
        } else {
            this.countDownTimer = new TimerTask(timerCallBack);
        }
    }

    @Override
    public void openSettingDialog() {
        final Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (currentMediaActionState == MediaAction.ACTION_VIDEO) {
            builder.setSingleChoiceItems(videoQualities, getVideoOptionCheckedIndex(), new DialogInterface.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    newQuality = ((VideoQualityOption) videoQualities[index]).getMediaQuality();
                }
            });
            if (configurationProvider.getVideoFileSize() > 0)
                builder.setTitle(String.format(getString(R.string.settings_video_quality_title),
                        "(Max " + configurationProvider.getVideoFileSize() / (1024 * 1024) + " MB)"));
            else
                builder.setTitle(String.format(getString(R.string.settings_video_quality_title), ""));
        } else {
            builder.setSingleChoiceItems(photoQualities, getPhotoOptionCheckedIndex(), new DialogInterface.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(DialogInterface dialogInterface, int index) {
                    newQuality = ((PhotoQualityOption) photoQualities[index]).getMediaQuality();
                }
            });
            builder.setTitle(R.string.settings_photo_quality_title);
        }

        builder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (newQuality > 0 && newQuality != configurationProvider.getMediaQuality()) {
                    configurationProvider.setMediaQuality(newQuality);
                    dialogInterface.dismiss();
                    if (cameraFragmentControlsListener != null) {
                        cameraFragmentControlsListener.lockControls();
                    }
                    cameraController.switchQuality();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        settingsDialog = builder.create();
        settingsDialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(settingsDialog.getWindow().getAttributes());
        layoutParams.width = Utils.convertDipToPixels(context, 350);
        layoutParams.height = Utils.convertDipToPixels(context, 350);
        settingsDialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void switchCameraTypeFrontBack() {
        if (cameraFragmentControlsListener != null) {
            cameraFragmentControlsListener.lockControls();
            cameraFragmentControlsListener.allowRecord(false);
        }

        int cameraFace = Configuration.CAMERA_FACE_REAR;
        switch (currentCameraType) {
            case Camera.CAMERA_TYPE_FRONT:
                currentCameraType = Camera.CAMERA_TYPE_REAR;
                cameraFace = Configuration.CAMERA_FACE_REAR;
                break;
            case Camera.CAMERA_TYPE_REAR:
                currentCameraType = Camera.CAMERA_TYPE_FRONT;
                cameraFace = Configuration.CAMERA_FACE_FRONT;
                break;
        }

        onCameraTypeFrontBackChanged();
        this.cameraController.switchCamera(cameraFace);

        if (cameraFragmentControlsListener != null) {
            cameraFragmentControlsListener.unLockControls();
        }
    }

    protected void setCameraTypeFrontBack(@Configuration.CameraFace int cameraFace) {
        switch (cameraFace) {
            case Configuration.CAMERA_FACE_FRONT:
                currentCameraType = Camera.CAMERA_TYPE_FRONT;
                cameraFace = Configuration.CAMERA_FACE_FRONT;
                break;
            case Configuration.CAMERA_FACE_REAR:
                currentCameraType = Camera.CAMERA_TYPE_REAR;
                cameraFace = Configuration.CAMERA_FACE_REAR;
                break;
        }
        onCameraTypeFrontBackChanged();
        this.cameraController.switchCamera(cameraFace);

    }

    protected void onCameraTypeFrontBackChanged() {
        if (cameraFragmentStateListener != null) {
            switch (currentCameraType) {
                case Camera.CAMERA_TYPE_REAR:
                    cameraFragmentStateListener.onCurrentCameraBack();
                    break;
                case Camera.CAMERA_TYPE_FRONT:
                    cameraFragmentStateListener.onCurrentCameraFront();
                    break;
            }
        }
    }

    @Override
    public void switchActionPhotoVideo() {
        switch (currentMediaActionState) {
            case MediaAction.ACTION_PHOTO:
                currentMediaActionState = MediaAction.ACTION_VIDEO;
                break;
            case MediaAction.ACTION_VIDEO:
                currentMediaActionState = MediaAction.ACTION_PHOTO;
                break;
        }
        onActionPhotoVideoChanged();
    }

    protected void onActionPhotoVideoChanged() {
        if (cameraFragmentStateListener != null) {
            switch (currentMediaActionState) {
                case MediaAction.ACTION_VIDEO:
                    cameraFragmentStateListener.onCameraSetupForVideo();
                    break;
                case MediaAction.ACTION_PHOTO:
                    cameraFragmentStateListener.onCameraSetupForPhoto();
                    break;
            }
        }
    }

    @Override
    public void toggleFlashMode() {
        switch (currentFlashMode) {
            case Flash.FLASH_AUTO:
                currentFlashMode = Flash.FLASH_OFF;
                break;
            case Flash.FLASH_OFF:
                currentFlashMode = Flash.FLASH_ON;
                break;
            case Flash.FLASH_ON:
                currentFlashMode = Flash.FLASH_AUTO;
                break;
        }
        onFlashModeChanged();
    }

    @Override
    public boolean isSquareMode() {
        return configurationProvider.isSquareMode();
    }

    private void onFlashModeChanged() {
        switch (currentFlashMode) {
            case Flash.FLASH_AUTO:
                if (cameraFragmentStateListener != null) cameraFragmentStateListener.onFlashAuto();
                configurationProvider.setFlashMode(Configuration.FLASH_MODE_AUTO);
                this.cameraController.setFlashMode(Configuration.FLASH_MODE_AUTO);
                break;
            case Flash.FLASH_ON:
                if (cameraFragmentStateListener != null) cameraFragmentStateListener.onFlashOn();
                configurationProvider.setFlashMode(Configuration.FLASH_MODE_ON);
                this.cameraController.setFlashMode(Configuration.FLASH_MODE_ON);
                break;
            case Flash.FLASH_OFF:
                if (cameraFragmentStateListener != null) cameraFragmentStateListener.onFlashOff();
                configurationProvider.setFlashMode(Configuration.FLASH_MODE_OFF);
                this.cameraController.setFlashMode(Configuration.FLASH_MODE_OFF);
                break;
        }
    }

    protected void onScreenRotation(int degrees) {
        if (cameraFragmentStateListener != null) {
            cameraFragmentStateListener.shouldRotateControls(degrees);
        }
        rotateSettingsDialog(degrees);
    }

    protected void setRecordState(@Record.RecordState int recordState) {
        this.currentRecordState = recordState;
    }


    //@Override
    //public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    if (resultCode == Activity.RESULT_OK) {
    //        if (requestCode == REQUEST_PREVIEW_CODE) {
    //            final FragmentActivity activity = getActivity();
    //            if (activity != null) {
    //                if (PreviewActivity.isResultConfirm(data)) {
    //                    Intent resultIntent = new Intent();
    //                    resultIntent.putExtra(Configuration.Arguments.FILE_PATH,
    //                            PreviewActivity.getMediaFilePatch(data));
    //                    activity.setResult(Activity.RESULT_OK, resultIntent);
    //                    activity.finish();
    //                } else if (PreviewActivity.isResultCancel(data)) {
    //                    activity.setResult(Activity.RESULT_CANCELED);
    //                    activity.finish();
    //                } else if (PreviewActivity.isResultRetake(data)) {
    //                    //ignore, just proceed the camera
    //                }
    //            }
    //        }
    //    }
    //}

    protected void setFlashMode(@Flash.FlashMode int mode) {
        this.currentFlashMode = mode;
        onFlashModeChanged();
    }

    protected void rotateSettingsDialog(int degrees) {
        if (settingsDialog != null && settingsDialog.isShowing() && Build.VERSION.SDK_INT > 10) {
            ViewGroup dialogView = (ViewGroup) settingsDialog.getWindow().getDecorView();
            for (int i = 0; i < dialogView.getChildCount(); i++) {
                dialogView.getChildAt(i).setRotation(degrees);
            }
        }
    }

    protected int getVideoOptionCheckedIndex() {
        int checkedIndex = -1;

        final int mediaQuality = configurationProvider.getMediaQuality();
        final int passedMediaQuality = configurationProvider.getPassedMediaQuality();

        if (mediaQuality == Configuration.MEDIA_QUALITY_AUTO) checkedIndex = 0;
        else if (mediaQuality == Configuration.MEDIA_QUALITY_HIGH) checkedIndex = 1;
        else if (mediaQuality == Configuration.MEDIA_QUALITY_MEDIUM) checkedIndex = 2;
        else if (mediaQuality == Configuration.MEDIA_QUALITY_LOW) checkedIndex = 3;

        if (passedMediaQuality != Configuration.MEDIA_QUALITY_AUTO) checkedIndex--;

        return checkedIndex;
    }

    protected int getPhotoOptionCheckedIndex() {
        int checkedIndex = -1;

        final int mediaQuality = configurationProvider.getMediaQuality();

        if (mediaQuality == Configuration.MEDIA_QUALITY_HIGHEST) checkedIndex = 0;
        else if (mediaQuality == Configuration.MEDIA_QUALITY_HIGH) checkedIndex = 1;
        else if (mediaQuality == Configuration.MEDIA_QUALITY_MEDIUM) checkedIndex = 2;
        else if (mediaQuality == Configuration.MEDIA_QUALITY_LOWEST) checkedIndex = 3;
        return checkedIndex;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void takePhoto(CameraFragmentResultListener callback) {
        if (Build.VERSION.SDK_INT > MIN_VERSION_ICECREAM) {
            new MediaActionSound().play(MediaActionSound.SHUTTER_CLICK);
        }
        setRecordState(Record.TAKE_PHOTO_STATE);
        this.cameraController.takePhoto(callback);
        if (cameraFragmentStateListener != null) {
            cameraFragmentStateListener.onRecordStatePhoto();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void startRecording() {
        if (Build.VERSION.SDK_INT > MIN_VERSION_ICECREAM) {
            new MediaActionSound().play(MediaActionSound.START_VIDEO_RECORDING);
        }

        setRecordState(Record.RECORD_IN_PROGRESS_STATE);
        this.cameraController.startVideoRecord();

        if (cameraFragmentStateListener != null) {
            cameraFragmentStateListener.onRecordStateVideoInProgress();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void stopRecording(CameraFragmentResultListener callback) {
        if (Build.VERSION.SDK_INT > MIN_VERSION_ICECREAM) {
            new MediaActionSound().play(MediaActionSound.STOP_VIDEO_RECORDING);
        }

        setRecordState(Record.READY_FOR_RECORD_STATE);
        this.cameraController.stopVideoRecord(callback);

        this.onStopVideoRecord(callback);

        if (cameraFragmentStateListener != null) {
            cameraFragmentStateListener.onRecordStateVideoReadyForRecord();
        }
    }

    protected void clearCameraPreview() {
        if (previewContainer != null)
            previewContainer.removeAllViews();
    }

    protected void setCameraPreview(View preview, Size previewSize) {
        //onCameraControllerReady()
        videoQualities = cameraController.getVideoQualityOptions();
        photoQualities = cameraController.getPhotoQualityOptions();

        if (previewContainer == null || preview == null) return;
        previewContainer.removeAllViews();
        previewContainer.addView(preview);

        previewContainer.setAspectRatio(previewSize.getHeight() / (double) previewSize.getWidth());
    }

    protected void setMediaFilePath(final File mediaFile) {
        this.mediaFilePath = mediaFile.toString();
    }

    protected void onStartVideoRecord(final File mediaFile) {
        setMediaFilePath(mediaFile);
        if (maxVideoFileSize > 0) {

            if (cameraFragmentVideoRecordTextListener != null) {
                cameraFragmentVideoRecordTextListener.setRecordSizeText(maxVideoFileSize, "1Mb" + " / " + maxVideoFileSize / (1024 * 1024) + "Mb");
                cameraFragmentVideoRecordTextListener.setRecordSizeTextVisible(true);
            }
            try {
                fileObserver = new FileObserver(this.mediaFilePath) {
                    private long lastUpdateSize = 0;

                    @Override
                    public void onEvent(int event, String path) {
                        final long fileSize = mediaFile.length() / (1024 * 1024);
                        if ((fileSize - lastUpdateSize) >= 1) {
                            lastUpdateSize = fileSize;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (cameraFragmentVideoRecordTextListener != null) {
                                        cameraFragmentVideoRecordTextListener.setRecordSizeText(maxVideoFileSize, fileSize + "Mb" + " / " + maxVideoFileSize / (1024 * 1024) + "Mb");
                                    }
                                }
                            });
                        }
                    }
                };
                fileObserver.startWatching();
            } catch (Exception e) {
                Log.e("FileObserver", "setMediaFilePath: ", e);
            }
        }

        if (countDownTimer == null) {
            this.countDownTimer = new TimerTask(timerCallBack);
        }
        countDownTimer.start();

        if (cameraFragmentStateListener != null) {
            cameraFragmentStateListener.onStartVideoRecord(mediaFile);
        }
    }

    protected void onStopVideoRecord(@Nullable CameraFragmentResultListener callback) {
        if (cameraFragmentControlsListener != null) {
            cameraFragmentControlsListener.allowRecord(false);
        }
        if (cameraFragmentStateListener != null) {
            cameraFragmentStateListener.onStopVideoRecord();
        }
        setRecordState(Record.READY_FOR_RECORD_STATE);

        if (fileObserver != null)
            fileObserver.stopWatching();

        if (countDownTimer != null) {
            countDownTimer.stop();
        }

        final int mediaAction = configurationProvider.getMediaAction();
        if (cameraFragmentControlsListener != null) {
            if (mediaAction != Configuration.MEDIA_ACTION_UNSPECIFIED) {
                cameraFragmentControlsListener.setMediaActionSwitchVisible(false);
            } else {
                cameraFragmentControlsListener.setMediaActionSwitchVisible(true);
            }
        }

        final String filePath = this.cameraController.getOutputFile().toString();
        if (cameraFragmentResultListener != null) {
            cameraFragmentResultListener.onVideoRecorded(filePath);
        }

        if (callback != null) {
            callback.onVideoRecorded(filePath);
        }
    }

    @Override
    public void setStateListener(CameraFragmentStateListener cameraFragmentStateListener) {
        this.cameraFragmentStateListener = cameraFragmentStateListener;
    }

    @Override
    public void setTextListener(CameraFragmentVideoRecordTextListener cameraFragmentVideoRecordTextListener) {
        this.cameraFragmentVideoRecordTextListener = cameraFragmentVideoRecordTextListener;
    }

    @Override
    public void setControlsListener(CameraFragmentControlsListener cameraFragmentControlsListener) {
        this.cameraFragmentControlsListener = cameraFragmentControlsListener;
    }

    @Override
    public void setResultListener(CameraFragmentResultListener cameraFragmentResultListener) {
        this.cameraFragmentResultListener = cameraFragmentResultListener;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelable(IMAGE_INFO, mImageParameters);
        super.onSaveInstanceState(outState);
    }
}
