package com.myrole;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.myrole.fragments.FindFragment;

/**
 * Created by Rakesh on 10/9/2016.
 */

public class FindActivity extends BaseActivity {
    public FragmentManager fragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container,
                        FindFragment.newInstance(),
                        "FINDFRAGMENT")
                .commit();
    }
}
