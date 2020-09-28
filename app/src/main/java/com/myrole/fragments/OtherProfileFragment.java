package com.myrole.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bumptech.glide.Glide;
import com.myrole.AboutProfileActivity;
import com.myrole.BaseActivity;
import com.myrole.BaseFragment;
import com.myrole.EditProfileActivity;
import com.myrole.OptionsActivity;
import com.myrole.R;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.RestClient;
import com.myrole.utils.Utils;
import com.myrole.vo.Contact;
import com.myrole.widget.FetchInterfaceAlert;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.myrole.utils.ActivityTransactions.getLogIn;
import static com.myrole.utils.AppPreferences.isLogin;

//import com.gs.instavideorecorder.FFmpegRecordActivity;

public class OtherProfileFragment extends BaseFragment implements View.OnClickListener, FetchInterfaceAlert {

    // private static final String USER_ID = "USER_ID";
    ArrayList<String> titleList;
    String userId;
    String videoUrl = "";
    String profileUrl = "";
    private OnFragmentInteractionListener mListener;
    private Activity activity;
    private int selectedMediaType;
    private String private_user, follow_request;
    private List<Contact> followingList;
    private boolean isSendRequest = false;
    private boolean isFollow = false;
    private RatingBar ratingBar;
    private String imagePath = "", videoPath = "", videoIcon = "", video_thumb;
    private Dialog m_dialog;
    private View view;

    int REQUEST_CODE = 345;
    String get_editTextString = "";

    private AppPreferences pref;

    String unfollow_user_name;
    public static String OtherUserID = "";
    private ProgressBarHandler progressBarHandler;
    private View lay_other_profile_fragment;
    private RelativeLayout relativeLayoutIsLogin;
    private Context mContext;

    private boolean isLoginCheck = false;

    public OtherProfileFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static OtherProfileFragment newInstance(String userId) {
        OtherProfileFragment fragment = new OtherProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USERID", userId);
        fragment.setArguments(bundle);
        Utils.isFromProfile = false;
        OtherUserID = userId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleList = new ArrayList<>();
        followingList = new ArrayList<>();
        activity = getActivity();
        Utils.isFromProfile = false;
        pref = AppPreferences.getAppPreferences(activity);
        pref.putBooleanValue(AppPreferences.allowRefreshgeneral, false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.isFromProfile = false;
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        activity = getActivity();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        progressBarHandler = new ProgressBarHandler(activity);
        lay_other_profile_fragment = view.findViewById(R.id.lay_profile_fragment);
        relativeLayoutIsLogin = view.findViewById(R.id.relativeLayoutIsLogin);
        view.findViewById(R.id.lay_profile_icon).setOnClickListener(this);
        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        applyFont(view);
        private_user = "0";
        follow_request = "0";
        view.findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        userId = getArguments().getString("USERID", "");// for stoping crash set defult value
        String userID = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        Log.e("MYUSERID",userID+"::"+userId);

        if (userId.equals("3")) {
            ((LinearLayout) view.findViewById(R.id.line_foll_post_tab)).setVisibility(View.GONE);
            ((RelativeLayout) view.findViewById(R.id.rel_button_view)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txt_about)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txt_add_video)).setVisibility(View.GONE);
        }

