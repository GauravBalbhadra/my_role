package com.myrole.camera_ck.activity.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.provider.MediaStore;

import com.myrole.R;
import com.myrole.camera_ck.activity.camera.CameraActivity;
import com.myrole.camera_ck.base.AppPresenter;
import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.internal.adapter.CameraOptionAdapter;
import com.myrole.camera_ck.internal.enums.CameraOption;
import com.myrole.camera_ck.internal.utils.DialogUtils;
import com.myrole.camera_ck.listeners.OnItemClickListener;

import static com.myrole.camera_ck.activity.camera.CameraActivity.REQUEST_CAMERA;
import static com.myrole.camera_ck.activity.main.MainActivity.REQUEST_IMAGE_GALLERY;
import static com.myrole.camera_ck.activity.main.MainActivity.REQUEST_VIDEO_GALLERY;

//import com.myrole.camera_ck.R;
//import com.myrole.camera_ck.recorder.activity.FFmpegRecorderActivity;
//import com.myrole.camera_ck.recorder.activity.params.FFmpegRecorderActivityParams;
//import com.myrole.camera_ck.recorder.camera.CameraControllerI;
//import com.myrole.camera_ck.recorder.recorder.common.ImageFit;
//import com.myrole.camera_ck.recorder.recorder.common.ImageScale;
//import com.myrole.camera_ck.recorder.recorder.common.ImageSize;
//import com.myrole.camera_ck.recorder.recorder.params.EncoderParamsI;

/**
 * Created by intel on 18-Jul-17.
 */

public class MainPresenter extends AppPresenter<MainView>
{


    public MainPresenter(MainView view) {
        super(view);
    }


    public void showDialog()
    {
        DialogUtils.showSingleChoiceDialog(getContext(),
                                getString(R.string.app_name),
                                getCameraOptionAdapter(),
                                0,
                                new OnItemClickListener<String>() {
                                @Override
                                    public void onItemClick(int resId, String value)
                                    {
                                        if(resId == AlertDialog.BUTTON_POSITIVE)
                                        {
                                            switch (CameraOption.getOptionType(value))
                                            {
                                                case CAMERA_IMAGE:
                                                    Intent intent = new Intent(getContext(), CameraActivity.class);
                                                    intent.putExtra(Configuration.MEDIA_ACTION, Configuration.MEDIA_ACTION_PHOTO);
                                                    getActivity().startActivityForResult(intent,REQUEST_CAMERA);
                                                    break;
                                                case CAMERA_VIDEO:
                                                    //openVideoRecorder(CameraHelper.getOutputMediaFile(Configuration.MEDIA_ACTION_VIDEO),CameraHelper.getOutputMediaFile(Configuration.MEDIA_ACTION_PHOTO));
                                                    break;
                                                case GALLERY_IMAGE:
                                                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                    getActivity().startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
                                                    break;
                                                case GALLERY_VIDEO:
                                                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                                    getActivity().startActivityForResult(intent, REQUEST_VIDEO_GALLERY);
                                                    break;
                                            }
                                        }
                                    }
                                },
                                true,
                                "Ok",
                                "Cancel");
    }

    public CameraOptionAdapter getCameraOptionAdapter()
    {
        return new CameraOptionAdapter(getContext(), R.layout.item_list_text);
    }

//    private void openVideoRecorder(File videoFile, File thumbnailFile)
//    {
//        FFmpegRecorderActivityParams.Builder paramsBuilder =
//                FFmpegRecorderActivityParams.builder(getContext())
//                        .setVideoOutputFileUri(videoFile)
//                        .setVideoThumbnailOutputFileUri(thumbnailFile);
//
//        paramsBuilder.recorderParamsBuilder()
//                .setVideoSize(new ImageSize(360, 360))
//                .setVideoCodec(EncoderParamsI.VideoCodec.H264)
//                .setVideoBitrate(800000)
//                .setVideoFrameRate(30)
//                .setVideoImageFit(ImageFit.FILL)
//                .setVideoImageScale(ImageScale.DOWNSCALE)
//                .setShouldCropVideo(true)
//                .setShouldPadVideo(true)
//                .setVideoCameraFacing(CameraControllerI.Facing.BACK)
//                .setAudioCodec(EncoderParamsI.AudioCodec.AAC)
//                .setAudioSamplingRateHz(44100)
//                .setAudioBitrate(64000)
//                .setAudioChannelCount(1)
//                .setOutputFormat(EncoderParamsI.OutputFormat.MP4);
//
//        paramsBuilder.interactionParamsBuilder()
//                .setMinRecordingMillis((int) TimeUnit.SECONDS.toMillis(MIN_TIME))
//                .setMaxRecordingMillis((int) TimeUnit.SECONDS.toMillis(MAX_TIME));
//
//
//        Intent intent = new Intent(getContext(), FFmpegRecorderActivity.class);
//        intent.putExtra(FFmpegRecorderActivity.REQUEST_PARAMS_KEY, paramsBuilder.build());
//        getActivity().startActivityForResult(intent, REQUEST_RECORD_VIDEO);
//    }

}
