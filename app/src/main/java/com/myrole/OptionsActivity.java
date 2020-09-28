package com.myrole;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.RestClient;
import com.myrole.utils.Utils;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OptionsActivity extends BaseActivity {

    private Switch switchAccount, switchNotification;
    private Context context;
    private Activity activity;
    private String accountSwitchState, notificationSwitchState;
    private String user_id;
    private AppPreferences preferences;
    private ProgressBarHandler progressBarHandler;
    private RelativeLayout lay_option_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        context = this;
        activity = this;
        progressBarHandler =new ProgressBarHandler(activity);
        lay_option_setting = (RelativeLayout) findViewById(R.id.lay_option_setting);
//        switchState = "1";
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.edit_profile_txt).setOnClickListener(this);
        findViewById(R.id.change_pass_txt).setOnClickListener(this);
        findViewById(R.id.private_account_txt).setOnClickListener(this);
        switchAccount = (Switch) findViewById(R.id.switch_account);
//        switchAccount.setChecked(true);
        Log.d("Log", "Switch State 1" + switchAccount.isChecked());
        switchAccount.setOnClickListener(this);
        switchNotification = (Switch) findViewById(R.id.switch_notification);
        Log.d("Log", "Switch State 1" + switchNotification.isChecked());
        switchNotification.setOnClickListener(this);
        findViewById(R.id.push_notifications_txt).setOnClickListener(this);
        findViewById(R.id.share_my_role_txt).setOnClickListener(this);
        findViewById(R.id.instagram_friends_txt).setOnClickListener(this);
        findViewById(R.id.facebook_friends_txt).setOnClickListener(this);
        findViewById(R.id.privacy_policy_txt).setOnClickListener(this);
        findViewById(R.id.terms_conditions_txt).setOnClickListener(this);
        findViewById(R.id.app_version_txt).setOnClickListener(this);
        findViewById(R.id.txt_feedback).setOnClickListener(this);
        findViewById(R.id.txt_logout).setOnClickListener(this);
        PackageInfo pInfo = null;

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
//        int verCode = pInfo.versionCode;
        ((TextView) findViewById(R.id.app_version_txt)).setText("App Version   " + version);

        applyFont();

        preferences = AppPreferences.getAppPreferences(OptionsActivity.this);

        user_id = preferences.getStringValue(AppPreferences.USER_ID);

        new GetUserDetailTask().execute(user_id);
        findViewById(R.id.material_edit_layout).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.edit_profile_txt:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
                case R.id.txt_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
                case R.id.txt_logout:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("MY Role");
                    alertDialog.setMessage("Are you sure you want logout this?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AppPreferences preferences = AppPreferences.getAppPreferences(context);
                            preferences.clear();
                            Intent intent = new Intent(context, WelcomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                break;
            case R.id.change_pass_txt:
                String phoneNumber = AppPreferences.getAppPreferences(this).getStringValue(
                        AppPreferences.USER_PHONE);
                startActivity((new Intent(getApplicationContext(), OTPActivity.class))
                        .putExtra("PHONE", phoneNumber)
                        .putExtra("FORGOT", true)
                        .putExtra("GOBACK", true));
                break;
            case R.id.switch_account:

                accountSwitchStateChanged();

                break;
            case R.id.switch_notification:

                notificationSwitchStateChanged();

                break;
            case R.id.share_my_role_txt:
                shareMyRole();
                break;
            case R.id.instagram_friends_txt:
                startActivity(new Intent(getApplicationContext(), FindContactsActivity.class).putExtra("Activity","1").putExtra("Skip","0"));
                break;
            case R.id.facebook_friends_txt:
                startActivity(new Intent(getApplicationContext(), FacebookConnectActivity.class).putExtra("Activity","1").putExtra("GOBACK", true).putExtra("Skip","1"));
                break;
            case R.id.privacy_policy_txt:
                Intent i = new Intent(this, PrivacyPolicyScreen.class);
                startActivity(i);
                break;
            case R.id.terms_conditions_txt:
                Intent intent = new Intent(this, TermsAndConditionScreen.class);
                startActivity(intent);

                break;
//            case R.id.app_version_txt:
//                PackageInfo pInfo = null;
//                try {
//                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
//                String version = pInfo.versionName;
//                ((TextView)findViewById(R.id.app_version_txt)).setText("App Version   "+version);
//                break;

        }
    }

    public void accountSwitchStateChanged() {
        if (switchAccount != null) {

            if (switchAccount.isChecked()) {
                switchAccount.setChecked(true);
                accountSwitchState = "1";

                new UpdatePrivateAccountProfileTask().execute();
            } else {
                switchAccount.setChecked(false);
                accountSwitchState = "0";

                new UpdatePrivateAccountProfileTask().execute();
            }
            switchAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    if (isChecked) {
                        switchAccount.setChecked(true);
                        accountSwitchState = "1";

                        new UpdatePrivateAccountProfileTask().execute();
                    } else {
                        switchAccount.setChecked(false);
                        accountSwitchState = "0";

                        new UpdatePrivateAccountProfileTask().execute();
                    }
                }
            });
        }
    }

    public void notificationSwitchStateChanged() {
        if (switchNotification != null) {

            if (switchNotification.isChecked()) {
                switchNotification.setChecked(true);
                notificationSwitchState = "1";

                new UpdateNotificationProfileTask().execute();
            } else {
                switchNotification.setChecked(false);
                notificationSwitchState = "0";

                new UpdateNotificationProfileTask().execute();
            }
            switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                    if (isChecked) {
                        switchNotification.setChecked(true);
                        notificationSwitchState = "1";

                        new UpdateNotificationProfileTask().execute();
                    } else {
                        switchNotification.setChecked(false);
                        notificationSwitchState = "0";

                        new UpdateNotificationProfileTask().execute();
                    }
                }
            });
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_title), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.account_setting_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.general_settings_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.follow_people_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.about_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.edit_profile_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.change_pass_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.private_account_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.push_notifications_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.share_my_role_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.instagram_friends_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.facebook_friends_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.privacy_policy_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.terms_conditions_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.app_version_txt), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_feedback), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_logout), Config.NEXA, Config.BOLD);

    }

    private void shareMyRole() {
        String shareBody = "https://play.google.com/store/apps/details?id=************************";

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "APP NAME (Open it in Google Play Store to Download the Application)");

        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    class UpdatePrivateAccountProfileTask extends AsyncTask<String, Void, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