        relativeLayoutIsLogin.setVisibility(View.GONE);
        if (Utils.isNetworkConnected(getActivity(), true)) {
            new GetFollowersTask().execute(userId);
            new GetUserDetailTask().execute(userId);
        }
        view.findViewById(R.id.card_block).setVisibility(View.GONE);
        view.findViewById(R.id.btn_back).setOnClickListener(this);
        if (userId.equals("3")) {
            ((LinearLayout) view.findViewById(R.id.line_foll_post_tab)).setVisibility(View.GONE);
            ((RelativeLayout) view.findViewById(R.id.rel_button_view)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txt_about)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txt_add_video)).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
            view.findViewById(R.id.txt_add_video).setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.txt_about).setOnClickListener(this);
        view.findViewById(R.id.btn_edit).setOnClickListener(this);
        view.findViewById(R.id.card_following).setOnClickListener(this);
        view.findViewById(R.id.btn_follow).setOnClickListener(this);
        // tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        //pager = (ViewPager) view.findViewById(R.id.pager);
        view.findViewById(R.id.txt_add_video).setOnClickListener(this);

        view.findViewById(R.id.edit_enter_sataus2).setVisibility(View.VISIBLE);

        String username = AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_NAME);
        String userImagePath = AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_IMAGE_URL);
        ((TextView) view.findViewById(R.id.txt_username)).setText(username);

        if (userImagePath != null) {
            try {
                Glide.with(getActivity()).asBitmap().load(profileUrl)
                        .into(((CircleImageView) view.findViewById(R.id.img_user_icon)));
            } catch (Exception e) {
            }
            try {
                Glide.with(getActivity()).asBitmap().load(profileUrl)
                        .into(((ImageView) view.findViewById(R.id.img_user_bg)));
            } catch (Exception e) {
            }
        }

        view.findViewById(R.id.followers).setOnClickListener(this);
        view.findViewById(R.id.following).setOnClickListener(this);
        if (userId.equals("3")) {
            ((LinearLayout) view.findViewById(R.id.line_foll_post_tab)).setVisibility(View.GONE);
            ((RelativeLayout) view.findViewById(R.id.rel_button_view)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txt_about)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txt_add_video)).setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
        }

        if(userID.equals(userId)){
            ((TextView) view.findViewById(R.id.txt_add_video)).setVisibility(View.VISIBLE);
        }else{
            ((TextView) view.findViewById(R.id.txt_add_video)).setVisibility(View.GONE);
        }


        return view;
    }

    public void AlertDialog(String name) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("MY Role");
        alertDialog.setMessage("Unfollow " + name + "?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new UnfollowFriendTask().execute();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Utils.hideKeybord(getContext(), v);
                getActivity().onBackPressed();
                break;
            case R.id.followers:
                if (isLogin(getContext())){
                    if (((MainDashboardActivity) getActivity()).fragmentManager != null) {
                        ((MainDashboardActivity) getActivity()).fragmentManager.beginTransaction()
                                .add(R.id.container,
                                        FollowerFragment.newInstance(userId),
                                        "FOLLOWERFRAGMENT")
                                .addToBackStack("FOLLOWERFRAGMENT")
                                .commit();
                    }
                } else getLogIn(getContext());
                break;

            case R.id.following:
                if (isLogin(getContext())){
                    if (((MainDashboardActivity) getActivity()).fragmentManager != null) {

                        ((MainDashboardActivity) getActivity()).fragmentManager.beginTransaction()
                                .add(R.id.container,
                                        FollowingFragment.newInstance(userId),
                                        "FOLLOWINGFRAGMENT")
                                .addToBackStack("FOLLOWINGFRAGMENT")
                                .commit();
                    }
                } else getLogIn(getContext());
                break;

            case R.id.btn_edit:
                if (isLogin(getContext())){
                    getActivity().startActivity(new Intent(getActivity(), EditProfileActivity.class));
                } else getLogIn(getContext());
                break;

            case R.id.txt_add_video:
                /*if (videoUrl != null && !videoUrl.isEmpty()) {
                    try {
                        Intent i = new Intent(getActivity(), VideoActivity.class); // Your list's Intent
                        i.putExtra("url", videoUrl);
                        i.putExtra("video_thumb", video_thumb);
                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.showAlertDialog(getContext(), "Alert", "No video added !", true);
                }*/
                if (isLogin(getContext())){
                    Intent i = new Intent(getActivity(), OptionsActivity.class);
                    startActivity(i);
                } else getLogIn(getContext());
                break;

            case R.id.lay_profile_icon:
                if (isLogin(getContext())){
                    if (profileUrl != null && !profileUrl.isEmpty() && !profileUrl.equals("null")) {
                        try {
                            m_dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                            m_dialog.setCancelable(false);
                            m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            m_dialog.setContentView(R.layout.layout_video_view);
                            if (userId.equals(AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID))) {
                                m_dialog.findViewById(R.id.btn_edit_profile).setVisibility(View.VISIBLE);
                            } else {
                                m_dialog.findViewById(R.id.btn_edit_profile).setVisibility(View.GONE);
                            }
                            m_dialog.findViewById(R.id.img_profile).setVisibility(View.VISIBLE);
                            ((TextView) m_dialog.findViewById(R.id.text_title)).setText("Profile Image");
                            RelativeLayout rl = (RelativeLayout) m_dialog.findViewById(R.id.videoSurfaceContainer);

                            try {
                                Glide.with(getActivity()).asBitmap().load(profileUrl)
                                        .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
                            } catch (Exception e) {
                            }

                            m_dialog.findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    m_dialog.dismiss();
                                }
                            });

                            m_dialog.findViewById(R.id.btn_edit_profile).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectedMediaType = Config.MEDIA_TYPE_IMAGE;
                                    Utils.showMediaSelectionDialog((BaseActivity) activity, selectedMediaType, "0");
                                }
                            });

                            m_dialog.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Utils.showAlertDialog(getActivity(), "Alert", "No image added !", true);
                    }
                } else getLogIn(getContext());
                break;

            case R.id.btn_follow:
                if (isLogin(getContext())){
                    if (private_user.equals("1")) {
                        new SendFollowRequestTask().execute();
                        break;
                    } else {
                        new FollowFriendTask().execute();
                        break;
                    }
                } else {

                }

            case R.id.card_following:
                if (isLogin(getContext())){
                    AlertDialog(unfollow_user_name);
                } else {
                    getLogIn(getContext());
                }
                // new UnfollowFriendTask().execute();
                break;

            case R.id.txt_about:
                if (isLogin(getContext())){
                    getActivity().startActivity(new Intent(getActivity(), AboutProfileActivity.class).putExtra("user_id", userId));
                } else getLogIn(getContext());
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Utils.hideKeybord(getContext(),view);
        mListener = null;
    }

    private void applyFont(View view) {
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_about), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_username), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.btn_edit), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_user_status), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_add_video), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_num_follower), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_num_following), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_num_post), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_follower), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_following), Config.NEXA, Config.BOLD);
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.txt_post), Config.NEXA, Config.BOLD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //  String editTextString = data.getStringExtra("yo");
            get_editTextString = data.getStringExtra("yo");
            try {
                ((TextView) view.findViewById(R.id.edit_enter_sataus)).setText(StringEscapeUtils.unescapeJava(get_editTextString));
            } catch (Exception e) {
            }
            new UpdateStatusIconTask(getContext()).execute();
            // Toast.makeText(getActivity(),editTextString,Toast.LENGTH_LONG).show();
        }
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == Config.CAMERAIMAGE) {
//                  Utils.performCrop(this, Config.CAMERAFILEURI);
//                    imagePath = data.getStringExtra(FFmpegRecordActivity.INTENT_NAME_IMAGE_PATH);
////                    bitmap = BitmapFactory.decodeFile(imagePath);
////                    imagePath = Utils.getRealPathFromURI(this, Config.CAMERAFILEURI);
//
//
////                    UCrop.of(Config.CAMERAFILEURI, Uri.fromFile(new File(imagePath)))
////                            .withAspectRatio(1, 1)
////                            .withMaxResultSize(1024, 1024)
////                            .start(this);
//
////                   bitmap = Utils.decodeSampledBitmapFromResource(imagePath, 300, 300);
//                    profileUrl = imagePath;
////                    ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
////                    ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
//                    try{
//                    Glide.with(getActivity()).load(profileUrl).asBitmap()
//                            .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
//                    }catch (Exception e){}

                } else if (requestCode == Config.GALLERYIMAGE) {
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
//                        Utils.performCrop(this, selectedImageUri);
                        imagePath = Utils.getRealPathFromURI(activity, selectedImageUri);

//                        UCrop.of(Config.CAMERAFILEURI, Uri.fromFile(new File(imagePath)))
//                                .withAspectRatio(1, 1)
//                                .withMaxResultSize(1024, 1024)
//                                .start(this);

                        profileUrl = imagePath;
//                        bitmap = Utils.decodeSampledBitmapFromResource(imagePath, 300, 300);
//                        ((ImageView)findViewById(R.id.img_add_pic)).setImageBitmap(bitmap);
//                        ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
//                        ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
                        try {
                            Glide.with(getActivity()).asBitmap().load(profileUrl)
                                    .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
                        } catch (Exception e) {
                        }
                    }

                } else if (requestCode == Config.CROPIMAGE) {
                    /*Bundle extras = data.getExtras();
                    bitmap = extras.getParcelable("data");
*/
                    if (Config.CROPPEDIMAGEURI != null) {
                        imagePath = Utils.getRealPathFromURI(activity, Config.CROPPEDIMAGEURI);
                        profileUrl = imagePath;
//                        bitmap = BitmapFactory.decodeFile(imagePath);
//                        ((ImageView) findViewById(R.id.img_add_pic)).setImageBitmap(bitmap);
//                        ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
//                        ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
                        try {
                            Glide.with(getActivity()).asBitmap().load(profileUrl)
                                    .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
                        } catch (Exception e) {
                        }
//                        ((TextView) findViewById(R.id.txt_add_pic)).setText("CHANGE");
                    }
                } else
