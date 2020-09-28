package com.myrole;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MergeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLinearSpeedContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);

        init();
    }

    private void init() {
        mLinearSpeedContainer = (LinearLayout) findViewById(R.id.linearSpeedContainer);

        findViewById(R.id.iv_camera_flash).setOnClickListener(this);
        findViewById(R.id.iv_camera_flip).setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_speed).setOnClickListener(this);
        findViewById(R.id.iv_timer).setOnClickListener(this);
        findViewById(R.id.ivVideoPlay).setOnClickListener(this);
        findViewById(R.id.ivCloseSpeedContainer).setOnClickListener(this);
        findViewById(R.id.tvGallery).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera_flip:
                Toast.makeText(this, "Flip clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_close:
                Toast.makeText(this, "Close clicked", Toast.LENGTH_SHORT).show();
                finishAffinity();
                break;
            case R.id.iv_speed:
                Toast.makeText(this, "speed clicked", Toast.LENGTH_SHORT).show();
                mLinearSpeedContainer.setVisibility(View.VISIBLE);
                break;

            case R.id.ivCloseSpeedContainer:
                Toast.makeText(this, "close clicked", Toast.LENGTH_SHORT).show();
                mLinearSpeedContainer.setVisibility(View.GONE);
                break;
            case R.id.iv_timer:
                Toast.makeText(this, "timer clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_camera_flash:
                Toast.makeText(this, "Flash clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ivVideoPlay:
                Toast.makeText(this, "play clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvGallery:
                Toast.makeText(this, "Gallery clicked", Toast.LENGTH_SHORT).show();
                break;
           /* case R.id.tvTemplates:
                Toast.makeText(this, "Template clicked", Toast.LENGTH_SHORT).show();
                break;*/


        }

    }
}
