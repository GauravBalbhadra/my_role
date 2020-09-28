package com.myrole.utilities;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.myrole.fragments.SoundTabFragment;
import com.v9kmedia.v9krecorder.utils.V9krecorderutil;

import java.io.File;
import java.lang.ref.WeakReference;


public class DownloadSongTask extends AsyncTask<Object, Void, String> {

    private static final boolean DEBUG = true;
    private static final String TAG = "DownloadSongTask";
    private WeakReference<SoundTabFragment> mWeakSoundTabFragment;



    public DownloadSongTask(SoundTabFragment soundTabFragment) {
        mWeakSoundTabFragment = new WeakReference<>(soundTabFragment);
        PRDownloader.initialize(mWeakSoundTabFragment.get().getContext());
    }


    @Override
    protected String doInBackground(Object... params) {

        String url = (String)params[0];
        File downloadDir = (File)params[1];
        String songName = (String)params[2];
        int songId = (Integer)params[3];

        Log.d(TAG, "url: " + params[0]);
        Log.d(TAG, "downDir: " + params[1]);
        Log.d(TAG, "songName: " + params[2]);
        Log.d(TAG, "songId: " + params[3]);

        try {

            return downloadSong(url, downloadDir, songName, songId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "result: " + result);
    }

    private String downloadSong(String url, File downloadDir, String songName, int songId) {

        final String downloadedSong = songName + "_remoteaac.aac";

        PRDownloader.download(url, downloadDir.getPath(), downloadedSong)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                }).start(new OnDownloadListener() {

                    @Override
                    public void onDownloadComplete() {

                        mWeakSoundTabFragment.get().onDownloadedSong(downloadDir.getPath() + "/" + downloadedSong, songId);

                        V9krecorderutil.mediaMap.put("remoteaac", downloadDir.getPath() + "/" + downloadedSong);

                        final String pcmStream = V9krecorderutil.createCacheStream(Environment.DIRECTORY_MOVIES, songName, "remoteraw");

                        Log.d(TAG, "remote pcmStream: " + pcmStream);

                        SongCodec.getPCMFromSong(downloadDir.getPath() + "/" + downloadedSong, pcmStream, new SongCodec.SongDecodeListener() {
                            @Override
                            public void decodeFail() {
                                Log.d(TAG, "song to pcm decode file: " + downloadedSong);
                            }

                            @Override
                            public void decodeOver() {
                                Log.d(TAG, "download song pcm: " + pcmStream);
                                mWeakSoundTabFragment.get().onDecodedSong(pcmStream, songId);
                            }

                        });

                    }

                    @Override
                    public void onError(Error error) {

                        Log.d(TAG, "songDownloaded onError: " + error);

                    }
                });

        return null;
    }


}