//                if (requestCode == UCrop.REQUEST_CROP) {
//                    final Uri resultUri = UCrop.getOutput(data);
//                    imagePath = Utils.getRealPathFromURI(this, resultUri);
//                    profileUrl = imagePath;
//                    ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
//                    ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
//                    Glide.with(context).load(imagePath).asBitmap()
//                            .centerCrop().into(((ImageView) findViewById(R.id.img_add_pic)));
////                    bitmap = BitmapFactory.decodeFile(imagePath);
////                    showPostDialog();
//
//                } else if (resultCode == UCrop.RESULT_ERROR) {
//                    final Throwable cropError = UCrop.getError(data);
//                }
                    if (requestCode == Config.CAMERAVIDEO) {

//                            videoPath = Utils.getRealPathFromURI(this, selectedVideoUri);
//                        videoPath = data.getStringExtra(FFmpegRecordActivity.INTENT_NAME_VIDEO_PATH);
//                        videoUrl = videoPath;
//
////                        videoIcon = videoPath.replace(".mp4",".jpg");
//
////                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
////                        ((ImageView) findViewById(R.id.vid_add_video)).setImageBitmap(bitmap);
////                        ((RelativeLayout) findViewById(R.id.lay_vid_add)).setVisibility(View.GONE);
////                        ((ImageView) findViewById(R.id.vid_icon_play)).setVisibility(View.VISIBLE);
////                        ((ImageView) findViewById(R.id.vid_update_icon)).setVisibility(View.VISIBLE);
//                        try{
//                        Glide.with(getActivity()).load(profileUrl).asBitmap()
//                                .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
//                        }catch (Exception e){}
//                    ((TextView) findViewById(R.id.txt_add_video)).setText("CHANGE");

                    } else if (requestCode == Config.GALLERYVIDEO) {
                        Uri selectedVideoUri = data.getData();
                        if (selectedVideoUri != null) {
                            videoPath = Utils.getRealPathFromURI(activity, selectedVideoUri);
                            videoUrl = videoPath;
//                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
//                        ((ImageView) findViewById(R.id.vid_add_video)).setImageBitmap(bitmap);
//                            ((RelativeLayout) findViewById(R.id.lay_vid_add)).setVisibility(View.GONE);
//                            ((ImageView) findViewById(R.id.vid_icon_play)).setVisibility(View.VISIBLE);
//                            ((ImageView) findViewById(R.id.vid_update_icon)).setVisibility(View.VISIBLE);
                            try {
                                Glide.with(getActivity()).asBitmap().load(profileUrl)
                                        .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
                            } catch (Exception e) {
                            }
//                        ((TextView) findViewById(R.id.txt_add_video)).setText("CHANGE");
                        }
                    }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity, "Something went wrong, please try again or use another image.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }

    public static class FragmentText extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.layout_fragment_hobby, container, false);
            return view;
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }

