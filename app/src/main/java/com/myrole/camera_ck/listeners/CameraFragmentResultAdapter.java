package com.myrole.camera_ck.listeners;

/**
 * Convenience implementation of {@link CameraFragmentResultListener}. Derive from this and only override what you need.
 * @author Skala
 */

public class CameraFragmentResultAdapter implements CameraFragmentResultListener {
    @Override
    public void onVideoRecorded(String filePath) {

    }

    @Override
    public void onPhotoTaken(byte[] bytes, String filePath) {

    }
}
