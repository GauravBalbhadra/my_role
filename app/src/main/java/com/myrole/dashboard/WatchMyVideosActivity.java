package com.myrole.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.myrole.R;
import com.myrole.dashboard.frags.MyWatchFragment;
import com.myrole.holders.PostListDTO;

import java.util.ArrayList;

public class WatchMyVideosActivity extends AppCompatActivity {

    public static ArrayList<PostListDTO> mFeedList = new ArrayList<PostListDTO>();
    public static int position = 0;


    public static BottomSheetBehavior shareInfo;
    public static RecyclerView recyclerView;
    public static Button buttonCancel;
    public static CoordinatorLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_my_videos);
        Intent bundle = getIntent();
        mFeedList.clear();
        if (bundle != null) {
            Uri appLinkData = bundle.getData();
            if (appLinkData == null) {
                mFeedList = (ArrayList<PostListDTO>) bundle.getSerializableExtra("arraylist");
                position = bundle.getIntExtra("position", 0);
            }
        }

        MyWatchFragment m = new MyWatchFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.watchContainer, m,"watched")
                .commit();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mFeedList.clear();
    }
}