//        public Fragment getItem(int num) {
//            return new FragmentText();
//        }

        public Fragment getItem(int num) {
//                return NewUserCategoryPostFragment1.newInstance(Config.categoryList.get(num).id, userId);
            //return NewUserGeneralPostFragment1.newInstance(Config.categoryList.get(num).id, userId);
            return null;
        }

        @Override
        public int getCount() {
            return Config.categoryList.size() - 1;
        }
        //for dont showdot in tab on other filed

        @Override
        public CharSequence getPageTitle(int position) {
            return Config.categoryList.get(position).name;
        }

    }

    class GetUserDetailTask extends AsyncTask<String, Void, String> {

        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
//            showProgessDialog();
            lay_other_profile_fragment.setVisibility(View.GONE);
            progressBarHandler.show();

        }

        @Override
        protected String doInBackground(String... params) {

            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            return HTTPUrlConnection.getInstance().load(getActivity(), Config.GET_USER, postDataParams);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            lay_other_profile_fragment.setVisibility(View.VISIBLE);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject userDetail = object.getJSONArray("data").getJSONObject(0);

                    ((TextView) view.findViewById(R.id.txt_num_follower)).setText(userDetail.getString("followers"));
                    ((TextView) view.findViewById(R.id.txt_num_following)).setText(userDetail.getString("following"));
                    ((TextView) view.findViewById(R.id.txt_num_post)).setText(userDetail.getString("posts"));
                    ((TextView) view.findViewById(R.id.edit_enter_sataus2)).setText(StringEscapeUtils.unescapeJava(
                            userDetail.isNull("status") ? "" : userDetail.getString("status")));
                    ((TextView) view.findViewById(R.id.txt_username)).setText(
                            userDetail.isNull("name") ? "" : userDetail.getString("name"));
                    unfollow_user_name = userDetail.getString("name");

                    try {
                        Glide.with(getActivity()).asBitmap().load(userDetail.getString("image"))
                                .placeholder(R.drawable.default_avatar).centerCrop().into(((CircleImageView) view.findViewById(R.id.img_user_icon)));
                    } catch (Exception e) {
                    }
                    try {
                        Glide.with(getActivity()).asBitmap().load(userDetail.getString("image"))
                                .placeholder(R.drawable.default_avatar).centerCrop().into(((ImageView) view.findViewById(R.id.img_user_bg)));
                    } catch (Exception e) {
                    }


                    private_user = userDetail.getString("private_user");
                    follow_request = userDetail.getString("follow_request");
                    videoUrl = userDetail.getString("video");
                    profileUrl = userDetail.getString("image");
                    String rating = userDetail.getString("rating");

                    if (!userDetail.isNull("video_thumb")) {
                        video_thumb = userDetail.getString("video_thumb");
                    }

                    Log.d("TAG", "onPostExecute: "+follow_request);
                    //Toast.makeText(activity, ""+follow_request, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(activity, "Id : "+AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID), Toast.LENGTH_SHORT).show();
                    if (follow_request.equals(AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID))) {

                        isSendRequest = true;
//
//                        view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                        view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                        view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
//                        ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request Sent");

                    }


