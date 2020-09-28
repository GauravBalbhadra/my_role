package com.myrole.camera_ck.activity.video;


import android.view.SurfaceHolder;
import android.view.View;

import androidx.annotation.StringRes;

import com.myrole.camera_ck.activity.video.conroller.CameraControllerI;
import com.myrole.camera_ck.base.AppView;


/**
 * Created by Divine on 20-08-2017.
 */

public interface VideoView extends AppView, SurfaceHolder.Callback,View.OnClickListener,View.OnTouchListener, CameraControllerI.CameraListener, CameraControllerI.PreviewCallback
{
    void setVisibility(int resId, int action);
    void showProgress(@StringRes int progressTextRes);
    void hideProgress();
    void hideUI();
    void hideControls();
    void hideProgress(int cameraCount, boolean supportsFlashMode);
    void updateCameraFace(CameraControllerI.Facing facing);
    void updateCameraFlash(CameraControllerI.FlashMode flashMode);
    void onError(Exception e);
    void runOnUI(Runnable runnable);
    void setTimer(long recordedTimeInMillis);
}
