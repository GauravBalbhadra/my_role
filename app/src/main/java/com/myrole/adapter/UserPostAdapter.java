package com.myrole.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myrole.R;
import com.myrole.utils.Utils;
import com.myrole.vo.Post;

import java.util.List;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.viewHolder> {
    List<Post> postList;
    Context context;

    public UserPostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
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

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((HomeActivity) context).fragmentManager != null) {
                    Utils.hideKeybord(context,v);
                    //Utils.hideKeybord2(activity,v);

                    //when i find other solution i can replace with this solution
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                    PostDetailFragment fragment = PostDetailFragment.newInstance(postList
                            .get(position).id);
                    ((HomeActivity) context).fragmentManager.beginTransaction()
                            .add(R.id.main_frame,
                                    fragment,
                                    "POSTDETAILFRAGMENT")
                            .addToBackStack(null)
                            .commit();  }
                    }, 500);//end
                }
            }
        });*/
        // loading album cover using Glide library
        String url = postList.get(position).postImage;
        Log.e("Sub>>", url.substring(url.length() - 4));
        if (url.substring(url.length() - 4).equalsIgnoreCase(".mp4")) {
            holder.vid_icon_play.setVisibility(View.VISIBLE);
            String[] url1 = postList.get(position).postImage.split(".mp4");
            String new_thumb = url1[0] + ".jpg";
            Glide.with(context).load(new_thumb).into(holder.postImage);
        } else{
            holder.vid_icon_play.setVisibility(View.GONE);
            Glide.with(context).load(postList.get(position).postImage).into(holder.postImage);
        }

       /* Glide.with(context).load(postList.get(position).postImage).asBitmap()
                .placeholder(R.drawable.singer_bg).centerCrop().into(holder.postImage);*/

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView postImage,vid_icon_play;

        public viewHolder(View itemView) {
            super(itemView);
            postImage = (ImageView) itemView.findViewById(R.id.img_post);
            vid_icon_play = (ImageView) itemView.findViewById(R.id.vid_icon_play);
        }
    }


}