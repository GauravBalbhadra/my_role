package com.myrole.camera_ck.activity.video;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import com.myrole.R;
import com.myrole.camera_ck.activity.video.common.ImageSize;
import com.myrole.camera_ck.activity.video.conroller.CameraControllerI;
import com.myrole.camera_ck.widgets.CameraPreviewView;

import java.util.concurrent.TimeUnit;

import static com.myrole.camera_ck.activity.camera.CameraActivity.REQUEST_PREVIEW_CODE;
import static com.myrole.camera_ck.activity.preview.PreviewActivity.ACTION_CANCEL;
import static com.myrole.camera_ck.activity.preview.PreviewActivity.ACTION_CONFIRM;
import static com.myrole.camera_ck.activity.preview.PreviewActivity.ACTION_RETAKE;
import static com.myrole.camera_ck.activity.preview.PreviewActivity.FILE_PATH_ARG;
import static com.myrole.camera_ck.activity.preview.PreviewActivity.RESPONSE_CODE_ARG;


/**
 * Created by Divine on 20-08-2017.
 */


public class VideoActivity extends AppCompatActivity implements VideoView {
    private static final String LOG_TAG = VideoPresenter.class.getSimpleName();

    public static final int RESULT_ERROR = RESULT_FIRST_USER;
    public static final String RESULT_ERROR_PATH_KEY = "error";

    protected CameraPreviewView mCameraPreviewView;

    //UI Components
    private AppCompatImageButton mRecordButton;
    private AppCompatImageButton mFlashButton;
    private AppCompatImageButton mSwitchCameraButton;
    private ProgressBar mProgressBar, mProgressVideo;
    private TextView mProgressText;
    private TextView mProgressTimer;
    private View mNextButton;
    private View mCancelButton;


    private VideoPresenter mPresenter;

