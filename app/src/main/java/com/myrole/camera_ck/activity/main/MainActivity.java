package com.myrole.camera_ck.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myrole.camera_ck.activity.camera.CameraActivity;
import com.myrole.camera_ck.activity.preview.PreviewActivity;
import com.myrole.camera_ck.activity.trim.TrimmerActivity;
import com.myrole.camera_ck.crop.CropImage;
import com.myrole.camera_ck.trimmer.utils.FileUtils;

import static com.myrole.camera_ck.activity.camera.CameraActivity.REQUEST_CAMERA;
import static com.myrole.camera_ck.activity.trim.TrimmerActivity.EXTRA_VIDEO_PATH;

//import com.myrole.camera_ck.R;

/**
 * Created by intel on 18-Jul-17.
 */

public class MainActivity extends AppCompatActivity implements MainView
{
    public static final int REQUEST_IMAGE_GALLERY= 2001;
    public static final int REQUEST_VIDEO_GALLERY= 2002;
    public static final int REQUEST_RECORD_VIDEO = 2000;

    public static final int MIN_TIME = 10;
    public static final int MAX_TIME = 60;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        getPresenter().showDialog();
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
    public MainPresenter getPresenter()
    {
        if(mPresenter == null)
            mPresenter = new MainPresenter(this);
        return mPresenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_IMAGE_GALLERY:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if(data != null && data.getData() != null)
                    {
                        CropImage.activity(data.getData())
                                .setFixAspectRatio(true)
                                .setDeleteImage(false)
                                .start(getActivity());
                    }
                }
                break;
            case REQUEST_VIDEO_GALLERY:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if(data != null && data.getData() != null)
                    {
                        Intent intent = new Intent(this, TrimmerActivity.class);
                        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(this, data.getData()));
                        startActivity(intent);
                    }
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK && result != null && result.getUri()!= null) {
                    Intent intent = PreviewActivity.newIntentPhoto(getContext(), FileUtils.getPath(this, result.getUri()));
                    startActivityForResult(intent, CameraActivity.REQUEST_PREVIEW_CODE);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
            case REQUEST_RECORD_VIDEO:
//                switch (resultCode) {
//                    case RESULT_OK:
//                        Uri videoUri = data.getData();
//                        Uri thumbnailUri = data.getParcelableExtra(FFmpegRecorderActivity.RESULT_THUMBNAIL_URI_KEY);
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        break;
//                    case FFmpegRecorderActivity.RESULT_ERROR:
//                        Exception error = (Exception) data.getSerializableExtra(FFmpegRecorderActivity.RESULT_ERROR_PATH_KEY);
//                        new AlertDialog.Builder(getContext())
//                                .setCancelable(false)
//                                .setTitle(R.string.error_label)
//                                .setMessage(error.getLocalizedMessage())
//                                .setPositiveButton(R.string.ok_label, null)
//                                .show();
//                        break;
//                }
                break;
            case REQUEST_CAMERA:
                result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK && result != null && result.getUri()!= null) {
                    Intent intent = PreviewActivity.newIntentPhoto(getContext(), FileUtils.getPath(this, result.getUri()));
                    startActivityForResult(intent, CameraActivity.REQUEST_PREVIEW_CODE);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
        }
    }
}
