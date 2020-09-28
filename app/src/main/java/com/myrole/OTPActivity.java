package com.myrole;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.myrole.utils.Config;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

//import com.google.android.exoplayer.C;

public class OTPActivity extends BaseActivity {

    String phoneNumber;
    String verifiedBy = "SMS";
    private int timercount = 60;
    private TextView timerText;
    private Timer timer;
    private ProgressBar timerBar;
    private boolean isForgotPass;
    private ProgressBarHandler progressBarHandler;
    private Activity activity;
    private Context context;
    EditText txt_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        context = this;
        activity = this;
        progressBarHandler = new ProgressBarHandler(activity);
        timerText = (TextView) findViewById(R.id.txt_otp_timer);
        timerBar = (ProgressBar) findViewById(R.id.timer_progress);
        timerBar.setMax(60);

        txt_otp = (EditText) findViewById(R.id.txt_otp);

        phoneNumber = getIntent().getStringExtra("PHONE");
        isForgotPass = getIntent().getBooleanExtra("FORGOT", false);
        if (Utils.isNetworkConnected(this, true))
            new SendOTPTask().execute("SMS", phoneNumber);

        findViewById(R.id.btn_verify).setOnClickListener(this);
        findViewById(R.id.txt_resend).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.txt_call_otp).setOnClickListener(this);
        applyFont();
    }
//    public void recivedSms(String message)
//    {
//        try
//        {
//           txt_otp.setText(message);
//        }
//        catch (Exception e)
//        {
//        }
//    }
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                txt_otp.setText(message);
                //Do whatever you want with the code here
            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_verify:
                String otp = ((EditText) findViewById(R.id.txt_otp)).getText().toString().trim();
                if (otp.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_otp)).setError(getString(R.string.error_otp_empty));
                    break;
                }
                if (Utils.isNetworkConnected(this, true))
                    new VerifyOTPTask().execute(verifiedBy, otp);
                //startActivity(new Intent(getApplicationContext(), UserNameActivity.class));
                break;
            case R.id.txt_resend:
                if (timercount != 60)
                    Toast.makeText(OTPActivity.this, "Please wait for " + timercount + " seconds", Toast.LENGTH_SHORT).show();
                else if (Utils.isNetworkConnected(this, true))
                    new SendOTPTask().execute("SMS", phoneNumber);
                break;
            case R.id.txt_call_otp:
                if (timercount != 60)
                    Toast.makeText(OTPActivity.this, "Please wait for " + timercount + " seconds", Toast.LENGTH_SHORT).show();
                else if (Utils.isNetworkConnected(this, true))
                    new SendOTPTask().execute("VOICE", phoneNumber);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_verification), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_resend), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_enter), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_otp), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_verify), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_received), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_call_otp), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_otp_timer), Config.NEXA, Config.BOLD);
    }

    private void startTimer() {
        //((TextView) findViewById(R.id.txt_resend)).setEnabled(false);
        //((TextView) findViewById(R.id.txt_call_otp)).setEnabled(false);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerBar.setProgress(60 - timercount);
                        timerText.setText("00:" + (timercount < 10 ? "0" + timercount : timercount));
                        if (timercount <= 0) {
                            stopTimer();
                            Toast.makeText(OTPActivity.this, getString(R.string.error_otp_suggestion), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                timercount--;
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        timer.cancel();
        timercount = 60;
        timerBar.setProgress(0);
        //((TextView) findViewById(R.id.txt_resend)).setEnabled(true);
        //((TextView) findViewById(R.id.txt_call_otp)).setEnabled(true);
    }

    class SendOTPTask extends AsyncTask<String, Void, String> {
        URL url;
        String response = "";
        String via;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            via = params[0];
            try {
                // url = new URL(Config.OTP_URL + Config.OTP_KEY + "/" + params[0] + "/" + params[1] + "/AUTOGEN");
                url = new URL(Config.OTP_URL + Config.OTP_KEY + "/" + params[0] + "/" + params[1] + "/AUTOGEN"+"/"+"myrole verify");

                //https://2factor.in/API/V1/{api_key}/SMS/{phone_number}/AUTOGEN/{template_name}
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                        Log.d("output lines", line);
                    }
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            if (result.isEmpty()) {
                ((EditText) findViewById(R.id.txt_otp)).setError(getString(R.string.error_otp_wrong));
                ((EditText) findViewById(R.id.txt_otp)).requestFocus();
            } else {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Status").equalsIgnoreCase("Success")) {
                        verifiedBy = via;
                        Config.OTP_SESSION = object.getString("Details");
                        startTimer();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class VerifyOTPTask extends AsyncTask<String, Void, String> {
        URL url;
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(Config.OTP_URL + Config.OTP_KEY + "/" + params[0] + "/VERIFY/" + Config.OTP_SESSION + "/" + params[1]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                        Log.d("output lines", line);
                    }
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
//            dismissProgressDialog();
            progressBarHandler.hide();
            try {
                if (result.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_otp)).setText("");
                    ((EditText) findViewById(R.id.txt_otp)).setError(getString(R.string.error_otp_invalid));
                    ((EditText) findViewById(R.id.txt_otp)).requestFocus();
                } else {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("Status").equalsIgnoreCase("Success")) {
                        Config.OTP_SESSION = "";
                        stopTimer();
                        if (isForgotPass) {
                            startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class)
                                    .putExtra("PHONE", phoneNumber)
                                    .putExtra("GOBACK", getIntent().getBooleanExtra("GOBACK", false)));
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), UserNameActivity.class)
                                    .putExtra("PHONE", phoneNumber));
                            finish();
                        }
                    } else {
                        ((EditText) findViewById(R.id.txt_otp)).setText("");
                        ((EditText) findViewById(R.id.txt_otp)).setError(getString(R.string.error_otp_invalid));
                        ((EditText) findViewById(R.id.txt_otp)).requestFocus();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}
