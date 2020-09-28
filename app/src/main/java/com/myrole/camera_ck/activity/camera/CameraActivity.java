package com.myrole.camera_ck.activity.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.myrole.R;
import com.myrole.camera_ck.CameraFragment;
import com.myrole.camera_ck.CameraFragmentApi;
import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.crop.CropImage;
import com.myrole.camera_ck.listeners.CameraFragmentControlsListener;
import com.myrole.camera_ck.listeners.CameraFragmentStateListener;
import com.myrole.camera_ck.listeners.CameraFragmentVideoRecordTextListener;
import com.myrole.camera_ck.widgets.CameraSettingsView;
import com.myrole.camera_ck.widgets.CameraSwitchView;
import com.myrole.camera_ck.widgets.FlashSwitchView;
import com.myrole.camera_ck.widgets.MediaActionSwitchView;
import com.myrole.camera_ck.widgets.RecordButton;

import java.io.File;

//import com.myrole.camera_ck.CameraFragment;
//import com.myrole.camera_ck.R;

public class CameraActivity extends AppCompatActivity implements CameraView, View.OnClickListener, CameraFragmentStateListener, CameraFragmentControlsListener,CameraFragmentVideoRecordTextListener
{
    public static final String FRAGMENT_TAG = "camera";
    public static final String TAG = CameraActivity.class.getSimpleName();

    public static final int REQUEST_CAMERA_PERMISSIONS = 931;
    public static final int REQUEST_PREVIEW_CODE = 1001;
    public static final int REQUEST_CAMERA = 1002;


    private CameraSettingsView settingsView;

    private FlashSwitchView flashSwitchView;

    private CameraSwitchView cameraSwitchView;

    private RecordButton recordButton;

    private MediaActionSwitchView mediaActionSwitchView;

    private TextView recordDurationText;

    private TextView recordSizeText;

    private View cameraLayout;

    private CameraPresenter mPresenter;

