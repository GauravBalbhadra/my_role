package com.myrole.fragments.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
import com.myrole.fragments.SoundTabFragment;
import com.myrole.model.Song;
import com.myrole.repository.AppRepository;
import com.myrole.utilities.DownloadSongTask;
import com.myrole.utilities.TrimSongTask;
import com.v9kmedia.v9krecorder.utils.V9krecorderutil;

import java.io.File;
import java.util.List;

import io.gresse.hugo.vumeterlibrary.VuMeterView;

public class SoundTabViewModel extends AndroidViewModel implements Player.EventListener {

    private static final String TAG = "SoundTabViewModel";

    public List<Song> mSongItemList;

    private MutableLiveData<String> mDownloadedSong = new MutableLiveData<>();
    private MutableLiveData<Boolean> showApplyBtn = new MutableLiveData<>();
    private MutableLiveData<Boolean> showVuMeter = new MutableLiveData<>();
    private MutableLiveData<Boolean> showLoader = new MutableLiveData<>();

    private SimpleExoPlayer mPlayer;

    public boolean mIsPlaying = false;
   
    private AppRepository appRepository;
    private String mPreviouslyDownloadedSong;
    private int mCurrentlyPlayingSongId;

    public LiveData<Boolean> getApplyBtnState() {
        return showApplyBtn;
    }

    public LiveData<Boolean> getVuMeterState() { return showVuMeter; }

    public LiveData<Boolean> getLoaderState() { return showLoader; }


    public SoundTabViewModel(Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance();
        mSongItemList = appRepository.songItemList;
    }

    public void setDownloadedSong(MutableLiveData<String> mDownloadedSong) {
        this.mDownloadedSong = mDownloadedSong;
    }

    public MutableLiveData<String> getDownloadedSong() {
        return mDownloadedSong;
    }

    public void downloadSong(String url, File downloadDir, String songName, SoundTabFragment soundTabFragment, int songId) {
        new DownloadSongTask(soundTabFragment).execute(url, downloadDir, songName, songId);
    }

    public void trimSong(String songToTrim, long duration) {
        new TrimSongTask().execute(0.0f, (duration/1000.0f), songToTrim, V9krecorderutil.createStreamPath(Environment.DIRECTORY_MOVIES, "trimaacaudio"));
    }


    public void playSong(String currentlyDownloadedSong, int currentlyPlayingSongId, AppCompatTextView applyBtn, VuMeterView vumeter) {

        mDownloadedSong.setValue(currentlyDownloadedSong);

        mPreviouslyDownloadedSong = "none";

        if(mPreviouslyDownloadedSong.equals(currentlyDownloadedSong)){

            mPreviouslyDownloadedSong = "none";
            mCurrentlyPlayingSongId = -1;
            
        } else {

            mPreviouslyDownloadedSong = currentlyDownloadedSong;

            mCurrentlyPlayingSongId = currentlyPlayingSongId;

            Log.d(TAG, "playing download song: " + currentlyDownloadedSong);

            DefaultTrackSelector trackSelector = new DefaultTrackSelector();

            mPlayer = ExoPlayerFactory.newSimpleInstance(getApplication().getApplicationContext(), trackSelector);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplication().getApplicationContext(),
                    Util.getUserAgent(getApplication().getApplicationContext(), "TikTok"));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(currentlyDownloadedSong));

            mPlayer.prepare(videoSource);

            mPlayer.addListener(this);

            mPlayer.setPlayWhenReady(true);
        }

        mIsPlaying = true;

    }

    public void stopSong(){

        if(mPlayer!=null){
            showApplyBtn.setValue(false);
            showVuMeter.setValue(false);
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
            showApplyBtn.setValue(true);
            showVuMeter.setValue(true);
        } else if(playbackState== Player.STATE_ENDED) {
            showApplyBtn.setValue(false);
            showVuMeter.setValue(false);
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
}
