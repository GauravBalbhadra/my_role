package com.myrole.camera_ck;

import android.Manifest;

import androidx.annotation.RequiresPermission;

import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.internal.ui.BaseAnncaFragment;

public class CameraFragment extends BaseAnncaFragment {

    @RequiresPermission(Manifest.permission.CAMERA)
    public static CameraFragment newInstance(Configuration configuration) {
        return (CameraFragment) BaseAnncaFragment.newInstance(new CameraFragment(), configuration);
    }
}
