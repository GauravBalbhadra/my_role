package com.myrole.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.myrole.R;

import java.io.IOException;

/**
 * Created by Vikesh Jhajhria on 7/30/2016.
 */

public class VideoPlayer extends FrameLayout implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    TextView tv;
    ProgressBarHandler progressBarHandler;

    public VideoPlayer(Context context) {
        super(context);
        makeView();
    }
    public VideoPlayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        makeView();
    }


    public void makeView() {



        videoSurface = new SurfaceView(getContext());
        addView(videoSurface);

        tv = new TextView(getContext());
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setText("Loading...");
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv.setBackgroundColor(getResources().getColor(R.color.bottom_bar));
        addView(tv);
       // progressBarHandler.show();

        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);
        ViewGroup.LayoutParams params = videoSurface.getLayoutParams();
        params.height = getResources().getDisplayMetrics().widthPixels;
        videoSurface.setLayoutParams(params);

        player = new MediaPlayer();
        controller = new VideoControllerView(getContext());
    }

    public void playUrl(String url) {
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(getContext(), Uri.parse(url));
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            player.setDisplay(holder);
            player.prepareAsync();
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView(this);
        tv.setVisibility(GONE);
       // progressBarHandler.hide();;
        player.start();
    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
        player.stop();

    }
//    private void stopPlaying() {
//        if (player != null) {
//            player.stop();
//            player.release();
//            player = null;
//        }
//    }
    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }
}
