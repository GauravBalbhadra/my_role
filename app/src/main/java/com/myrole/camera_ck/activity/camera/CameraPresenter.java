package com.myrole.camera_ck.activity.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.myrole.camera_ck.CameraFragmentApi;
import com.myrole.camera_ck.activity.preview.PreviewActivity;
import com.myrole.camera_ck.base.AppPresenter;
import com.myrole.camera_ck.crop.CropImage;
import com.myrole.camera_ck.listeners.CameraFragmentResultAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 17-Jul-17.
 */

public class CameraPresenter extends AppPresenter<CameraView>
{


    public CameraPresenter(CameraView view) {
        super(view);
    }

    public void renderCamera() {
        if (Build.VERSION.SDK_INT > 15) {
            final String[] permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};

            final List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(), permissionsToRequest.toArray(new String[permissionsToRequest.size()]), CameraActivity.REQUEST_CAMERA_PERMISSIONS);
            } else getView().onRenderCamera();
        } else {
            getView().onRenderCamera();
        }
    }


    public void onFlashSwitchClicked() {
        final CameraFragmentApi cameraFragment = getView().getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.toggleFlashMode();
        }
    }


    public void onSwitchCameraClicked() {
        final CameraFragmentApi cameraFragment = getView().getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.switchCameraTypeFrontBack();
        }
    }


    public void onRecordButtonClicked() {
        final CameraFragmentApi cameraFragment = getView().getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultAdapter() {
                @Override
                    public void onVideoRecorded(String filePath) {
                        Intent intent = PreviewActivity.newIntentVideo(getContext(), filePath);
                        getActivity().startActivityForResult(intent, CameraActivity.REQUEST_PREVIEW_CODE);
                    }

                    @Override
                    public void onPhotoTaken(byte[] bytes, String filePath) {
                        CropImage.activity(Uri.fromFile(new File(filePath)))
                                .setFixAspectRatio(true)
                                .setDeleteImage(true)
                                .start(getActivity());
                        /*Intent intent = PreviewActivity.newIntentPhoto(getContext(), filePath);
                        getActivity().startActivityForResult(intent, CameraActivity.REQUEST_PREVIEW_CODE);*/
                    }
            });
        }
    }


    public void onSettingsClicked() {
        final CameraFragmentApi cameraFragment = getView().getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.openSettingDialog();
        }
    }


    public void onMediaActionSwitchClicked() {
        final CameraFragmentApi cameraFragment = getView().getCameraFragment();
        if (cameraFragment != null) {
            cameraFragment.switchActionPhotoVideo();
        }
    }

}
