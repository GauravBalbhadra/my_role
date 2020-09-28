package com.myrole.utilities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MergeMediaMP4Parser extends AsyncTask<Object, Integer, String> {

    private static final boolean DEBUG = true;
    private static final String TAG = "MergeMediaMP4Parser";


    public MergeMediaMP4Parser() {

    }

    //public SnapshotToolkit snapshotToolkit = new SnapshotToolkit();

    @Override
    protected String doInBackground(Object... params) {

        String audioPath = (String)params[0];
        String videoPath = (String)params[1];
        String mergedPath = (String)params[2];

        Log.d(TAG, "audioPath: " + params[0]);
        Log.d(TAG, "videoPath: " + params[1]);
        Log.d(TAG, "mergedPath: " + params[2]);

        try {

            return merge(audioPath, videoPath, mergedPath);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "result: " + result);

        EventBus.getDefault().post(new MergeMediaEvent(result));
    }

    private String merge(String audio, String video, String mergedVideo) {

        try {

            Movie movie = MovieCreator.build(video);

            List numTracks = new ArrayList<>();

            for (Track track : movie.getTracks()) {
                if (!"soun".equals(track.getHandler())) {
                    numTracks.add(track);
                }
            }

            AACTrackImpl aacTrack = new AACTrackImpl(new FileDataSourceImpl(audio));

            Track trimedTrack = trimAudio(video, aacTrack);

            numTracks.add(trimedTrack);

            //CroppedTrack aacCroppedTrack = new CroppedTrack(aacTrack, 1, aacTrack.getSamples().size());

            //movie.addTrack(trimedTrack);

            movie.setTracks(numTracks);

            Container mp4file = new DefaultMp4Builder().build(movie);

            FileChannel fc = new FileOutputStream(new File(mergedVideo)).getChannel();

            mp4file.writeContainer(fc);

            fc.close();

            return mergedVideo;

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MergeMediaMP4Parser",e.toString());
        }

        return null;
    }

    public Track trimAudio(String video, Track fullAudio){
        try {

            IsoFile isoFile = new IsoFile(video);

            double lengthInSeconds = (double)
                    isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
                    isoFile.getMovieBox().getMovieHeaderBox().getTimescale();

            Track audioTrack = (Track) fullAudio;

            double startTime1 = 0;
            double endTime1 = lengthInSeconds;

            long currentSample = 0;
            double currentTime = 0;
            double lastTime = -1;
            long startSample1 = -1;
            long endSample1 = -1;

            for (int i = 0; i < audioTrack.getSampleDurations().length; i++) {
                long delta = audioTrack.getSampleDurations()[i];


                if (currentTime > lastTime && currentTime <= startTime1) {
                    // current sample is still before the new starttime
                    startSample1 = currentSample;
                }
                if (currentTime > lastTime && currentTime <= endTime1) {
                    // current sample is after the new start time and still before the new endtime
                    endSample1 = currentSample;
                }

                lastTime = currentTime;
                currentTime += (double) delta / (double) audioTrack.getTrackMetaData().getTimescale();
                currentSample++;
            }

            CroppedTrack cropperAacTrack = new CroppedTrack(fullAudio, startSample1, endSample1);

            return cropperAacTrack;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fullAudio;
    }

}
