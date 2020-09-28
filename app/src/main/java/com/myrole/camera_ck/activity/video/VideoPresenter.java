package com.myrole.camera_ck.activity.video;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import com.google.common.base.Preconditions;
import com.myrole.R;
import com.myrole.camera_ck.activity.camera.CameraActivity;
import com.myrole.camera_ck.activity.preview.PreviewActivity;
import com.myrole.camera_ck.activity.video.conroller.CameraController;
import com.myrole.camera_ck.activity.video.conroller.CameraControllerI;
import com.myrole.camera_ck.activity.video.conroller.CameraUtil;
import com.myrole.camera_ck.activity.video.model.FrameToRecord;
import com.myrole.camera_ck.activity.video.model.RecordFragment;
import com.myrole.camera_ck.activity.video.params.CameraParams;
import com.myrole.camera_ck.activity.video.params.RecorderParamsI;
import com.myrole.camera_ck.activity.video.recorder.FFmpegFrameRecorder;
import com.myrole.camera_ck.base.AppPresenter;
import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.internal.utils.CameraHelper;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacv.FFmpegFrameFilter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameFilter;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.lang.Thread.State.WAITING;

/**
 * Created by Divine on 20-08-2017.
 */

public class VideoPresenter extends AppPresenter<VideoView>
{
    private static final String LOG_TAG = VideoPresenter.class.getSimpleName();

    private static final int PREFERRED_PREVIEW_WIDTH = 360;
    private static final int PREFERRED_PREVIEW_HEIGHT = 360;

    // both in milliseconds
    private static final long MIN_VIDEO_LENGTH = 1 * 1000;
    private static final long MAX_VIDEO_LENGTH = 60 * 1000;

    private int sampleAudioRateInHz = 44100;
    /* The sides of width and height are based on camera orientation.
    That is, the preview size is the size before it is rotated. */


    //OutPut Video Length
    private long minVideoLengh = MIN_VIDEO_LENGTH;
    private long maxVideoLengh = MAX_VIDEO_LENGTH;

    private RecorderParamsI recorderParamsI;

    // Output video size
    private int frameRate = 30;
    private int frameDepth = Frame.DEPTH_UBYTE;
    private int frameChannels = 2;

    private OpenCameraTask mOpenCameraTask;

    private CameraControllerI mCameraController;

    // forRecording
    private FFmpegFrameRecorder mFrameRecorder;
    private File mVideo;

    private LinkedBlockingQueue<FrameToRecord> mFrameToRecordQueue;
    private LinkedBlockingQueue<FrameToRecord> mRecycledFrameQueue;
    private int mFrameToRecordCount;
    private int mFrameRecordedCount;
    private long mTotalProcessFrameTime;
    private long lastPreviewFrameTime;
    private Stack<RecordFragment> mRecordFragments;
    private volatile boolean mRecording = false;

    private VideoRecordThread mVideoRecordThread;
    private AudioRecordRunnable audioRecordRunnable;

