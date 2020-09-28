package com.myrole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ResetPasswordActivity extends BaseActivity {

    String newPassword, confirmPassword, phoneNumber;

    private ProgressBar progressBarHandler;
    private Activity activity;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumber = getIntent().getStringExtra("PHONE");
        setContentView(R.layout.activity_reset_password);
        context = this;
        activity = this;
        progressBarHandler=(ProgressBar)findViewById(R.id.progressBarHandler);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_done).setOnClickListener(this);

        applyFont();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_done:
                newPassword = ((EditText) findViewById(R.id.txt_new_password)).getText().toString().trim();
                if (newPassword.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_new_password)).setError(getString(R.string.error_password_empty));
                    break;
                } else if (newPassword.length() < 6) {
                    ((EditText) findViewById(R.id.txt_new_password)).setError(getString(R.string.error_password_small));
                    break;
                }
                confirmPassword = ((EditText) findViewById(R.id.txt_confirm_password)).getText().toString().trim();
                if (confirmPassword.isEmpty()) {
                    ((EditText) findViewById(R.id.txt_confirm_password)).setError(getString(R.string.error_password_empty));
                    break;
                } else if (confirmPassword.length() < 6) {
                    ((EditText) findViewById(R.id.txt_confirm_password)).setError(getString(R.string.error_password_small));
                    break;
                }
                if (!newPassword.equalsIgnoreCase(confirmPassword)) {
                    ((EditText) findViewById(R.id.txt_confirm_password)).setError(getString(R.string.error_password_no_match));
                    break;
                }
                if (Utils.isNetworkConnected(this, true))
                    new ResetPasswordTask().execute();
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_reset), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_new_password), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_confirm_password), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.btn_done), Config.NEXA, Config.REGULAR);

    }

    class ResetPasswordTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("mobile", phoneNumber);
            postDataParams.put("new_password", newPassword);
            postDataParams.put("confirm_password", confirmPassword);
            return HTTPUrlConnection.getInstance().load(getApplicationContext(), Config.RESET_PASSWORD, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    if (getIntent().getBooleanExtra("GOBACK", false)) {
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finishAffinity();
                    }
                }
                Toast.makeText(ResetPasswordActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
