package com.myrole;//package com.myrole;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.SpannedString;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.AnalyticsListener;
//import com.androidnetworking.interfaces.StringRequestListener;
//import com.google.gson.Gson;
//import com.myrole.adapter.CommentsAdapter;
//import com.myrole.data.CommentsDTO;
//import com.myrole.fragments.PostDetailFragment;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.ProgressBarHandler;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by Rakesh on 10/22/2016.
// */
//
//public class AddComments extends BaseActivity {
//    String post_id;
//    ImageView back;
//    ListView lst_chat;
//    EditText et_message;
//    ImageButton btn_send;
//    String userId;
//    CommentsDTO commentsDTO;
//    CommentsAdapter adapter;
//    String username="",user_img_url="";
//    PostDetailFragment postDetailFragment;
//    int num_comm;
//    private ProgressBarHandler progressBarHandler;
//    private Activity activity;
//    private  Context context;
//    private RelativeLayout lay_add_comment;
//    private int postPosition;
//
//    public int getNum_comm() {
//        return num_comm;
//    }
//
//    public void setNum_comm(int num_comm) {
//        this.num_comm = num_comm;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.add_comments);
//        activity = this;
//        context = this;
//      //  Toast.makeText(AddComments.this,"comment activity", Toast.LENGTH_LONG).show();
//        lay_add_comment = (RelativeLayout) findViewById(R.id.lay_add_comment);
//        progressBarHandler = new ProgressBarHandler(activity);
//        userId = AppPreferences.getAppPreferences(this).getStringValue(AppPreferences.USER_ID);
//        username = AppPreferences.getAppPreferences(this).getStringValue(AppPreferences.U_NAME);
//        user_img_url = AppPreferences.getAppPreferences(this).getStringValue(AppPreferences.USER_IMAGE_URL);
//
//        post_id = getIntent().getStringExtra("post_id");
//        postPosition = getIntent().getIntExtra("post_position", -1);
//        back = (ImageView) findViewById(R.id.iv_back);
//        btn_send = (ImageButton) findViewById(R.id.btn_send);
//        et_message = (EditText) findViewById(R.id.et_message);
//        lst_chat = (ListView) findViewById(R.id.lst_chat);
//        postDetailFragment = new PostDetailFragment();
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//        et_message.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                SpannedString spannedString = new SpannedString(s);
//            }
//        });
//
//
//        btn_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (et_message.getText().toString().length() > 0) {
//                    callServiceForAddComments(et_message.getText().toString());
//                } else {
//                    Toast.makeText(AddComments.this, "Please add comments first", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
////      lst_chat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
////          @Override
////          public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
////              adapter.onLongClickEvent(view,position);
////              return true;
////          }
////      });
//        webServiceCall();
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("num_comm", String.valueOf(getNum_comm()));
//        returnIntent.putExtra("position", getIntent().getStringExtra("position"));
//        returnIntent.putExtra("fromAddComment", true);
//        setResult(RESULT_OK, returnIntent);
//        finish();
//    }
//
//    private void callServiceForAddComments(String s) {
////        showProgessDialog();
//
//        progressBarHandler.show();
//        AndroidNetworking.post(Config.ADD_POST)
//                .setTag(this)
//                .addBodyParameter("user_id", userId)
//                .addBodyParameter("post_id", post_id)
//                .addBodyParameter("comments", s)
//                .setPriority(Priority.IMMEDIATE)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
//                        Log.d(TAG, " bytesSent : " + bytesSent);
//                        Log.d(TAG, " bytesReceived : " + bytesReceived);
//                        Log.d(TAG, " isFromCache : " + isFromCache);
//                    }
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        dismissProgressDialog();
//                        progressBarHandler.hide();
//                        System.out.println("ADD_COMMENTS:-" + response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getBoolean("status")) {
////                                webServiceCall();
//                                CommentsDTO cm = new CommentsDTO();
//                                cm.setComments(et_message.getText().toString());
//                                cm.setName(username);
//                                cm.setUser_id(AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID));
//                                cm.setComment_id(jsonObject.getJSONObject("data").getString("comment_id"));
//                                cm.setCreated_at("Just Now");
//                                cm.setImage(user_img_url);
//                                commentsDTO.getData().add(cm);
//                                adapter.notifyDataSetChanged();
//                                setNum_comm((num_comm + 1));
//                                et_message.setText("");
//
//                            } else {
//                                Toast.makeText(AddComments.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
////                        dismissProgressDialog();
//                       progressBarHandler.hide();
//
//                        if (error.getErrorCode() != 0) {
//                            // received ANError from server
//                            // error.getErrorCode() - the ANError code from server
//                            // error.getErrorBody() - the ANError body from server
//                            // error.getErrorDetail() - just a ANError detail
//                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
//                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//    }
//
//    public void webServiceCall() {
////        showProgessDialog();
//        lay_add_comment.setVisibility(View.GONE);
//        progressBarHandler.show();
//        AndroidNetworking.post(Config.GET_comm_POST)
//                .setTag(this)
//                .addBodyParameter("user_id", userId)
//                .addBodyParameter("post_id", post_id)
//                .setPriority(Priority.IMMEDIATE)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
//                        Log.d(TAG, " bytesSent : " + bytesSent);
//                        Log.d(TAG, " bytesReceived : " + bytesReceived);
//                        Log.d(TAG, " isFromCache : " + isFromCache);
//                    }
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
////                        dismissProgressDialog();
//                        lay_add_comment.setVisibility(View.VISIBLE);
//                        progressBarHandler.hide();
//                        System.out.println("POST_COMMENTS:-" + response);
//                        commentsDTO = new Gson().fromJson(response, CommentsDTO.class);
//                        setNum_comm(commentsDTO.getData().size());
////                        postDetailFragment.setNum_comment(String.valueOf(getNum_comm()));
//                        adapter = new CommentsAdapter(context,AddComments.this, commentsDTO.getData());
//                        lst_chat.setAdapter(adapter);
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        //dismissProgressDialog();
//                        if (error.getErrorCode() != 0) {
//                            // received ANError from server
//                            // error.getErrorCode() - the ANError code from server
//                            // error.getErrorBody() - the ANError body from server
//                            // error.getErrorDetail() - just a ANError detail
//                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
//                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//    }
//}