    public VideoPresenter(VideoView view) {
        super(view);
        mCameraController = new CameraController();
        mCameraController.addListener(getView());

        // At most buffer 10 Frame
        mFrameToRecordQueue = new LinkedBlockingQueue<>(10);
        // At most recycle 2 Frame
        mRecycledFrameQueue = new LinkedBlockingQueue<>(2);
        mRecordFragments = new Stack<>();
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public int getVideoWidth() {
        return getRecorderParams().getVideoSize().getWidthUnchecked();
    }

    public int getVideoHeight() {
        return getRecorderParams().getVideoSize().getHeightUnchecked();
    }

    public long getMinVideoLengh() {
        return minVideoLengh;
    }

    public long getMaxVideoLength() {
        return maxVideoLengh;
    }

    public boolean isRecording() {
        return mRecording;
    }

    public void setRecording(boolean mRecording) {
        this.mRecording = mRecording;
    }

    public CameraControllerI getCameraController() {
        return mCameraController;
    }

    public Stack<RecordFragment> getRecordFragments() {
        return mRecordFragments;
    }

    public RecorderParamsI getRecorderParams()
    {
        if(recorderParamsI == null)
        {
            /*recorderParamsI = RecorderParams.BuilderI.setVideoSize(new ImageSize(PREFERRED_PREVIEW_WIDTH, PREFERRED_PREVIEW_HEIGHT))
                    .setVideoFrameRate(frameRate)
                    .setVideoImageFit(ImageFit.FILL)
                    .setVideoImageScale(ImageScale.DOWNSCALE)
                    .setShouldCropVideo(true)
                    .setShouldPadVideo(true)
                    .setVideoCameraFacing(CameraControllerI.Facing.BACK)
                    .build();*/
        }
        return recorderParamsI;
    }

    public void initResources()
    {
        getView().setVisibility(R.id.progressVideo, GONE);
        getView().setVisibility(R.id.next_media_action, INVISIBLE);
        getView().setTimer(0);
        openCamera(getRecorderParams().getVideoCameraFacing());
    }


    public void releaseResources() {

        pauseRecording();
        stopRecording();
        releaseAudioRecorder();
        stopRecorder();
        // Try to lock the camera if we can so that we can close it
        try {
            mCameraController.lock();
        } catch (Throwable expected) {
        }
        mCameraController.closeCamera();
    }


    public void onCameraOpen()
    {
        if (mFrameRecorder == null) {
            initRecorder();
            startRecorder();
        }
        initAudioRecorder();
        startRecording();
    }

    public void setCameraPreviewDisplayIfReady(SurfaceHolder surfaceHolder) {
        if (mCameraController.isCameraOpen()) {
            try {
                mCameraController.setPreviewDisplay(surfaceHolder);
            } catch (IOException exception) {
                Log.e(LOG_TAG, "Error setting camera preview display", exception);
            }
        }
    }

    public void onCameraStartPreview()
    {
        mCameraController.setPreviewCallback(getView());
    }

    public void onCameraStopPreview()
    {
        mCameraController.setPreviewCallback(null);
    }

    public void updateFlashMode()
    {
        if (!mCameraController.isCameraOpen()) {
            return;
        }
        CameraControllerI.FlashMode flashMode;
        switch (mCameraController.getFlashMode()) {
            case ON:
                flashMode = CameraControllerI.FlashMode.OFF;
                break;
            case OFF:
            default:
                flashMode = CameraControllerI.FlashMode.ON;
                break;
        }
        getView().updateCameraFlash(flashMode);
    }

    public void doSwitchCamera()
    {
        if (!mCameraController.isCameraOpen()) {
            return;
        }
        CameraControllerI.Facing cameraFacing =
                mCameraController.getCameraFacing() == CameraControllerI.Facing.BACK
                        ? CameraControllerI.Facing.FRONT
                        : CameraControllerI.Facing.BACK;
        releaseRecorder(true);
        releaseResources();
        openCamera(cameraFacing);
    }


    protected void openCamera(CameraControllerI.Facing facing) {
        if (mOpenCameraTask != null) {
            return;
        }
        Log.d(LOG_TAG, String.format("Opening camera facing %s", facing));
        getView().onCameraAdjustPreview(null);
        getView().updateCameraFace(facing);
        mOpenCameraTask = new OpenCameraTask();
        mOpenCameraTask.execute(Preconditions.checkNotNull(facing));
    }


    public void initRecorder()
    {
        mVideo = CameraHelper.getOutputMediaFile(Configuration.MEDIA_ACTION_VIDEO);

        mFrameRecorder = new FFmpegFrameRecorder(mVideo, getVideoWidth(), getVideoHeight(), 1);
        mFrameRecorder.setFormat("mp4");
        mFrameRecorder.setSampleRate(sampleAudioRateInHz);
        mFrameRecorder.setFrameRate(frameRate);
        mFrameRecorder.setFrameRate(frameRate);
        // Use H264
        mFrameRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);


        mFrameRecorder.setVideoOption("crf", "18");
        mFrameRecorder.setVideoOption("preset", "superfast");
        mFrameRecorder.setVideoOption("tune", "zerolatency");

    }

