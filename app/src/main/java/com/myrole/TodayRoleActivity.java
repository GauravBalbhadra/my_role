package com.myrole;//package com.myrole;
//
//import android.*;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.text.Editable;
//import android.text.SpannedString;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.view.Window;
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
//import com.myrole.fragments.TodayRoleFragment;
//import com.myrole.holders.TodayRoleDetailsDTO;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.utils.ProgressBarHandler;
//import com.myrole.utils.Utils;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Created by Rakesh on 10/22/2016.
// */
//
//public class TodayRoleActivity extends BaseActivity {
//    public static TodayRoleDetailsDTO todayRoleDetailsDTO1;
//    private TodayRoleFragment.OnFragmentInteractionListener mListener;
//    private ArrayList<String> generList;
//    private SwipeRefreshLayout swipeView;
//    private boolean isRefreshing;
//    private String categoryID;
//    private String req_image = "1";
//    private String req_video = "0";
//    private String imagePath = "", videoPath = "";
//    private Bitmap bitmap = null;
//    TodayRoleDetailsDTO todayRoleDetailsDTO;
//
//    public Activity activity;
//    public Context context;
//
////    public int getNum_comm() {
////        return num_comm;
////    }
////
////    public void setNum_comm(int num_comm) {
////        this.num_comm = num_comm;
////    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.acitivity_today_role);
//        activity = this;
//        context = this;
//        categoryID = getIntent().getStringExtra("categoryID");
//        new GetTodayRole().execute(categoryID);
//
//        findViewById(R.id.btn_back).setOnClickListener(this);
//        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
//        swipeView.setEnabled(false);//
//        //swipeView.setOnRefreshListener(this);
//        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//        });
//        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
//                Color.RED, Color.CYAN);
//        swipeView.setDistanceToTriggerSync(20);// in dips
//        swipeView.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
//        //applyFont();
//
//       findViewById(R.id.main_layout).getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//
//            @Override
//            public void onScrollChanged() {
//                int scrollY = findViewById(R.id.main_layout).getScrollY();
//                if(scrollY == 0) swipeView.setEnabled(true);
//                else swipeView.setEnabled(false);
//
//            }
//        });
//        findViewById(R.id.upload_btn).setOnClickListener(this);
//        findViewById(R.id.img_preview_image).setOnClickListener(this);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        Intent returnIntent = new Intent();
////        returnIntent.putExtra("num_comm", String.valueOf(getNum_comm()));
////        returnIntent.putExtra("position", getIntent().getStringExtra("position"));
////        returnIntent.putExtra("fromAddComment", true);
////        setResult(RESULT_OK, returnIntent);
////        finish();
//    }
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        switch (v.getId()) {
//            case R.id.img_preview_image:
//                final Dialog m_dialog;
//                m_dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
//                m_dialog.setCancelable(false);
//                m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                m_dialog.setContentView(R.layout.layout_image_view);
//                // ((TextView) m_dialog.findViewById(R.id.text_title)).setText("Profile Video");
//
////                        RelativeLayout rl = (RelativeLayout) m_dialog.findViewById(R.id.videoSurfaceContainer);
////                        final VideoPlayer videoPlayer = new VideoPlayer(getContext());
////                        videoPlayer.makeView();
////                        rl.addView(videoPlayer);
////                        videoPlayer.playUrl(videoUrl);
//
//                m_dialog.findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // videoPlayer.pause();
//                        m_dialog.dismiss();
//                    }
//                });
////                        m_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
////                            @Override
////                            public void onDismiss(DialogInterface dialog) {
////                                updateProfileImage(profileUrl);
////                            }
////                        });
//
//                m_dialog.show();
//                break;
//            case R.id.upload_btn:
////                ((HomeActivity) getActivity()).isDirectPost = true;
////                ((HomeActivity) getActivity()).selected_cat_id = categoryID;
////                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////                    // Request permission to save videos in external storage
////                    ActivityCompat.requestPermissions((Activity)getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6969);
////                }
////                else {
////                    File saveDir = null;
////                    File saveDir1 = null;
////
////                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                        // Only use external storage directory if permission is granted, otherwise cache directory is used by default
////                        saveDir = new File(Environment.getExternalStorageDirectory(), "MyRoleApp/Videos");
////                        if (!saveDir.exists())
////                            saveDir.mkdirs();
////                        saveDir1 = new File(Environment.getExternalStorageDirectory(), "MyRoleApp/Images");
////                        if (!saveDir1.exists())
////                            saveDir1.mkdirs();
////                    }
////                if (req_image.equals("1")) {
////                    if (req_video.equals("1")) {
////                        Utils.showMediaSelectionDialog((BaseActivity) getActivity(), Config.MEDIA_TYPE_ALL,String.valueOf( todayRoleDetailsDTO.getData().get(0).getId()));
////                    } else {
////                        Utils.showMediaSelectionDialog((BaseActivity) getActivity(), Config.MEDIA_TYPE_IMAGE,String.valueOf( todayRoleDetailsDTO.getData().get(0).getId()));
////                    }
////                } else if (req_video.equals("1")) {
////
////                        Utils.showMediaSelectionDialog((BaseActivity) getActivity(), Config.MEDIA_TYPE_VIDEO,String.valueOf( todayRoleDetailsDTO.getData().get(0).getId()));
////
////
////                }}
//
//              //  ((HomeActivity) activity).isDirectPost = true;
//              //  ((HomeActivity) activity).selected_cat_id = categoryID;
//                HomeActivity.isDirectPost=true;
//                HomeActivity.selected_cat_id=categoryID;
//
//                todayRoleDetailsDTO1 = todayRoleDetailsDTO;
//                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    // Request permission to save videos in external storage
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6969);
//                } else {
//                    File saveDir = null;
//                    File saveDir1 = null;
//
//                    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        // Only use external storage directory if permission is granted, otherwise cache directory is used by default
//                        saveDir = new File(Environment.getExternalStorageDirectory(), "MyRoleApp/Videos");
//                        if (!saveDir.exists())
//                            saveDir.mkdirs();
//                        saveDir1 = new File(Environment.getExternalStorageDirectory(), "MyRoleApp/Images");
//                        if (!saveDir1.exists())
//                            saveDir1.mkdirs();
//                    }
//                    if (req_image.equals("1")) {
//                        if (req_video.equals("1")) {
//
//                            Utils.showMediaSelectionDialog((BaseActivity) activity, Config.MEDIA_TYPE_ALL, String.valueOf( todayRoleDetailsDTO.getData().get(0).getId()));
//                        } else {
//
//                            Utils.showMediaSelectionDialog((BaseActivity) activity, Config.MEDIA_TYPE_IMAGE, String.valueOf( todayRoleDetailsDTO.getData().get(0).getId()));
//                        }
//                    } else if (req_video.equals("1")) {
//
//                        Utils.showMediaSelectionDialog((BaseActivity) activity, Config.MEDIA_TYPE_VIDEO, String.valueOf( todayRoleDetailsDTO.getData().get(0).getId()));
//
//
//                    }
//                }
//                break;
//        }
//    }
//    class GetTodayRole extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
//        String category_id;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //showProgessDialog();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            category_id = params[0];
//            postDataParams = new HashMap<String, String>();
//            postDataParams.put("category_id",params[0]);
//            return HTTPUrlConnection.getInstance().load(context,Config.GET_TODAY_ROLE, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//          //  if (getView() != null) {
//                if (isRefreshing)
//                    swipeView.setRefreshing(false);
//                //dismissProgressDialog();
//                System.out.println("onPostExecute_result:-"+result);
//                todayRoleDetailsDTO = new Gson().fromJson(result,TodayRoleDetailsDTO.class);
//                if(todayRoleDetailsDTO!=null&&todayRoleDetailsDTO.getData()!=null&&todayRoleDetailsDTO.getData().size()>0){
//                    String array[]= todayRoleDetailsDTO.getData().get(0).getGener().split(",");
//                    if(array!=null&&array.length>0){
//                        generList = new ArrayList<>();
//                        for(int j=0;j<array.length;j++){
//                            generList.add(array[j]);
//                        }
//
//                        for(int i = 0; i < generList.size(); i++ ) {
//                            //((FlowLayout)getView().findViewById(R.id.gener_grid)).addView(gener(generList.get(i)));
//                        }
//                    }
//                }
//
//
//                try {
//                    JSONObject object = new JSONObject(result);
//                    if (object.getBoolean("status")) {
////                        ((ScrollView) getView().findViewById(R.id.main_layout)).setVisibility(View.VISIBLE);
////                        JSONObject categoryDetail = object.getJSONArray("data").getJSONObject(0);
////                        req_video = categoryDetail.isNull("req_video") ? "1" : categoryDetail.getString("req_video");
////                        req_image = categoryDetail.isNull("req_image") ? "1" : categoryDetail.getString("req_image");
////                        ((TextView) getView().findViewById(R.id.today_role_txt)).setText(categoryDetail.getString("description"));
////                        ((TextView) getView().findViewById(R.id.trending_txt)).setText(categoryDetail.getString("end_date"));
////                        ((TextView) getView().findViewById(R.id.participating_txt)).setText("0");
////                        ((TextView) getView().findViewById(R.id.reward_txt)).setText(categoryDetail.getString("gift_count") + " " + categoryDetail.getString("gift_base"));
//                    } else {
////                        ((ScrollView) getView().findViewById(R.id.main_layout)).setVisibility(View.GONE);
//                        //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//          //  }
//        }
//    }
//}