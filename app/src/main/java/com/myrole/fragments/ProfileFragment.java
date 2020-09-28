package com.myrole.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.myrole.AboutProfileActivity;
import com.myrole.BaseFragment;
import com.myrole.EditProfileActivity;
import com.myrole.OptionsActivity;
import com.myrole.R;
import com.myrole.camera_ck.crop.CropImage;
import com.myrole.components.TabHost.MaterialTab;
import com.myrole.components.TabHost.MaterialTabListener;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.utils.RestClient;
import com.myrole.utils.Utils;
import com.myrole.widget.FetchInterfaceAlert;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.myrole.dashboard.MainDashboardActivity.transferUtility;
import static com.myrole.utils.ActivityTransactions.getDashboard;
import static com.myrole.utils.ActivityTransactions.getLogIn;
import static com.myrole.utils.AppPreferences.isLogin;

public class ProfileFragment extends BaseFragment implements MaterialTabListener, FetchInterfaceAlert {

    boolean allowRefreshfollower = true;
    private AppPreferences pref;


    final static String TAG = "MYROLE";
    TabLayout tabLayout;
    String videoUrl = "", video_thumb;
    String profileUrl = "";
    private String userId;
    private OnFragmentInteractionListener mListener;
    private int selectedMediaType;
    private Context context;
    private Activity activity;
    private String imagePath = "";
    private Dialog m_dialog;
    private String rating;
    private RatingBar ratingBar;
    private ProgressBarHandler progressBarHandler;
    private RelativeLayout lay_profile_fragment, relativeLayoutIsLogin;
    private View view;
    String get_editTextString = "";
    //SwipeRefreshLayout swipe_refresh;
    int REQUEST_CODE = 345;
    int width;

    public Button btnRemindLater;
    public Button btnLogin;

