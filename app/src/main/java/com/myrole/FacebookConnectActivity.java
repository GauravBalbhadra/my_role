package com.myrole;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.FBLoginClass;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;

import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FacebookConnectActivity extends BaseActivity implements FBLoginClass.OnFBResultListener {

    private FBLoginClass fbLoginClass;
    private Context context;
    private Activity activity;
    private String done, incomplete, skip;

    private ProgressBarHandler progressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;

        progressBarHandler = new ProgressBarHandler(activity);

        setContentView(R.layout.activity_facebook_connect);
        skip = getIntent().getStringExtra("Skip");
        done = getIntent().getStringExtra("Activity");
        incomplete = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_PROFILE_INCOMPLETE);
        if (skip.equals("userNameActivity")) {

            ((TextView) findViewById(R.id.txt_skip)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.btn_back)).setVisibility(View.GONE);

        } else if (skip.equals("0")) {
            ((ImageView) findViewById(R.id.btn_back)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.txt_skip)).setVisibility(View.VISIBLE);
        } else {
            ((ImageView) findViewById(R.id.btn_back)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.txt_skip)).setVisibility(View.GONE);
        }
        findViewById(R.id.btn_connect).setOnClickListener(this);
        findViewById(R.id.txt_skip).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        getKeyHash();
        applyFont();
    }

    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.myrole",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                Log.d("KeyHash:==", Base64.encodeToString(md.digest(), Base64.DEFAULT));

                Log.d("KeyHash:", key);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    @Override
    public void onFBResult(JSONArray objects, Profile profile, String id) {
        Intent intent = new Intent(context, FBContactsActivity.class);
        intent.putExtra("Activity", done);
        intent.putExtra("FBID", id);
        intent.putExtra("FBFRIENDS", objects.toString());
        intent.putExtra("GOBACK", getIntent().getBooleanExtra("GOBACK", false));
        progressBarHandler.hide();
        //   Toast.makeText(activity, "onFBResult", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBarHandler.show();
        //  Toast.makeText(activity, "onActivityResult", Toast.LENGTH_SHORT).show();
        Log.v(TAG, "code=" + requestCode);
        Log.v(TAG, "data=" + data);
        fbLoginClass.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_skip:
                if (getIntent().getBooleanExtra("GOBACK", false)) {
                    finish();
                } else {
                    startActivity(new Intent(this, BuildProfileActivity.class));
                    finishAffinity();
                }
                break;
            case R.id.btn_connect:
                fbLoginClass = new FBLoginClass();
                fbLoginClass.setFBResultListener(this);
                fbLoginClass.sdkInitialize(this);
                fbLoginClass.login(this);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_skip), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_find), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_connect), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_connect), Config.NEXA, Config.REGULAR);

    }

}
