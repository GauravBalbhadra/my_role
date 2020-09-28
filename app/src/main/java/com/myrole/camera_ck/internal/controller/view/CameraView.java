package com.myrole.camera_ck.internal.controller.view;


import android.view.View;

import androidx.annotation.Nullable;

import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.internal.utils.Size;
import com.myrole.camera_ck.listeners.CameraFragmentResultListener;

/*
 * Created by memfis on 7/6/16.
 */
public interface CameraView {

    void updateCameraPreview(Size size, View cameraPreview);

    void updateUiForMediaAction(@Configuration.MediaAction int mediaAction);

    void updateCameraSwitcher(int numberOfCameras);

    void onPhotoTaken(byte[] bytes, @Nullable CameraFragmentResultListener callback);

    void onVideoRecordStart(int width, int height);

    void onVideoRecordStop(@Nullable CameraFragmentResultListener callback);

    void releaseCameraPreview();

}
