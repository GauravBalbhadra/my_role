package com.myrole.camera_ck.activity.camera;

import com.myrole.camera_ck.CameraFragmentApi;
import com.myrole.camera_ck.base.AppView;

/**
 * Created by intel on 17-Jul-17.
 */

public interface CameraView extends AppView
{
    CameraFragmentApi getCameraFragment();

    void onRenderCamera();

    int getMediaAction();
}
