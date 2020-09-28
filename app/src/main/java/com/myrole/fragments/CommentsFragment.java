package com.myrole.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.myrole.R;
import com.myrole.adapter.CommentsAdapter2;
import com.myrole.dashboard.interfaces.InvokedFeedsFunctions;
import com.myrole.data.CommentsDTO;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.Utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentsFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    final static String TAG = "CommentsFragment";
    String post_id;
    ImageView back;
    RecyclerView lst_chat;
    EditText et_message;
    ImageButton btn_send;
    String userId;
    CommentsDTO commentsDTO;
    CommentsAdapter2 adapter;
    String username = "", user_img_url = "";
    int num_comm;
    private ProgressBarHandler progressBarHandler;
    private Activity activity;
    private Context context;
    private ConstraintLayout lay_add_comment;
    private int postPosition;
    private View view;
   // BottomBar bottom_bar;
    //FrameLayout main_frame;
    InvokedFeedsFunctions mFunctionsFeeds ;

    public int getNum_comm() {
        return num_comm;
    }

    public void setNum_comm(int num_comm) {
        this.num_comm = num_comm;
    }

    public CommentsFragment(InvokedFeedsFunctions m) {
        mFunctionsFeeds = m ;
    }

    public static CommentsFragment newInstance(String post_id, String position, InvokedFeedsFunctions mFunction) {
        CommentsFragment fragment = new CommentsFragment(mFunction);
        Bundle bndl = new Bundle();
        bndl.putString("POSTID", post_id);
        bndl.putString("position", position);
        fragment.setArguments(bndl);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();
        post_id = getArguments().getString("POSTID");
        int pPosition = Integer.valueOf(getArguments().getString("position"));
        postPosition = pPosition - 1;
       /* try {
            bottom_bar = getActivity().findViewById(R.id.bottom_bar);
            main_frame = getActivity().findViewById(R.id.main_frame);
        } catch (Exception e) {
        }
*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* try {
            main_frame.setPadding(0, 0, 0, 0);
          //  bottom_bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(bottom_bar.getHeight());
        } catch (Exception e) {
        }*/
        // bottom_bar.setVisibility(View.GONE);
        view = inflater.inflate(R.layout.add_comments, container, false);


        //  Toast.makeText(AddComments.this,"comment activity", Toast.LENGTH_LONG).show();
        lay_add_comment = view.findViewById(R.id.lay_add_comment);
        progressBarHandler = new ProgressBarHandler(activity);
        userId = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID);
        username = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.U_NAME);
        user_img_url = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_IMAGE_URL);

        back = (ImageView) view.findViewById(R.id.iv_back);
        btn_send = (ImageButton) view.findViewById(R.id.btn_send);
        et_message = (EditText) view.findViewById(R.id.et_message);
        lst_chat = (RecyclerView) view.findViewById(R.id.lst_chat);

        lst_chat.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        lst_chat.setLayoutManager(mLayoutManager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFunctionsFeeds.dismissCommentSheet();
                Utils.hideKeybord(context, v);
            }
        });

        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SpannedString spannedString = new SpannedString(s);
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_message.getText().toString().length() > 0) {
                    callServiceForAddComments(StringEscapeUtils.escapeJava(et_message.getText().toString()));
                } else {
                    Toast.makeText(activity, "Please add comments first", Toast.LENGTH_LONG).show();
                }
            }
        });

        webServiceCall();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                Utils.hideKeybord(context, v);
                break;
        }

    }

    @Override

    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(String command);
//    }

    private void callServiceForAddComments(String s) {
//        showProgessDialog();

        progressBarHandler.show();
        AndroidNetworking.post(Config.ADD_POST)
                .setTag(this)
                .addBodyParameter("user_id", userId)
                .addBodyParameter("post_id", post_id)
                .addBodyParameter("comments", s)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        // dismissProgressDialog();
                        progressBarHandler.hide();
                        System.out.println("ADD_COMMENTS:-" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
//                                webServiceCall();
                                CommentsDTO cm = new CommentsDTO();
                                cm.setComments(et_message.getText().toString());
                                cm.setName(username);
                                cm.setUser_id(AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID));
                                cm.setComment_id(jsonObject.getJSONObject("data").getString("comment_id"));
                                cm.setCreated_at("Just Now");
                                cm.setImage(user_img_url);
                                commentsDTO.getData().add(cm);
                                adapter.notifyDataSetChanged();
                                setNum_comm((num_comm + 1));
                                et_message.setText("");
                                // that can scroll to laast postion
                                int last_pos = adapter.getItemCount() - 1;
                                lst_chat.scrollToPosition(last_pos);
                                //end
                            } else {
                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
//                        dismissProgressDialog();
                        progressBarHandler.hide();

                        if (error.getErrorCode() != 0) {
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    public void webServiceCall() {
        lay_add_comment.setVisibility(View.GONE);
        progressBarHandler.show();
        AndroidNetworking.post(Config.GET_comm_POST)
                .setTag(this)
                .addBodyParameter("user_id", userId)
                .addBodyParameter("post_id", post_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        lay_add_comment.setVisibility(View.VISIBLE);
                        progressBarHandler.hide();
                        System.out.println("POST_COMMENTS:-" + response);
                        commentsDTO = new Gson().fromJson(response, CommentsDTO.class);
                        setNum_comm(commentsDTO.getData().size());
                        adapter = new CommentsAdapter2(context, activity, commentsDTO.getData(), mFunctionsFeeds);
                        lst_chat.setAdapter(adapter);
                    }

                    @Override
                    public void onError(ANError error) {
                        progressBarHandler.hide();
                        if (error.getErrorCode() != 0) {
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }
}