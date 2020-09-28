package com.myrole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.SimpleExoPlayer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final Boolean DEBUG = false;

    private SimpleExoPlayer mPlayer;
    private boolean mIsPlaying;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void openCamera(View view) {
        mRecordForResult.launch(new Intent(this, RecordingActivity.class));
    }

    ActivityResultLauncher<Intent> mRecordForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if(DEBUG) Log.d(TAG, result.getData().getStringArrayListExtra("records").get(3));
                    }
                }
            }
        );
}
