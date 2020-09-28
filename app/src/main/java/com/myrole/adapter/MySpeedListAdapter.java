package com.myrole.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;

import java.util.ArrayList;
import java.util.List;

public class MySpeedListAdapter extends RecyclerView.Adapter<MySpeedListAdapter.ViewHolder> {
    private List<String> mSpeedDataList = new ArrayList<String>();

    public MySpeedListAdapter() {
        mSpeedDataList.add(".3x");
        mSpeedDataList.add(".5x");
        mSpeedDataList.add("1x");
        mSpeedDataList.add("2x");
        mSpeedDataList.add("3x");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.speed_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSpeedData.setText(mSpeedDataList.get(position));
    }


    @Override
    public int getItemCount() {
        return mSpeedDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvSpeedData;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvSpeedData = (TextView) itemView.findViewById(R.id.tv_speed_value);
        }
    }
}
