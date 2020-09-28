package com.myrole.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.myrole.R;
import com.myrole.utils.Config;

/**
 * Created by vikesh.kumar on 6/1/2016.
 */
public class BottomBar extends LinearLayout {

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.layout_bottom_bar, this, true);


    }


    public void selectTab(String selectedTab) {
        ((ImageView) findViewById(R.id.tab_home)).setImageDrawable(getResources()
                .getDrawable(R.drawable.time_line_icon));
        ((ImageView) findViewById(R.id.tab_find)).setImageDrawable(getResources()
                .getDrawable(R.drawable.find_iocn));
        ((ImageView) findViewById(R.id.tab_post)).setImageDrawable(getResources()
                .getDrawable(R.drawable.cam_icon));
        ((ImageView) findViewById(R.id.tab_activity)).setImageDrawable(getResources()
                .getDrawable(R.drawable.presentation_icon));
        ((ImageView) findViewById(R.id.tab_profile)).setImageDrawable(getResources()
                .getDrawable(R.drawable.profile_icon));

        switch (selectedTab) {
            case Config.HOME_2:
                ((ImageView) findViewById(R.id.tab_home))
                        .setImageDrawable(getResources()
                                .getDrawable(R.drawable.time_line_active_icon));
                break;
            case Config.HOME:
                ((ImageView) findViewById(R.id.tab_home))
                        .setImageDrawable(getResources()
                        .getDrawable(R.drawable.time_line_active_icon));
                break;
            case Config.FIND:
                ((ImageView) findViewById(R.id.tab_find))
                        .setImageDrawable(getResources()
                        .getDrawable(R.drawable.find_active_iocn));
                break;
            case Config.POST:
                ((ImageView) findViewById(R.id.tab_post))
                        .setImageDrawable(getResources()
                        .getDrawable(R.drawable.cam_active_icon));
                break;
            case Config.ACTIVITY:
                ((ImageView) findViewById(R.id.tab_activity))
                        .setImageDrawable(getResources()
                        .getDrawable(R.drawable.presentation_active_icon));
                break;
            case Config.PROFILE:
                ((ImageView) findViewById(R.id.tab_profile))
                        .setImageDrawable(getResources()
                        .getDrawable(R.drawable.profile_active_icon));
                break;

        }
    }
}