//            AppPreferences preferences = AppPreferences.getAppPreferences(OptionsActivity.this);

            try {

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                RestClient client;

                entity.addPart("user_id", new StringBody(preferences.getStringValue(AppPreferences.USER_ID)));
                entity.addPart("private_user", new StringBody(accountSwitchState));

                client = new RestClient(Config.UPDATE_PROFILE);
                client.setMultipartEntity(entity);
                client.Execute(RestClient.RequestMethod.POST);
                response = client.getResponse();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
//            progressBar.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    Log.v(TAG, result);
//                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                    finishAffinity();
                } else {
                    Toast.makeText(OptionsActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class UpdateNotificationProfileTask extends AsyncTask<String, Void, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
//            AppPreferences preferences = AppPreferences.getAppPreferences(OptionsActivity.this);

            try {

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                RestClient client;

                entity.addPart("user_id", new StringBody(preferences.getStringValue(AppPreferences.USER_ID)));
                entity.addPart("notification", new StringBody(notificationSwitchState));

                client = new RestClient(Config.UPDATE_PROFILE);
                client.setMultipartEntity(entity);
                client.Execute(RestClient.RequestMethod.POST);
                response = client.getResponse();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
//            progressBar.setVisibility(View.GONE);

            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    Log.v(TAG, result);
//                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                    finishAffinity();
                } else {
                    Toast.makeText(OptionsActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class GetUserDetailTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            lay_option_setting.setVisibility(View.GONE);
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            return HTTPUrlConnection.getInstance().load(context, Config.GET_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            lay_option_setting.setVisibility(View.VISIBLE);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject userDetail = object.getJSONArray("data").getJSONObject(0);

                    if (userDetail.getString("private_user").equals("1")) {
                        switchAccount.setChecked(true);
                        accountSwitchState = "1";
                    } else {
                        switchAccount.setChecked(false);
                        accountSwitchState = "0";
                    }


                    if (userDetail.getString("notification").equals("1")) {
                        switchNotification.setChecked(true);
                        notificationSwitchState = "1";
                    } else {
                        switchNotification.setChecked(false);
                        notificationSwitchState = "0";
                    }

                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
