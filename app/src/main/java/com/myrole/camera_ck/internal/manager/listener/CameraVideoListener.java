package com.myrole.camera_ck.internal.manager.listener;

import com.myrole.camera_ck.internal.utils.Size;
import com.myrole.camera_ck.listeners.CameraFragmentResultListener;

import java.io.File;

/*
 * Created by memfis on 8/14/16.
 */
public interface CameraVideoListener {
    void onVideoRecordStarted(Size videoSize);

    void onVideoRecordStopped(File videoFile, CameraFragmentResultListener callback);

    void onVideoRecordError();
}
