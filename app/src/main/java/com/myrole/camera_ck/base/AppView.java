package com.myrole.camera_ck.base;


import android.app.Activity;

/**
 * Created by intel on 07-Jul-17.
 */

public interface AppView extends BaseView
{
    Activity getActivity();
    AppPresenter getPresenter();
}
