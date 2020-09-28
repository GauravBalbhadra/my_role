package com.myrole;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ForgotPasswordActivity extends BaseActivity {

    private String phoneNumber = "";
    private ProgressBarHandler progressBarHandler;
    private Activity activity;
    //String mPermission2 = Manifest.permission.RECEIVE_SMS;
    //String mPermission1 = Manifest.permission.READ_SMS;
    private AppPreferences pref;
    String phone_value="";
    private static final int REQUEST_CODE_PERMISSION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        activity = this;
        /*try {
            if (ActivityCompat.checkSelfPermission(ForgotPasswordActivity.this, mPermission2)
                    != PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity, new String[]{mPermission2},
                        REQUEST_CODE_PERMISSION);
                try {
                    if (ActivityCompat.checkSelfPermission(ForgotPasswordActivity.this, mPermission1)
                            != PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(activity, new String[]{mPermission1},
                                REQUEST_CODE_PERMISSION);

                        // If any permission above not allowed by user, this condition will
                        // execute every time, else your else part will work
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        pref = AppPreferences.getAppPreferences(activity);
        phone_value = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.LOGIN_PHONE_NO);

        if (!phone_value.equals("")){
            ((EditText) findViewById(R.id.txt_mobile_number)).setText(phone_value);
        }

        progressBarHandler = new ProgressBarHandler(activity);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_proceed).setOnClickListener(this);
        applyFont();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_proceed:
                phoneNumber = ((EditText) findViewById(R.id.txt_mobile_number)).getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_mobile_number)).setError(getString(R.string.error_mobile_number_empty));
                    break;
                } else if (phoneNumber.length() != 10) {
                    ((EditText) findViewById(R.id.txt_mobile_number)).setError(getString(R.string.error_mobile_number_digit));
                    break;
                }
                //phoneNumber = ((TextView) findViewById(R.id.txt_mobile_code)).getText().toString().trim() + phoneNumber;
                if (Utils.isNetworkConnected(this, true))
                    new VerifyNumberTask().execute();
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_forgot), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_enter), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_mobile_code), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_mobile_number), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.btn_proceed), Config.NEXA, Config.REGULAR);

    }

    class VerifyNumberTask extends AsyncTask<Void, Void, String> {

        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
           progressBarHandler.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("phone", phoneNumber);
            return HTTPUrlConnection.getInstance().load(getApplicationContext(), Config.VERIFY_PHONE, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();

            try {
                if ((new JSONObject(result)).getBoolean("status")) {

                    startActivity((new Intent(getApplicationContext(), OTPActivity.class))
                            .putExtra("PHONE", phoneNumber)
                            .putExtra("FORGOT", true)
                            .putExtra("GOBACK", false));
                } else {
                    ((EditText) findViewById(R.id.txt_mobile_number)).setError((new JSONObject(result)).getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
