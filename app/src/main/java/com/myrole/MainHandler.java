package com.myrole;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.myrole.fragments.CameraFragment;
import com.v9kmedia.v9krecorder.encoder.MediaVideoEncoder;
import com.v9kmedia.v9kview.CameraGLView;

import java.lang.ref.WeakReference;


public class MainHandler extends Handler implements MediaVideoEncoder.MediaVideoEncoderListener {

    private static final int MSG_FILE_SAVE_COMPLETE = 0;
    private static final int MSG_BUFFER_STATUS = 1;
    private static final int MSG_BUFFER_FILLED = 2;
    private static final int MSG_PLAY_RECORD_ANIMATION = 3;
    private static final int MSG_START_RECORDING = 4;

    private CameraGLView mCameraGLView;
    private Context mContext;
    private WeakReference<CameraFragment> mWeakCameraFragment;

    public MainHandler(Context context, CameraFragment cameraFragment, CameraGLView cameraGLView) {
        mContext = context;
        mWeakCameraFragment = new WeakReference<>(cameraFragment);
        mCameraGLView = cameraGLView;
    }

    @Override
    public void fileSaveComplete(int status) {
        sendMessage(obtainMessage(MSG_FILE_SAVE_COMPLETE, status, 0, null));
    }

    @Override
    public void bufferStatus(long totalTimeMsec) {
        sendMessage(obtainMessage(MSG_BUFFER_STATUS,
                (int) (totalTimeMsec >> 32), (int) totalTimeMsec));
    }

    @Override
    public void bufferFilled(int status) {
        sendMessage(obtainMessage(MSG_BUFFER_FILLED, status, 0, null));
    }

    @Override
    public void onPrepared(MediaVideoEncoder mediaVideoEncoder) {
        mCameraGLView.setVideoEncoder(mediaVideoEncoder);
    }

    @Override
    public void onResumed(MediaVideoEncoder mediaVideoEncoder) {
        mCameraGLView.updateEGLContext(mediaVideoEncoder);
    }

    @Override
    public void onStopped(MediaVideoEncoder mediaVideoEncoder) {
        mCameraGLView.setVideoEncoder(null);
    }

    public void handleMessage(Message msg) {

        CameraFragment fragment = mWeakCameraFragment.get();

        switch (msg.what) {

            case MSG_PLAY_RECORD_ANIMATION: {

            }
            break;

            case MSG_START_RECORDING: {

            }
            break;

            case MSG_FILE_SAVE_COMPLETE: {

            }
            break;

            case MSG_BUFFER_FILLED: {
                fragment.bufferFilledUp(msg.arg1);
            }
            break;

            case MSG_BUFFER_STATUS: {
                long duration = (((long) msg.arg1) << 32) |
                        (((long) msg.arg2) & 0xffffffffL);
                fragment.updateBufferStatus(duration);
            }
            break;

            default:
                throw new RuntimeException("Unknown message " + msg.what);
        }
    }
}
