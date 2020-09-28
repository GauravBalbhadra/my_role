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
import com.myrole.utils.Utils;
import com.myrole.vo.Post;

import java.util.List;

public class UserPostAdapter1 extends RecyclerView.Adapter<UserPostAdapter1.viewHolder> {
    List<Post> postList;
    Context context;

    public UserPostAdapter1(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user_post, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = ((int) Utils.getDeviceSize((Activity) context).get("Width"))/3;
        view.setLayoutParams(layoutParams);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((FindActivity) context).fragmentManager != null) {
                    PostDetailFragment fragment = PostDetailFragment.newInstance(postList
                            .get(position).id);
                    ((FindActivity) context).fragmentManager.beginTransaction()
                            .add(R.id.fragment_container,
                                    fragment,
                                    "POSTDETAILFRAGMENT")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });*/
        Glide.with(context).asBitmap().load(postList.get(position).postImage)
             .centerCrop().into(holder.postImage);

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;

        public viewHolder(View itemView) {
            super(itemView);
            postImage = (ImageView) itemView.findViewById(R.id.img_post);
        }
    }


}