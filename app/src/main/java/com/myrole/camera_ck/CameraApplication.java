package com.myrole.camera_ck;



import androidx.multidex.MultiDexApplication;

import com.myrole.camera_ck.internal.utils.CameraHelper;

/**
 * Created by intel on 17-Jul-17.
 */

public class CameraApplication extends MultiDexApplication
{
    @Override
    public void onCreate() {
        super.onCreate();
        CameraHelper.createApplicationFolder();
    }
}
