package com.myrole.NewCamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.myrole.NewCamera.CameraFilter.widget.SampleCameraGLView;
import com.myrole.NewCamera.Filters.LookupFilterTypes;
import com.myrole.NewCamera.GpuVideoFilters.camerarecorder.CameraRecordListener;
import com.myrole.NewCamera.GpuVideoFilters.camerarecorder.GPUCameraRecorder;
import com.myrole.NewCamera.GpuVideoFilters.camerarecorder.GPUCameraRecorderBuilder;
import com.myrole.NewCamera.GpuVideoFilters.camerarecorder.LensFacing;
import com.myrole.NewCamera.GpuVideoFilters.egl.CustomFilter.NewCustomFilter.GPUImageLookupFilter;
import com.myrole.R;

import java.util.List;

public class NewCameraActivity extends AppCompatActivity {
    protected GPUCameraRecorder gpuCameraRecorder;
    protected LensFacing lensFacing = LensFacing.FRONT;
    private SampleCameraGLView sampleGLView;
    private FrameLayout frameLayout;
    private int heights, widths;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);
        frameLayout = findViewById(R.id.wrap_view);

        setHeight_width();
    }

    public static List<LookupFilterTypes> getallLookupFiles() {
        return LookupFilterTypes.createVideoEffectList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpCamera();
        Bitmap bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.lookup_phaselis);
        GPUImageLookupFilter gpuImageLookupFilter = new GPUImageLookupFilter(bitmap5);
        gpuCameraRecorder.setFilter(gpuImageLookupFilter);
    }

    private void setHeight_width() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (displayMetrics.widthPixels == 1440 && displayMetrics.heightPixels == 2792) {
            widths = displayMetrics.widthPixels;
            heights = displayMetrics.heightPixels;
        } else {
            widths = displayMetrics.widthPixels;
            heights = displayMetrics.heightPixels;
        }
    }

    private void setUpCameraView() {//set up camera in frame layout
        runOnUiThread(() -> {
            frameLayout.removeAllViews();
            sampleGLView = null;
            sampleGLView = new SampleCameraGLView(getApplicationContext());
            frameLayout.setLayoutParams(new RelativeLayout.LayoutParams(widths, heights));
            frameLayout.addView(sampleGLView);
        });
    }

    private void setUpCamera() {
        setUpCameraView();
        gpuCameraRecorder = new GPUCameraRecorderBuilder(this, sampleGLView)// camera preaper capture video get url
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(boolean flashSupport) {
                    }

                    @Override
                    public void onRecordComplete() {

                    }
                    @Override
                    public void onRecordStart() {

                    }

                    @Override
                    public void onError(Exception exception) {

                    }

                    @Override
                    public void onCameraThreadFinish() {

                    }
                })
                .videoSize(widths, heights)
                .lensFacing(lensFacing)
                .mute(false)
                .build();


    }
}
