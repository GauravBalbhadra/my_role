package com.myrole;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.Utils;
import com.myrole.vo.Contact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FindContactsActivity extends BaseActivity {
    ArrayList<Contact> fetchedContacts;
    private Context context;
    private Activity activity;
    private String done, incomplete,skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        activity = this;
        setContentView(R.layout.activity_find_contacts);
        skip = getIntent().getStringExtra("Skip");
        done = getIntent().getStringExtra("Activity");
        incomplete = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_PROFILE_INCOMPLETE);
        if (skip.equals("userNameActivity")) {

            ((TextView) findViewById(R.id.txt_skip)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.btn_back)).setVisibility(View.GONE);


        } else {
            ((TextView) findViewById(R.id.txt_skip)).setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.btn_back)).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.btn_contact).setOnClickListener(this);
        findViewById(R.id.txt_skip).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        context = this;
        applyFont();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_skip:
                startActivity(new Intent(getApplicationContext(), FacebookConnectActivity.class).putExtra("Skip",skip)
                        .putExtra("GOBACK", false));
                finishAffinity();
                break;
            case R.id.btn_contact:
                int hasCameraPermission = ContextCompat.checkSelfPermission(FindContactsActivity.this, Manifest.permission.READ_CONTACTS);
                if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FindContactsActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                    return;
                }
                new FetchPhoneContactsTask().execute();
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == 0)
                    new FetchPhoneContactsTask().execute();
                break;
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_skip), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_find), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_see), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_contact), Config.NEXA, Config.REGULAR);

    }

    class FetchPhoneContactsTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog("Fetching contacts from phone...");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Config.CONTACTS_JSON = new JSONObject();
//                JSONObject friend;
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                int i = 0;

                while (phones.moveToNext()) {
                    String pNumber = "";
                    // get display name
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    // get phone number
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


//                    String countryCode = edtCountryCode.getText().toString().trim();
//                    String phone_Number = edtPhone.getText().toString().trim();
                    if (phoneNumber.length() > 0) {
                        if (Utils.isValidPhoneNumber(phoneNumber)) {
                            Log.d("Log", "ContactsLogs" + phoneNumber);

                            if (phoneNumber.length() > 10) {

                                pNumber = phoneNumber.replace("+91", "");

                            } else if (phoneNumber.length() == 10) {

                                pNumber = phoneNumber;
                            } else {

                                pNumber = phoneNumber;
                            }

//                            boolean status=false;
//                            try {
//                                status = Utils.validateUsing_libphonenumber(pNumber, name, i);
//                            } catch (Exception e) {
//
//                            }

                            boolean status = Utils.validateUsing_libphonenumber(pNumber, name, i);

                            if (status) {

//                                tvIsValidPhone.setText("Valid Phone Number (libphonenumber)");

                                System.out.println("Valid Phone Number (libphonenumber)");

//                                friend = new JSONObject();
//                                friend.put("name", name);
//                                friend.put("phone", pNumber);
//                                Config.CONTACTS_JSON.put(i + "", friend);
//                                Log.v("Contact:", name + ".................." + phoneNumber);


                            } else {
//                                tvIsValidPhone.setText("Invalid Phone Number (libphonenumber)");

                                System.out.println("Invalid Phone Number (isValidPhoneNumber)");
                            }
                        } else {
                            System.out.println("Invalid Phone Number (isValidPhoneNumber)");
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Country Code and Phone Number is required", Toast.LENGTH_SHORT).show();
                    }

                    i++;

                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            super.onPostExecute(result);
            if (Config.CONTACTS_JSON.length() > 0)
                new UploadPhoneContactsTask().execute();
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_no_contact_phone), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), FacebookConnectActivity.class).putExtra("Skip","0"));
            }
        }
    }

    class UploadPhoneContactsTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog("Syncing contacts with server...");
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                ArrayList<String> contactArr = new ArrayList<String>();

//                for (int i = 0; i < Config.CONTACTS_JSON.length(); i++) {
//                    try {
//
//                        contactArr.add(Config.CONTACTS_JSON.getJSONObject(i + "").getString("phone"));
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }

                Iterator<String> temp = Config.CONTACTS_JSON.keys();
                while (temp.hasNext()) {
                    String key = temp.next();
                    try {

                        contactArr.add(Config.CONTACTS_JSON.getJSONObject(key + "").getString("phone"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Log", "ContactArray" + contactArr);
                postDataParams = new HashMap<String, String>();
                postDataParams.put("user_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
                postDataParams.put("mobile", contactArr.toString().replaceAll("\\s", "").trim());

                String result = HTTPUrlConnection.getInstance().load(context, Config.MATCH_PHONE_CONTACT, postDataParams);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            super.onPostExecute(result);

            Intent intent = new Intent(context, PhoneContactsActivity.class);
            intent.putExtra("Activity", done);
            intent.putExtra("RESULT", result);
            startActivity(intent);
        }
    }
}