    protected int mOriginalRequestedOrientation;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public VideoPresenter getPresenter() {
        if (mPresenter == null)
            mPresenter = new VideoPresenter(this);
        return mPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_video);

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getPresenter().initResources();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // getPresenter().releaseResources();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //getPresenter().detachView();
    }

    private void initialize() {
        mRecordButton = (AppCompatImageButton) findViewById(R.id.record_button);
        mFlashButton = (AppCompatImageButton) findViewById(R.id.flash_button);
        mSwitchCameraButton = (AppCompatImageButton) findViewById(R.id.switch_camera_button);
        mNextButton = findViewById(R.id.next_media_action);
        mCancelButton = findViewById(R.id.cancel_media_action);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressText = (TextView) findViewById(R.id.progress_text);
        mProgressTimer = (TextView) findViewById(R.id.progress_timer);

        mCameraPreviewView = (CameraPreviewView) findViewById(R.id.recorder_view);

        mCameraPreviewView = (CameraPreviewView) findViewById(R.id.recorder_view);
        mCameraPreviewView.getHolder().addCallback(this);
        mCameraPreviewView.setParams(getPresenter().getRecorderParams());
        mCameraPreviewView.setVisibility(View.INVISIBLE);

        mProgressVideo = (ProgressBar) findViewById(R.id.progressVideo);
        mProgressVideo.setMax((int) getPresenter().getMaxVideoLength());

        mFlashButton.setOnClickListener(this);
        mSwitchCameraButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mRecordButton.setOnTouchListener(this);

        mOriginalRequestedOrientation = getRequestedOrientation();
    }

    @Override
    public void setVisibility(int resId, int action) {
        switch (resId) {
            case R.id.progress_bar:
                mProgressBar.setVisibility(action);
                break;
            case R.id.progress_text:
                mProgressText.setVisibility(action);
                break;
            case R.id.next_media_action:
                mNextButton.setVisibility(action);
                break;
            case R.id.progressVideo:
                mProgressVideo.setVisibility(action);
                break;
            case R.id.switch_camera_button:
                mSwitchCameraButton.setVisibility(action);
                break;
        }
    }

    @Override
    public void updateCameraFace(CameraControllerI.Facing facing) {
        @DrawableRes int resId = facing == CameraControllerI.Facing.BACK
                ? R.drawable.ic_camera_front_white_24dp
                : R.drawable.ic_camera_rear_white_24dp;
        mSwitchCameraButton.setImageDrawable(ContextCompat.getDrawable(this, resId));
    }

    @Override
    public void updateCameraFlash(CameraControllerI.FlashMode flashMode) {
        CameraControllerI cameraManager = getPresenter().getCameraController();
        if (cameraManager.setFlashMode(flashMode)) {
            @DrawableRes int resId = flashMode == CameraControllerI.FlashMode.ON
                    ? R.drawable.ic_flash_on_white_24dp
                    : R.drawable.ic_flash_off_white_24dp;
            mFlashButton.setImageDrawable(ContextCompat.getDrawable(this, resId));
        }
    }

    @Override
    public void showProgress(@StringRes int progressTextRes) {
        if (!mProgressBar.isIndeterminate()) {
            mProgressBar.setIndeterminate(true);
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressText.setVisibility(View.VISIBLE);
        mProgressText.setText(progressTextRes);
        hideControls();
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mProgressText.setVisibility(View.GONE);
    }

    @Override
    public void hideUI() {
        mProgressVideo.setVisibility(View.GONE);
        hideControls();
    }

    @Override
    public void hideControls() {
        mRecordButton.setVisibility(View.INVISIBLE);
        mSwitchCameraButton.setVisibility(View.INVISIBLE);
        mFlashButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress(int cameraCount, boolean supportsFlashMode) {
        hideProgress();

        mCameraPreviewView.setVisibility(View.VISIBLE);
        mProgressVideo.setVisibility(View.VISIBLE);
        mRecordButton.setVisibility(View.VISIBLE);
        if (cameraCount > 1) {
            mSwitchCameraButton.setVisibility(View.VISIBLE);
        } else {
            mSwitchCameraButton.setVisibility(View.INVISIBLE);
        }
        if (supportsFlashMode) {
            mFlashButton.setVisibility(View.VISIBLE);
        } else {
            mFlashButton.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_media_action:
                onBackPressed();
                break;
            case R.id.switch_camera_button:
                getPresenter().doSwitchCamera();
                break;
            case R.id.flash_button:
                getPresenter().updateFlashMode();
                break;
            case R.id.next_media_action:
                getPresenter().saveRecording();
                break;
        }
    }

    @Override
    public void onCameraOpen() {

        getPresenter().onCameraOpen();
        getPresenter().setCameraPreviewDisplayIfReady(mCameraPreviewView.getHolder());
        getPresenter().getCameraController().startPreview();
    }

    @Override
    public void onCameraClose() {
        mCameraPreviewView.setPreviewSize(null);
    }

    @Override
    public void onCameraAdjustPreview(ImageSize imageSize) {
        mCameraPreviewView.setPreviewSize(imageSize);
    }

    @Override
    public void onCameraStartPreview() {
        getPresenter().onCameraStartPreview();

        Log.d(LOG_TAG, "Ready to record");
    }

    @Override
    public void onCameraStopPreview() {
        getPresenter().onCameraStopPreview();
        runOnUI(new Runnable() {
            @Override
            public void run() {
                mCameraPreviewView.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onPreviewFrame(byte[] data) {

        getPresenter().onPreviewFrame(data);
    }

    @Override
    public void onFlashModeChanged(CameraControllerI.FlashMode flashMode) {

    }

    @Override
    public void onCameraFocusOnRect(Rect rect) {

    }

    @Override
    public void onCameraAutoFocus() {

    }

    @Override
    public void onError(Exception e) {
        getPresenter().discardRecording();
        Intent intent = new Intent();
        intent.putExtra(RESULT_ERROR_PATH_KEY, e);
        setResult(RESULT_ERROR, intent);
        finish();
    }

    @Override
    public void runOnUI(Runnable runnable) {
        runOnUiThread(runnable);
    }

    @Override
    public void setTimer(long recordedTimeInMillis) {
        if (recordedTimeInMillis > 0)
            mProgressTimer.setVisibility(View.VISIBLE);
        mProgressVideo.setProgress((int) recordedTimeInMillis);
        mProgressTimer.setText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(recordedTimeInMillis)));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        getPresenter().setCameraPreviewDisplayIfReady(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (/*mSaveVideoTask == null &&*/ getPresenter().getCameraController().isCameraOpen()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    getPresenter().resumeRecording();
                    break;
                case MotionEvent.ACTION_UP:
                    getPresenter().pauseRecording();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getX();
                    float y = event.getY();
                    if (x < 0 || x > v.getWidth() || y < 0 || y > v.getHeight()) {
                        //stopRecordingAndPrepareForNext();
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getPresenter().isRecording()) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.are_you_sure)
                    .setMessage(R.string.discard_video_msg)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPresenter().discardRecording();
                            setResult(RESULT_CANCELED);
                            VideoActivity.super.finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        } else {
            getPresenter().discardRecording();
            setResult(RESULT_CANCELED);
            VideoActivity.super.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PREVIEW_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    switch (data.getIntExtra(RESPONSE_CODE_ARG, 0)) {
                        case ACTION_CONFIRM:
                            Intent intent = new Intent();
                            intent.setData(Uri.parse(data.getStringExtra(FILE_PATH_ARG)));
                            setResult(resultCode, intent);
                            finish();
                            break;
                        case ACTION_RETAKE:
                            getPresenter().discardRecording();
                            break;
                        case ACTION_CANCEL:
                            onBackPressed();
                            break;
                    }
                }

                break;
        }
    }
}
