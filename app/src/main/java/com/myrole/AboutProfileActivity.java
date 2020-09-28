package com.myrole;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.myrole.adapter.CountrySpinnerAdapter;
import com.myrole.camera_ck.activity.video.VideoActivity;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;
import com.myrole.utils.VideoControllerView;
import com.myrole.vo.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AboutProfileActivity extends BaseActivity {

    ArrayList<Country> nationalityList;
    ArrayList<String> appearanceList;
    ArrayList<String> colorList;
    TextView spinnerAppearance, spinnerColor;
    CountrySpinnerAdapter nationalitySpinnerAdapter;
    TextView nationalityView;
    private int selectedMediaType;
    private String videoIcon = "";
    private String videoUrl = "",video_thumb="";
    String profileUrl = "";
    private Context context;
    private Activity activity;
    private ProgressBarHandler progressBarHandler;
    private RelativeLayout lay_edit_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_profile);
        context = this;
        activity = this;
        progressBarHandler = new ProgressBarHandler(activity);
        lay_edit_profile = (RelativeLayout) findViewById(R.id.lay_edit_profile);

        String userId = getIntent().getStringExtra("user_id");


        findViewById(R.id.btn_back).setOnClickListener(this);
//
        findViewById(R.id.vid_icon_play).setOnClickListener(this);
        findViewById(R.id.img_add_pic).setOnClickListener(this);

        nationalityView = ((TextView) findViewById(R.id.auto_nationality));

        new CountryListTask().execute();
        new GetUserDetailTask().execute(userId);

        applyFont();

        spinnerAppearance = (TextView) findViewById(R.id.txt_spinner_appearance);
        spinnerColor = (TextView) findViewById(R.id.txt_spinner_color);
        // Spinner Drop down elements
        nationalityList = new ArrayList<Country>();
        Country country = new Country();
        country.name = "India";
        country.code = "IN";
        nationalityList.add(country);

        appearanceList = new ArrayList<String>();
        appearanceList.add("One");
        appearanceList.add("Two");
        appearanceList.add("Three");
        appearanceList.add("Four");

        colorList = new ArrayList<String>();
        colorList.add("Dark");
        colorList.add("Light");
        colorList.add("Brown");

        nationalitySpinnerAdapter = new CountrySpinnerAdapter(this, nationalityList);
    }

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Country country : nationalityList) {
            String str = country.name;
            int len = input.length() > str.length() ? str.length() : input.length();
            if (len == 0)
                arrayList.add(str);
            else if (input.equalsIgnoreCase(str.substring(0, len)))
                arrayList.add(str);
        }
        return arrayList;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.lay_img_add_pic:
                selectedMediaType = Config.MEDIA_TYPE_IMAGE;
                Utils.showMediaSelectionDialog(this, selectedMediaType, "0");
                break;
            case R.id.lay_vid_add:
                selectedMediaType = Config.MEDIA_TYPE_VIDEO;
                Utils.showMediaSelectionDialog(this, selectedMediaType, "0");
                break;
            case R.id.vid_icon_play:
                if (videoUrl != null && !videoUrl.isEmpty() && !videoUrl.equals("null")) {
                    try {
                        Intent i = new Intent(activity, VideoActivity.class); // Your list's Intent
                        i.putExtra("url", videoUrl);
                        i.putExtra("video_thumb", video_thumb);
                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Utils.showAlertDialog(context, "Alert", "No video added !", true);
                }
                break;

            case R.id.img_add_pic:
                if (profileUrl != null && !profileUrl.isEmpty() && !profileUrl.equals("null")) {
                    try {
                        SurfaceView videoSurface;
                        MediaPlayer player;
                        VideoControllerView controller;

                        final Dialog m_dialog;
                        m_dialog = new Dialog(AboutProfileActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                        m_dialog.setCancelable(false);
                        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        m_dialog.setContentView(R.layout.layout_video_view);
                        m_dialog.findViewById(R.id.img_profile).setVisibility(View.VISIBLE);
                        ((TextView) m_dialog.findViewById(R.id.text_title)).setText("Profile Image");
                        RelativeLayout rl = (RelativeLayout) m_dialog.findViewById(R.id.videoSurfaceContainer);
                        Glide.with(context).asBitmap().load(profileUrl)
                                .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));

                        m_dialog.findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                videoPlayer.pause();
                                m_dialog.dismiss();
                            }
                        });

                        m_dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Utils.showAlertDialog(context, "Alert", "No image added !", true);
                }
                break;
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_title), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_few), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_dob_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_dob), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_gender_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_gender), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_nationality), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_hobby_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_hobby), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_height_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_height), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_weight_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_weight), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_appearance_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_color_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_follower_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_following_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.auto_nationality), Config.NEXA, Config.REGULAR);

        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_address_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_address), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_district_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_district), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_state_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_state), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_pincode_hint), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (EditText) findViewById(R.id.txt_pincode), Config.NEXA, Config.REGULAR);
    }

    class CountryListTask extends AsyncTask<String, Void, String> {
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

            return HTTPUrlConnection.getInstance().load(getApplicationContext(), Config.GET_COUNTRY_LIST, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONArray arr = object.getJSONArray("data");
                    nationalityList.clear();
                    for (int i = 0; i < arr.length(); i++) {
                        Country country = new Country();
                        country.name = arr.getJSONObject(i).getString("name");
                        country.code = arr.getJSONObject(i).getString("iso");
                        nationalityList.add(i, country);
                    }
                    nationalitySpinnerAdapter.notifyDataSetChanged();
                    Log.v(TAG, result);
                } else {
                    Toast.makeText(AboutProfileActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
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
            lay_edit_profile.setVisibility(View.GONE);
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
            progressBarHandler.hide();
            lay_edit_profile.setVisibility(View.VISIBLE);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject userDetail = object.getJSONArray("data").getJSONObject(0);

                    videoUrl = userDetail.getString("video");
                    profileUrl = userDetail.getString("image");
                    if (!userDetail.isNull("video_thumb")) {
                        video_thumb = userDetail.getString("video_thumb");
                    }

                    if (!videoUrl.equals("null") && videoUrl != null && !videoUrl.isEmpty()) {

                        videoIcon = videoUrl.replace(".mp4", ".jpg");

                        ((ImageView) findViewById(R.id.vid_icon_play)).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.vid_update_icon)).setVisibility(View.GONE);

                        Glide.with(context).asBitmap().load(video_thumb)
                                .centerCrop().into(((ImageView) findViewById(R.id.vid_add_video)));

                    } else {

                        try {
                            Glide.with(context).asBitmap().load(videoIcon)
                                    .placeholder(R.drawable.add_video_icon).centerCrop().into(((ImageView) findViewById(R.id.vid_add_video)));
                        } catch (Exception e){}

                    }

                    if (!profileUrl.equals("null") && profileUrl != null && !profileUrl.isEmpty()) {
                        ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.GONE);

                        Glide.with(context).asBitmap().load(userDetail.getString("image"))
                                .centerCrop().into(((ImageView) findViewById(R.id.img_add_pic)));
                    } else {
                        Glide.with(context).asBitmap().load(userDetail.getString("image"))
                                .placeholder(R.drawable.default_avatar).centerCrop().into(((ImageView) findViewById(R.id.img_add_pic)));
                    }

                    if ((!userDetail.getString("dob").equals("null") || userDetail.getString("dob") != null || !userDetail.getString("dob").equals(""))) {
                        ((TextView) findViewById(R.id.txt_dob)).setText(userDetail.getString("dob"));
                        String date = Utils.formatdate(userDetail.getString("dob"));
                        ((TextView) findViewById(R.id.txt_dob)).setText(date);
                    }

                    if ((!userDetail.getString("nationality").equals("null") || userDetail.getString("nationality") != null || !userDetail.getString("nationality").equals(""))) {

                        for (Country country : nationalityList) {
                            String str = country.code;
                            if (userDetail.getString("nationality").equals(str)) {
                                String countryName = country.name;
                                ((TextView) findViewById(R.id.auto_nationality)).setText(countryName);
                                break;
                            }
                        }
                    }

                    if ((!userDetail.getString("address").equals("null") || userDetail.getString("address") != null || !userDetail.getString("address").equals(""))) {

                        if (userDetail.getString("address").equals("null")){
                            ((TextView) findViewById(R.id.txt_address)).setText("");
                        }else{
                            ((TextView) findViewById(R.id.txt_address)).setText(userDetail.getString("address"));
                        }

                    }
                    if ((!userDetail.getString("district").equals("null") || userDetail.getString("district") != null || !userDetail.getString("district").equals(""))) {
                        if (userDetail.getString("district").equals("null")){
                            ((TextView) findViewById(R.id.txt_district)).setText("");
                        }else{
                            ((TextView) findViewById(R.id.txt_district)).setText(userDetail.getString("district"));
                        }

                    }
                    if ((!userDetail.getString("state").equals("null") || userDetail.getString("state") != null || !userDetail.getString("state").equals(""))) {
                        if (userDetail.getString("state").equals("null")){
                            ((TextView) findViewById(R.id.txt_state)).setText("");
                        }else{
                            ((TextView) findViewById(R.id.txt_state)).setText(userDetail.getString("state"));
                        }

                    }

                    if ((!userDetail.getString("pincode").equals("null") || userDetail.getString("pincode") != null || !userDetail.getString("pincode").equals(""))) {
                        if (userDetail.getString("pincode").equals("null")){
                            ((TextView) findViewById(R.id.txt_pincode)).setText("");
                        }else{
                            ((TextView) findViewById(R.id.txt_pincode)).setText(userDetail.getString("pincode"));
                        }

                    }

                    if ((!userDetail.getString("gender").equals("null") || userDetail.getString("gender") != null || !userDetail.getString("gender").equals(""))) {

                        ((TextView) findViewById(R.id.txt_gender)).setText(userDetail.getString("gender"));
                    }

                    if ((!userDetail.getString("hobby").equals("null") || userDetail.getString("hobby") != null || !userDetail.getString("hobby").equals(""))) {

                        ((TextView) findViewById(R.id.txt_hobby)).setText(userDetail.getString("hobby"));
                    }

                    if ((!userDetail.getString("height").equals("null") || userDetail.getString("height") != null || !userDetail.getString("height").equals(""))) {

                        ((TextView) findViewById(R.id.txt_height)).setText(userDetail.getString("height"));
                    }

                    if ((!userDetail.getString("weight").equals("null") || userDetail.getString("weight") != null || !userDetail.getString("weight").equals(""))) {

                        ((TextView) findViewById(R.id.txt_weight)).setText(userDetail.getString("weight"));
                    }

                    if ((!userDetail.getString("color").equals("null") || userDetail.getString("color") != null || !userDetail.getString("color").equals(""))) {

                        spinnerColor.setText(userDetail.getString("color"));
                    }

                    if ((!userDetail.getString("apperance").equals("null") || userDetail.getString("apperance") != null || !userDetail.getString("apperance").equals(""))) {

                        spinnerAppearance.setText(userDetail.getString("apperance"));
                    }

                    if ((!userDetail.getString("followers").equals("null") || userDetail.getString("followers") != null || !userDetail.getString("followers").equals(""))) {

                        ((TextView) findViewById(R.id.txt_follower_count)).setText(userDetail.getString("followers"));
                    }

                    if ((!userDetail.getString("following").equals("null") || userDetail.getString("following") != null || !userDetail.getString("following").equals(""))) {

                        ((TextView) findViewById(R.id.txt_following_count)).setText(userDetail.getString("following"));
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
