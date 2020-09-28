package com.myrole.camera_ck.base;

import android.app.Activity;
import android.content.Context;


/**
 * Created by intel on 07-Jul-17.
 */

public abstract class AppPresenter<V extends AppView> implements BasePresenter<V>
{
    V view;

    public AppPresenter(V view)
    {
        attachView(view);
    }

    @Override
    public V getView() {
        return view;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView()
    {
        view = null;
    }

    @Override
    public Context getContext() {
        return getView().getContext();
    }

    @Override
    public String getString(int resId) {
        return getView().getString(resId);
    }

    @Override
    public String getString(int resId, Object... args) {
        return getView().getString(resId,args);
    }

    public Activity getActivity()
    {
        return getView().getActivity();
    }

}
