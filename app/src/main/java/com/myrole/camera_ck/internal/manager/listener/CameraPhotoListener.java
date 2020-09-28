package com.myrole.camera_ck.internal.manager.listener;

import com.myrole.camera_ck.listeners.CameraFragmentResultListener;

import java.io.File;

/*
 * Created by memfis on 8/14/16.
 */
public interface CameraPhotoListener {
    void onPhotoTaken(byte[] bytes, File photoFile, CameraFragmentResultListener callback);

    void onPhotoTakeError();
}
