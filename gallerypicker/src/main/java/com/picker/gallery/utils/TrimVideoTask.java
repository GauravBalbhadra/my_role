package com.picker.gallery.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_SUCCESS;

//import com.v9k.minimal.vpl.SnapshotToolkit;

public class TrimVideoTask extends AsyncTask<Object, Integer, String> {

    private static final boolean DEBUG = true;
    private static final String TAG = "TrimVideoTask";

    private WeakReference<ProgressDialog> mWeakProgressDialog;

    //public SnapshotToolkit mSnapshotToolkit = new SnapshotToolkit();

    public TrimVideoTask(ProgressDialog progressDialog) {
        mWeakProgressDialog = new WeakReference<ProgressDialog>(progressDialog);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mWeakProgressDialog.get() != null)
            mWeakProgressDialog.get().setProgress(values[0]);
    }

    @Override
    protected void onPreExecute() {
        ProgressDialog progressDialog = mWeakProgressDialog.get();

        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    /* access modifiers changed from: protected */
    public String doInBackground(Object... params) {

        float start = (float)params[0];
        float end = (float)params[1];
        String videoPath = (String)params[2];
        String trimedPath = (String)params[3];

//        try {
//            return mSnapshotToolkit.trim((double) start, (double) end, videoPath, trimedPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }

        int rc = FFmpeg.execute("-ss " + start + " -i " + videoPath + " -to " + end + " -c copy " + trimedPath);

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

        if (mWeakProgressDialog.get() != null && mWeakProgressDialog.get().isShowing())
            mWeakProgressDialog.get().dismiss();

        EventBus.getDefault().post(new TrimVideoEvent(result));
    }
}
