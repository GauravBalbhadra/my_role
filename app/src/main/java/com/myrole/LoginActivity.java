package com.myrole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.fragments.AlertLoginDilogFragment;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {

    String username, password;
    private ProgressBarHandler progressBarHandler;
    private Activity activity;
    private Context context;
    private AppPreferences pref;
    String login_value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;
        context = this;
        progressBarHandler = new ProgressBarHandler(activity);

        pref = AppPreferences.getAppPreferences(activity);
        login_value = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.LOGIN_USER_NAME);
        if (!login_value.equals("")){
            ((EditText) findViewById(R.id.txt_name)).setText(login_value);
        }

        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.txt_sign_up).setOnClickListener(this);
        findViewById(R.id.txt_forgot_pass).setOnClickListener(this);

        applyFont();
    }

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_sign_in:
                username = ((EditText) findViewById(R.id.txt_name)).getText().toString().trim();
                AppPreferences pref = AppPreferences.getAppPreferences(getApplicationContext());
//                try {
                    String regexStr = "^[0-9]*$";
                    if(((EditText) findViewById(R.id.txt_name)).getText().toString().trim().matches(regexStr))
                    {
                        Log.i("",username+" is a number");
                        pref.putStringValue(AppPreferences.LOGIN_PHONE_NO, username);
                        pref.putStringValue(AppPreferences.LOGIN_USER_NAME, username);
                    }
                    else{
                        pref.putStringValue(AppPreferences.LOGIN_PHONE_NO, "");
                        pref.putStringValue(AppPreferences.LOGIN_USER_NAME, username);
                    Log.i("",username+" is not a number");
                    }
//                    if()
//                  //  int num = Integer.parseInt(username);
//                    Long nu=Long.p(username);
//                    String no=String.valueOf(nu);
//                    Log.i("",nu+" is a number");
//                    pref.putStringValue(AppPreferences.LOGIN_PHONE_NO, no);
//                    pref.putStringValue(AppPreferences.LOGIN_USER_NAME, username);
//                } catch (NumberFormatException e) {
//                    pref.putStringValue(AppPreferences.LOGIN_USER_NAME, username);
//                    Log.i("",username+" is not a number");
//                }

                if (username.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_name)).setError(getString(R.string.error_username_empty));
                    break;
                }
                password = ((EditText) findViewById(R.id.txt_password)).getText().toString().trim();
                if (password.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_password)).setError(getString(R.string.error_password_empty));
                    break;
                } else if (password.length() < 6) {
                    ((EditText) findViewById(R.id.txt_password)).setError(getString(R.string.error_password_small));
                    break;
                }
                if (Utils.isNetworkConnected(this, true))
                    new SignInTask().execute();
                break;
            case R.id.txt_sign_up:
                startActivity(new Intent(getApplicationContext(), MobileNumberActivity.class));
                break;
            case R.id.txt_forgot_pass:
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                break;
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_name), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_forgot_pass), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_password), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_sign_up), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_dont), Config.NEXA, Config.REGULAR);

    }

    class SignInTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
        progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("username", username);
            postDataParams.put("password", password);

            return HTTPUrlConnection.getInstance().load(getApplicationContext(), Config.LOGIN, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONArray("data").getJSONObject(0);
                    AppPreferences pref = AppPreferences.getAppPreferences(getApplicationContext());
                    pref.putStringValue(AppPreferences.USER_ID, data.getString("id"));
//                    pref.putStringValue(AppPreferences.USER_NAME, data.getString("username"));
                    pref.putStringValue(AppPreferences.USER_NAME, data.getString("name"));
                    pref.putStringValue(AppPreferences.U_NAME, data.getString("name"));
                    pref.putStringValue(AppPreferences.USER_PHONE, data.getString("mobile"));
                    pref.putStringValue(AppPreferences.USER_IMAGE_URL, data.getString("image"));
                    pref.putStringValue(AppPreferences.USER_PROFILE_INCOMPLETE,data.getString("incomplete"));
                    if (data.getString("incomplete").equals("1")){
                        startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
                        //startActivity(new Intent(getApplicationContext(), BuildProfileActivity.class));
                        finishAffinity();
                    }else {
                        startActivity(new Intent(getApplicationContext(), MainDashboardActivity.class));
                        finishAffinity();
                    }

                } else {


                    FragmentActivity activity = (FragmentActivity)(context);
                    FragmentManager fm = activity.getSupportFragmentManager();
                    AlertLoginDilogFragment alertdFragment = new AlertLoginDilogFragment();
                    Bundle data = new Bundle();//create bundle instance
                data.putString("yo", object.getString("message"));//put string to pass with a key value
                    alertdFragment.setArguments(data);//Set bundle data to fragment
                    // alertdFragment.setFetchVehicleInterface(activity);
                    // Show Alert DialogFragment
                    alertdFragment.show(fm, "Alert Dialog Fragment");


//
////                                            SurfaceView videoSurface;
////                        MediaPlayer player;
////                        VideoControllerView controller;
////                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                      View view = getLayoutInflater().inflate(R.layout.alert_deatil, null);
////                        alertDialogBuilder.setView(view);
////                        alertDialogBuilder.setCancelable(false);
////                        final AlertDialog alertDialog = alertDialogBuilder.create();
//                        final Dialog m_dialog;
//                        m_dialog = new Dialog(LoginActivity.this, android.R.style.Theme_DeviceDefault_NoActionBar_Overscan);
//                        m_dialog.setCancelable(false);
//                        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        m_dialog.setContentView(R.layout.alert_deatil);
//                        ((TextView) m_dialog.findViewById(R.id.txt2)).setText(object.getString("message"));
//                       // RelativeLayout rl = (RelativeLayout) m_dialog.findViewById(R.id.videoSurfaceContainer);
//                      //  final VideoPlayer videoPlayer = new VideoPlayer(context);
////                        rl.addView(videoPlayer);
//                       // videoPlayer.playUrl(videoUrl);
//
//                        /*
//                        final VideoView videoView = (VideoView) view.findViewById(R.id.video_view);
//                        MediaController mediacontroller = new MediaController(getActivity());
//                        mediacontroller.setAnchorView(videoView);
//                        // Get the URL from String VideoURL
//                        Uri video = Uri.parse(videoUrl);
//                        videoView.setMediaController(mediacontroller);
//                        videoView.setVideoURI(video);
//                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                            // Close the progress bar and play the video
//                            public void onPrepared(MediaPlayer mp) {
//                                videoView.start();
//                            }
//                        });*/
//
//
//                        m_dialog.findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                m_dialog.dismiss();
//                            }
//                        });
//
//                        m_dialog.show();


                   // Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                //Toast.makeText(activity, "JsonException : "+e.get, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
