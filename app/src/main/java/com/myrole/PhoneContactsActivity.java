package com.myrole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;
import com.myrole.vo.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PhoneContactsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsAdapter contactsAdapter;
    private Context context;
    private String fetchedContacts = "";
    private String done;
    private String userId;
    private Activity activity;
    private int count = 0;
    private ProgressBarHandler progressBarHandler;
    private RelativeLayout lay_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        context = this;
        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Config.APPBARTITLE = "Phone Friends";
        setToolbar();
        applyFont();
        progressBarHandler = new ProgressBarHandler(activity);
        lay_contacts = (RelativeLayout) findViewById(R.id.lay_contacts);
        userId = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID);
        done = getIntent().getStringExtra("Activity");
        fetchedContacts = getIntent().getStringExtra("RESULT");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        contactList = new ArrayList<>();
//        for (int i = 0; i < Config.CONTACTS_JSON.length(); i++) {
//            try {
//                contactList.add(Config.CONTACTS_JSON.getJSONObject(i + "").getString("name"));
//                contactList.add(Config.CONTACTS_JSON.getJSONObject(i + "").getString("phone"));
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }


        contactsAdapter = new ContactsAdapter(contactList, context,activity);
        recyclerView.setAdapter(contactsAdapter);
        new AddContactsTask().execute();
        ((TextView) findViewById(R.id.txt_done)).setOnClickListener(this);



        EditText searchView = (EditText) findViewById(R.id.edtsearch);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                s = s.toString().toLowerCase();

                final List<Contact> filteredList = new ArrayList<>();

                for (int i = 0; i < contactList.size(); i++) {

                    final String text = contactList.get(i).name.toLowerCase();
                    if (text.contains(s)) {

                        filteredList.add(contactList.get(i));
                    }
                }

                contactsAdapter = new ContactsAdapter(filteredList,context,activity);

                recyclerView.setAdapter(contactsAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                contactsAdapter.notifyDataSetChanged();  // data set changed

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        searchView.setQueryHint("Search category");
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(activity.getComponentName()));
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                query = query.toString().toLowerCase();
//
//                final List<Contact> filteredList = new ArrayList<>();
//
//                for (int i = 0; i < contactList.size(); i++) {
//
//                    final String text = contactList.get(i).name.toLowerCase();
//                    if (text.contains(query)) {
//
//                        filteredList.add(contactList.get(i));
//                    }
//                }
//
//                contactsAdapter = new ContactsAdapter(filteredList,context,activity);
//
//                recyclerView.setAdapter(contactsAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
//                contactsAdapter.notifyDataSetChanged();  // data set changed
//                return false;
//            }
//        });

    }


    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_follow), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.edtsearch), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_done), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.btn_follow_all), Config.NEXA, Config.BOLD);
    }

    public void setToolbar() {
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        getSupportActionBar().setTitle(Config.APPBARTITLE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_pink_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_done:

                    if (getIntent().getBooleanExtra("GOBACK", false)) {
                        finish();
                    } else if (done.equals("1")) {
                        /*startActivity(new Intent(context, OptionsActivity.class));
                        finishAffinity();*/
                        finish();
                    } else if (done.equals("2")) {
                        startActivity(new Intent(context, FacebookConnectActivity.class).putExtra("Skip","0"));
                        finishAffinity();
                    } else  {
                        startActivity(new Intent(context, MainDashboardActivity.class));
                        finishAffinity();
                    }


//                startActivity(new Intent(context, FacebookConnectActivity.class));

                break;
        }
    }

    class AddContactsTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            lay_contacts.setVisibility(View.GONE);
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {

            postDataParams = new HashMap<String, String>();

            try {

                Contact contactVO;

                if (Config.CONTACTS_JSON != null) {
//                    for (int j = 0; j < Config.CONTACTS_JSON.length(); j++) {
//                        contactVO = new Contact();
//                        contactVO.type = "PHONE";
//                        contactVO.status = "INVITE";
//                        contactVO.name = Config.CONTACTS_JSON.getJSONObject(j + "").getString("name");
//                        contactVO.phone = Config.CONTACTS_JSON.getJSONObject(j + "").getString("phone");
//                        contactList.add(contactVO);
//                    }
                    Iterator<String> temp = Config.CONTACTS_JSON.keys();
                    while (temp.hasNext()) {
                        String key = temp.next();
//                            Object value = Config.CONTACTS_JSON.get(key);
                        contactVO = new Contact();
                        contactVO.type = "PHONE";
                        contactVO.status = "INVITE";
                        contactVO.name = Config.CONTACTS_JSON.getJSONObject(key + "").getString("name");
                        contactVO.phone = Config.CONTACTS_JSON.getJSONObject(key + "").getString("phone");
                        contactList.add(contactVO);
                    }


                }
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
            lay_contacts.setVisibility(View.VISIBLE);

            if (!fetchedContacts.isEmpty()) {
                try {
                    JSONObject object = new JSONObject(fetchedContacts);
                    if (object.getBoolean("status")) {
                        JSONArray arr = object.getJSONArray("data");
                        Contact contact;

                        if (contactList.size() > 0) {
                            for (int l = 0; l < arr.length(); l++) {
                                JSONObject obj = arr.getJSONObject(l);

                                for (int i = 0; i < contactList.size(); i++) {
                                    contact = contactList.get(i);

                                    if (obj.getString("mobile").equals(contact.phone)) {

                                        if (obj.getString("follow").equals("0")){
                                            contact.id = obj.getString("id");
                                            contact.follow = obj.getString("follow");
                                            contact.status = "FOLLOW";
                                        }else {
                                            contact.id = obj.getString("id");
                                            contact.follow = obj.getString("follow");
                                            contact.status = "FOLLOWING";
                                        }

//                                        contact.id = obj.getString("id");
//                                        contact.name = obj.getString("name");
//                                        contact.follow = obj.getString("follow");
//                                        contact.status = "FOLLOW";
                                        count++;
                                    }
                                }
                            }
                            Collections.sort(contactList);
                            contactsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(PhoneContactsActivity.this, "Contact list is empty ", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(PhoneContactsActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(PhoneContactsActivity.this, getString(R.string.error_no_contact), Toast.LENGTH_LONG).show();
            }
            String txt = ((TextView) findViewById(R.id.txt_follow)).getText().toString().trim();
            ((TextView) findViewById(R.id.txt_follow)).setText(count + " " + txt);
            if (count == 0)
                ((TextView) findViewById(R.id.txt_follow)).setVisibility(View.GONE);
            findViewById(R.id.btn_follow_all).setVisibility(View.GONE);
            contactsAdapter.notifyDataSetChanged();
        }
    }
}
