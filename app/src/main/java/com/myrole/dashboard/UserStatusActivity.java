package com.myrole.dashboard;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.myrole.R;
import com.myrole.adapter.VideoCategoryAdapter;
import com.myrole.dashboard.frags.StatusFeedFragment;
import com.myrole.widget.Variables;

import java.util.ArrayList;
import java.util.List;

public class UserStatusActivity extends AppCompatActivity {

    private RecyclerView recycler_video_category;
    private VideoCategoryAdapter videoCategoryAdapter;
    private List<StatusVideoCategory> videoCategoryList = new ArrayList<StatusVideoCategory>();
    private StatusFeedFragment statusFeedFragment;
    public FragmentManager fragmentManager;

    public static BottomSheetBehavior shareInfo;
    public static RecyclerView recyclerView;
    public static Button buttonCancel;
    public static CoordinatorLayout coordinator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_status);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Variables.screen_height = displayMetrics.heightPixels;
        Variables.screen_width = displayMetrics.widthPixels;
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            initScreen();
        } else {
            statusFeedFragment = (StatusFeedFragment) getSupportFragmentManager().getFragments().get(0);
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

    private void initScreen() {
        statusFeedFragment = new StatusFeedFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.main, statusFeedFragment,"Status")
                .commit();

        findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


}
