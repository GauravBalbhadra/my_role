// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.myrole.camera_ck.crop;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.myrole.R;
import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.internal.utils.CameraHelper;

import java.io.File;

//import com.myrole.camera_ck.R;

/**
 * Built-in activity for image cropping.<br>
 * Use {@link CropImage#activity(Uri)} to create a builder to start this activity.
 */
public class CropImageActivity extends AppCompatActivity implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnCropImageCompleteListener, View.OnClickListener {

    /**
     * The crop image view library widget used in the activity
     */
    private CropImageView mCropImageView;

    /**
     * the options that were set for the crop image
     */
    private CropImageOptions mOptions;

    private View cropMediaAction;
    private View rotateRightMediaAction;
    private View rotateLeftMediaAction;
    private View cancelMediaAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        mCropImageView = (CropImageView) findViewById(R.id.cropImageView);
        cropMediaAction = findViewById(R.id.crop_image);
        rotateLeftMediaAction = findViewById(R.id.rotate_media_left);
        rotateRightMediaAction = findViewById(R.id.rotate_media_right);
        cancelMediaAction = findViewById(R.id.cancel_media_action);


        Intent intent = getIntent();
        Uri source = intent.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_SOURCE);
        mOptions = intent.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_OPTIONS);
        mOptions.inputUri = source;

        if (savedInstanceState == null) {
            mCropImageView.setImageUriAsync(source);
        }

        if (!mOptions.allowRotation) {
            rotateLeftMediaAction.setVisibility(View.GONE);
            rotateRightMediaAction.setVisibility(View.GONE);
        } else if (mOptions.allowCounterRotation) {
            rotateLeftMediaAction.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
        cropMediaAction.setOnClickListener(this);
        rotateLeftMediaAction.setOnClickListener(this);
        rotateRightMediaAction.setOnClickListener(this);
        cancelMediaAction.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCropImageView.setOnSetImageUriCompleteListener(null);
        mCropImageView.setOnCropImageCompleteListener(null);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deleteMediaFile();
        setResultCancel();
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            if (mOptions.initialCropWindowRectangle != null) {
                mCropImageView.setCropRect(mOptions.initialCropWindowRectangle);
            }
            if (mOptions.initialRotation > -1) {
                mCropImageView.setRotatedDegrees(mOptions.initialRotation);
            }
        } else {
            setResult(null, error, 1);
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        setResult(result.getUri(), result.getError(), result.getSampleSize());
    }

    //region: Private methods

    /**
     * Execute crop image and save the result tou output uri.
     */
    protected void cropImage() {
        if (mOptions.noOutputImage) {
            setResult(null, null, 1);
        } else {
            Uri outputUri = getOutputUri();
            mCropImageView.saveCroppedImageAsync(outputUri,
                    mOptions.outputCompressFormat,
                    mOptions.outputCompressQuality,
                    mOptions.outputRequestWidth,
                    mOptions.outputRequestHeight,
                    mOptions.outputRequestSizeOptions);
        }
    }

    /**
     * Rotate the image in the crop image view.
     */
    protected void rotateImage(int degrees) {
        mCropImageView.rotateImage(degrees);
    }

    /**
     * Get Android uri to save the cropped image into.<br>
     * Use the given in options or create a temp file.
     */
    protected Uri getOutputUri() {
        Uri outputUri = mOptions.outputUri;
        if (outputUri.equals(Uri.EMPTY)) {
            String ext = mOptions.outputCompressFormat == Bitmap.CompressFormat.JPEG ? ".jpg" :
                    mOptions.outputCompressFormat == Bitmap.CompressFormat.PNG ? ".png" : ".webp";
            outputUri = Uri.fromFile(CameraHelper.getOutputMediaFile(Configuration.MEDIA_ACTION_PHOTO, ext));
        }
        return outputUri;
    }

    /**
     * Result with cropped image data or error if failed.
     */
    protected void setResult(Uri uri, Exception error, int sampleSize) {
        int resultCode = error == null ? RESULT_OK : CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE;
        setResult(resultCode, getResultIntent(uri, error, sampleSize));
        deleteMediaFile();
        finish();
    }

    /**
     * Cancel of cropping activity.
     */
    protected void setResultCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    protected boolean deleteMediaFile() {
        if ( mOptions.deleteImage && !mOptions.inputUri.equals(Uri.EMPTY)) {
            File mediaFile = new File(mOptions.inputUri.getPath());
            return mediaFile.delete();
        } else {
            return false;
        }
    }

    /**
     * Get intent instance to be used for the result of this activity.
     */
    protected Intent getResultIntent(Uri uri, Exception error, int sampleSize) {
        CropImage.ActivityResult result = new CropImage.ActivityResult(null,
                uri,
                error,
                mCropImageView.getCropPoints(),
                mCropImageView.getCropRect(),
                mCropImageView.getRotatedDegrees(),
                sampleSize);
       // Uri selectedImage = data.getData();
//
//                    String picturePath = getPath(getApplicationContext(), uri);
//
//                    Intent returnIntent = new Intent(this,HomeActivity.class);
//                    returnIntent.putExtra("result",picturePath);
//                    //   setResult(Config.CAMERAIMAGE,returnIntent);
//                    startActivityForResult(returnIntent, Config.CAMERAIMAGE);
        Intent intent = new Intent();
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, result);
        return intent;
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
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.crop_image) {
            cropImage();
        } else if (i == R.id.cancel_media_action) {
            onBackPressed();
        } else if (i == R.id.rotate_media_left) {
            rotateImage(-mOptions.rotationDegrees);
        } else if (i == R.id.rotate_media_right) {
            rotateImage(-mOptions.rotationDegrees);
        }

//        switch (v.getId()) {
//            case R.id.crop_image:
//                cropImage();
//                break;
//            case R.id.cancel_media_action:
//                onBackPressed();
//                break;
//            case R.id.rotate_media_left:
//                rotateImage(-mOptions.rotationDegrees);
//                break;
//            case R.id.rotate_media_right:
//                rotateImage(-mOptions.rotationDegrees);
//                break;
//        }
    }
    //endregion
}

