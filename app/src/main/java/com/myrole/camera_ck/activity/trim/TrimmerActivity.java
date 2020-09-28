package com.myrole.camera_ck.activity.trim;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myrole.R;
import com.myrole.camera_ck.trimmer.K4LVideoTrimmer;
import com.myrole.camera_ck.trimmer.interfaces.OnK4LVideoListener;
import com.myrole.camera_ck.trimmer.interfaces.OnTrimVideoListener;
import com.myrole.utils.Utils;

import java.io.File;

//import com.myrole.camera_ck.R;


public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener, OnK4LVideoListener {

    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    private K4LVideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer2);

        Intent extraIntent = getIntent();
        String path = "";

        if (extraIntent != null) {
            path = extraIntent.getStringExtra(EXTRA_VIDEO_PATH);
        }

        //setting progressbar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.trimming_progress));

        mVideoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        if (mVideoTrimmer != null) {
            mVideoTrimmer.setMaxDuration(60);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setOnK4LVideoListener(this);
            //mVideoTrimmer.setDestinationPath("/storage/emulated/0/DCIM/CameraCustom/");
            mVideoTrimmer.setVideoURI(Uri.parse(path));
            mVideoTrimmer.setVideoInformationVisibility(true);
        }
    }

    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(final Uri uri) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               /// Toast.makeText(TrimmerActivity.this, getString(R.string.video_saved_at, uri.getPath()), Toast.LENGTH_SHORT).show();
            }
        });
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra("yoyo",uri.getPath());
//        startActivityForResult(intent, Config.GALLERYVIDEO);
        startPreviewActivity(new File(uri.getPath()),uri.getPath(),uri);
    }

    protected void startPreviewActivity(File mVideoOutputFile,String path,Uri uri) {
       // Intent previewIntent = new Intent(this, HomeActivity.class);
//        previewIntent.putExtra("some_key", path);
//      //  startActivityForResult( Config.GALLERYVIDEO,Activity.RESULT_OK,previewIntent);
//        startActivityForResult(previewIntent,Config.REQUEST_VIDEO_GALLERY);
//        startactivi
        String realpath= Utils.getRealPathFromURI(this, uri);
        Intent resultIntent = new Intent();
// TODO Add extras or a data URI to this intent as appropriate.
        resultIntent.putExtra("some_key", realpath);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
//        Intent previewIntent = new Intent(this, FFmpegPreviewActivity.class);
//        previewIntent.putExtra(FFmpegPreviewActivity.REQUEST_PARAMS_KEY, FFmpegPreviewActivityParams.builder()
//                        .setVideoFileUri(mVideoOutputFile)
//                        .setThemeParams(ActivityThemeParams.Builder.setOnlyClassDefaults(
//                        ActivityThemeParams.builder(), this).build())
//                        .setConfirmation(true)
//                        .build());
//        startActivityForResult(previewIntent, PREVIEW_ACTIVITY_RESULT);
    }


    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        mVideoTrimmer.destroy();
        finish();
    }

    @Override
    public void onError(final String message) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrimmerActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVideoPrepared() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrimmerActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
