package com.myrole.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrole.R;
import com.myrole.dashboard.WatchMyVideosActivity;
import com.myrole.fragments.ExploreFragment;
import com.myrole.holders.PostListDTO;

import java.util.ArrayList;

/**
 * Created by Rakesh on 10/8/2016.
 */

public class ExploreScreenAdapter extends RecyclerView.Adapter<ExploreScreenAdapter.MyViewHolder> {

    private ExploreFragment gffragment;
    private Context mContext;
    ArrayList<PostListDTO> post_list;

    public void addData(ArrayList<PostListDTO> list) {
        this.post_list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail, vid_icon_play;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            vid_icon_play = (ImageView) view.findViewById(R.id.vid_icon_play);

        }
    }

    public ExploreScreenAdapter(Context mContext, ExploreFragment gffragment, ArrayList<PostListDTO> data) {
        this.gffragment = gffragment;
        this.mContext = mContext;
        this.post_list = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.vid_icon_play.setVisibility(View.VISIBLE);
        String new_thumb = post_list.get(position).getThumbnail();
        Glide.with(mContext).load(new_thumb).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WatchMyVideosActivity.class);
                intent.putExtra("arraylist", post_list);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return post_list.size();
    }
}
