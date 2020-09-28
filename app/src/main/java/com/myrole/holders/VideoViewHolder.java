package com.myrole.holders;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;


/**
 * Created by Rakesh on 8/3/2016.
 */

public class VideoViewHolder extends RecyclerView.ViewHolder{

    //public final VideoPlayerView mPlayer;
   // public final ImageView mCover;
  //  public final ImageButton iv_volume,iv_play;
    //public final TextView mVisibilityPercents;
    public TextView name, categoty_name, num_loves, num_comment, description,comment,love,txt_time;
    public ImageView postImage, userIcon, loveIcon, commentIcon;
    public RelativeLayout media_controlar_layout;
    public LinearLayout lay_num_loves;

    public VideoViewHolder(View view) {
        super(view);
        //mPlayer = (VideoPlayerView) view.findViewById(R.id.player);
       // lay_num_loves = (LinearLayout)itemView.findViewById(R.id.lay_num_loves);
       // mCover = (ImageView) view.findViewById(R.id.cover);
        /*iv_volume = (ImageButton) view.findViewById(R.id.iv_volume);
        iv_play = (ImageButton) view.findViewById(R.id.iv_play);
        mVisibilityPercents = (TextView) view.findViewById(R.id.visibility_percents);
        */name = (TextView) itemView.findViewById(R.id.txt_user_name);
        //comment = (TextView) itemView.findViewById(R.id.txt_comment);
        //categoty_name = (TextView) itemView.findViewById(R.id.txt_category_name);
        num_loves = (TextView) itemView.findViewById(R.id.txt_num_loves);
        txt_time = (TextView) itemView.findViewById(R.id.txt_time);
        num_comment = (TextView) itemView.findViewById(R.id.txt_num_comment);
       // love = (TextView) itemView.findViewById(R.id.txt_loves);
        description = (TextView) itemView.findViewById(R.id.txt_description);
        postImage = (ImageView) itemView.findViewById(R.id.img_post);
        loveIcon = (ImageView) itemView.findViewById(R.id.img_love);

        userIcon = (ImageView) itemView.findViewById(R.id.usericon);
        //media_controlar_layout = (RelativeLayout) itemView.findViewById(R.id.mediacontroller_layout);

    }
}