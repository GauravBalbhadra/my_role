package com.myrole.widget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.myrole.R;
import com.myrole.dashboard.MainDashFragment;

public class GuestMainActivity extends AppCompatActivity {
    private MainDashFragment mDashFragment;
    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);
    }
}