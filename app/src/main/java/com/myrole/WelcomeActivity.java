package com.myrole;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;

public class WelcomeActivity extends BaseActivity {

    private Activity activity;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context =this;
        activity = this;

        findViewById(R.id.btn_get_started).setOnClickListener(this);
        findViewById(R.id.txt_sign_in).setOnClickListener(this);

        String token = FirebaseInstanceId.getInstance().getToken();
        AppPreferences.getAppPreferences(activity).putStringValue(AppPreferences.DEVICE_TOKEN_ID,token);
        System.out.println("Token: "+token);
//        Toast.makeText(context, "Token : " +token, Toast.LENGTH_SHORT).show();
//        Log.d(TAG,token);

        applyFont();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_get_started:
                startActivity(new Intent(getApplicationContext(), MobileNumberActivity.class));
                //startActivity(new Intent(getApplicationContext(), HomeActivity.class).putExtra("PHONE", "9548344675"));
                finish();
                break;
            case R.id.txt_sign_in:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
    }

    private void applyFont() {

        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_sign_in), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_hey), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_discover), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_sign_up), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_already), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_get_started), Config.NEXA, Config.REGULAR);

    }
}
