package com.myrole;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedbackActivity extends BaseActivity {


    private Context context;
    private EditText edt_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        context = this;
        applyFont();

        findViewById(R.id.btn_back).setOnClickListener(this);
        edt_comment = ((EditText)findViewById(R.id.edttxt_comment));
//        final String comment = edt_comment.getText().toString();

        findViewById(R.id.btn_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_comment.getText().toString().isEmpty()){

                    Toast.makeText(context, "Please enter feedback.", Toast.LENGTH_SHORT).show();
                }else {
                    callServiceForFeedback(edt_comment.getText().toString());
                }

            }
        });

    }

    private void callServiceForFeedback(String s) {

        AppPreferences preferences = AppPreferences.getAppPreferences(context);
        AndroidNetworking.post(Config.FEEDBACK_USER)
                .setTag(this)
                .addBodyParameter("user_id",preferences.getStringValue(AppPreferences.USER_ID))
                .addBodyParameter("comment",s)

                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d("TAG", " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d("TAG", " bytesSent : " + bytesSent);
                        Log.d("TAG", " bytesReceived : " + bytesReceived);
                        Log.d("TAG", " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        //dismissProgressDialog();
                        try {
                            JSONObject object = new JSONObject(response);

                            Log.d("TAG", "delete_onResponse object : " + response);
                            Log.d("TAG", "onResponse isMainThread : " + (Looper.myLooper() == Looper.getMainLooper()));

                            if (object.getBoolean("status")) {
                                Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                edt_comment.setText("");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError error) {

                        //dismissProgressDialog();
                        if (error.getErrorCode() != 0) {
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail
                            Log.d("TAG", "onError errorCode : " + error.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + error.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    private void applyFont() {
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.txt_title), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getApplicationContext(), (TextView) findViewById(R.id.edttxt_comment), Config.NEXA, Config.REGULAR);
        Utils.setTypeface(getApplicationContext(), (Button) findViewById(R.id.btn_feedback), Config.NEXA, Config.REGULAR);

    }

}