    private String uniqueId, img_name;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Utils.isFromProfile = true;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        this.context = getContext();
        Utils.isFromProfile = true;
        pref = AppPreferences.getAppPreferences(getActivity());
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
    }

    @Override
    public void onTabSelected(MaterialTab tab) {

        //pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utils.isFromProfile = true;
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        applyFont(view);

        progressBarHandler = new ProgressBarHandler(activity);
        lay_profile_fragment = (RelativeLayout) view.findViewById(R.id.lay_profile_fragment);
        relativeLayoutIsLogin = (RelativeLayout) view.findViewById(R.id.relativeLayoutIsLogin);
        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        btnRemindLater = view.findViewById(R.id.btnRemindLater);
        btnLogin = view.findViewById(R.id.btnLogin);
        ratingBar.setFocusable(false);
        ratingBar.setClickable(false);
        ratingBar.setFocusableInTouchMode(false);
        ratingBar.setIsIndicator(true);
        view.findViewById(R.id.btn_edit).setOnClickListener(this);
        view.findViewById(R.id.lay_profile_icon).setOnClickListener(this);
        view.findViewById(R.id.txt_about).setOnClickListener(this);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        //pager = (ViewPager) view.findViewById(R.id.pager);
        view.findViewById(R.id.txt_add_video).setOnClickListener(this);
        view.findViewById(R.id.txt_update_status).setOnClickListener(this);

        view.findViewById(R.id.txt_update_status).setVisibility(View.VISIBLE);
        view.findViewById(R.id.edit_enter_sataus).setVisibility(View.VISIBLE);
        view.findViewById(R.id.txt_user_status).setVisibility(View.VISIBLE);

        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
        ((TextView) view.findViewById(R.id.txt_update_status)).setTypeface(face);

        String username = AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_NAME);
        Bitmap userImage = AppPreferences.getAppPreferences(getContext()).getImage(AppPreferences.USER_IMAGE);
        ((TextView) view.findViewById(R.id.txt_username)).setText(username);

        if (userImage != null) {
            ((CircleImageView) view.findViewById(R.id.img_user_icon)).setImageBitmap(userImage);
            ((ImageView) view.findViewById(R.id.img_user_bg)).setImageBitmap(userImage);
        }
        view.findViewById(R.id.followers).setOnClickListener(this);
        view.findViewById(R.id.following).setOnClickListener(this);

        userId = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        if (userId.equals("3")) {
            ((LinearLayout) view.findViewById(R.id.line_foll_post_tab)).setVisibility(View.GONE);
            ((RelativeLayout) view.findViewById(R.id.rel_button_view)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.txt_about)).setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogIn(getContext());
                getActivity().finish();
            }
        });

        btnRemindLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDashboard(getContext());
                getActivity().finish();
            }
        });
        //swipe_refresh = view.findViewById(R.id.swipe_refresh);
        //swipe_refresh.setOnRefreshListener(this);

        if (Utils.isNetworkConnected(getActivity(), true)) {
            if (isLogin(getContext())){
                lay_profile_fragment.setVisibility(View.VISIBLE);
                relativeLayoutIsLogin.setVisibility(View.GONE);
                new GetUserDetailTask().execute(userId);
            } else {
                lay_profile_fragment.setVisibility(View.GONE);
                relativeLayoutIsLogin.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utils.isNetworkConnected(getActivity(), true)) {
                new GetUserDetailTask().execute(userId);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mContext.registerReceiver(receiver, new IntentFilter("REFRESH_PROFILE"));
    }

    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.followers:
                //    getActivity().startActivity(new Intent(getActivity(), FollowerActivity.class).putExtra("user_id", userId));
                if (((MainDashboardActivity) getActivity()).fragmentManager != null) {
                    ((MainDashboardActivity) getActivity()).fragmentManager.beginTransaction()
                            .add(R.id.container,
                                    FollowerFragment.newInstance(userId),   //not changed
                                    "FOLLOWERFRAGMENT")
                            .addToBackStack("FOLLOWER_123")
                            .commit();
//                    ((HomeActivity) context).fragmentManager.popBackStack();
                }
                break;

            case R.id.following:
                //  getActivity().startActivity(new Intent(getActivity(), FollowingActivity.class).putExtra("user_id", userId));
                if (((MainDashboardActivity) getActivity()).fragmentManager != null) {
                    ((MainDashboardActivity) getActivity()).fragmentManager.beginTransaction()
                            .add(R.id.container,
                                    FollowingFragment.newInstance(userId),
                                    "FOLLOWINGFRAGMENT")
                            .addToBackStack("FOLLOWING_123")
                            .commit();
                }
                break;

            case R.id.btn_edit:
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("fromProfile", true);
                startActivityForResult(intent, 4002);
                break;

            case R.id.txt_update_status:
                FragmentManager fm = getFragmentManager();
                AlertStatusDilogFragment dialogFragment = new AlertStatusDilogFragment();
                dialogFragment.setTargetFragment(this, REQUEST_CODE);
                dialogFragment.show(fm, "Alert Status Dialog Fragment");
                break;

            case R.id.txt_add_video:
                Intent i = new Intent(getActivity(), OptionsActivity.class);
                startActivity(i);
               /* if (videoUrl != null && !videoUrl.isEmpty() && !videoUrl.equals("null")) {
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
                break;
            case R.id.lay_profile_icon:
                showIcon(context, profileUrl);
                break;

            case R.id.txt_about:
                getActivity().startActivity(new Intent(getActivity(), AboutProfileActivity.class).putExtra("user_id", userId));
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        // Toro.register(mRecyclerView);
        pref = AppPreferences.getAppPreferences(getActivity());
        allowRefreshfollower = AppPreferences.getAppPreferences(getContext()).getBooleanValue(AppPreferences.allowRefreshfollower);

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
        Utils.setTypeface(getContext(), (TextView) view.findViewById(R.id.edit_enter_sataus), Config.NEXA, Config.BOLD);

    }


    //    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProfileFragment fragment = new ProfileFragment();

        if (m_dialog != null) {
            m_dialog.dismiss();
        }
        if (requestCode == REQUEST_CODE) {
            //  String editTextString = data.getStringExtra("yo");
            get_editTextString = data.getStringExtra("yo");
            try {
                ((TextView) view.findViewById(R.id.edit_enter_sataus)).setText(StringEscapeUtils.unescapeJava(get_editTextString));
            } catch (Exception e) {
            }
            new UpdateStatusIconTask(context).execute();
            // Toast.makeText(getActivity(),editTextString,Toast.LENGTH_LONG).show();
        }
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri selectedImageUri = result.getUri();
                    // uri = selectedImageUri;
                    //  Toast.makeText(this, "yoynono", Toast.LENGTH_LONG).show();
                    imagePath = Utils.getRealPathFromURI(getActivity(), selectedImageUri);
                    upload(imagePath);

                } else if (requestCode == CropImage.CROP_IMAGE_FRAGMENT_REQUEST_CODE) {

                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri selectedImageUri = result.getUri();

                    imagePath = Utils.getRealPathFromURI(getActivity(), selectedImageUri);
                    upload(imagePath);

                } else if (requestCode == Config.GALLERYIMAGE) {
                    Toast.makeText(context, "profile frag", Toast.LENGTH_LONG).show();
                    CropImage.activity(data.getData())
                            .setFixAspectRatio(true)
                            .setDeleteImage(false)
                            .start2(getActivity());
                } else if (requestCode == Config.CROPIMAGE) {
                    if (Config.CROPPEDIMAGEURI != null) {
                        imagePath = Utils.getRealPathFromURI(context, Config.CROPPEDIMAGEURI);
                        profileUrl = imagePath;
                        upload(imagePath);

                    }
                } else if (requestCode == 4002) {
                    new GetUserDetailTask().execute(userId);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Something went wrong, please try again or use another image.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void showIcon(Context context, String profileUrl) {
        if (profileUrl == null && profileUrl.isEmpty() && profileUrl.equals("null")) {
            profileUrl = "";
        }

        try {
            m_dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            m_dialog.setCancelable(false);
            m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            m_dialog.setContentView(R.layout.layout_video_view);
            m_dialog.findViewById(R.id.img_profile).setVisibility(View.VISIBLE);
            m_dialog.findViewById(R.id.btn_edit_profile).setVisibility(View.VISIBLE);
            ((TextView) m_dialog.findViewById(R.id.text_title)).setText("Profile Image");
            RelativeLayout rl = (RelativeLayout) m_dialog.findViewById(R.id.videoSurfaceContainer);

            Glide.with(getActivity()).asBitmap().load(profileUrl)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));

            m_dialog.findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_dialog.dismiss();
                }
            });

            m_dialog.findViewById(R.id.btn_edit_profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    captureImage();
                }
            });

            m_dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureImage() {
        Config.isFragment = true;
        selectedMediaType = Config.MEDIA_TYPE_IMAGE;
        Utils.showMediaSelectionDialog((MainDashboardActivity) activity, selectedMediaType, "0");
    }

    private void updateProfileImage(String profileUrl) {
        pref.putStringValue(AppPreferences.USER_IMAGE_URL, profileUrl);
        Glide.with(this).asBitmap().load(profileUrl)
                .centerCrop().placeholder(R.drawable.default_avatar).into(((CircleImageView) getActivity().findViewById(R.id.img_user)));
        if (m_dialog != null) {
            Glide.with(getActivity()).asBitmap().load(profileUrl)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
        }
        if (this.view != null) {
            Glide.with(getActivity()).asBitmap().load(profileUrl)
                    .placeholder(R.drawable.default_avatar).centerCrop().into(((CircleImageView) view.findViewById(R.id.img_user_icon)));
            Glide.with(getActivity()).asBitmap().load(profileUrl)
                    .placeholder(R.drawable.default_avatar).centerCrop().into(((ImageView) view.findViewById(R.id.img_user_bg)));
        }
    }

    public void upload(String img_path) {
        uniqueId = UUID.randomUUID().toString();
        File file = new File(img_path);
        img_name = uniqueId + ".jpg";
        TransferObserver transferObserver = transferUtility.upload(
                "myrole-app-data/images",     /* The bucket to upload to */
                img_name,       /* The key for the uploaded object */
                file       /* The file where the data to upload exists */
        );
