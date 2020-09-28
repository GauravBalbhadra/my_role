package com.myrole;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.tabs.TabLayout;
import com.myrole.utils.Config;

/**
 * Created by Deorwine-2 on 26-02-2018.
 */

public class MyRoleDeatil extends BaseActivity {

    public TabLayout tabLayout;
    public ViewPager pager;
    public ViewPagerAdapter adapter;
    public Activity mActivity;
    public Context mContext;
    public String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrole_deatil);
        mContext=this;
        mActivity=this;
        user_id=getIntent().getStringExtra("user_id");

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //end
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        findViewById(R.id.btn_back).setOnClickListener(this);

//        String user_id = AppPreferences.getAppPreferences(mActivity).getStringValue(AppPreferences.USER_ID);

    }

    public void callServiceForGeneralFeed() {
        AndroidNetworking.post(Config.GET_USER_UPLOADED_POST)
                .setTag(this)
//                .addBodyParameter("user_id", getArguments().getString("USERID"))
//                .addBodyParameter("category_id", getArguments().getString("CATEGORYID"))
//                .addBodyParameter("width", String.valueOf(width))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " category_id : " +"" );
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                    }

                    @Override
                    public void onError(ANError error) {
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


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int num) {
           /* if (num > 0) {
                return MyRoleDeatilPostFragment1.newInstance(Config.categoryList.get(num).id, user_id);
            }
            return MyRoleDeatilPostFragment.newInstance(Config.categoryList.get(num).id, user_id);
*///
           return  null;
        }

        @Override
        public int getCount() {
            //for avoding bullet category to size -1
            return Config.categoryList.size() - 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Config.categoryList.get(position).name;
        }
    }

}
