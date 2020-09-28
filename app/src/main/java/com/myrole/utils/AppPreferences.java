package com.myrole.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

//import org.bytedeco.librealsense.context;

import java.io.ByteArrayOutputStream;

//import autovalue.shaded.org.checkerframework$.framework.qual.$DefaultFor;

public class AppPreferences {

    public static final String API_KEY = "APIKEY";
    public static final String USER_ID = "USERID";
    public static final String ROLE_ID = "ROLE_ID";
    public static final String DEVICE_TOKEN_ID = "DEVICETOKENID";
    public static final String USER_NAME = "USERNAME";
    public static final String U_NAME = "U_NAME";
    public static final String USER_PHONE = "USERPHONE";
    public static final String USER_IMAGE = "USERIMAGE";
    public static final String USER_IMAGE_URL = "USERIMAGEURL";
    public static final String USER_PROFILE_INCOMPLETE = "USERPROFILEINCOMPLETE";
    private static final String SHARED_PREFERENCE_NAME = "MYROLE";
    private static final String USER_BADGES_COUNT = "BADGES_COUNT";

    public static final String allowRefreshgeneral = "allowRefreshgeneral";
    public static final String allowRefreshfollower = "allowRefreshfollower";

    public static final String category = "category";

    public static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";
    public static final String LOGIN_PHONE_NO = "LOGIN_PHONE_NO";

    public static final String TODAY_ROLE_STATUS = "TODAY_ROLE_STATUS";
    public static final String TODAY_POSTDEATAIL_STATUS = "TODAY_POSTDEATAIL_STATUS";

    public static final String FOLLOW_NOTIFICATION_CATEGORY = "FOLLOW_NOTIFICATION_CATEGORY";
    public static final String FOLLOW_NOTIFICATION_CATEGORY_ID = "FOLLOW_NOTIFICATION_CATEGORY_ID";

    public static final String NOTIFICATION_CATEGORY = "NOTIFICATION_CATEGORY";
    public static final String NOTIFICATION_CATEGORY_ID = "NOTIFICATION_CATEGORY_ID";
    public static final String NOTIFICATION_POST_ID = "NOTIFICATION_POST_ID";

    public static final String REMINDER_DATE = "REMINDER_DATE";


    public static final String CHECK_TAB_OPEN = "CHECK_TAB_OPEN";




    public static final String VIDEO_URI_IMMEDIATE = "VIDEO_URI_IMMEDIATE";








    private static AppPreferences appPreferences;
    private SharedPreferences mPrefs;


    private AppPreferences(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isLogin(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return mPrefs.getString(USER_ID, null) != null;
    }

    public void clear(){
        mPrefs.edit().clear().apply();
    }

    public static AppPreferences getAppPreferences(Context context) {
        if (appPreferences == null)
            appPreferences = new AppPreferences(context);
        return appPreferences;

    }

    public String getStringValue(String Key) {
        return mPrefs.getString(Key, "");
    }

    public void putStringValue(String Key, String value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(Key, value);
        editor.commit();
    }

    public void putImage(String key, Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, encodedImage);
        editor.commit();
    }

    public Bitmap getImage(String key) {
        Bitmap bitmap = null;
        String previouslyEncodedImage = mPrefs.getString(key, "");

        if (!previouslyEncodedImage.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }

    public int getIntValue(String key) {
        return mPrefs.getInt(key, 0);
    }

    public void putIntValue(String key, int value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public boolean getBooleanValue(String key) {

        return mPrefs.getBoolean(key, false);
    }

    public void putBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void removeFromPreferences(String key) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove(key);
        editor.commit();
    }

    public void saveBadgeCount(int count) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(USER_BADGES_COUNT,count);
        editor.commit();
    }

    public int fetchBadgeCount() {
        return mPrefs.getInt(USER_BADGES_COUNT, 0);
    }
}
