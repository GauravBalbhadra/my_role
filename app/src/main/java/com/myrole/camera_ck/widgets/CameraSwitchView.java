package com.myrole.camera_ck.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.myrole.R;
import com.myrole.camera_ck.internal.utils.Utils;

/*
 * Created by memfis on 6/24/16.
 */
public class CameraSwitchView extends AppCompatImageButton {

    private Drawable frontCameraDrawable;
    private Drawable rearCameraDrawable;
    private int padding = 5;

    public CameraSwitchView(Context context) {
        this(context, null);
    }

    public CameraSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public CameraSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void initializeView() {
        Context context = getContext();
        frontCameraDrawable = ContextCompat.getDrawable(context, R.drawable.ic_camera_front_white_24dp);
        frontCameraDrawable = DrawableCompat.wrap(frontCameraDrawable);
        DrawableCompat.setTintList(frontCameraDrawable.mutate(), ContextCompat.getColorStateList(context, R.color.switch_camera_mode_selector));

        rearCameraDrawable = ContextCompat.getDrawable(context, R.drawable.ic_camera_rear_white_24dp);
        rearCameraDrawable = DrawableCompat.wrap(rearCameraDrawable);
        DrawableCompat.setTintList(rearCameraDrawable.mutate(), ContextCompat.getColorStateList(context, R.color.switch_camera_mode_selector));

        setBackgroundResource(R.drawable.circle_frame_background_dark);
        displayBackCamera();

        padding = Utils.convertDipToPixels(context, padding);
        setPadding(padding, padding, padding, padding);

        displayBackCamera();
    }

    public void displayFrontCamera() {
        setImageDrawable(frontCameraDrawable);
    }

    public void displayBackCamera() {
        setImageDrawable(rearCameraDrawable);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (Build.VERSION.SDK_INT > 10) {
            if (enabled) {
                setAlpha(1f);
            } else {
                setAlpha(0.5f);
            }
        }
    }
}
