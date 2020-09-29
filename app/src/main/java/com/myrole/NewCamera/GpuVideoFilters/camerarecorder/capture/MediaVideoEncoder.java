package com.myrole.NewCamera.GpuVideoFilters.camerarecorder.capture;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.opengl.EGLContext;
import android.util.Log;
import android.view.Surface;

import com.myrole.NewCamera.GpuVideoFilters.egl.filter.GlFilter;

import java.io.IOException;


public class MediaVideoEncoder extends MediaEncoder {
    private static final String TAG = "MediaVideoEncoder";

    private static final String MIME_TYPE = "video/avc";
    // parameters for recording
    private static final int FRAME_RATE = 30;


    private static final float BPP = 0.25f;
    private final int fileWidth;
    private final int fileHeight;
    private EncodeRenderHandler encodeRenderHandler;
    private Surface surface;


    public MediaVideoEncoder(final MediaMuxerCaptureWrapper muxer,
                             final MediaEncoderListener listener,
                             final int fileWidth,
                             final int fileHeight,
                             final boolean flipHorizontal,
                             final boolean flipVertical,
                             final float viewWidth,
                             final float viewHeight,
                             final boolean recordNoFilter,
                             final GlFilter filter
    ) {
        super(muxer, listener);
        this.fileWidth = fileWidth;
        this.fileHeight = fileHeight;
        encodeRenderHandler = EncodeRenderHandler.createHandler(
                TAG,
                flipVertical,
                flipHorizontal,
                (viewWidth > viewHeight) ? (viewWidth / viewHeight) : (viewHeight / viewWidth),
                fileWidth,
                fileHeight,
                recordNoFilter,
                filter
        );
    }

    private static int calcBitRate(int width, int height) {
        final int bitrate = (int) (BPP * FRAME_RATE * width * height);
        Log.i(TAG, "bitrate=" + bitrate);
        return bitrate;
    }

    /**
     * select the first codec that match a specific MIME type
     *
     * @param mimeType
     * @return null if no codec matched
     */
    private static MediaCodecInfo selectVideoCodec(final String mimeType) {
        Log.v(TAG, "selectVideoCodec:");

        // get the list of available codecs
        MediaCodecList list = new MediaCodecList(MediaCodecList.ALL_CODECS);
        MediaCodecInfo[] codecInfos = list.getCodecInfos();

        final int numCodecs = codecInfos.length;
        for (final MediaCodecInfo codecInfo : codecInfos) {
            if (!codecInfo.isEncoder()) {    // skipp decoder
                continue;
            }
            // select first codec that match a specific MIME type and color format
            final String[] types = codecInfo.getSupportedTypes();
            for (String type : types) {
                if (type.equalsIgnoreCase(mimeType)) {
                    Log.i(TAG, "codec:" + codecInfo.getName() + ",MIME=" + type);
                    final int format = selectColorFormat(codecInfo, mimeType);
                    if (format > 0) {
                        return codecInfo;
                    }
                }
            }
        }
        return null;
    }

    /**
     * select color format available on specific codec and we can use.
     *
     * @return 0 if no colorFormat is matched
     */
    private static int selectColorFormat(final MediaCodecInfo codecInfo, final String mimeType) {
        Log.i(TAG, "selectColorFormat: ");
        int result = 0;
        final MediaCodecInfo.CodecCapabilities caps;
        try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            caps = codecInfo.getCapabilitiesForType(mimeType);
        } finally {
            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        }
        int colorFormat;
        for (int i = 0; i < caps.colorFormats.length; i++) {
            colorFormat = caps.colorFormats[i];
            if (isRecognizedViewoFormat(colorFormat)) {
                if (result == 0)
                    result = colorFormat;
                break;
            }
        }
        if (result == 0)
            Log.e(TAG, "couldn't find a good color format for " + codecInfo.getName() + " / " + mimeType);
        return result;
    }

    private static boolean isRecognizedViewoFormat(final int colorFormat) {
        Log.i(TAG, "isRecognizedViewoFormat:colorFormat=" + colorFormat);
        return (colorFormat == MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
    }

    public void frameAvailableSoon(final int texName, final float[] stMatrix, final float[] mvpMatrix, final float aspectRatio) {
        if (super.frameAvailableSoon()) {
            encodeRenderHandler.draw(texName, stMatrix, mvpMatrix, aspectRatio);
        }
        //result;
    }

    @Override
    public boolean frameAvailableSoon() {
        boolean result;
        if (result = super.frameAvailableSoon()) {
            encodeRenderHandler.prepareDraw();
        }
        return result;
    }

    @Override
    protected void prepare() throws IOException {
        Log.i(TAG, "prepare: ");
        trackIndex = -1;
        muxerStarted = isEOS = false;
        final MediaCodecInfo videoCodecInfo = selectVideoCodec(MIME_TYPE);
        if (videoCodecInfo == null) {

            return;
        }


        final MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, fileWidth, fileHeight);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, calcBitRate(fileWidth, fileHeight));
        format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 3);
        mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        surface = mediaCodec.createInputSurface();
        mediaCodec.start();
        if (listener != null) {
            try {
                listener.onPrepared(this);
            } catch (final Exception e) {
                Log.e(TAG, "prepare:", e);
            }
        }
    }

    public void setEglContext(final EGLContext shared_context, final int tex_id) {
        encodeRenderHandler.setEglContext(shared_context, tex_id, surface);
    }

    @Override
    protected void release() {
        Log.i(TAG, "release:");
        if (surface != null) {
            surface.release();
            surface = null;
        }
        if (encodeRenderHandler != null) {
            encodeRenderHandler.release();
            encodeRenderHandler = null;
        }
        super.release();
    }

    @Override
    protected void signalEndOfInputStream() {
        Log.d(TAG, "sending EOS to encoder");
        mediaCodec.signalEndOfInputStream();    // API >= 18
        isEOS = true;
    }

}

