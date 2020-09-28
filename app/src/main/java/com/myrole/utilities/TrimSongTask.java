package com.myrole.utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import org.greenrobot.eventbus.EventBus;

import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_SUCCESS;

//import com.v9k.minimal.vpl.SnapshotToolkit;

public class TrimSongTask extends AsyncTask<Object, Void, String> {

    private static final boolean DEBUG = true;
    private static final String TAG = "TrimSongTask";

    //public SnapshotToolkit mSnapshotToolkit = new SnapshotToolkit();

    /* access modifiers changed from: protected */
    public String doInBackground(Object... params) {

        float start = (float)params[0];
        float end = (float)params[1];
        String audioPath = (String)params[2];
        String trimedPath = (String)params[3];

        Log.d(TAG, "start: " + params[0]);
        Log.d(TAG, "end: " + params[1]);
        Log.d(TAG, "audioPath: " + params[2]);
        Log.d(TAG, "trimedPath: " + params[3]);

//        try {
//            return mSnapshotToolkit.trim((double) start, (double) end, audioPath, trimedPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }

        int rc = FFmpeg.execute("-ss " + start + " -i " + audioPath + " -to " + end + " -c copy " + trimedPath);

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
            return trimedPath;
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
            return null;
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            return null;
        }

    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String result) {
        Log.d(TAG, "result: " + result);
        EventBus.getDefault().post(new TrimSongEvent(result));
    }
}
