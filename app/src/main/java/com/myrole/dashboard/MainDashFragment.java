package com.myrole.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.myrole.NewCamera.NewCameraActivity;
import com.myrole.R;
import com.myrole.dashboard.frags.DashFeedFragment;
import com.myrole.dashboard.onfragmentback.OnBackPressListener;
import com.myrole.dashboard.onfragmentback.RootFragment;
import com.myrole.fragments.ExploreFragment;
import com.myrole.fragments.NewActivityFragment;
import com.myrole.fragments.ProfileFragment;

import static com.myrole.utils.ActivityTransactions.getLogIn;
import static com.myrole.utils.AppPreferences.isLogin;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainDashFragment extends RootFragment implements View.OnClickListener {

    public TabLayout mTabLayout;
    protected Custom_ViewPager mPager;
    private MyPagerAdapter mAdapter;
    Context context;

    public MainDashFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_dash, container, false);
        context = getContext();
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mPager = view.findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(5);
        mPager.setPagingEnabled(false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Note that we are passing childFragmentManager, not FragmentManager
        mAdapter = new MyPagerAdapter(getResources(), getChildFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        setupTabIcons();

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

    ImageView cameraImageView;

    // this function will set all the icon and text in
    // Bottom tabs when we open an activity
    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView1 = view1.findViewById(R.id.image);
        TextView title1 = view1.findViewById(R.id.text);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_bottom));
        title1.setText("For You");
        title1.setTextColor(context.getResources().getColor(R.color.white));
        mTabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView2 = view2.findViewById(R.id.image);
        TextView title2 = view2.findViewById(R.id.text);
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_bottom));
        imageView2.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        title2.setText("Discover");
        title2.setTextColor(context.getResources().getColor(R.color.white));
        mTabLayout.getTabAt(1).setCustomView(view2);


        View view3 = LayoutInflater.from(context).inflate(R.layout.item_add_tab_layout, null);
        cameraImageView = view3.findViewById(R.id.image);
        mTabLayout.getTabAt(2).setCustomView(view3);

        View view4 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView4 = view4.findViewById(R.id.image);
        TextView title4 = view4.findViewById(R.id.text);
        imageView4.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_bottom));
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        title4.setText("Notification");
        title4.setTextColor(context.getResources().getColor(R.color.white));
        mTabLayout.getTabAt(3).setCustomView(view4);

        View view5 = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);
        ImageView imageView5 = view5.findViewById(R.id.image);
        TextView title5 = view5.findViewById(R.id.text);
        imageView5.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_bottom));
        imageView5.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        title5.setText("Profile");
        title5.setTextColor(context.getResources().getColor(R.color.white));
        mTabLayout.getTabAt(4).setCustomView(view5);


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);
                TextView title = v.findViewById(R.id.text);

                switch (tab.getPosition()) {
                    case 0:
                        OnHome_Click();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_bottom));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                        cameraImageView.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.white));
                        break;

                    case 1:
                        Another_Tab_Click();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_bottom));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.app_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        cameraImageView.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.app_color));
                        break;
                    case 3:
                        Another_Tab_Click();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_bottom));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.app_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        cameraImageView.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.app_color));
                        break;
                    case 4:
                        Another_Tab_Click();
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_bottom));
                        image.setColorFilter(ContextCompat.getColor(context, R.color.app_color), android.graphics.PorterDuff.Mode.SRC_IN);
                        cameraImageView.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                        title.setTextColor(context.getResources().getColor(R.color.app_color));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);
                TextView title = v.findViewById(R.id.text);

                switch (tab.getPosition()) {
                    case 0:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_bottom));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_bottom));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(cameraImageView.getDrawable()),
                                ContextCompat.getColor(context, R.color.black)
                        );

                        break;

                    case 3:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_notification_bottom));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                    case 4:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_bottom));
                        title.setTextColor(context.getResources().getColor(R.color.darkgray));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


        final LinearLayout tabStrip = ((LinearLayout) mTabLayout.getChildAt(0));
        tabStrip.setEnabled(false);

        tabStrip.getChildAt(2).setClickable(false);
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), Video_Recoder_A.class));

                if (isLogin(getContext())){
                    mRecordForResult.launch(new Intent(getContext(), NewCameraActivity.class));
                } else getLogIn(getContext());
                //Toast.makeText(context, "Recording clicked", Toast.LENGTH_SHORT).show();
                /*if(check_permissions()) {
                    if(Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {

                        Intent intent = new Intent(getActivity(), Video_Recoder_A.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                    }
                    else {
                        Toast.makeText(context, "You have to login First", Toast.LENGTH_SHORT).show();
                    }
                }*/

            }
        });


        tabStrip.getChildAt(3).setClickable(false);
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = mTabLayout.getTabAt(3);
                tab.select();
            }
        });

        //tabStrip.getChildAt(4).setClickable(false);
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout.Tab tab = mTabLayout.getTabAt(4);
                tab.select();
            }
        });


    }

    ActivityResultLauncher<Intent> mRecordForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //if(DEBUG) Log.d(TAG, result.getData().getStringArrayListExtra("records").get(3));
                    }
                }
            }
    );

    public void OnHome_Click() {

        TabLayout.Tab tab1 = mTabLayout.getTabAt(1);
        View view1 = tab1.getCustomView();
        ImageView imageView1 = view1.findViewById(R.id.image);
        imageView1.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex1 = view1.findViewById(R.id.text);
        tex1.setTextColor(context.getResources().getColor(R.color.white));
        tab1.setCustomView(view1);

        TabLayout.Tab tab2 = mTabLayout.getTabAt(2);
        View view2 = tab2.getCustomView();
        ImageView image = view2.findViewById(R.id.image);
        image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_bottom));
        tab2.setCustomView(view2);

        TabLayout.Tab tab3 = mTabLayout.getTabAt(3);
        View view3 = tab3.getCustomView();
        ImageView imageView3 = view3.findViewById(R.id.image);
        imageView3.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex3 = view3.findViewById(R.id.text);
        tex3.setTextColor(context.getResources().getColor(R.color.white));
        tab3.setCustomView(view3);


        TabLayout.Tab tab4 = mTabLayout.getTabAt(4);
        View view4 = tab4.getCustomView();
        ImageView imageView4 = view4.findViewById(R.id.image);
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex4 = view4.findViewById(R.id.text);
        tex4.setTextColor(context.getResources().getColor(R.color.white));
        tab4.setCustomView(view4);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPager.setLayoutParams(params);
        mTabLayout.setBackground(getResources().getDrawable(R.drawable.d_top_white_line));
    }

    public void Another_Tab_Click() {

        TabLayout.Tab tab0 = mTabLayout.getTabAt(0);
        View view0 = tab0.getCustomView();
        TextView text0 = view0.findViewById(R.id.text);
        ImageView imageView0 = view0.findViewById(R.id.image);
        imageView0.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        text0.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab0.setCustomView(view0);

        TabLayout.Tab tab1 = mTabLayout.getTabAt(1);
        View view1 = tab1.getCustomView();
        TextView tex1 = view1.findViewById(R.id.text);
        ImageView imageView1 = view1.findViewById(R.id.image);
        imageView1.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        tex1.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab1.setCustomView(view1);

        TabLayout.Tab tab2 = mTabLayout.getTabAt(2);
        View view2 = tab2.getCustomView();
        ImageView image = view2.findViewById(R.id.image);
        image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_bottom));
        tab2.setCustomView(view2);

        TabLayout.Tab tab3 = mTabLayout.getTabAt(3);
        View view3 = tab3.getCustomView();
        ImageView imageView3 = view3.findViewById(R.id.image);
        imageView3.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex3 = view3.findViewById(R.id.text);
        tex3.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab3.setCustomView(view3);

        TabLayout.Tab tab4 = mTabLayout.getTabAt(4);
        View view4 = tab4.getCustomView();
        ImageView imageView4 = view4.findViewById(R.id.image);
        imageView4.setColorFilter(ContextCompat.getColor(context, R.color.darkgray), android.graphics.PorterDuff.Mode.SRC_IN);
        TextView tex4 = view4.findViewById(R.id.text);
        tex4.setTextColor(context.getResources().getColor(R.color.darkgray));
        tab4.setCustomView(view4);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ABOVE, R.id.tabs);
        mPager.setLayoutParams(params);
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.white));

    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public MyPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new DashFeedFragment();
                    break;
                case 1:
                    fragment = new ExploreFragment();
                    /*if (isLogin(getContext())){
                        fragment = new ExploreFragment();
                    } else getLogIn(getContext());*/
                    break;

               /* case 2:
                    fragment = new Video_Recoder_A();
                    break;*/

                case 3:
                    fragment = new NewActivityFragment();
                    break;

                case 4:
                    fragment = new ProfileFragment();
                    break;

                default:
                    fragment = new DashFeedFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            registeredFragments.remove(position);

            super.destroyItem(container, position, object);

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
