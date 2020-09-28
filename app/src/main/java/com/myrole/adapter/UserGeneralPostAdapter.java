package com.myrole.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrole.R;
import com.myrole.fragments.NewUserGeneralPostFragment1;
import com.myrole.holders.PostListDTO;
import com.myrole.utils.Utils;

import java.util.ArrayList;

public class UserGeneralPostAdapter extends RecyclerView.Adapter<UserGeneralPostAdapter.viewHolder> {

    ArrayList<PostListDTO> postList;
    Context context;
    NewUserGeneralPostFragment1 gffragment;

    public UserGeneralPostAdapter(ArrayList<PostListDTO> postList, Context context, NewUserGeneralPostFragment1 gffragment) {
        this.postList = postList;
        this.context = context;
        this.gffragment = gffragment;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user_post, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = ((int) Utils.getDeviceSize((Activity) context).get("Width")) / 3;
        view.setLayoutParams(layoutParams);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = postList.get(position).getUrl();
//        Log.e("Sub>>", url.substring(url.length() - 4, url.length()));
                if (url.substring(url.length() - 4).equalsIgnoreCase(".mp4")) {
                 //   gffragment.PostDetails3(String.valueOf(postList.get(position).getPost_id()), postList.get(position).getOwner_image(),postList.get(position).getThumbnail(),postList.get(position).getUrl());
                } else {
                   // gffragment.PostDetails2(String.valueOf(postList.get(position).getPost_id()), postList.get(position).getOwner_image(),postList.get(position).getUrl(),postList.get(position).getThumbnail());
                }
//                if (((HomeActivity) context).fragmentManager != null) {
//                    PostDetailFragment fragment = PostDetailFragment.newInstance(String.valueOf(postList.get(position).getPost_id()));
//                    ((HomeActivity) context).fragmentManager.beginTransaction()
//                            .add(R.id.main_frame,
//                                    fragment,
//                                    "POSTDETAILFRAGMENT")
//                            .addToBackStack(null)
//                            .commit();
//                }
            }
        });
        // loading album cover using Glide library
        String url = postList.get(position).getUrl();
//        Log.e("Sub>>", url.substring(url.length() - 4, url.length()));
        if (url.substring(url.length() - 4).equalsIgnoreCase(".mp4")) {
            holder.vid_icon_play.setVisibility(View.VISIBLE);
            String[] url1 = postList.get(position).getUrl().split(".mp4");
            String thumbanli = postList.get(position).getThumbnail();
            String new_thumb = url1[0] + ".jpg";
            Glide.with(context).load(thumbanli).into(holder.postImage);
        } else {
            holder.vid_icon_play.setVisibility(View.GONE);
            Glide.with(context).load(postList.get(position).getUrl()).into(holder.postImage);
        }

       /* Glide.with(context).load(postList.get(position).postImage).asBitmap()
                .placeholder(R.drawable.singer_bg).centerCrop().into(holder.postImage);*/

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView postImage, vid_icon_play;

        public viewHolder(View itemView) {
            super(itemView);
            postImage = (ImageView) itemView.findViewById(R.id.img_post);
            vid_icon_play = (ImageView) itemView.findViewById(R.id.vid_icon_play);
        }
    }


}