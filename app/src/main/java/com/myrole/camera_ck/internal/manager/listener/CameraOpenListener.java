package com.myrole.camera_ck.internal.manager.listener;

import com.myrole.camera_ck.internal.utils.Size;

/*
 * Created by memfis on 8/14/16.
 */
public interface CameraOpenListener<CameraId, SurfaceListener> {
    void onCameraOpened(CameraId openedCameraId, Size previewSize, SurfaceListener surfaceListener);

    void onCameraOpenError();
}
