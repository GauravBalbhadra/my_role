package com.myrole.camera_ck.internal.controller;

import android.os.Bundle;

import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.internal.manager.CameraManager;
import com.myrole.camera_ck.listeners.CameraFragmentResultListener;

import java.io.File;

/*
 * Created by memfis on 7/6/16.
 */
public interface CameraController<CameraId> {

    void onCreate(Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onDestroy();

    void takePhoto(CameraFragmentResultListener resultListener);

    void startVideoRecord();

    void stopVideoRecord(CameraFragmentResultListener callback);

    boolean isVideoRecording();

    void switchCamera(@Configuration.CameraFace int cameraFace);

    void switchQuality();

    void setFlashMode(@Configuration.FlashMode int flashMode);

    int getNumberOfCameras();

    @Configuration.MediaAction
    int getMediaAction();

    CameraId getCurrentCameraId();

    File getOutputFile();

    CameraManager getCameraManager();

    CharSequence[] getVideoQualityOptions();

    CharSequence[] getPhotoQualityOptions();
}