//                    else {
//
//                        view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                        view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                        view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
//                        ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow");
//                    }

                    if ((!rating.equals("null") && rating != null && !rating.equals(""))) {

                        ratingBar.setRating(Float.parseFloat(rating));

                    } else {

                        rating = "0.0";
                        ratingBar.setRating(Float.parseFloat(rating));

                    }

                    if (userId.equals(AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID))) {

//                        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
                        Typeface face = null;
                        try {
                            face = Typeface.createFromAsset(mContext.getAssets(), "fonts/fontawesome-webfont.ttf");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (face != null) {
                            ((TextView) view.findViewById(R.id.txt_update_status)).setTypeface(face);
                        }

                        view.findViewById(R.id.card_following).setVisibility(View.GONE);
                        view.findViewById(R.id.btn_follow).setVisibility(View.GONE);
                        view.findViewById(R.id.card_block).setVisibility(View.GONE);
                        view.findViewById(R.id.btn_edit).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.edit_enter_sataus).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.txt_user_status).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.txt_update_status).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.edit_enter_sataus2).setVisibility(View.GONE);

                        try {
                            ((TextView) view.findViewById(R.id.edit_enter_sataus)).setText(StringEscapeUtils.unescapeJava(
                                    userDetail.isNull("status") ? "" : userDetail.getString("status")));
                        } catch (Exception e) {
                        }

                        ratingBar.setIsIndicator(true);
                        ratingBar.setClickable(false);
                        ratingBar.setFocusableInTouchMode(false);
                        ratingBar.setFocusable(false);

                        ((TextView) view.findViewById(R.id.txt_update_status)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                AlertStatusDilogFragment dialogFragment = new AlertStatusDilogFragment();
                                dialogFragment.setTargetFragment(OtherProfileFragment.this, REQUEST_CODE);
                                dialogFragment.show(fm, "Alert Statud Dialog Fragment");


                            }
                        });

                    } else {

                        if (private_user.equals("1")) {

                            if (isFollow) {

                                view.findViewById(R.id.card_following).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.btn_follow).setVisibility(View.INVISIBLE);
                                view.findViewById(R.id.btn_edit).setVisibility(View.GONE);

                            } else {
                                view.findViewById(R.id.txt_about).setVisibility(View.GONE);

                                view.findViewById(R.id.followers).setClickable(false);

                                view.findViewById(R.id.following).setClickable(false);

                                // pager.setVisibility(View.GONE);

                                view.findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);

                                //tabLayout.setVisibility(View.GONE);

                                if (isSendRequest) {
                                    view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
                                    view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
                                    view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
                                    ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request Sent");

                                } else {
                                    view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
                                    view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
                                    view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
                                    ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request");
                                }
                            }

                        } else {
                            if (userId.equals("3")) {
                                ((LinearLayout) view.findViewById(R.id.line_foll_post_tab)).setVisibility(View.GONE);
                                ((RelativeLayout) view.findViewById(R.id.rel_button_view)).setVisibility(View.GONE);
                                ((TextView) view.findViewById(R.id.txt_about)).setVisibility(View.GONE);
                               // ((TextView) view.findViewById(R.id.txt_add_video)).setVisibility(View.GONE);
                            } else {
                                view.findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
                             //   view.findViewById(R.id.txt_add_video).setVisibility(View.VISIBLE);
                            }




                            view.findViewById(R.id.followers).setClickable(true);

                            view.findViewById(R.id.following).setClickable(true);

                            //  pager.setVisibility(View.VISIBLE);

                            view.findViewById(R.id.txt_private_user).setVisibility(View.GONE);

                            //tabLayout.setVisibility(View.VISIBLE);

                            if (isFollow) {

                                view.findViewById(R.id.card_following).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.btn_follow).setVisibility(View.INVISIBLE);
                                view.findViewById(R.id.btn_edit).setVisibility(View.GONE);

                            } else {

                                if (isSendRequest) {

                                    view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
                                    view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
                                    view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
                                    ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request Sent");

                                } else {

                                    view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
                                    view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
                                    view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
                                    ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow");

                                }

                            }

                        }

                    }


