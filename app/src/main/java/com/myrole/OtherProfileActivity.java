package com.myrole;//package com.myrole;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
////import com.gs.instavideorecorder.FFmpegRecordActivity;
//import com.myrole.adapter.CountrySpinnerAdapter;
//import com.myrole.components.TabHost.MaterialTab;
//import com.myrole.fragments.FollowerFragment;
//import com.myrole.fragments.FollowingFragment;
//import com.myrole.fragments.NewUserGeneralPostFragment1;
//import com.myrole.fragments.OtherProfileFragment;
//import com.myrole.holders.TodayRoleDetailsDTO;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.utils.ProgressBarHandler;
//import com.myrole.utils.RestClient;
//import com.myrole.utils.Utils;
//import com.myrole.utils.VideoControllerView;
//import com.myrole.utils.VideoPlayer;
//import com.myrole.vo.Category;
//import com.myrole.vo.Contact;
//import com.myrole.vo.Country;
//
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.StringBody;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class OtherProfileActivity extends BaseActivity {
//
//    private static final String USER_ID = "USER_ID";
//    ArrayList<String> titleList;
//    int count = 3;
//    TabLayout tabLayout;
//    ViewPager pager;
//    ViewPagerAdapter adapter;
//    String userId;
//    String videoUrl = "";
//    String profileUrl = "";
//    private OtherProfileFragment.OnFragmentInteractionListener mListener;
//    private Activity activity;
//    private Context context;
//    private int selectedMediaType;
//    private String private_user, follow_request;
//    private List<Contact> followingList;
//    private boolean isSendRequest = false;
//    private boolean isFollow = false;
//    private RatingBar ratingBar;
//    private String imagePath = "", videoPath = "", videoIcon = "";
//    private Dialog m_dialog;
//  //  private View view;
//
//    private AppPreferences pref;
//    String user_id_pref="";
//
//
//    private ProgressBarHandler progressBarHandler;
//    private RelativeLayout lay_other_profile_fragment;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_profile);
//        context = this;
//        activity = this;
//        progressBarHandler = new ProgressBarHandler(activity);
//       // String userId = getIntent().getStringExtra("user_id");
//         userId = getIntent().getStringExtra("ownerId");
//
//        Toast.makeText(context,   "activity he", Toast.LENGTH_SHORT).show();
//        titleList = new ArrayList<>();
//        followingList = new ArrayList<>();
//        activity = this;
//        for (Category category : Config.categoryList) {
//            titleList.add(category.name);
//        }
//        pref = AppPreferences.getAppPreferences(activity);
//        user_id_pref = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID);
//
//
//        progressBarHandler = new ProgressBarHandler(activity);
//        lay_other_profile_fragment = (RelativeLayout) findViewById(R.id.lay_profile_fragment);
//
//        findViewById(R.id.lay_profile_icon).setOnClickListener(this);
//        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
//        applyFont();
//        private_user = "0";
//        follow_request = "0";
//        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
//
//        //userId = getArguments().getString(USER_ID);
//
//        new GetFollowersTask().execute(AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
//        new GetUserDetailTask().execute(userId);
//
//
//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//
//                new UpdateProfileRatingTask().execute(String.valueOf(rating));
//
//                pref = AppPreferences.getAppPreferences(activity);
//                pref.putBooleanValue(AppPreferences.allowRefreshgeneral, false);
//                pref.putStringValue(AppPreferences.category, "0");
//
//            }
//        });
//
//
////      view.findViewById(R.id.txt_private_user).setVisibility(View.GONE);
//        findViewById(R.id.btn_back).setOnClickListener(this);
//        findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
//        findViewById(R.id.txt_about).setOnClickListener(this);
//        findViewById(R.id.btn_edit).setOnClickListener(this);
//        findViewById(R.id.card_following).setOnClickListener(this);
//        findViewById(R.id.btn_follow).setOnClickListener(this);
//        tabLayout = (TabLayout)findViewById(R.id.tabs);
//        pager = (ViewPager) findViewById(R.id.pager);
//        findViewById(R.id.txt_add_video).setOnClickListener(this);
//
//        findViewById(R.id.edit_enter_sataus2).setVisibility(View.VISIBLE);
//
//        // init view pager
//        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//
//        adapter = new ViewPagerAdapter(fragmentManager);
//        pager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(pager);
//
////        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
////            @Override
////            public void onPageSelected(int position) {
////                // when user do a swipe the selected tab change
////                tabHost.setSelectedNavigationItem(position);
////
////            }
////        });
////
////        // insert all tabs from pagerAdapter data
////        for (int i = 0; i < adapter.getCount(); i++) {
////            tabHost.addTab(
////                    tabHost.newTab()
////                            .setText(adapter.getPageTitle(i))
////                            .setTabListener(OtherProfileActivity.this)
////            );
//
//
//            String username = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_NAME);
//            String userImagePath = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_IMAGE_URL);
////        Bitmap userImage = AppPreferences.getAppPreferences(getContext()).getImage(AppPreferences.USER_IMAGE);
//            ((TextView) findViewById(R.id.txt_username)).setText(username);
////
////
//
//
//            if (userImagePath != null) {
////            ((CircleImageView) view.findViewById(R.id.img_user_icon)).setImageBitmap(userImage);
////            ((ImageView) view.findViewById(R.id.img_user_bg)).setImageBitmap(userImage);
//                Glide.with(activity).load(profileUrl).asBitmap()
//                        .into(((CircleImageView) findViewById(R.id.img_user_icon)));
//                Glide.with(activity).load(profileUrl).asBitmap()
//                        .into(((ImageView) findViewById(R.id.img_user_bg)));
//            }
//
//            findViewById(R.id.followers).setOnClickListener(this);
//            findViewById(R.id.following).setOnClickListener(this);
//
//
//       // }
//    }
//
////    @Override
////    public void onTabSelected(MaterialTab tab) {
////        pager.setCurrentItem(tab.getPosition());
////    }
////
////    @Override
////    public void onTabReselected(MaterialTab tab) {
////
////    }
////
////    @Override
////    public void onTabUnselected(MaterialTab tab) {
////
////    }
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        view = inflater.inflate(R.layout.fragment_profile, container, false);
////        activity = getActivity();
////        progressBarHandler = new ProgressBarHandler(activity);
////        lay_other_profile_fragment = (RelativeLayout) view.findViewById(R.id.lay_profile_fragment);
////        view.findViewById(R.id.lay_profile_icon).setOnClickListener(this);
////        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
////        applyFont(view);
////        private_user = "0";
////        follow_request = "0";
////        view.findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
////        userId = getArguments().getString(USER_ID);
////
////        new OtherProfileFragment.GetFollowersTask().execute(AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID));
////
////        new OtherProfileFragment.GetUserDetailTask().execute(userId);
////
////        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
////
////            @Override
////            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
////
////                new OtherProfileFragment.UpdateProfileRatingTask().execute(String.valueOf(rating));
////
////                pref = AppPreferences.getAppPreferences(activity);
////                pref.putBooleanValue(AppPreferences.allowRefreshgeneral, false);
////                pref.putStringValue(AppPreferences.category, "0");
////
////            }
////        });
////
////
////
//////      view.findViewById(R.id.txt_private_user).setVisibility(View.GONE);
////        view.findViewById(R.id.btn_back).setOnClickListener(this);
////        view.findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
////        view.findViewById(R.id.txt_about).setOnClickListener(this);
////        view.findViewById(R.id.btn_edit).setOnClickListener(this);
////        view.findViewById(R.id.card_following).setOnClickListener(this);
////        view.findViewById(R.id.btn_follow).setOnClickListener(this);
////        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
////        pager = (ViewPager) view.findViewById(R.id.pager);
////        view.findViewById(R.id.txt_add_video).setOnClickListener(this);
////
////        view.findViewById(R.id.edit_enter_sataus2).setVisibility(View.VISIBLE);
////
////        // init view pager
////
////        adapter = new OtherProfileFragment.ViewPagerAdapter(activity.getSupportFragmentManager());
////        pager.setAdapter(adapter);
////        tabLayout.setupWithViewPager(pager);
////
////        /*pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
////            @Override
////            public void onPageSelected(int position) {
////                // when user do a swipe the selected tab change
////                tabHost.setSelectedNavigationItem(position);
////
////            }
////        });
////
////        // insert all tabs from pagerAdapter data
////        for (int i = 0; i < adapter.getCount(); i++) {
////            tabHost.addTab(
////                    tabHost.newTab()
////                            .setText(adapter.getPageTitle(i))
////                            .setTabListener(OtherProfileFragment.this)
////            );
////
////        }*/
////        /*view.findViewById(R.id.btn_follow).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                count++;
////                titleList.add("Tab " + count);
////                adapter.notifyDataSetChanged();
////                tabHost.addTab(tabHost.newTab()
////                        .setText(adapter.getPageTitle(titleList.size()-1))
////                        .setTabListener(ProfileFragment.this));
////                tabHost.notifyDataSetChanged();
////
////            }
////        });*/
////
////        String username = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_NAME);
////        String userImagePath = AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_IMAGE_URL);
//////        Bitmap userImage = AppPreferences.getAppPreferences(getContext()).getImage(AppPreferences.USER_IMAGE);
////        ((TextView) view.findViewById(R.id.txt_username)).setText(username);
//////
//////        if (userImage != null) {
//////            ((CircleImageView) view.findViewById(R.id.img_user_icon)).setImageBitmap(userImage);
//////            ((ImageView) view.findViewById(R.id.img_user_bg)).setImageBitmap(userImage);
//////        }
////
////
////        if (userImagePath != null) {
//////            ((CircleImageView) view.findViewById(R.id.img_user_icon)).setImageBitmap(userImage);
//////            ((ImageView) view.findViewById(R.id.img_user_bg)).setImageBitmap(userImage);
////            Glide.with(activity).load(profileUrl).asBitmap()
////                    .into(((CircleImageView) view.findViewById(R.id.img_user_icon)));
////            Glide.with(activity).load(profileUrl).asBitmap()
////                    .into(((ImageView) view.findViewById(R.id.img_user_bg)));
////        }
////
////        view.findViewById(R.id.followers).setOnClickListener(this);
////        view.findViewById(R.id.following).setOnClickListener(this);
////
////        return view;
////    }
//
////    @Override
////    public void setFragmentInteractionListener(BaseFragment.OnFragmentInteractionListener listener) {
////        super.setFragmentInteractionListener(listener);
////    }
//
//    @Override
//    public void onClick(View v) {
////        super.onClick(v);
//        switch (v.getId()) {
////            case R.id.rating_bar:
////                changeRating();
//            case R.id.btn_back:
//                onBackPressed();
//                onBackPressed();
//                //   Toast.makeText(activity, " use another image.", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.followers:
//                Intent i2 = new Intent(activity, FollowerActivity.class); // Your list's Intent
//                i2.putExtra("user_id", userId);
//                i2.setFlags(i2.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
//                startActivity(i2);
//              //  startActivity(new Intent(activity, FollowerActivity.class).putExtra("user_id", userId));
////                if (((HomeActivity) activity).fragmentManager != null) {
////                    ((HomeActivity) activity).fragmentManager.beginTransaction()
////                            .add(R.id.main_frame,
////                                    FollowerFragment.newInstance(userId),
////                                    "FOLLOWERFRAGMENT")
////                            .addToBackStack("FOLLOWERFRAGMENT")
////                            .commit();
////                }
//                break;
//
//            case R.id.following:
//                Intent i1 = new Intent(activity, FollowingActivity.class); // Your list's Intent
//                i1.putExtra("user_id", userId);
//                i1.setFlags(i1.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
//                startActivity(i1);
//              //  startActivity(new Intent(activity, FollowingActivity.class).putExtra("user_id", userId));
////                if (((HomeActivity) activity).fragmentManager != null) {
////                    ((HomeActivity) activity).fragmentManager.beginTransaction()
////                            .add(R.id.main_frame,
////                                    FollowingFragment.newInstance(userId),
////                                    "FOLLOWINGFRAGMENT")
////                            .addToBackStack("FOLLOWINGFRAGMENT")
////                            .commit();
////                }
//                break;
//
//            case R.id.btn_edit:
//                activity.startActivity(new Intent(activity, EditProfileActivity.class));
//                break;
//
//            case R.id.txt_add_video:
//
//                if (videoUrl != null && !videoUrl.isEmpty()) {
//                    try {
//
//                        Intent i = new Intent(activity, VideoActivity.class); // Your list's Intent
//                        i.putExtra("url", videoUrl);
//                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
//                        startActivity(i);
//
////
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//
//                    Utils.showAlertDialog(context, "Alert", "No video added !", true);
//
//                }
//                break;
//
//            case R.id.lay_profile_icon:
//
//                if (profileUrl != null && !profileUrl.isEmpty() && !profileUrl.equals("null")) {
//
//                    try {
//                        SurfaceView videoSurface;
//                        MediaPlayer player;
//                        VideoControllerView controller;
////                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
////                        View view = getLayoutInflater().inflate(R.layout.layout_video_view, null);
////                        alertDialogBuilder.setView(view);
////                        alertDialogBuilder.setCancelable(false);
////                        final AlertDialog alertDialog = alertDialogBuilder.create();
//
//                        m_dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
//                        m_dialog.setCancelable(false);
//                        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        m_dialog.setContentView(R.layout.layout_video_view);
//                        if (userId.equals(AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID))) {
//                            m_dialog.findViewById(R.id.btn_edit_profile).setVisibility(View.VISIBLE);
//                        } else {
//                            m_dialog.findViewById(R.id.btn_edit_profile).setVisibility(View.GONE);
//                        }
//                        m_dialog.findViewById(R.id.img_profile).setVisibility(View.VISIBLE);
//                        ((TextView) m_dialog.findViewById(R.id.text_title)).setText("Profile Image");
//                        RelativeLayout rl = (RelativeLayout) m_dialog.findViewById(R.id.videoSurfaceContainer);
//
//                        Glide.with(activity).load(profileUrl).asBitmap()
//                                .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
//
////                        rl.addView(imageView);
////
//
//                        m_dialog.findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
////                                videoPlayer.pause();
//                                m_dialog.dismiss();
//                            }
//                        });
//
//                        m_dialog.findViewById(R.id.btn_edit_profile).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                selectedMediaType = Config.MEDIA_TYPE_IMAGE;
//                                Utils.showMediaSelectionDialog((BaseActivity) activity, selectedMediaType, "0");
////                              m_dialog.dismiss();
//                            }
//                        });
//
//                        m_dialog.show();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    Utils.showAlertDialog(activity, "Alert", "No image added !", true);
//                }
//                break;
//
//            case R.id.btn_follow:
//
//                if (private_user.equals("1")) {
//                    new SendFollowRequestTask().execute();
//                    break;
//                } else {
//                    new FollowFriendTask().execute();
//                    break;
//                }
//
////                if (((TextView) view.findViewById(R.id.btn_follow)).getText().equals("Follow Request Sent")) {
////
////
////                }
//
//            case R.id.card_following:
//                new UnfollowFriendTask().execute();
//                break;
//
//            case R.id.txt_about:
//                activity.startActivity(new Intent(activity, AboutProfileActivity.class).putExtra("user_id", userId));
//                break;
//        }
//    }
//
////    @Override
////    public void onDetach() {
////        super.onDetach();
////        mListener = null;
////    }
//
//    private void applyFont() {
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_about), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_username), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.btn_edit), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_user_status), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_add_video), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_num_follower), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_num_following), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_num_post), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_follower), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_following), Config.NEXA, Config.BOLD);
//        Utils.setTypeface(context, (TextView) findViewById(R.id.txt_post), Config.NEXA, Config.BOLD);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            try {
//                if (requestCode == Config.CAMERAIMAGE) {
////                  Utils.performCrop(this, Config.CAMERAFILEURI);
////                    imagePath = data.getStringExtra(FFmpegRecordActivity.INTENT_NAME_IMAGE_PATH);
//////                    bitmap = BitmapFactory.decodeFile(imagePath);
//////                    imagePath = Utils.getRealPathFromURI(this, Config.CAMERAFILEURI);
////
////
//////                    UCrop.of(Config.CAMERAFILEURI, Uri.fromFile(new File(imagePath)))
//////                            .withAspectRatio(1, 1)
//////                            .withMaxResultSize(1024, 1024)
//////                            .start(this);
////
//////                   bitmap = Utils.decodeSampledBitmapFromResource(imagePath, 300, 300);
////                    profileUrl = imagePath;
//////                    ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
//////                    ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
////                    Glide.with(activity).load(profileUrl).asBitmap()
////                            .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
//
//                } else if (requestCode == Config.GALLERYIMAGE) {
//                    Uri selectedImageUri = data.getData();
//                    if (selectedImageUri != null) {
////                        Utils.performCrop(this, selectedImageUri);
//                        imagePath = Utils.getRealPathFromURI(activity, selectedImageUri);
//
////                        UCrop.of(Config.CAMERAFILEURI, Uri.fromFile(new File(imagePath)))
////                                .withAspectRatio(1, 1)
////                                .withMaxResultSize(1024, 1024)
////                                .start(this);
//
//                        profileUrl = imagePath;
////                        bitmap = Utils.decodeSampledBitmapFromResource(imagePath, 300, 300);
////                        ((ImageView)findViewById(R.id.img_add_pic)).setImageBitmap(bitmap);
////                        ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
////                        ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
//                        Glide.with(activity).load(profileUrl).asBitmap()
//                                .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
//                    }
//
//                } else if (requestCode == Config.CROPIMAGE) {
//                    /*Bundle extras = data.getExtras();
//                    bitmap = extras.getParcelable("data");
//*/
//                    if (Config.CROPPEDIMAGEURI != null) {
//                        imagePath = Utils.getRealPathFromURI(activity, Config.CROPPEDIMAGEURI);
//                        profileUrl = imagePath;
////                        bitmap = BitmapFactory.decodeFile(imagePath);
////                        ((ImageView) findViewById(R.id.img_add_pic)).setImageBitmap(bitmap);
////                        ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
////                        ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
//                        Glide.with(activity).load(profileUrl).asBitmap()
//                                .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
////                        ((TextView) findViewById(R.id.txt_add_pic)).setText("CHANGE");
//                    }
//                } else
////                if (requestCode == UCrop.REQUEST_CROP) {
////                    final Uri resultUri = UCrop.getOutput(data);
////                    imagePath = Utils.getRealPathFromURI(this, resultUri);
////                    profileUrl = imagePath;
////                    ((RelativeLayout) findViewById(R.id.lay_img_add_pic)).setVisibility(View.GONE);
////                    ((ImageView) findViewById(R.id.img_update_icon)).setVisibility(View.VISIBLE);
////                    Glide.with(context).load(imagePath).asBitmap()
////                            .centerCrop().into(((ImageView) findViewById(R.id.img_add_pic)));
//////                    bitmap = BitmapFactory.decodeFile(imagePath);
//////                    showPostDialog();
////
////                } else if (resultCode == UCrop.RESULT_ERROR) {
////                    final Throwable cropError = UCrop.getError(data);
////                }
//                    if (requestCode == Config.CAMERAVIDEO) {
//
////                            videoPath = Utils.getRealPathFromURI(this, selectedVideoUri);
//                    //    videoPath = data.getStringExtra(FFmpegRecordActivity.INTENT_NAME_VIDEO_PATH);
////                        videoUrl = videoPath;
////
//////                        videoIcon = videoPath.replace(".mp4",".jpg");
////
//////                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
//////                        ((ImageView) findViewById(R.id.vid_add_video)).setImageBitmap(bitmap);
//////                        ((RelativeLayout) findViewById(R.id.lay_vid_add)).setVisibility(View.GONE);
//////                        ((ImageView) findViewById(R.id.vid_icon_play)).setVisibility(View.VISIBLE);
//////                        ((ImageView) findViewById(R.id.vid_update_icon)).setVisibility(View.VISIBLE);
////                        Glide.with(activity).load(profileUrl).asBitmap()
////                                .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
//////                    ((TextView) findViewById(R.id.txt_add_video)).setText("CHANGE");
//
//                    } else if (requestCode == Config.GALLERYVIDEO) {
//                        Uri selectedVideoUri = data.getData();
//                        if (selectedVideoUri != null) {
//                            videoPath = Utils.getRealPathFromURI(activity, selectedVideoUri);
//                            videoUrl = videoPath;
////                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
////                        ((ImageView) findViewById(R.id.vid_add_video)).setImageBitmap(bitmap);
////                            ((RelativeLayout) findViewById(R.id.lay_vid_add)).setVisibility(View.GONE);
////                            ((ImageView) findViewById(R.id.vid_icon_play)).setVisibility(View.VISIBLE);
////                            ((ImageView) findViewById(R.id.vid_update_icon)).setVisibility(View.VISIBLE);
//                            Glide.with(activity).load(profileUrl).asBitmap()
//                                    .into(((ImageView) m_dialog.findViewById(R.id.img_profile)));
////                        ((TextView) findViewById(R.id.txt_add_video)).setText("CHANGE");
//                        }
//                    }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(activity, "Something went wrong, please try again or use another image.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(String command);
//    }
//
//    public static class FragmentText extends Fragment {
//        @Override
//        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.layout_fragment_hobby, container, false);
//            return view;
//        }
//    }
//
//    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
//
//        public ViewPagerAdapter(FragmentManager fm) {
//            super(fm);
//
//        }
//
////        public Fragment getItem(int num) {
////            return new FragmentText();
////        }
//
//        public Fragment getItem(int num) {
//
//            if (num > 0)
//                return NewUserGeneralPostFragment1.newInstance("", userId);
////                return NewUserCategoryPostFragment1.newInstance(Config.categoryList.get(num).id, userId);
//            return NewUserGeneralPostFragment1.newInstance("", userId);
//        }
//
//        @Override
//        public int getCount() {
//            return Config.categoryList.size();
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return Config.categoryList.get(position).name;
//        }
//
//    }
//
//    class GetUserDetailTask extends AsyncTask<String, Void, String> {
//
//        HashMap<String, String> postDataParams;
//
//        @Override
//        protected void onPreExecute() {
//
//            super.onPreExecute();
////            showProgessDialog();
//            lay_other_profile_fragment.setVisibility(View.GONE);
//            progressBarHandler.show();
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            postDataParams = new HashMap<String, String>();
//            postDataParams.put("user_id", params[0]);
//            return HTTPUrlConnection.getInstance().load(activity, Config.GET_USER, postDataParams);
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            dismissProgressDialog();
//            progressBarHandler.hide();
//            lay_other_profile_fragment.setVisibility(View.VISIBLE);
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//                    JSONObject userDetail = object.getJSONArray("data").getJSONObject(0);
//
//                    ((TextView) findViewById(R.id.txt_num_follower)).setText(userDetail.getString("followers"));
//                    ((TextView) findViewById(R.id.txt_num_following)).setText(userDetail.getString("following"));
//                    ((TextView) findViewById(R.id.txt_num_post)).setText(userDetail.getString("posts"));
//                    ((TextView) findViewById(R.id.edit_enter_sataus2)).setText(
//                            userDetail.isNull("status") ? "" : userDetail.getString("status"));
//                    ((TextView) findViewById(R.id.txt_username)).setText(
//                            userDetail.isNull("name") ? "" : userDetail.getString("name"));
//                    Glide.with(activity).load(userDetail.getString("image")).asBitmap()
//                            .placeholder(R.drawable.default_avatar).centerCrop().into(((CircleImageView) findViewById(R.id.img_user_icon)));
//                    Glide.with(activity).load(userDetail.getString("image")).asBitmap()
//                            .placeholder(R.drawable.default_avatar).centerCrop().into(((ImageView) findViewById(R.id.img_user_bg)));
//                    private_user = userDetail.getString("private_user");
//                    follow_request = userDetail.getString("follow_request");
//                    videoUrl = userDetail.getString("video");
//                    profileUrl = userDetail.getString("image");
//                    String rating = userDetail.getString("rating");
//
//                    if (follow_request.equals(AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID))) {
//
//                        isSendRequest = true;
////
////                        view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
////                        view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
////                        view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
////                        ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request Sent");
//
//                    }
//
//
////                    else {
////
////                        view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
////                        view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
////                        view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
////                        ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow");
////                    }
//
//                    if ((!rating.equals("null") && rating != null && !rating.equals(""))) {
//
//                        ratingBar.setRating(Float.parseFloat(rating));
//
//                    } else {
//
//                        rating = "0.0";
//                        ratingBar.setRating(Float.parseFloat(rating));
//
//                    }
//
//                  //  if (userId.equals(AppPreferences.getAppPreferences(context).getStringValue(AppPreferences.USER_ID))) {
//                    if (userId.equals(user_id_pref)) {
//
//                        findViewById(R.id.card_following).setVisibility(View.GONE);
//                        findViewById(R.id.btn_follow).setVisibility(View.GONE);
//                        findViewById(R.id.btn_edit).setVisibility(View.VISIBLE);
//                        ratingBar.setIsIndicator(true);
//                        ratingBar.setClickable(false);
//                        ratingBar.setFocusableInTouchMode(false);
//                        ratingBar.setFocusable(false);
//
//                    } else {
//
//                        if (private_user.equals("1")) {
//
//                            if (isFollow) {
//
//                                findViewById(R.id.card_following).setVisibility(View.VISIBLE);
//                                findViewById(R.id.btn_follow).setVisibility(View.INVISIBLE);
//                                findViewById(R.id.btn_edit).setVisibility(View.GONE);
//
//                            } else {
//                                findViewById(R.id.txt_about).setVisibility(View.GONE);
//
//                                findViewById(R.id.followers).setClickable(false);
//
//                                findViewById(R.id.following).setClickable(false);
//
//                                pager.setVisibility(View.GONE);
//
//                                findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);
//
//                                tabLayout.setVisibility(View.GONE);
//
//                                if (isSendRequest) {
//                                    findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                                    findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                                    findViewById(R.id.btn_edit).setVisibility(View.GONE);
//                                    ((TextView) findViewById(R.id.btn_follow)).setText("Follow Request Sent");
//
//                                } else {
//
//                                    findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                                    findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                                    findViewById(R.id.btn_edit).setVisibility(View.GONE);
//                                    ((TextView) findViewById(R.id.btn_follow)).setText("Follow Request");
//
//                                }
//
//                            }
//
//                        } else {
//
//                            findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
//
//                            findViewById(R.id.followers).setClickable(true);
//
//                            findViewById(R.id.following).setClickable(true);
//
//                            pager.setVisibility(View.VISIBLE);
//
//                            findViewById(R.id.txt_private_user).setVisibility(View.GONE);
//
//                            tabLayout.setVisibility(View.VISIBLE);
//
//                            if (isFollow) {
//
//                                findViewById(R.id.card_following).setVisibility(View.VISIBLE);
//                                findViewById(R.id.btn_follow).setVisibility(View.INVISIBLE);
//                                findViewById(R.id.btn_edit).setVisibility(View.GONE);
//
//                            } else {
//
//                                if (isSendRequest) {
//
//                                    findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                                    findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                                    findViewById(R.id.btn_edit).setVisibility(View.GONE);
//                                    ((TextView) findViewById(R.id.btn_follow)).setText("Follow Request Sent");
//
//                                } else {
//
//                                    findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                                    findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                                    findViewById(R.id.btn_edit).setVisibility(View.GONE);
//                                    ((TextView) findViewById(R.id.btn_follow)).setText("Follow");
//
//                                }
//
//                            }
//
//                        }
//
//                    }
//
//
////                    if (private_user.equals("1")) {
////                        if (isSendRequest) {
////
//////                            view.findViewById(R.id.card_following).setVisibility(View.VISIBLE);
////
////                            view.findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
////
////                            view.findViewById(R.id.followers).setClickable(true);
////
////                            view.findViewById(R.id.following).setClickable(true);
////
////                            pager.setVisibility(View.VISIBLE);
////
////                            view.findViewById(R.id.txt_private_user).setVisibility(View.GONE);
////
////                            tabLayout.setVisibility(View.VISIBLE);
////                        } else {
////
////                            if (follow_request.equals(AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID))) {
////                                view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
////                                view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
////                                ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow Request Sent");
////                            } else {
////
////                                view.findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
////                                view.findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
////                                view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
////                                ((TextView) view.findViewById(R.id.btn_follow)).setText("Follow");
////                            }
////
////                            view.findViewById(R.id.txt_about).setVisibility(View.GONE);
////
////                            view.findViewById(R.id.followers).setClickable(false);
////
////                            view.findViewById(R.id.following).setClickable(false);
////
////                            pager.setVisibility(View.GONE);
////
////                            view.findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);
////
////                            tabLayout.setVisibility(View.GONE);
////                        }
////
////
////                    } else {
////
////
////                        Toast.makeText(getContext(), "Public user", Toast.LENGTH_SHORT).show();
////                    }
//
//                } else {
//                    Toast.makeText(activity, object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    class GetFollowersTask extends AsyncTask<String, Void, String> {
//
//        HashMap<String, String> postDataParams;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            showProgessDialog();
//            progressBarHandler.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            postDataParams = new HashMap<String, String>();
//            postDataParams.put("user_id", params[0]);
//            postDataParams.put("type_by", "following");
//
//            return HTTPUrlConnection.getInstance().load(activity, Config.GET_FOLLOWER, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            dismissProgressDialog();
//            progressBarHandler.hide();
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//
//                    JSONArray userArray = object.getJSONArray("data");
////                    if (userArray.length() > 0) {
////                        getView().findViewById(R.id.no_following).setVisibility(View.GONE);
////                    }
//                    for (int i = 0; i < userArray.length(); i++) {
//                        Contact contact = new Contact();
//                        contact.id = userArray.getJSONObject(i).getString("id");
//                        contact.name = userArray.getJSONObject(i).getString("name");
//                        contact.image = userArray.getJSONObject(i).getString("image");
//                        contact.follow = userArray.getJSONObject(i).getString("follow");
//                        followingList.add(contact);
//                    }
////                    followingAdapter.notifyDataSetChanged();
//
//                    for (int i = 0; i < followingList.size(); i++) {
//                        if (followingList.get(i).id.equals(userId)) {
//                            isFollow = true;
////                            view.findViewById(R.id.card_following).setVisibility(View.VISIBLE);
////                            view.findViewById(R.id.btn_follow).setVisibility(View.INVISIBLE);
////                            view.findViewById(R.id.btn_edit).setVisibility(View.GONE);
//                        }
//                    }
//
//                } else {
//                    //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//
//                e.printStackTrace();
//
//            }
//        }
//    }
//
//    class SendFollowRequestTask extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
////        int pos;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////           showProgessDialog();
//            progressBarHandler.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
////          pos = Integer.parseInt(params[0]);
//            postDataParams = new HashMap<String, String>();
//            postDataParams.put("user_id", userId);
//            postDataParams.put("follower_id", AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID));
//            return HTTPUrlConnection.getInstance().load(activity, Config.SEND_FOLLOW_REQUEST, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            dismissProgressDialog();
//            progressBarHandler.hide();
//
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
////                  liker_list.get(pos).follows = "1";
//
//                    isSendRequest = true;
//                    findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                    findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                    ((TextView) findViewById(R.id.btn_follow)).setText("Follow Request Sent");
//                    adapter.notifyDataSetChanged();
//
////                    if (private_user.equals("1")){
////                        if (isFollow){
////
////                            pager.setVisibility(View.VISIBLE);
////
////                            getView().findViewById(R.id.txt_private_user).setVisibility(View.GONE);
////
////                            tabLayout.setVisibility(View.VISIBLE);
////                        }else {
////                            pager.setVisibility(View.GONE);
////
////                            getView().findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);
////
////                            tabLayout.setVisibility(View.GONE);
////                        }
////
////                    }else {
////                        Toast.makeText(getContext(), "Public user", Toast.LENGTH_SHORT).show();
////                    }
//
//                } else {
//                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    class UnfollowFriendTask extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
////        int pos;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            showProgessDialog();
//            progressBarHandler.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
////            pos = Integer.parseInt(params[0]);
//            postDataParams = new HashMap<String, String>();
//            postDataParams.put("user_id", userId);
//            postDataParams.put("follower_id", AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID));
//            return HTTPUrlConnection.getInstance().load(activity, Config.UNFOLLOW_USER, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            dismissProgressDialog();
//            progressBarHandler.hide();
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//                    adapter.notifyDataSetChanged();
//                    isSendRequest = false;
//                    isFollow = false;
//                    findViewById(R.id.card_following).setVisibility(View.INVISIBLE);
//                    findViewById(R.id.btn_follow).setVisibility(View.VISIBLE);
//                    if (private_user.equals("1")) {
//                        if (isSendRequest) {
//
//                            findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
//
//                            findViewById(R.id.followers).setClickable(true);
//
//                            findViewById(R.id.following).setClickable(true);
//
//                            pager.setVisibility(View.VISIBLE);
//
//                            findViewById(R.id.txt_private_user).setVisibility(View.GONE);
//
//                            tabLayout.setVisibility(View.VISIBLE);
//
//                        } else {
//
//                            pager.setVisibility(View.GONE);
//
//                            findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);
//
//                            tabLayout.setVisibility(View.GONE);
//
//                            findViewById(R.id.txt_about).setVisibility(View.GONE);
//
//                            findViewById(R.id.followers).setClickable(false);
//
//                            findViewById(R.id.following).setClickable(false);
//
//                        }
//
//                    } else {
//
//                        Toast.makeText(context, "Public user", Toast.LENGTH_SHORT).show();
//
//                    }
//                } else {
//
//                    Toast.makeText(activity, object.getString("message"), Toast.LENGTH_LONG).show();
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    class UpdateProfileRatingTask extends AsyncTask<String, Void, String> {
//
//        String response = "";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            showProgessDialog();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            try {
//
//                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//                RestClient client;
//
//                entity.addPart("user_id", new StringBody(userId));
//                entity.addPart("rating", new StringBody(params[0]));
//
//                client = new RestClient(Config.UPDATE_PROFILE);
//                client.setMultipartEntity(entity);
//                client.Execute(RestClient.RequestMethod.POST);
//                response = client.getResponse();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return response;
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
////            dismissProgressDialog();
//            try {
//
//                JSONObject object = new JSONObject(result);
//
//                if (object.getBoolean("status")) {
//
//                    Log.v("Log", result);
//
//                    ratingBar.setRating(Float.parseFloat(object.getString("rating")));
//                    ;
//
//
//                } else {
//
//                    Toast.makeText(activity, object.getString("message"), Toast.LENGTH_LONG).show();
//
//                }
//
//            } catch (JSONException e) {
//
//                e.printStackTrace();
//
//            }
//
//        }
//    }
//
//    class FollowFriendTask extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
////        int pos;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            showProgessDialog();
//            progressBarHandler.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
////            pos = Integer.parseInt(params[0]);
//            postDataParams = new HashMap<String, String>();
//            postDataParams.put("user_id", userId);
//            postDataParams.put("follower_id", AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID));
//            return HTTPUrlConnection.getInstance().load(activity, Config.FOLLOW_USER, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            dismissProgressDialog();
//            progressBarHandler.hide();
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//                    adapter.notifyDataSetChanged();
//                    isSendRequest = false;
//                    findViewById(R.id.card_following).setVisibility(View.VISIBLE);
//                    findViewById(R.id.btn_follow).setVisibility(View.INVISIBLE);
//                    if (private_user.equals("1")) {
//                        if (isSendRequest) {
//
//                            findViewById(R.id.txt_about).setVisibility(View.VISIBLE);
//
//                            findViewById(R.id.followers).setClickable(true);
//
//                            findViewById(R.id.following).setClickable(true);
//
//                            pager.setVisibility(View.VISIBLE);
//
//                            findViewById(R.id.txt_private_user).setVisibility(View.GONE);
//
//                            tabLayout.setVisibility(View.VISIBLE);
//
//                        } else {
//
//                            pager.setVisibility(View.GONE);
//
//                            findViewById(R.id.txt_private_user).setVisibility(View.VISIBLE);
//
//                            tabLayout.setVisibility(View.GONE);
//
//                            findViewById(R.id.txt_about).setVisibility(View.GONE);
//
//                            findViewById(R.id.followers).setClickable(false);
//
//                            findViewById(R.id.following).setClickable(false);
//
//                        }
//
//                    } else {
//
//                        Toast.makeText(context, "Public user", Toast.LENGTH_SHORT).show();
//
//                    }
//                } else {
//
//                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