    private CameraFragment cameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initialize();
        getPresenter().renderCamera();
    }

    private void initialize() {
        //settingsView = (CameraSettingsView) findViewById(R.id.settings_view);
        flashSwitchView = (FlashSwitchView) findViewById(R.id.flash_switch_view);
        cameraSwitchView = (CameraSwitchView) findViewById(R.id.front_back_camera_switcher);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        mediaActionSwitchView = (MediaActionSwitchView) findViewById(R.id.photo_video_camera_switcher);
        recordDurationText = (TextView) findViewById(R.id.record_duration_text);
        recordSizeText = (TextView) findViewById(R.id.record_size_mb_text);
        cameraLayout = findViewById(R.id.cameraLayout);

        //settingsView.setOnClickListener(this);
        flashSwitchView.setOnClickListener(this);
        cameraSwitchView.setOnClickListener(this);
        recordButton.setOnClickListener(this);
        mediaActionSwitchView.setOnClickListener(this);
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public CameraPresenter getPresenter() {
        if (mPresenter == null)
            mPresenter = new CameraPresenter(this);
        return mPresenter;
    }

    @Override
    public CameraFragmentApi getCameraFragment() {
        return (CameraFragmentApi) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    @Override
    public void onRenderCamera() {
        cameraLayout.setVisibility(View.VISIBLE);

        cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                .setCamera(Configuration.CAMERA_FACE_REAR)
                .setSquareMode(true)
                .setMediaAction(getMediaAction())
                .build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();
        if(cameraFragment != null)
        {
            cameraFragment.setStateListener(this);
            cameraFragment.setControlsListener(this);
            cameraFragment.setTextListener(this);
        }
    }

    @Override
    public int getMediaAction()
    {
        int mediaAction = getIntent().getIntExtra(Configuration.MEDIA_ACTION, Configuration.MEDIA_ACTION_PHOTO);
        setMediaActionSwitchVisible(false);
        return mediaAction;
    }

    @Override
    public void onCurrentCameraBack() {
        cameraSwitchView.displayBackCamera();
    }

    @Override
    public void onCurrentCameraFront() {
        cameraSwitchView.displayFrontCamera();
    }

    @Override
    public void onFlashAuto() {
        flashSwitchView.displayFlashAuto();
    }

    @Override
    public void onFlashOn() {
        flashSwitchView.displayFlashOn();
    }

    @Override
    public void onFlashOff() {
        flashSwitchView.displayFlashOff();
    }

    @Override
    public void onCameraSetupForPhoto() {
        mediaActionSwitchView.displayActionWillSwitchVideo();

        recordButton.displayPhotoState();
        flashSwitchView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCameraSetupForVideo() {
        mediaActionSwitchView.displayActionWillSwitchPhoto();

        recordButton.displayVideoRecordStateReady();
        flashSwitchView.setVisibility(View.GONE);
    }

    @Override
    public void shouldRotateControls(int degrees) {
        ViewCompat.setRotation(cameraSwitchView, degrees);
        ViewCompat.setRotation(mediaActionSwitchView, degrees);
        ViewCompat.setRotation(flashSwitchView, degrees);
        ViewCompat.setRotation(recordDurationText, degrees);
        ViewCompat.setRotation(recordSizeText, degrees);
    }

    @Override
    public void onRecordStateVideoReadyForRecord() {
        recordButton.displayVideoRecordStateReady();
    }

    @Override
    public void onRecordStateVideoInProgress() {
        recordButton.displayVideoRecordStateInProgress();
    }

    @Override
    public void onRecordStatePhoto() {
        recordButton.displayPhotoState();
    }

    @Override
    public void onStopVideoRecord() {
        recordSizeText.setVisibility(View.GONE);
        //cameraSwitchView.setVisibility(View.VISIBLE);
        //settingsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartVideoRecord(File outputFile)
    {

    }

    @Override
    public void lockControls() {
        cameraSwitchView.setEnabled(false);
        recordButton.setEnabled(false);
        //settingsView.setEnabled(false);
        flashSwitchView.setEnabled(false);
    }

    @Override
    public void unLockControls() {
        cameraSwitchView.setEnabled(true);
        recordButton.setEnabled(true);
        //settingsView.setEnabled(false);
        flashSwitchView.setEnabled(true);
    }

    @Override
    public void allowCameraSwitching(boolean allow) {
        cameraSwitchView.setVisibility(allow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void allowRecord(boolean allow) {
        recordButton.setEnabled(allow);
    }

    @Override
    public void setMediaActionSwitchVisible(boolean visible) {
        mediaActionSwitchView.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setRecordSizeText(long size, String text) {
        recordSizeText.setText(text);
    }

    @Override
    public void setRecordSizeTextVisible(boolean visible) {
        recordSizeText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRecordDurationText(String text) {
        recordDurationText.setText(text);
    }

    @Override
    public void setRecordDurationTextVisible(boolean visible) {
        recordDurationText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == R.id.settings_view) {
            getPresenter().onSettingsClicked();
        } else if (i == R.id.flash_switch_view) {
            getPresenter().onFlashSwitchClicked();
        } else if (i == R.id.front_back_camera_switcher) {
            getPresenter().onSwitchCameraClicked();
        } else if (i == R.id.record_button) {
            getPresenter().onRecordButtonClicked();
        } else if (i == R.id.photo_video_camera_switcher) {
            getPresenter().onMediaActionSwitchClicked();
        }

//        switch (v.getId())
//        {
//            case R.id.settings_view:
//                getPresenter().onSettingsClicked();
//                break;
//            case R.id.flash_switch_view:
//                getPresenter().onFlashSwitchClicked();
//                break;
//            case R.id.front_back_camera_switcher:
//                getPresenter().onSwitchCameraClicked();
//                break;
//            case R.id.record_button:
//                getPresenter().onRecordButtonClicked();
//                break;
//            case R.id.photo_video_camera_switcher:
//                getPresenter().onMediaActionSwitchClicked();
//                break;
//
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            onRenderCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String uri=String.valueOf(selectedImage);
//
//                    String picturePath = getPath(getApplicationContext(), selectedImage);
//
//                    Intent returnIntent = new Intent(this,HomeActivity.class);
//                    returnIntent.putExtra("result",picturePath);
//                    //   setResult(Config.CAMERAIMAGE,returnIntent);
//                    startActivityForResult(returnIntent, Config.CAMERAIMAGE);
                 //   Toast.makeText(this,"inside crop"+uri,Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK,data);
                   // Intent returnIntent = new Intent(this,HomeActivity.class);
                    //startActivityForResult(returnIntent, Config.CAMERAIMAGE);
                    finish();
                }
                break;
        }
    }
    //     that can gat the path of image selected to galeery
    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

}
