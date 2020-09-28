package com.myrole.camera_ck.base;

import android.content.Context;

/**
 * Created by intel on 07-Jul-17.
 */

public interface BaseView
{
    Context getContext();

    String getString(int resId);

    String getString(int resId, Object... args);



}
