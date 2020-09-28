package com.myrole;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.adapter.ContactsAdapter;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.RestClient;
import com.myrole.utils.Utils;
import com.myrole.vo.Contact;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FBContactsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsAdapter contactsAdapter;
    private Context context;
    private Activity activity;
    private String done,incomplete;
    private ProgressBarHandler progressBarHandler;
    private RelativeLayout lay_contacts;
    String txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        context = this;
        activity = this;
        progressBarHandler = new ProgressBarHandler(activity);
        lay_contacts = (RelativeLayout) findViewById(R.id.lay_contacts);
        incomplete = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_PROFILE_INCOMPLETE);
        done = getIntent().getStringExtra("Activity");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Config.APPBARTITLE = "Facebook Friends";
        setToolbar();
        applyFont();
        Intent intent = getIntent();
        try {
            Config.FACEBOOK_JSON = new JSONArray(intent.getStringExtra("FBFRIENDS"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((RelativeLayout)findViewById(R.id.search_rl)).setVisibility(View.GONE);

        new UpdateUserFbIdProfileTask().execute();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        contactList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(contactList,context,activity );
        recyclerView.setAdapter(contactsAdapter);

        findViewById(R.id.txt_done).setOnClickListener(this);

         txt = ((TextView) findViewById(R.id.txt_follow)).getText().toString().trim();
//        ((TextView) findViewById(R.id.txt_follow)).setText(Config.FACEBOOK_JSON.length() + " " + txt);

        if (Config.FACEBOOK_JSON.length() == 0)
            findViewById(R.id.btn_follow_all).setVisibility(View.GONE);

        new UploadFbIdsTask().execute();
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_follow), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_done), Config.NEXA, Config.BOLD);
    }

    public void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle(Config.APPBARTITLE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_pink_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_done:
                if (incomplete.equals("1")){
                    if (getIntent().getBooleanExtra("GOBACK", false)) {
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), BuildProfileActivity.class));
                        finishAffinity();
                    }
                }else {

                    if (done.equals("1")) {
                        startActivity(new Intent(context, OptionsActivity.class));
                        finishAffinity();
                    } else if (done.equals("2")) {
                        startActivity(new Intent(context, FacebookConnectActivity.class));
                        finishAffinity();
                    } else  {
                        startActivity(new Intent(context, MainDashboardActivity.class));
                        finishAffinity();
                    }

                }

//                if (getIntent().getBooleanExtra("GOBACK", false)) {
//                    finish();
//                } else {
//                    startActivity(new Intent(getApplicationContext(), BuildProfileActivity.class));
//                    finishAffinity();
//                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


    class AddContactsTask extends AsyncTask<String, Void, String> {
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

            try {
                Contact contact;
                if (Config.FACEBOOK_JSON != null) {
                    for (int l = 0; l < Config.FACEBOOK_JSON.length(); l++) {
                        contact = new Contact();
                        contact.type = "FACEBOOK";
                        contact.id = Config.FACEBOOK_JSON.getJSONObject(l).getString("id");
                        contact.name = Config.FACEBOOK_JSON.getJSONObject(l).getString("name");
                        contact.image = "http://graph.facebook.com/" + contact.id + "/picture?type=large";
                        //contact.email= Config.FACEBOOK_JSON.getJSONObject(l).getString("email");


//                        contact.status = "FOLLOW";
                        contactList.add(contact);
                    }
                }
//                contactsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();

        }
    }

    class UpdateUserFbIdProfileTask extends AsyncTask<String, Void, String> {

        String response = "";
        String id = getIntent().getStringExtra("FBID");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            AppPreferences preferences = AppPreferences.getAppPreferences(FBContactsActivity.this);

            try {

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                RestClient client;

                entity.addPart("user_id", new StringBody(preferences.getStringValue(AppPreferences.USER_ID)));
                entity.addPart("fb_id", new StringBody(id));

//                entity.addPart("fb_id", new StringBody(preferences.getStringValue(AppPreferences.USER_ID)));
//                entity.addPart("user_id", new StringBody(id));

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
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                  //  JSONObject data = new JSONObject("data");
                    Log.v(TAG, "result =" +result);
                   // Log.v(TAG, "fb_id =" +object.getString("id"));

                   // Toast.makeText(FBContactsActivity.this, object.getString("fb_id"), Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                    finishAffinity();
                } else {

                    Toast.makeText(FBContactsActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class UploadFbIdsTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgessDialog("Syncing fb contacts with server...");
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                ArrayList<String> contactArr = new ArrayList<String>();

                for (int i = 0; i < Config.FACEBOOK_JSON.length(); i++) {
                    try {

                        contactArr.add(Config.FACEBOOK_JSON.getJSONObject(i).getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

//                Iterator<String> temp = Config.FACEBOOK_JSON.keys();
//                while (temp.hasNext()) {
//                    String key = temp.next();
//                    try {
//
//                        contactArr.add(Config.FACEBOOK_JSON.getJSONObject(key + "").getString("phone"));
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }

                Log.d("Log", "ContactArray" + contactArr);
                postDataParams = new HashMap<String, String>();
                postDataParams.put("user_id", AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
               postDataParams.put("fb_ids", contactArr.toString().replaceAll("\\s", "").trim());
                ArrayList<String>  appearanceList = new ArrayList<String>();

              //  appearanceList.add("1610041602356183");
//                postDataParams.put("fb_ids", appearanceList.toString().replaceAll("\\s", "").trim());
                String result = HTTPUrlConnection.getInstance().load(context, Config.MATCH_FB_ID, postDataParams);
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

            try {
                JSONObject object = new JSONObject(result);
                Contact contact;
                if (object.getBoolean("status")) {
                    Log.v(TAG, result);
                    JSONArray data = object.getJSONArray("data");
                        for (int l = 0; l < data.length(); l++) {
                            JSONObject jobj = data.getJSONObject(l);
                            contact = new Contact();
                            contact.type = "FACEBOOK";
                            contact.id = jobj.getString("id");
                            contact.name = jobj.getString("name");
                            contact.image = jobj.getString("image");

                            //contact.follow_request = jobj.getString("follow_request");
                            //contact.email= Config.FACEBOOK_JSON.getJSONObject(l).getString("email");





                            if (jobj.getInt("follow")==0){
//                                contact.id = jobj.getString("id");
                                contact.follow = jobj.getString("follow");
                                contact.status = "FOLLOW";
                            }else {
//                                contact.id = jobj.getString("id");
                                contact.follow = jobj.getString("follow");
                                contact.status = "FOLLOWING";
                            }

                            if (jobj.isNull( "private_user" )){
                                contact.private_user = "0";
                            }else{
                               // Toast.makeText(FBContactsActivity.this, String.valueOf(jobj.getInt("private_user")), Toast.LENGTH_LONG).show();
                                contact.private_user = jobj.getString("private_user");
                                if (jobj.getInt("follow_requert")==0){
//                                contact.id = jobj.getString("id");
                                    contact.follow_request = jobj.getString("follow_requert");
                                    contact.status = "REQUEST";
                                }else {
//                                contact.id = jobj.getString("id");
                                    contact.follow_request = jobj.getString("follow_requert");
                                    contact.status = "SEND_REQUEST";
                                }
                            }

                            contactList.add(contact);
                        }
                    ((TextView) findViewById(R.id.txt_follow)).setText(contactList.size() + " " + txt);
                    contactsAdapter.notifyDataSetChanged();

//                    new AddContactsTask().execute();

                } else {
                    Toast.makeText(FBContactsActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
