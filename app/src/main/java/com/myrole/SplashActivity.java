package com.myrole;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;

public class SplashActivity extends BaseActivity implements Animation.AnimationListener {
    ImageView logo;
    Animation animScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.logo);
        animScale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        animScale.setAnimationListener(this);
        logo.startAnimation(animScale);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                AppPreferences preferences = AppPreferences.getAppPreferences(getApplicationContext());
                String userId = preferences.getStringValue(AppPreferences.USER_ID);
                Log.v(TAG, "userId=" + userId);
                if (TextUtils.isEmpty(userId)) {
                    //startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                    startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
                    finishAffinity();
                } else {
                    startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
                    finishAffinity();
                }
            }
        }, Config.SPLASH_TIME);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
