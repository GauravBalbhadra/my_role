package com.myrole.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;
import com.myrole.dashboard.StatusVideoCategory;
import com.myrole.dashboard.frags.StatusFeedFragment;
import com.myrole.dashboard.interfaces.OnStatusItemSelected;
import com.myrole.databinding.ListVideoCategoryItemBinding;

import java.util.List;

public class VideoCategoryAdapter extends RecyclerView.Adapter<VideoCategoryAdapter.VideoCategoryViewHolder> {

    private Activity mActivity;
    private List<StatusVideoCategory> videoCategoryList;
    private int row_index = -1;
    OnStatusItemSelected mCategorySelected;

    public VideoCategoryAdapter(StatusFeedFragment activity, List<StatusVideoCategory> videoCategroy, Activity act) {
        this.mActivity = act;
        this.videoCategoryList = videoCategroy;
        this.mCategorySelected = activity;
    }

    public void addData(List<StatusVideoCategory> videoCategoryList) {
        this.videoCategoryList = videoCategoryList;
    }

    public static class VideoCategoryViewHolder extends RecyclerView.ViewHolder {
        ListVideoCategoryItemBinding mBinding;
        public VideoCategoryViewHolder(ListVideoCategoryItemBinding view) {
            super(view.getRoot());
            mBinding=view;
        }
    }

    @NonNull
    @Override
    public VideoCategoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        ListVideoCategoryItemBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_video_category_item, parent, false);
        return new VideoCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoCategoryViewHolder holder, final int listPosition) {

        holder.mBinding.txtVideoCategoryName.setText(videoCategoryList.get(listPosition).getVideo_category_name());

        holder.mBinding.txtVideoCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = listPosition;
                mCategorySelected.onStatusCategorySelected(videoCategoryList.get(listPosition).getVideo_category_id());
                notifyDataSetChanged();
            }
        });

        if (row_index == listPosition) {
            holder.mBinding.setStatus(true);
            holder.mBinding.txtVideoCategoryName.setTextColor(mActivity.getResources().getColor(R.color.white));
        } else {
            holder.mBinding.setStatus(false);
            holder.mBinding.txtVideoCategoryName.setTextColor(mActivity.getResources().getColor(R.color.black));
        }

    }

    @Override
    public int getItemCount() {
        return videoCategoryList.size();
    }
}