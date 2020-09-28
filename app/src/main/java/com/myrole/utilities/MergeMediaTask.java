package com.myrole.utilities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

public class MergeMediaTask extends AsyncTask<Object, Integer, String> {

    private static final boolean DEBUG = true;
    private static final String TAG = "MergeMediaTask";

    private WeakReference<ProgressDialog> mWeakProgressDialog;

    public MergeMediaTask(ProgressDialog progressDialog) {
        mWeakProgressDialog = new WeakReference<ProgressDialog>(progressDialog);
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

        /*try {

            return snapshotToolkit.merge(audioPath, videoPath, mergedPath);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }*/
        return null;
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

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "result: " + result);

        if (mWeakProgressDialog.get() != null && mWeakProgressDialog.get().isShowing())
            mWeakProgressDialog.get().dismiss();
        EventBus.getDefault().post(new MergeMediaEvent(result));
    }
}
