package com.myrole.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.utils.Config;

/**
 * Created by Deorwine-2 on 26-02-2018.
 */

public class MyRoleDeatilFragment extends BaseFragment {

    public TabLayout tabLayout;
    public ViewPager pager;
    public ViewPagerAdapter adapter;
    public Activity mActivity;
    public Context mContext;
    public String user_id;


    public static MyRoleDeatilFragment newInstance(String userID) {
        MyRoleDeatilFragment fragment = new MyRoleDeatilFragment();
        Bundle bndl = new Bundle();
//        bndl.putString("CATEGORYID", categoryID);
        bndl.putString("USERID", userID);
        fragment.setArguments(bndl);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.activity_myrole_deatil, container, false);

//        user_id=getIntent().getStringExtra("user_id");
        user_id = getArguments().getString("USERID");

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        view.findViewById(R.id.btn_back).setOnClickListener(this);

        return view;
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
*/
           return null;
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
