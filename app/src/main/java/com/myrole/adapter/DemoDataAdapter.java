package com.myrole.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;
import com.myrole.utils.Utils;

public class DemoDataAdapter extends RecyclerView.Adapter<DemoDataAdapter.viewHolder> {


    Context context;

    public DemoDataAdapter(Context context) {
        this.context = context;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_demo_data, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = ((int) Utils.getDeviceSize((Activity) context).get("Width")) / 3;
        view.setLayoutParams(layoutParams);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

    }


    @Override
    public int getItemCount() {
        return 3;
    }

    class viewHolder extends RecyclerView.ViewHolder {
        View view;

        public viewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.demo_view);
        }
    }


}