    private void releaseRecorder(boolean deleteFile) {
        if (mFrameRecorder != null) {
            try {
                mFrameRecorder.release();
            } catch (FFmpegFrameRecorder.Exception e) {
                e.printStackTrace();
            }
            mFrameRecorder = null;

            if (deleteFile) {
                mVideo.delete();
            }
        }
    }

    private void startRecorder() {
        try {
            mFrameRecorder.start();
        } catch (FFmpegFrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecorder() {
        if (mFrameRecorder != null) {
            try {
                mFrameRecorder.stop();
            } catch (FFmpegFrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }

        mRecordFragments.clear();

        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBtnReset.setVisibility(View.INVISIBLE);
            }
        });*/
    }

    private void initAudioRecorder() {
        audioRecordRunnable = new AudioRecordRunnable();
    }

    private void releaseAudioRecorder() {
        if (audioRecordRunnable != null) {
            audioRecordRunnable.release();
            audioRecordRunnable = null;
        }
    }

    private void startRecording() {
        mVideoRecordThread = new VideoRecordThread();
        mVideoRecordThread.start();
    }

    private void stopRecording() {
        if (mVideoRecordThread != null && mVideoRecordThread.isRunning()) {
            mVideoRecordThread.stopRunning();
            try {
                mVideoRecordThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mVideoRecordThread = null;
        }

        mFrameToRecordQueue.clear();
        mRecycledFrameQueue.clear();
    }


    public void discardRecording() {
        Log.i(LOG_TAG, "Discard recording");
        if (mVideo != null && mVideo.exists()) {
            mVideo.delete();
            mVideo = null;
        }
        getView().setTimer(0);
    }

    public void saveRecording()
    {
        pauseRecording();
        new FinishRecordingTask().execute();
    }


    public void resumeRecording() {
        if (!isRecording()) {
            RecordFragment recordFragment = new RecordFragment();
            recordFragment.setStartTimestamp(System.currentTimeMillis());
            mRecordFragments.push(recordFragment);
            getView().runOnUI(new Runnable() {
                @Override
                public void run() {
                    getView().setVisibility(R.id.switch_camera_button, INVISIBLE);
                }
            });
            mRecording = true;
            new Thread(audioRecordRunnable).start();
        }
    }

    public void pauseRecording() {
        if (isRecording()) {
            mRecordFragments.peek().setEndTimestamp(System.currentTimeMillis());
            getView().runOnUI(new Runnable() {
                @Override
                public void run() {
                    getView().setVisibility(R.id.switch_camera_button, VISIBLE);
                }
            });
            mRecording = false;
            audioRecordRunnable.stop();
        }
    }


    public void onPreviewFrame(byte[] data)
    {
        long thisPreviewFrameTime = System.currentTimeMillis();
        if (lastPreviewFrameTime > 0) {
            Log.d(LOG_TAG, "Preview frame interval: " + (thisPreviewFrameTime - lastPreviewFrameTime) + "ms");
        }
        lastPreviewFrameTime = thisPreviewFrameTime;

        // get video data
        if (isRecording()) {
            if (audioRecordRunnable == null || !audioRecordRunnable.isRunning()) {
                // wait for AudioRecord to init and start
                mRecordFragments.peek().setStartTimestamp(System.currentTimeMillis());
            } else {
                // pop the current record fragment when calculate total recorded time
                RecordFragment curFragment = mRecordFragments.pop();
                long recordedTime = calculateTotalRecordedTime(mRecordFragments);
                // push it back after calculation
                mRecordFragments.push(curFragment);
                long curRecordedTime = System.currentTimeMillis()
                        - curFragment.getStartTimestamp() + recordedTime;
                getView().setTimer(curRecordedTime);
                // check if exceeds time limit
                if(curRecordedTime > MIN_VIDEO_LENGTH)
                {
                    getView().setVisibility(R.id.next_media_action, VISIBLE);
                }

                if (curRecordedTime > MAX_VIDEO_LENGTH) {
                    pauseRecording();
                    new FinishRecordingTask().execute();
                    return;
                }

                long timestamp = 1000 * curRecordedTime;
                Frame frame;
                FrameToRecord frameToRecord = mRecycledFrameQueue.poll();
                if (frameToRecord != null) {
                    frame = frameToRecord.getFrame();
                    frameToRecord.setTimestamp(timestamp);
                } else {
                    int previewWidth = getCameraController().getPreviewSize().getWidthUnchecked();
                    int previewHeight = getCameraController().getPreviewSize().getHeightUnchecked();

                    frame = new Frame(previewWidth, previewHeight, frameDepth, frameChannels);
                    frameToRecord = new FrameToRecord(timestamp, frame);
                }
                ((ByteBuffer) frame.image[0].position(0)).put(data);

                if (mFrameToRecordQueue.offer(frameToRecord)) {
                    mFrameToRecordCount++;
                }
            }
        }
        getCameraController().getCamera().addCallbackBuffer(data);
    }

    private long calculateTotalRecordedTime(Stack<RecordFragment> recordFragments) {
        long recordedTime = 0;
        for (RecordFragment recordFragment : recordFragments) {
            recordedTime += recordFragment.getDuration();
        }
        return recordedTime;
    }



    class AudioRecordRunnable implements Runnable {

        private boolean isRunning;
        private AudioRecord mAudioRecord;
        private ShortBuffer audioData;
        private CountDownLatch latch;

        public AudioRecordRunnable() {
            int bufferSize = AudioRecord.getMinBufferSize(sampleAudioRateInHz,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleAudioRateInHz,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

            audioData = ShortBuffer.allocate(bufferSize);
        }

        @Override
        public void run() {
            latch = new CountDownLatch(1);
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

            Log.d(LOG_TAG, "mAudioRecord startRecording");
            mAudioRecord.startRecording();

            isRunning = true;
            /* ffmpeg_audio encoding loop */
            while (isRunning) {
                if (mRecording && mFrameRecorder != null) {
                    int bufferReadResult = mAudioRecord.read(audioData.array(), 0, audioData.capacity());
                    audioData.limit(bufferReadResult);
                    if (bufferReadResult > 0) {
                        Log.v(LOG_TAG, "bufferReadResult: " + bufferReadResult);
                        try {
                            mFrameRecorder.recordSamples(audioData);
                        } catch (Exception e) {
                            Log.v(LOG_TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
            Log.d(LOG_TAG, "mAudioRecord stopRecording");
            mAudioRecord.stop();
            latch.countDown();
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void stop() {
            this.isRunning = false;
        }

        public void release() {
            if (latch == null) {
                return;
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mAudioRecord != null) {
                mAudioRecord.release();
                mAudioRecord = null;
                Log.d(LOG_TAG, "mAudioRecord released");
            }
        }
    }

    class VideoRecordThread extends Thread {

        private boolean isRunning;

        @Override
        public void run() {
            List<String> filters = new ArrayList<>();
            // Transpose
            String transpose = null;

            if (getCameraController().getCameraFacing() == CameraControllerI.Facing.FRONT) {
                switch (getCameraController().getCameraOrientationDegrees()) {
                    case 270:
//                        transpose = "transpose=clock_flip"; // Same as preview display
                        transpose = "transpose=cclock"; // Mirrored horizontally as preview display
                        break;
                    case 90:
//                        transpose = "transpose=cclock_flip"; // Same as preview display
                        transpose = "transpose=clock"; // Mirrored horizontally as preview display
                        break;
                }
            } else {
                switch (getCameraController().getCameraOrientationDegrees()) {
                    case 270:
                        transpose = "transpose=cclock";
                        break;
                    case 90:
                        transpose = "transpose=clock";
                        break;
                }
            }
            if (transpose != null) {
                filters.add(transpose);
            }
            // Crop (only vertically)
            int previewHeight = getCameraController().getPreviewSize().getHeightUnchecked();
            int previewWidth = getCameraController().getPreviewSize().getWidthUnchecked();
            int videoHeight = getRecorderParams().getVideoSize().getHeightUnchecked();
            int videoWidth = getRecorderParams().getVideoSize().getWidthUnchecked();


            int width = previewHeight;
            int height = width * videoHeight / videoWidth;
            String crop = String.format("crop=%d:%d:%d:%d",
                    width, height,
                    (previewHeight - width) / 2, (previewWidth - height) / 2);
            filters.add(crop);
            // Scale (to designated size)
            String scale = String.format("scale=%d:%d", videoHeight, videoWidth);
            filters.add(scale);

            FFmpegFrameFilter frameFilter = new FFmpegFrameFilter(TextUtils.join(",", filters),
                    previewWidth, previewHeight);
            frameFilter.setPixelFormat(avutil.AV_PIX_FMT_NV21);
            frameFilter.setFrameRate(frameRate);
            try {
                frameFilter.start();
            } catch (FrameFilter.Exception e) {
                e.printStackTrace();
            }

            isRunning = true;
            FrameToRecord recordedFrame;

            while (isRunning || !mFrameToRecordQueue.isEmpty()) {
                try {
                    recordedFrame = mFrameToRecordQueue.take();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                    try {
                        frameFilter.stop();
                    } catch (FrameFilter.Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }

                if (mFrameRecorder != null) {
                    long timestamp = recordedFrame.getTimestamp();
                    if (timestamp > mFrameRecorder.getTimestamp()) {
                        mFrameRecorder.setTimestamp(timestamp);
                    }
                    long startTime = System.currentTimeMillis();
//                    Frame filteredFrame = recordedFrame.getFrame();
                    Frame filteredFrame = null;
                    try {
                        frameFilter.push(recordedFrame.getFrame());
                        filteredFrame = frameFilter.pull();
                    } catch (FrameFilter.Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mFrameRecorder.record(filteredFrame);
                    } catch (FFmpegFrameRecorder.Exception e) {
                        e.printStackTrace();
                    }
                    long endTime = System.currentTimeMillis();
                    long processTime = endTime - startTime;
                    mTotalProcessFrameTime += processTime;
                    Log.d(LOG_TAG, "This frame process time: " + processTime + "ms");
                    long totalAvg = mTotalProcessFrameTime / ++mFrameRecordedCount;
                    Log.d(LOG_TAG, "Avg frame process time: " + totalAvg + "ms");
                }
                Log.d(LOG_TAG, mFrameRecordedCount + " / " + mFrameToRecordCount);
                mRecycledFrameQueue.offer(recordedFrame);
            }
        }

        public void stopRunning() {
            this.isRunning = false;
            if (getState() == WAITING) {
                interrupt();
            }
        }

        public boolean isRunning() {
            return isRunning;
        }
    }

    protected class OpenCameraTask extends ProgressDialogTask<CameraControllerI.Facing, Void, Exception> {

        public OpenCameraTask() {
            super(R.string.initializing);
        }

        @Override
        protected Exception doInBackground(CameraControllerI.Facing[] params) {
            try {
                CameraControllerI.Facing facing = Preconditions.checkNotNull(params[0]);
                CameraParams.Builder cameraParamsBuilder =
                        CameraParams.Builder.merge(CameraParams.builder(), getRecorderParams());
                if (getRecorderParams().getVideoCameraFacing() != facing) {
                    cameraParamsBuilder.setVideoCameraFacing(facing);
                }
                mCameraController
                        .openCamera(cameraParamsBuilder.build(), CameraUtil.getContextRotation(getContext()));
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception e) {
            mOpenCameraTask = null;
            getView().hideProgress(getCameraController().getCameraCount(),getCameraController().supportsFlashMode(CameraControllerI.FlashMode.ON));
            if (e != null) {
                Log.e(LOG_TAG, "Error opening camera", e);
                getView().onError(e);
            }
        }
    }

    abstract class ProgressDialogTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

        private int promptRes;

        public ProgressDialogTask(int promptRes) {
            this.promptRes = promptRes;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getView().showProgress(promptRes);
        }



        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            getView().hideProgress();
        }
    }

    class FinishRecordingTask extends ProgressDialogTask<Void, Integer, Void> {

        public FinishRecordingTask() {
            super(R.string.preparing);
        }

        @Override
        protected Void doInBackground(Void... params) {
            stopRecording();
            stopRecorder();
            releaseRecorder(false);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = PreviewActivity.newIntentPhoto(getContext(), mVideo.getPath());
            getActivity().startActivityForResult(intent, CameraActivity.REQUEST_PREVIEW_CODE);
        }
    }

}
