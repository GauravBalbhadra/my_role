package com.myrole.dashboard;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.myrole.R;
import com.myrole.widget.Variables;

public class MainDashboardActivity extends AppCompatActivity {

    private MainDashFragment mDashFragment;
    public FragmentManager fragmentManager;
    private AmazonS3 s3;
    public static TransferUtility transferUtility;
    private int lockBackPress = 1;

    public static BottomSheetBehavior shareInfo;
    public static RecyclerView recyclerView;
    public static Button buttonCancel;
    public static CoordinatorLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Variables.screen_height = displayMetrics.heightPixels;
        Variables.screen_width = displayMetrics.widthPixels;
        fragmentManager = getSupportFragmentManager();
        setTransferUtility();
        if (savedInstanceState == null) {
            initScreen();
        } else {
            mDashFragment = (MainDashFragment) getSupportFragmentManager().getFragments().get(0);
        }

        coordinator = findViewById(R.id.coordinator);
        View viewUpdate = coordinator.findViewById(R.id.update_info);
        shareInfo = BottomSheetBehavior.from(viewUpdate);
        closeShare(BottomSheetBehavior.STATE_HIDDEN);
        buttonCancel = findViewById(R.id.buttonCancel);
        recyclerView = findViewById(R.id.recyclerViewShare);

    }

    public static void closeShare(int stateHidden) {
        shareInfo.setState(stateHidden);
    }

    public void setTransferUtility() {
        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        if (shareInfo.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            closeShare(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                if (mDashFragment.mPager.getCurrentItem() != 0) {
                    mDashFragment.mPager.setCurrentItem(0);
                } else {
                    if (lockBackPress != 0) {
                        lockBackPress--;
                        Toast.makeText(this, "Back press again to exit.", Toast.LENGTH_SHORT).show();
                    } else
                        super.onBackPressed();
                }
            }
        }
    }


    private void initScreen() {
        mDashFragment = new MainDashFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mDashFragment, "FEEDS")
                .commit();

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
