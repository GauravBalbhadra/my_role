package com.myrole.camera_ck.internal.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myrole.camera_ck.internal.enums.CameraOption;

import java.util.List;

/**
 * Created by intel on 18-Jul-17.
 */

public class CameraOptionAdapter extends ArrayAdapter<String> {

    private List<CameraOption> cameraOptions;

    public CameraOptionAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        cameraOptions = CameraOption.getOptions();
    }

    public void setCameraOptions(List<CameraOption> cameraOptions) {
        this.cameraOptions = cameraOptions;
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return cameraOptions.get(position).getValue();
    }

    @Override
    public int getCount() {
        return cameraOptions.size();
    }

}