//                    if (private_user.equals("1")) {
//                        if (isSendRequest) {
//
////                            view.findViewById(R.id.card_following).setVisibility(View.VISIBLE);
//
//                            view.findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
//
//                            view.findViewById(R.id.followers).setClickable(true);
//
//                            view.findViewById(R.id.following).setClickable(true);
//
//                            pager.setVisibility(View.VISIBLE);
//
//                            view.findViewById(R.id.txt_private_user).setVisibility(View.GONE);
//
//                            tabLayout.setVisibility(View.VISIBLE);
//                        } else {
//
//                            if (follow_request.equals(AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID))) {
//                                view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                                view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                                ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request Sent");
//                            } else {
//
//                                view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                                view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                                view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
//                                ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow");
//                            }
//
//                            view.findViewById(R.id.txt_about).setVisibility(View.GONE);
//
//                            view.findViewById(R.id.followers).setClickable(false);
//
//                            view.findViewById(R.id.following).setClickable(false);
//
//                            pager.setVisibility(View.GONE);
//
//                            view.findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);
//
//                            tabLayout.setVisibility(View.GONE);
//                        }
//
//
//                    } else {
//
//
//                        Toast.makeText(getContext(), "Public user", Toast.LENGTH_SHORT).show();
//                    }

                } else {
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class GetFollowersTask extends AsyncTask<String, Void, String> {

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
            postDataParams.put("user_id", params[0]);
            postDataParams.put("type_by", "following");

            return HTTPUrlConnection.getInstance().load(getActivity(), Config.GET_FOLLOWER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {

                    JSONArray userArray = object.getJSONArray("data");
//                    if (userArray.length() > 0) {
//                        getView().findViewById(R.id.no_following).setVisibility(View.GONE);
//                    }
                    for (int i = 0; i < userArray.length(); i++) {
                        Contact contact = new Contact();
                        contact.id = userArray.getJSONObject(i).getString("id");
                        contact.name = userArray.getJSONObject(i).getString("name");
                        contact.image = userArray.getJSONObject(i).getString("image");
                        contact.follow = userArray.getJSONObject(i).getString("follow");
                        followingList.add(contact);
                    }
//                    followingAdapter.notifyDataSetChanged();

                    for (int i = 0; i < followingList.size(); i++) {
                        if (followingList.get(i).id.equals(userId)) {
                            isFollow = true;
//                            view.findViewById(R.id.card_following).setVisibility(View.VISIBLE);
//                            view.findViewById(R.id.btn_follow).setVisibility(View.INVISIBLE);
//                            view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
                        }
                    }

                } else {
                    //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {

                e.printStackTrace();

            }
        }
    }

    class SendFollowRequestTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
//        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//           showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
//          pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", userId);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(getActivity(), Config.SEND_FOLLOW_REQUEST, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();

            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
//                  liker_list.get(pos).follows = "1";

                    isSendRequest = true;
                    view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request Sent");
                    //  adapter.notifyDataSetChanged();

//                    if (private_user.equals("1")){
//                        if (isFollow){
//
//                            pager.setVisibility(View.VISIBLE);
//
//                            getView().findViewById(R.id.txt_private_user).setVisibility(View.GONE);
//
//                            tabLayout.setVisibility(View.VISIBLE);
//                        }else {
//                            pager.setVisibility(View.GONE);
//
//                            getView().findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);
//
//                            tabLayout.setVisibility(View.GONE);
//                        }
//
//                    }else {
//                        Toast.makeText(getContext(), "Public user", Toast.LENGTH_SHORT).show();
//                    }

                } else {
                    //Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class UnfollowFriendTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
//        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", userId);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID));
            Log.v("koko", "user_id " + userId);
            Log.v("koko", "follower_id " + AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(getActivity(), Config.UNFOLLOW_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    // adapter.notifyDataSetChanged();
                    isSendRequest = false;
                    isFollow = false;
                    view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);

                    if (private_user.equals("1")) {
                        view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
                        ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request");
                        if (isSendRequest) {
                            if (userId.equals("3")) {
                                ((LinearLayout) view.findViewById(R.id.line_foll_post_tab)).setVisibility(View.GONE);
                                ((RelativeLayout) view.findViewById(R.id.rel_button_view)).setVisibility(View.GONE);
                                ((TextView) view.findViewById(R.id.txt_about)).setVisibility(View.GONE);
                              //  ((TextView) view.findViewById(R.id.txt_add_video)).setVisibility(View.GONE);
                            } else {
                                view.findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
                               // view.findViewById(R.id.txt_add_video).setVisibility(View.VISIBLE);
                            }

                            view.findViewById(R.id.followers).setClickable(true);

                            view.findViewById(R.id.following).setClickable(true);

                            //  pager.setVisibility(View.VISIBLE);

                            view.findViewById(R.id.txt_private_user).setVisibility(View.GONE);

                            //tabLayout.setVisibility(View.VISIBLE);

                        } else {

                            //pager.setVisibility(View.GONE);

                            view.findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);

                            //tabLayout.setVisibility(View.GONE);

                            view.findViewById(R.id.txt_about).setVisibility(View.GONE);

                            view.findViewById(R.id.followers).setClickable(false);

                            view.findViewById(R.id.following).setClickable(false);

                        }

                    } else {
                        view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
                        //Toast.makeText(getContext(), "Public user", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class UpdateProfileRatingTask extends AsyncTask<String, Void, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                RestClient client;

                entity.addPart("user_id", new StringBody(userId));
                entity.addPart("rating", new StringBody(params[0]));

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

                    Log.v("Log", result);
                    if (!object.getString("rating").equals("")) {
                        ratingBar.setRating(Float.parseFloat(object.getString("rating")));
                    }


                } else {

                    Toast.makeText(activity, object.getString("message"), Toast.LENGTH_LONG).show();

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

        }
    }

    class FollowFriendTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
