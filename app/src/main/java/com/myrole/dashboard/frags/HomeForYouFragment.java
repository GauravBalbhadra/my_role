package com.myrole.dashboard.frags;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.myrole.R;
import com.myrole.dashboard.Custom_ViewPager;
import com.myrole.dashboard.onfragmentback.OnBackPressListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeForYouFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeForYouFragment extends Fragment {

    public TabLayout mTabLayout;
    protected Custom_ViewPager mPager;
    private MyForYouPagerAdapter mAdapter;
    Context context;

    public HomeForYouFragment() {
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("Hello "," : "+isVisibleToUser);
       /* is_visible_to_user = isVisibleToUser;

        if (previous_player != null && isVisibleToUser) {
            previous_player.setPlayWhenReady(true);
        } else if (previous_player != null && !isVisibleToUser) {
            previous_player.setPlayWhenReady(false);
        }*/
    }

    public static HomeForYouFragment newInstance(String param1, String param2) {
        HomeForYouFragment fragment = new HomeForYouFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Note that we are passing childFragmentManager, not FragmentManager
        mAdapter = new MyForYouPagerAdapter(getResources(), getChildFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        //setupTabIcons();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_for_you, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.childTabs);
        mPager = view.findViewById(R.id.viewpager1);
        mPager.setOffscreenPageLimit(0);
        mPager.setPagingEnabled(true);

        /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPager.setLayoutParams(params);*/
        //mTabLayout.setBackground(getResources().getDrawable(R.drawable.d_top_white_line));


        return view;
    }

    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) mAdapter.getRegisteredFragment(mPager.getCurrentItem());
        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }



    class MyForYouPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
        private String[] tabTitles = new String[]{"For You", "Status"};

        public MyForYouPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
        }
        // overriding getPageTitle()
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    //Log.e("insa : ",""+((fragment instanceof DashFeedFragment)));
                    fragment = new DashFeedFragment();
                    break;
                case 1:
                    //Log.e("insa : ",""+((fragment instanceof DashFeedFragment)));
                    fragment = new DemoFragment();
                    break;
                default:
                   // Log.e("insa : ",""+((fragment instanceof DashFeedFragment)));
                    fragment = new DashFeedFragment();
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            registeredFragments.remove(position);
        }


        /**
         * Get the Fragment by position
         *
         * @param position tab position of the fragment
         * @return
         */
        public Fragment getRegisteredFragment(int position) {

            return registeredFragments.get(position);

        }
    }



}