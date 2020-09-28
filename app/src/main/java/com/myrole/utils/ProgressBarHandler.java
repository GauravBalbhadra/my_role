package com.myrole.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

//import com.gs.instavideorecorder.FFmpegRecordActivity;
//import com.myrole.ImageCropActivity;

/**
 * Created by Vikesh on 06-05-2016.
 */
public class ProgressBarHandler {

    private ProgressBar mProgressBar;
    private Activity mActivity;

    public ProgressBarHandler(Activity activity) {
        mActivity = activity;

        ViewGroup layout = (ViewGroup) mActivity.findViewById(android.R.id.content).getRootView();

        mProgressBar = new ProgressBar(mActivity, null, android.R.attr.progressBarStyle);
        mProgressBar.setIndeterminate(true);

        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(mActivity);

        rl.setGravity(Gravity.CENTER);
        rl.addView(mProgressBar);

        layout.addView(rl, params);

        hide();
    }

    public void show() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