//        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            pos = Integer.parseInt(params[0]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", userId);
            postDataParams.put("follower_id", AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID));
            return HTTPUrlConnection.getInstance().load(getActivity(), Config.FOLLOW_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
//            Toast.makeText(activity, "user -id :" + userId, Toast.LENGTH_LONG).show();
//            Toast.makeText(activity, "follower_id :" +  AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID), Toast.LENGTH_LONG).show();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                String msg = object.optString("message");
                if (object.getBoolean("status")) {
                    //adapter.notifyDataSetChanged();
                    isSendRequest = false;
                    view.findViewById(R.id.card_following).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.btn_follow).setVisibility(View.INVISIBLE);
                    if (private_user.equals("1")) {
                        if (isSendRequest) {
                            if (userId.equals("3")) {
                                ((LinearLayout) view.findViewById(R.id.line_foll_post_tab)).setVisibility(View.GONE);
                                ((RelativeLayout) view.findViewById(R.id.rel_button_view)).setVisibility(View.GONE);
                                ((TextView) view.findViewById(R.id.txt_about)).setVisibility(View.GONE);
                                //((TextView) view.findViewById(R.id.txt_add_video)).setVisibility(View.GONE);
                            } else {
                                view.findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
                                //view.findViewById(R.id.txt_add_video).setVisibility(View.VISIBLE);
                            }

                            view.findViewById(R.id.followers).setClickable(true);

                            view.findViewById(R.id.following).setClickable(true);

                            //      pager.setVisibility(View.VISIBLE);

                            view.findViewById(R.id.txt_private_user).setVisibility(View.GONE);

                            //    tabLayout.setVisibility(View.VISIBLE);

                        } else {

                            //  pager.setVisibility(View.GONE);

                            view.findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);

                            //tabLayout.setVisibility(View.GONE);

                            view.findViewById(R.id.txt_about).setVisibility(View.GONE);

                            view.findViewById(R.id.followers).setClickable(false);

                            view.findViewById(R.id.following).setClickable(false);

                        }

                    } else {

                        //  Toast.makeText(getActivity(), "Public user", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    //                                                                                                            Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class UpdateStatusIconTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        private Context context;

        public UpdateStatusIconTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(this.context);
//            progressDialog.setMessage("Loading...");
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                RestClient client;

                entity.addPart("user_id", new StringBody(AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID)));

                entity.addPart("status", new StringBody(get_editTextString));

                client = new RestClient(Config.UPDATE_PROFILE);
                client.setMultipartEntity(entity);
                client.Execute(RestClient.RequestMethod.POST);
                return client.getResponse();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                super.onPostExecute(result);
                // progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("status")) {
                        Log.v("Log", result);
//                        JSONObject userDetail = object.getJSONArray("data").getJSONObject(0);
//                        profileUrl = userDetail.getString("image");
//                        updateProfileImage(profileUrl);
//                        Toast.makeText(context, "Profile pic updated.", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void status(String text) {
        try {
            ((TextView) view.findViewById(R.id.edit_enter_sataus)).setText(StringEscapeUtils.unescapeJava(text));
        } catch (Exception e) {
        }
    }

}
