package com.myrole;//package com.myrole;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Looper;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.PopupMenu;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.MediaController;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.AnalyticsListener;
//import com.androidnetworking.interfaces.StringRequestListener;
//import com.bumptech.glide.Glide;
//import com.google.gson.Gson;
//import com.myrole.fragments.LikerFragment;
//import com.myrole.fragments.OtherProfileFragment;
//import com.myrole.fragments.PostDetailFragment;
//import com.myrole.holders.PostDetailsDTO;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.utils.ProgressBarHandler;
//import com.myrole.utils.Utils;
//import com.myrole.vo.Post;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
////import com.gs.instavideorecorder.FFmpegRecordActivity;
//
//public class PostDetailActivity extends BaseActivity {
//    private Activity activity;
//
//    private PostDetailFragment.OnFragmentInteractionListener mListener;
//    private Context context;
//    private View view;
//    private String postId = "";
//    VideoView postVideo;
//    ImageView postImage;
//    TextView categoty_name;
//    public TextView num_loves, post_time, txt_role_time, tv_more, txt_role_title, txt_role_desc,txt_comment;
//    public TextView num_comment;
//    TextView description;
//    ImageView loveIcon;
//    ImageView commentIcon;
//    ImageView userIcon;
//    Post post;
//    String userId;
//    LinearLayout lay_num_loves, ll_role_detail,lay_num_comm;
//    public PostDetailsDTO postDetailsDTO;
//    AppPreferences preferences;
//    private static final int REQUEST_ADD_COMMENTS = 786;
//    private ProgressBarHandler progressBarHandler;
//    private ScrollView scrollView_post_details_fragment;
//    String y="0";
//    private AppPreferences pref;
//    String user_id_pref="";
//
//
//
//
//
//    private RelativeLayout lay_other_profile_fragment;
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        getActivity();
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 786) {
//                String num_comm = data.getStringExtra("num_comm");
//                num_comment.setText(num_comm);
//            }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_post_detail);
//        context = this;
//        activity = this;
//        progressBarHandler = new ProgressBarHandler(activity);
//        // String userId = getIntent().getStringExtra("user_id");
//        postId = getIntent().getStringExtra("postId");
//        pref = AppPreferences.getAppPreferences(activity);
//        user_id_pref = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID);
//
//
//        Toast.makeText(context, "activity he", Toast.LENGTH_SHORT).show();
//       // view = inflater.inflate(R.layout.fragment_post_detail, container, false);
//        findViewById(R.id.btn_back).setOnClickListener(this);
//        progressBarHandler = new ProgressBarHandler(activity);
//        scrollView_post_details_fragment = (ScrollView) findViewById(R.id.scroll_view_post_details_fragment);
//        postVideo = ((VideoView) findViewById(R.id.video_post));
//        postImage = ((ImageView) findViewById(R.id.img_post));
//        lay_num_loves = ((LinearLayout) findViewById(R.id.lay_num_loves));
//        lay_num_comm = ((LinearLayout) findViewById(R.id.lay_num_comm));
//        num_loves = (TextView) findViewById(R.id.txt_num_loves);
//        ll_role_detail = ((LinearLayout) findViewById(R.id.ll_role_detail));
//        txt_role_title = (TextView) findViewById(R.id.today_role_title);
//        txt_role_desc = (TextView) findViewById(R.id.today_role_txt);
//        txt_role_time = (TextView) findViewById(R.id.txt_role_time);
//        txt_comment = (TextView) findViewById(R.id.txt_comment);
//        post_time = (TextView) findViewById(R.id.txt_time);
//        categoty_name = (TextView) findViewById(R.id.txt_category_name);
//        num_comment = (TextView) findViewById(R.id.txt_num_comment);
//        description = (TextView) findViewById(R.id.txt_description);
//        loveIcon = (ImageView) findViewById(R.id.img_love);
//        commentIcon = (ImageView) findViewById(R.id.img_comment);
//        userIcon = (ImageView) findViewById(R.id.usericon);
//        tv_more = (TextView) findViewById(R.id.tv_more);
//
//        if (Utils.isNetworkConnected(context, true) && !postId.isEmpty())
//            new GetPostDetailTask().execute(postId, userId);
//
//        applyFont();
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        // super.onClick(v);
//        switch (v.getId()) {
////            case R.id.rating_bar:
////                changeRating();
//            case R.id.btn_back:
////                if (y.equals("1")){
////                    getActivity().onBackPressed();
////                    //getActivity().onBackPressed();
////                }else {
////                    getActivity().onBackPressed();
////                }
//
//
//            onBackPressed();
//                //   Toast.makeText(activity, " use another image.", Toast.LENGTH_LONG).show();
//                break;
//        }
//    }
//
//
//
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(String command);
//    }
//
//    class GetPostDetailTask extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            showProgessDialog();
//            scrollView_post_details_fragment.setVisibility(View.GONE);
//            progressBarHandler.show();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            postDataParams = new HashMap<String, String>();
//            if (context == null) return null;
//            postDataParams.put("post_id", params[0]);
//            postDataParams.put("user_id", params[1]);
//
//            return HTTPUrlConnection.getInstance().load(activity, Config.POST_DETAIL, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            dismissProgressDialog();
//            progressBarHandler.hide();
//            scrollView_post_details_fragment.setVisibility(View.VISIBLE);
//
//            if (result != null) {
//
//                postDetailsDTO = new Gson().fromJson(result, PostDetailsDTO.class);
//
//                try {
//                    if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getOwner_image() != null) {
//                        Glide.with(context).load(postDetailsDTO.getData().get(0).getOwner_image()).asBitmap()
//                                .placeholder(R.drawable.default_avatar).centerCrop().into(userIcon);
//
//                    }
//                }catch (Exception e){}
//
//
////                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getOwner_name() != null) {
////                }
//                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getOwner_name() != null) {
//                    ((TextView) findViewById(R.id.txt_user_name)).setText(postDetailsDTO.getData().get(0).getOwner_name());
//
//                }
//
//                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getUrl() != null) {
//                    Log.e("Sub>>", postDetailsDTO.getData().get(0).getUrl().substring(postDetailsDTO.getData().get(0).getUrl().length() - 4, postDetailsDTO.getData().get(0).getUrl().length()));
//                    if (postDetailsDTO.getData().get(0).getUrl().substring(postDetailsDTO.getData().get(0).getUrl().length() - 4, postDetailsDTO.getData().get(0).getUrl().length()).equalsIgnoreCase(".mp4")) {
//
//                        try {
//                            ((ImageView) findViewById(R.id.img_post)).setVisibility(View.INVISIBLE);
//                            postVideo.setVisibility(View.VISIBLE);
//                            MediaController mediacontroller = new MediaController(context);
//                            mediacontroller.setAnchorView((VideoView) findViewById(R.id.video_post));
//                            // Get the URL from String VideoURL
//                            Uri video = Uri.parse(postDetailsDTO.getData().get(0).getUrl());
//                            postVideo.setMediaController(mediacontroller);
//                            postVideo.setVideoURI(video);
//
//                        } catch (Exception e) {
//                            Log.e("Error", e.getMessage());
//                            e.printStackTrace();
//                        }
//
//                        postVideo.requestFocus();
//                        postVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                            // Close the progress bar and play the video
//                            public void onPrepared(MediaPlayer mp) {
//                                postVideo.start();
//                            }
//                        });
//                    } else {
//                        postImage.setVisibility(View.VISIBLE);
//                        postVideo.setVisibility(View.INVISIBLE);
//                        Glide.with(context).load(postDetailsDTO.getData().get(0).getUrl()).asBitmap()
//                                .centerCrop().into(postImage);
//                    }
//                }
//
//
//                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getCat_id() != null) {
//                    if (postDetailsDTO.getData().get(0).getCat_id() != null) {
//                        ll_role_detail.setVisibility(View.VISIBLE);
//                        txt_role_title.setText(postDetailsDTO.getData().get(0).getRole_name());
//                        txt_role_desc.setText(postDetailsDTO.getData().get(0).getRole_description());
//
//                    } else {
//                        ll_role_detail.setVisibility(View.INVISIBLE);
//                    }
//
//                }
//
//                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getCat_name() != null) {
//                    if (postDetailsDTO.getData().get(0).getCat_name() != null) {
//                        categoty_name.setVisibility(View.VISIBLE);
//                        categoty_name.setText(postDetailsDTO.getData().get(0).getCat_name());
//                    } else {
//                        categoty_name.setVisibility(View.INVISIBLE);
//                    }
//                }
//                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getDescription() != null) {
//                    description.setText(postDetailsDTO.getData().get(0).getDescription());
//                }
//
//                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getPost_likes() != null) {
//                    num_loves.setText(postDetailsDTO.getData().get(0).getPost_likes());
//
//                }
//                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getComment_count() != null) {
//                    setNum_comment(postDetailsDTO.getData().get(0).getComment_count());
//
////                    num_comment.setText(postDetailsDTO.getData().get(0).getComment_count());
//
//                }
//                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getIslike() != null) {
//
//                    if (postDetailsDTO.getData().get(0).getIslike().equals("1")) {
//                        loveIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_liked));
//                    } else {
//                        loveIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
//                    }
//                }
//                if (postDetailsDTO != null && postDetailsDTO.getData() != null && postDetailsDTO.getData().get(0).getPost_time() != null) {
//                    post_time.setText(Utils.timeCalculation(postDetailsDTO.getData().get(0).getPost_time()));
//                }
//                // num_comment.setText(postDetailsDTO.getData().getP);
////                ((TextView) getView().findViewById(R.id.txt_user_name)).setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        if (((HomeActivity) getActivity()).fragmentManager != null) {
////                            OtherProfileFragment fragment = OtherProfileFragment.newInstance(postDetailsDTO.getData().get(0).getOwner_id());
////                            ((HomeActivity) getActivity()).fragmentManager.beginTransaction()
////                                    .add(R.id.main_frame,
////                                            fragment,
////                                            "OTHERPROFILEFRAGMENT")
////                                    .addToBackStack(null)
////                                    .commit();
////                        }
////                    }
////                });
//
//
//                userIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (((HomeActivity) activity).fragmentManager != null) {
//                            OtherProfileFragment fragment = OtherProfileFragment.newInstance(userId);
//                            ((HomeActivity) activity).fragmentManager.beginTransaction()
//                                    .add(R.id.main_frame,
//                                            fragment,
//                                            "OTHERPROFILEFRAGMENT")
//                                    .addToBackStack(null)
//                                    .commit();
//                        }
//                    }
//                });
//
//                tv_more.setTag(0);
//                tv_more.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        onPopupButtonClick(view, (int) view.getTag());
//                    }
//                });
//
//                lay_num_loves.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (((HomeActivity) activity).fragmentManager != null) {
//                            ((HomeActivity) activity).fragmentManager.beginTransaction()
//                                    .add(R.id.main_frame,
//                                            LikerFragment.newInstance(String.valueOf(postDetailsDTO.getData().get(0).getPost_id())),
//                                            "LIKERFRAGMENT")
//                                    .addToBackStack("LIKERFRAGMENT")
//                                    .commit();
//                        }
//                    }
//                });
//
//                loveIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String userId = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID);
//                        if (postDetailsDTO.getData().get(0).getIslike().equals("0")) {
//                            PostDetailActivity.this.num_loves.setText("" + (Integer.parseInt(PostDetailActivity.this.num_loves.getText().toString()) + 1));
//                            postDetailsDTO.getData().get(0).setIslike("1");
//                        } else {
//                            PostDetailActivity.this.num_loves.setText("" + (Integer.parseInt(PostDetailActivity.this.num_loves.getText().toString()) - 1));
//                            postDetailsDTO.getData().get(0).setIslike("0");
//                        }
//
//
//                        loveIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart_liked));
//                        new LikeTask().execute(userId, postId, postDetailsDTO.getData().get(0).getOwner_id(), "1");
//
//                        if (postDetailsDTO.getData().get(0).getIslike().equals("1")) {
//                            loveIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_liked));
//                        } else {
//                            loveIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
//                        }
//
////                        num_loves.setText(postDetailsDTO.getData().get(0).getPost_likes());
//                    }
//                });
//
////               num_comment.setText(postDetailsDTO.getData().get(0).getComment_count());
//                commentIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
////                        if (((HomeActivity) context).fragmentManager != null) {
////                            ((HomeActivity) context).fragmentManager.beginTransaction()
////                                    .add(R.id.main_frame,
////                                            CommentsFragment.newInstance(postId),
////                                            "COMMENTSFRAGMENT")
////                                    .addToBackStack("COMMENTSFRAGMENT_123")
////                                    .commit();
////                        }
//                        Intent in_comment = new Intent(activity, AddComments.class);
//                        in_comment.putExtra("post_id", postId);
//                        startActivityForResult(in_comment, REQUEST_ADD_COMMENTS);
//                    }
//                });
////                txt_comment.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent in_comment = new Intent(getActivity(), AddComments.class);
////                        in_comment.putExtra("post_id", postId);
////                        startActivityForResult(in_comment, REQUEST_ADD_COMMENTS);
////                    }
////                });
//                lay_num_comm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent in_comment = new Intent(activity, AddComments.class);
//                        in_comment.putExtra("post_id", postId);
//                        startActivityForResult(in_comment, REQUEST_ADD_COMMENTS);
//                    }
//                });
//                setNum_comment(postDetailsDTO.getData().get(0).getComment_count());
//
////                num_comment.setText(postDetailsDTO.getData().get(0).getComment_count());
//
//            } else {
//                Toast.makeText(activity, postDetailsDTO.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//
//    public void setNum_comment(String num_comments) {
//
//        num_comment.setText(num_comments);
//
//    }
//
//
//    public void onPopupButtonClick(View button, final int position) {
//        PopupMenu popup = new PopupMenu(activity, button);
////        final PostListDTO postListDTO = post_list.get(position);
//        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
//        String userId = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID);
//        int ownerId = Integer.parseInt(postDetailsDTO.getData().get(0).getOwner_id());
//        if (ownerId == Integer.parseInt(userId)) {
//            MenuItem item = popup.getMenu().findItem(R.id.Report);
//            item.setVisible(false);
//            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
//            item1.setVisible(true);
//        } else {
//            MenuItem item = popup.getMenu().findItem(R.id.Report);
//            item.setVisible(true);
//            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
//            item1.setVisible(false);
//        }
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.Share:
//                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//                        share.setType("text/plain");
//                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//                        // Add data to the intent, the receiving app will decide
//                        // what to do with it.
//                        share.putExtra(Intent.EXTRA_SUBJECT, "MY Role");
//                        share.putExtra(Intent.EXTRA_TEXT, postDetailsDTO.getUrl());
//
//                        activity.startActivity(Intent.createChooser(share, "Share link!"));
//
//                        break;
//                    case R.id.Report:
//                        optionDialog(position);
//                        break;
//                    case R.id.delete:
//                        AlertDialog(position, postDetailsDTO.getData().get(0).getPost_id());
//                        break;
//                }
//                return true;
//            }
//        });
//
//        popup.show();
//    }
//
//    public void optionDialog(final int pos) {
//
//        CharSequence options[] = new CharSequence[]{"It’s Spam", "It’s Inappropriate"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setCancelable(true);
//        builder.setTitle("Select your option:");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // the user clicked on options[which]
//                switch (which) {
//                    case 0:
//                        callServiceForReport("It’s Spam");
////                        deleteRecord(pos);
//                        ((HomeActivity) activity).onBackPressed();
//                        break;
//                    case 1:
//                        callServiceForReport("It’s Inappropriate");
////                        deleteRecord(pos);
//                        ((HomeActivity) activity).onBackPressed();
//                        break;
//
//                }
//            }
//        });
//
//        builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //the user clicked on Cancel
//            }
//        });
//        builder.show();
//    }
//
//    private void callServiceForReport(String s) {
//
//        AppPreferences preferences = AppPreferences.getAppPreferences(activity);
//        AndroidNetworking.post(Config.REPORT_USER_POST)
//                .setTag(this)
//                .addBodyParameter("user_id", preferences.getStringValue(AppPreferences.USER_ID))
//                .addBodyParameter(" comment", s)
//
//                .setPriority(Priority.IMMEDIATE)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//                        Log.d("TAG", " timeTakenInMillis : " + timeTakenInMillis);
//                        Log.d("TAG", " bytesSent : " + bytesSent);
//                        Log.d("TAG", " bytesReceived : " + bytesReceived);
//                        Log.d("TAG", " isFromCache : " + isFromCache);
//                    }
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        //dismissProgressDialog();
//
//                        Log.d("TAG", "delete_onResponse object : " + response.toString());
//                        Log.d("TAG", "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
//
//
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//
//                        //dismissProgressDialog();
//                        if (error.getErrorCode() != 0) {
//                            // received ANError from server
//                            // error.getErrorCode() - the ANError code from server
//                            // error.getErrorBody() - the ANError body from server
//                            // error.getErrorDetail() - just a ANError detail
//                            Log.d("TAG", "onError errorCode : " + error.getErrorCode());
//                            Log.d("TAG", "onError errorBody : " + error.getErrorBody());
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//
//    }
//
//    public void deleteRecord(int Pos) {
////        postDetailsDTO.getData().remove(Pos);
////        mVideos1.remove(Pos);
////        gffragment.mRecyclerView.setAdapter(new GeneralFeedAdapter(mContext, post_list, mVideos1, gffragment));
////        notifyDataSetChanged();
//
//        ((HomeActivity) activity).onBackPressed();
//    }
//
//    public void AlertDialog(final int pos, final int post_id) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("MY Role");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Are you sure you want to delete this?");
//
//        // Setting Positive "Yes" Button
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                callServiceForFeeds(post_id);
////                deleteRecord(pos);
//                ((HomeActivity) activity).onBackPressed();
//            }
//        });
//
//        // Setting Negative "NO" Button
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // Write your code here to invoke NO event
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//    private void callServiceForFeeds(int post_id) {
//
//        AppPreferences preferences = AppPreferences.getAppPreferences(activity);
//        AndroidNetworking.post(Config.DELETE_USER_POST)
//                .setTag(this)
//                .addBodyParameter("user_id", preferences.getStringValue(AppPreferences.USER_ID))
//                .addBodyParameter("post_id", String.valueOf(post_id))
//
//                .setPriority(Priority.IMMEDIATE)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//                        Log.d("TAG", " timeTakenInMillis : " + timeTakenInMillis);
//                        Log.d("TAG", " bytesSent : " + bytesSent);
//                        Log.d("TAG", " bytesReceived : " + bytesReceived);
//                        Log.d("TAG", " isFromCache : " + isFromCache);
//                    }
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        //dismissProgressDialog();
//
//                        Log.d("TAG", "delete_onResponse object : " + response.toString());
//                        Log.d("TAG", "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
//
//
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//
//                        //dismissProgressDialog();
//                        if (error.getErrorCode() != 0) {
//                            // received ANError from server
//                            // error.getErrorCode() - the ANError code from server
//                            // error.getErrorBody() - the ANError body from server
//                            // error.getErrorDetail() - just a ANError detail
//                            Log.d("TAG", "onError errorCode : " + error.getErrorCode());
//                            Log.d("TAG", "onError errorBody : " + error.getErrorBody());
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//
//    }
//
//    class LikeTask extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //showProgessDialog();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            postDataParams = new HashMap<String, String>();
//            postDataParams.put("user_id", params[0]);
//            postDataParams.put("post_id", params[1]);
//            postDataParams.put("owner_id", params[2]);
//            postDataParams.put("type", params[3]);
//
//            return HTTPUrlConnection.getInstance().load(activity, Config.ADD_LIKE, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            //dismissProgressDialog();
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//
////                    PostDetailFragment.this.num_loves.setText("" + (Integer.parseInt(postDetailsDTO.getData().get(0).getPost_likes()) + 1));
//                    Log.v("ISLIKE", postDetailsDTO.getData().get(0).getIslike());
//                    Log.v("MYROLE", result);
//
//                } else
//                    Toast.makeText(activity, object.getString("message"), Toast.LENGTH_LONG).show();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void applyFont() {
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.txt_title), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, description, Config.QUICKSAND, Config.BOLD);
//        Utils.setTypeface(context, post_time, Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.txt_loves), Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.txt_num_loves), Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.txt_comment), Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.txt_num_comment), Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.txt_user_name), Config.QUICKSAND, Config.BOLD);
//
//        Utils.setTypeface(context, txt_role_desc, Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context, txt_role_time, Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context, txt_role_title, Config.QUICKSAND, Config.BOLD);
//
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.gener_title), Config.QUICKSAND, Config.BOLD);
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.trending_title), Config.QUICKSAND, Config.BOLD);
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.participating_title), Config.QUICKSAND, Config.BOLD);
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.trending_txt), Config.QUICKSAND, Config.REGULAR);
//        Utils.setTypeface(context, (TextView) view.findViewById(R.id.participating_txt), Config.QUICKSAND, Config.REGULAR);
//
//
//    }
//}
//
//
//