//        transferObserverListener(transferObserver);

        transferObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (String.valueOf(state).equals("IN_PROGRESS")) {
                } else if (String.valueOf(state).equals("COMPLETED")) {
                    new UpdateProfileIconTask(context).execute(img_name);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            }

            @Override
            public void onError(int id, Exception ex) {
            }
        });
    }

  /*  @Override
    public void onRefresh() {
        if (Utils.isNetworkConnected(getActivity(), true)) {
            new GetUserDetailTask().execute(userId);
        }
        hide();
    }
    private void hide(){
        swipe_refresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(false);
            }
        },500);
    }*/

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }

    class GetUserDetailTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            lay_profile_fragment.setVisibility(View.GONE);
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
//            dismissProgressDialog();
            progressBarHandler.hide();
            lay_profile_fragment.setVisibility(View.VISIBLE);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {

                    JSONObject userDetail = object.getJSONArray("data").getJSONObject(0);
                    try {
                        if (!userDetail.isNull("name")) {
                            pref.putStringValue(AppPreferences.USER_NAME, userDetail.getString("name"));
                            ((TextView) getActivity().findViewById(R.id.txt_username_nav)).setText(userDetail.getString("name"));
                        }
                    } catch (Exception e) {
                    }

                    ((TextView) view.findViewById(R.id.txt_num_follower)).setText(userDetail.getString("followers"));
                    ((TextView) view.findViewById(R.id.txt_num_following)).setText(userDetail.getString("following"));
                    ((TextView) view.findViewById(R.id.txt_num_post)).setText(userDetail.getString("posts"));
                    try {
                        ((TextView) view.findViewById(R.id.edit_enter_sataus)).setText(StringEscapeUtils.unescapeJava(
                                userDetail.isNull("status") ? "user status" : userDetail.getString("status")));
                    } catch (Exception e) {
                    }

                    ((TextView) view.findViewById(R.id.txt_username)).setText(userDetail.isNull("name") ? "" : userDetail.getString("name"));
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

                    videoUrl = userDetail.getString("video");
                    if (!userDetail.isNull("video_thumb")) {
                        video_thumb = userDetail.getString("video_thumb");
                    }
                    profileUrl = userDetail.getString("image");
                    pref.putStringValue(AppPreferences.USER_IMAGE_URL, profileUrl);
                    try {
                        Glide.with(getActivity()).asBitmap().load(userDetail.getString("image")).error(R.drawable.default_avatar)
                                .placeholder(R.drawable.default_avatar).centerCrop().into(((CircleImageView) getActivity().findViewById(R.id.img_user)));
                    } catch (Exception e) {
                    }
                    rating = userDetail.getString("rating");

                    if ((!rating.equals("null") && rating != null && !rating.equals(""))) {

                        ratingBar.setRating(Float.parseFloat(rating));

                    } else {
                        rating = "0.0";
                        ratingBar.setRating(Float.parseFloat(rating));

                    }

                } else {
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class UpdateProfileIconTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        private Context context;

        public UpdateProfileIconTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(this.context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                RestClient client;
                entity.addPart("user_id", new StringBody(AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID)));
                entity.addPart("image", new StringBody(params[0]));
//                entity.addPart("image", new FileBody(new File(params[0])));

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
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("status")) {
                        Log.v("Log", result);
                        JSONObject userDetail = object.getJSONArray("data").getJSONObject(0);
                        profileUrl = userDetail.getString("image");
                        updateProfileImage(profileUrl);
                        Toast.makeText(context, "Profile pic updated.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class UpdateStatusTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarHandler.show();

        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", userId);
            postDataParams.put("status", params[0]);
            return HTTPUrlConnection.getInstance().load(context, Config.UPDATE_PROFILE, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            progressBar.setVisibility(View.GONE);
            progressBarHandler.hide();

            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONObject userDetail = object.getJSONArray("data").getJSONObject(0);


                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
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
        ((TextView) view.findViewById(R.id.edit_enter_sataus)).setText(text);
    }
}
