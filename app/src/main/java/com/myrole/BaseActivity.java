package com.myrole;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.myrole.utils.Config;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    final static String TAG = "MYROLE";
    private ProgressDialog dialog;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.colorPrimaryDark);
        progressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleSmall);
        progressBar.setIndeterminate(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
    }

    public void showProgessDialog() {
        if (dialog != null && !dialog.isShowing() && !this.isFinishing())
            dialog.show();
    }

    public void showProgessDialog(String message) {
        if (dialog != null && !dialog.isShowing() && !this.isFinishing()) {
            dialog.setMessage(message);
            dialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing() && !this.isFinishing())
            dialog.dismiss();
    }

 /*   public void showProgessBar() {
//        if (progressBar != null && !progressBar.isShown() && !this.isFinishing())

            progressBar.setVisibility(View.VISIBLE);
    }


    public void dismissProgressBar() {
//        if (progressBar != null && !progressBar.isShown() && !this.isFinishing())

            progressBar.setVisibility(View.GONE);
    }
*/


    @Override
    protected void onStart() {
        super.onStart();
        Config.APP_IN_FRONT = true;
        Log.v(TAG, "Started");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Config.APP_IN_FRONT = false;
        Log.v(TAG, "Paused");

    }

    protected void setStatusBarColor(int colorId)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorId));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
