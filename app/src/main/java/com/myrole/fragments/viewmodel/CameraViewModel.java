package com.myrole.fragments.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CameraViewModel extends AndroidViewModel implements Player.EventListener {

    private static final String TAG = "CameraViewModel";

    private SimpleExoPlayer mPlayer;
    private String mPreviouslyDownloadedSong;
    private int mCurrentlySelectedSongId;

    private List<File> mAudioStream = new ArrayList<>();

    private List<File> mVideoStream = new ArrayList<>();

    public boolean mIsPlaying;

    private Application mAppContext;

    public CameraViewModel(Application application)
    {
        super(application);
        mAppContext = application;
    }

    public void playSong(String currentlySelectedSong, Integer currentlySelectedSongId) {

        mPreviouslyDownloadedSong = "none";

        if(mPreviouslyDownloadedSong.equals(currentlySelectedSong)){

            mPreviouslyDownloadedSong = "none";
            mCurrentlySelectedSongId = -1;

        } else {

            mPreviouslyDownloadedSong = currentlySelectedSong;

            mCurrentlySelectedSongId = currentlySelectedSongId;

            Log.d(TAG, "playing currently selected song: " + currentlySelectedSong);

            DefaultTrackSelector trackSelector = new DefaultTrackSelector();

            mPlayer = ExoPlayerFactory.newSimpleInstance(getApplication().getApplicationContext(), trackSelector);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplication().getApplicationContext(),
                    Util.getUserAgent(getApplication().getApplicationContext(), "TikTok"));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(currentlySelectedSong));

            mPlayer.prepare(videoSource);

            mPlayer.addListener(this);

            mPlayer.setPlayWhenReady(true);
        }

        mIsPlaying = true;

    }

    public void pauseSong() {
        if(mPlayer!=null){
            mPlayer.setPlayWhenReady(!mPlayer.getPlayWhenReady());
            mIsPlaying = false;
        }
    }

    public void stopSong(){

        if(mPlayer!=null){
            mPlayer.setPlayWhenReady(false);
            mPlayer.removeListener(this);
            mPlayer.release();
            mIsPlaying = false;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState== Player.STATE_BUFFERING) {
            //showLoadingState();
        } else if(playbackState== Player.STATE_READY) {

        } else if(playbackState== Player.STATE_ENDED) {

        } else {

        }
    }

    private void showRunState() {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    //private mergeMedia

}
