package com.myrole.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.vo.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pws on 11/4/2016.
 */

public class NewActivityFragment extends BaseFragment {

    ArrayList<Category> categoryList;
    TabLayout tabLayout;
    ViewPager pager;
    TabLayout.Tab tab1;
    RelativeLayout mainContainer;

    public static NewActivityFragment newInstance() {

        NewActivityFragment fragment = new NewActivityFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_activity1, container, false);
        mainContainer = (RelativeLayout) view.findViewById(R.id.rl_tabs);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainContainer.getLayoutParams();
        params.setMargins(0, getStatusBarHeight(), 0, 0);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = (ViewPager) view.findViewById(R.id.pager);
        setupViewPager(pager);

        tabLayout.setupWithViewPager(pager);
        setupTabIcons();
        tab1 = tabLayout.getTabAt(0);
        if (tab1 != null && tab1.getCustomView() != null) {
            TextView b = (TextView) tab1.getCustomView().findViewById(android.R.id.text1);
            b.setTextColor(getResources().getColorStateList(R.color.textColorPrimary));
        }
        String userId = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        new GetFollowerRequestTask().execute(userId);

        String FOLLOW_NOTIFICATION_CATEGORY = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.FOLLOW_NOTIFICATION_CATEGORY);
        String NOTIFICATION_POST_ID = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.FOLLOW_NOTIFICATION_CATEGORY_ID);

        if (FOLLOW_NOTIFICATION_CATEGORY != null) {
            if (FOLLOW_NOTIFICATION_CATEGORY.equals("0")) {
                pager.setCurrentItem(0);
                AppPreferences.getAppPreferences(getActivity()).putStringValue(AppPreferences.NOTIFICATION_CATEGORY, "0");
                AppPreferences.getAppPreferences(getActivity()).putStringValue(AppPreferences.NOTIFICATION_POST_ID, "0");
            } else if (FOLLOW_NOTIFICATION_CATEGORY.equals("1")) {
                pager.setCurrentItem(1);
                AppPreferences.getAppPreferences(getActivity()).putStringValue(AppPreferences.NOTIFICATION_CATEGORY, "0");
            }
        }


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                if (tab != null && tab.getCustomView() != null) {
                    TextView b = (TextView) tab.getCustomView().findViewById(android.R.id.text1);
                    b.setTextColor(getResources().getColorStateList(R.color.textColorPrimary));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
                if (tab != null && tab.getCustomView() != null) {
                    TextView b = (TextView) tab.getCustomView().findViewById(android.R.id.text1);
                    b.setTextColor(getResources().getColorStateList(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // called when a tab is reselected
            }
        });

        return view;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setCustomView(R.layout.badged_tab);
        tabLayout.getTabAt(1).setCustomView(R.layout.badged_tab);
    }

    //
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter2 adapter = new ViewPagerAdapter2(getChildFragmentManager());
        adapter.addFrag(FollowingActivityFragment.newInstance(), "NOTIFICATION");
        adapter.addFrag(RequestFragment.newInstance(), "REQUEST");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter2 extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter2(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }

    class GetFollowerRequestTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            return HTTPUrlConnection.getInstance().load(getActivity(), Config.GET_FOLLOWER_REQUEST, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {

                    JSONArray userArray = object.getJSONArray("data");


                    tab1 = tabLayout.getTabAt(1);
                    if (tab1 != null && tab1.getCustomView() != null) {
                        TextView b = (TextView) tab1.getCustomView().findViewById(R.id.badge);
                        if (b != null) {
                            b.setText(userArray.length() + "");
                        }
                        View v = tab1.getCustomView().findViewById(R.id.badgeCotainer);
                        if (v != null) {
                            v.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    tab1 = tabLayout.getTabAt(1);
                    if (tab1 != null && tab1.getCustomView() != null) {
                        TextView b = (TextView) tab1.getCustomView().findViewById(R.id.badge);
                        if (b != null) {
                            b.setText("" + "");
                        }
                        View v = tab1.getCustomView().findViewById(R.id.badgeCotainer);
                        if (v != null) {
                            v.setVisibility(View.GONE);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
