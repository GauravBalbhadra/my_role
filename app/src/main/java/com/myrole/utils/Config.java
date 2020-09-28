package com.myrole.utils;

import android.net.Uri;

import com.myrole.vo.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vikesh on 04-05-2016.
 */
public class Config {

    public final static int SPLASH_TIME = 10;
    public static boolean isFragment = false;
    public  static int VIEW_TYPE ;
    public final static String EXTRABOLD = "EXTRABOLD";
    public final static String BOLD = "BOLD";
    public final static String REGULAR = "REGULAR";
    public final static String NEXA = "NEXA";
    public final static String QUICKSAND = "QUICKSAND";
    public final static String RALEWAY = "RALEWAY";

    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";


    public static final int REQUEST_VIDEO_TRIMMER = 0x01;
    public static final int CAMERAIMAGE = 123;
    public static final int GALLERYIMAGE = 124;
    public static final int CAMERAVIDEO = 125;
    public static final int GALLERYVIDEO = 126;
    public static final int CROPIMAGE = 127;
    public static final int ROTATEIMAGE = 128;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_ALL = 3;
    public static final int REQUEST_IMAGE_CROP = 10001;
    public static final int REQUEST_CAMERA = 1002;
    public static final int REQUEST_RECORD_VIDEO = 2000;
    public static final int REQUEST_VIDEO_GALLERY= 2002;
    public static final int FRAGEMNT_TO_EDITACTIVITY= 130;
    public static final String IMAGE_DIRECTORY_NAME = "MyRoleImages";
    /*Tabs*/
    public static final String HOME = "HOME";
    public static final String HOME_2 = "HOME_2";
    public static final String FEEDBACK = "FEEDBACK";
    public static final String FIND = "FIND";
    public static final String POST = "POST";
    public static final String ACTIVITY = "ACTIVITY";
    public static final String PROFILE = "PROFILE";
    public static final String OTHERPROFILE = "OTHERPROFILE";
    public static final String OTHERPROFILE_2 = "OTHERPROFILE_2";
    /*2Factor API start*/
    public final static String OTP_KEY = "edb9d4f0-11f6-11e6-9a14-00163ef91450";
    public final static String OTP_URL = "https://2factor.in/API/V1/";
    public static Uri CAMERAFILEURI;
    public static Uri CROPPEDIMAGEURI;
    public static boolean APP_IN_FRONT;
    public static String OTP_SESSION = "";
    public static String APPBARTITLE = "";
    public static JSONObject CONTACTS_JSON;
    public static JSONArray FACEBOOK_JSON;
    public static ArrayList<Category> categoryList = new ArrayList<Category>();
    public static ArrayList<Category> categoryList2 = new ArrayList<Category>();
    /*2Factor API end*/

    /*Web Services start*/
    public static String BASEURL = "http://172.105.59.69:3000/";
   // public static String BASEURL = "https://myrole-webservice.herokuapp.com/";
    public static String VERIFY_PHONE = BASEURL + "verify_phone_number";
    public static String VERIFY_USERNAME = BASEURL + "verify_username";
    public static String LOGIN = BASEURL + "login";
    public static String REGISTER = BASEURL + "register";
    public static String SUGGEST_USERNAME = BASEURL + "suggest_username";
    public static String GET_COUNTRY_LIST = BASEURL + "get_country_list";
    public static String UPDATE_PROFILE = BASEURL + "update";
    public static String MATCH_PHONE_CONTACT = BASEURL + "match_phone_number";
    public static String MATCH_FB_CONTACT = BASEURL + "match_email";
    public static String RESET_PASSWORD = BASEURL + "reset_password";
    public static String SEND_FOLLOW_REQUEST = BASEURL + "follow_request";
    public static String FOLLOW_USER = BASEURL + "follow_friend";
    public static String FOLLOW_REQUEST_ACTION = BASEURL + "follow_request_action";
    public static String UNFOLLOW_USER = BASEURL + "unfollow_friend";
    public static String GET_CATEGORIES = BASEURL + "get_category_list ";
    public static String GET_TODAY_ROLE = BASEURL + "get_myrole";
    public static String GET_USER = BASEURL + "get_user";
    public static String GET_ACTIVITY = BASEURL + "get_activities";
    public static String FAME_USER = BASEURL + "fame_users";
    public static String GET_FOLLOWER = BASEURL + "get_follower";
    public static String GET_FOLLOWER_REQUEST = BASEURL + "get_follower_request";
    public static String GET_LIKER = BASEURL + "get_liked_user";
    public static String ADD_POSTS = BASEURL + "posts";
    public static String ADD_COMMENT = BASEURL + "add_comment";
    public static String ADD_LIKE = BASEURL + "add_like";
    public static String GET_COMMENT = BASEURL + "get_comment";
    public static String GET_USER_POST = BASEURL + "get_user_post";
    public static String GET_MYROLE_POST = BASEURL + "get_myrole_post";
    public static String GET_comm_POST = BASEURL + "get_post_comment";
    public static String ADD_POST = BASEURL + "add_comment";
    public static String DELETE_COMMENT = BASEURL + "delete_comment";
    public static String DELETE_USER_POST = BASEURL + "delete_post";
    public static String REPORT_USER_POST = BASEURL + "report";
    public static String FEEDBACK_USER = BASEURL + "feedback";
    public static String GET_USER_POST_BY_CATEGORY = BASEURL + "get_user_post_by_category";
    public static String GET_USER_UPLOADED_POST = BASEURL + "get_user_uploaded_post";
    public static String GET_CATEGORY_POST = BASEURL + "get_category_post";
    public static String USER_SEARCH = BASEURL + "user_search";
    public static String POST_SEARCH = BASEURL + "post_search";
    public static String POST_DETAIL = BASEURL + "post_detail";
    public static String POST_BY_CAT = BASEURL + "get_category_by_post";
    public static String MATCH_FB_ID = BASEURL + "match_fbid";

    public static String STATUS_CATEGORY = BASEURL + "get_status_video_category";
    public static String GET_STATUS_VIDEOS = BASEURL + "get_status_video";
    public static String GET_DISCOVER_POST = BASEURL + "get_discover_post";

    /*Web Services end*/



}
