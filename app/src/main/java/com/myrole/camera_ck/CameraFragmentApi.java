package com.myrole.camera_ck;

import com.myrole.camera_ck.listeners.CameraFragmentControlsListener;
import com.myrole.camera_ck.listeners.CameraFragmentResultListener;
import com.myrole.camera_ck.listeners.CameraFragmentStateListener;
import com.myrole.camera_ck.listeners.CameraFragmentVideoRecordTextListener;

/*
 * Created by florentchampigny on 16/01/2017.
 */

public interface CameraFragmentApi {

    void takePhotoOrCaptureVideo(CameraFragmentResultListener resultListener);

    void openSettingDialog();

    void switchCameraTypeFrontBack();

    void switchActionPhotoVideo();

    void toggleFlashMode();

    boolean isSquareMode();

    void setStateListener(CameraFragmentStateListener cameraFragmentStateListener);

    void setTextListener(CameraFragmentVideoRecordTextListener cameraFragmentVideoRecordTextListener);

    void setControlsListener(CameraFragmentControlsListener cameraFragmentControlsListener);

    void setResultListener(CameraFragmentResultListener cameraFragmentResultListener);

}
