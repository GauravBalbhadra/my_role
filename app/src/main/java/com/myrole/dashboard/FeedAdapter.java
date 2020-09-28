package com.myrole.dashboard;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.PlayerView;
import com.myrole.BuildConfig;
import com.myrole.R;
import com.myrole.dashboard.frags.DashFeedFragment;
import com.myrole.dashboard.interfaces.InvokedFeedsFunctions;
import com.myrole.fragments.OtherProfileFragment;
import com.myrole.holders.PostListDTO;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.Utils;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.myrole.utils.ActivityTransactions.getLogIn;
import static com.myrole.utils.AppPreferences.isLogin;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedsViewHolder> {

    public Context mCtx;
    private ArrayList<PostListDTO> mFeedList;
    // Cache cache;
    InvokedFeedsFunctions mInvokeCommentSheet;

    public FeedAdapter(Context context, ArrayList<PostListDTO> dataList, DashFeedFragment activity) {
        this.mCtx = context;
        mInvokeCommentSheet = activity;
        this.mFeedList = dataList;
        // if(cache == null)
        //cache = new SimpleCache(new File(context.getCacheDir(), "exoCache"), new LeastRecentlyUsedCacheEvictor(1024 * 1024 * 10));
    }

    @NonNull
    @Override
    public FeedsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vh_fb_feed_post_video, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        FeedsViewHolder viewHolder = new FeedsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedsViewHolder holder, int position) {
        // holder.setIsRecyclable(false);
        PostListDTO pto = mFeedList.get(position);
        if (BuildConfig.DEBUG)
            Log.e("pos : " + position, " : " + pto.getThumbnail());

        holder.tv_share_count.setText(String.valueOf(mFeedList.get(position).getShare_cnt()));
        holder.tvDownloadCount.setText(String.valueOf(mFeedList.get(position).getDownload_cnt()));

        Glide.with(mCtx).load(pto.getThumbnail()).into(holder.v_thumbnail);
        holder.txt_num_comment.setText(String.valueOf(pto.getComment_count()));
        holder.txt_user_name.setText(pto.getOwner_name());
        holder.txt_time.setText(Utils.timeCalculation(pto.getPost_time()));

        String userID = AppPreferences.getAppPreferences(mCtx).getStringValue(AppPreferences.USER_ID);

        try {
            if (pto != null && (pto.getOwner_id() == Integer.parseInt(userID))) {
                holder.ivFollow.setVisibility(View.GONE);
            } else {
                holder.ivFollow.setVisibility(View.VISIBLE);
            }



        } catch (Exception ignored) {
        }
        if (pto != null && pto.getOwner_image() != null) {
            Picasso.get().load(pto.getOwner_image())
                    .error(R.drawable.default_avatar)
                    .into(holder.usericon);
        } else {
            Picasso.get().load(R.drawable.default_avatar).into(holder.usericon);
        }
        if (pto.getIslike() == 1) {
            holder.img_love.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_heart_active));
        } else {
            holder.img_love.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_heart));
        }

        try {
            if (pto.getDescription() != null) {
                if ((pto.getDescription()).equals("")) {
                    holder.txt_description.setVisibility(View.GONE);
                } else {
                    holder.txt_description.setVisibility(View.VISIBLE);
                    holder.txt_description.setText(StringEscapeUtils.unescapeJava(pto.getDescription()));
                }
            } else {
                holder.txt_description.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
        holder.txt_num_loves.setText(String.valueOf(pto.getPost_likes()));

        holder.linearHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(mCtx)) {
                    if (Utils.isNetworkConnected(mCtx, true)) {
                        String userId = AppPreferences.getAppPreferences(mCtx).getStringValue(AppPreferences.USER_ID);
                        if (mFeedList.get(position).getIslike() == 0) {
                            mFeedList.get(position).setPost_likes(mFeedList.get(position).getPost_likes() + 1);
                            holder.txt_num_loves.setText(String.valueOf(mFeedList.get(position).getPost_likes()));
                            holder.img_love.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_heart_active));
                            mFeedList.get(position).setIslike(1);
                        } else {
                            mFeedList.get(position).setPost_likes(mFeedList.get(position).getPost_likes() - 1);
                            holder.txt_num_loves.setText(String.valueOf(mFeedList.get(position).getPost_likes()));
                            holder.img_love.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.ic_heart));
                            mFeedList.get(position).setIslike(0);
                        }
                        new LikeTask1().execute(userId, String.valueOf(mFeedList.get(position).getPost_id()), String.valueOf(mFeedList.get(position).getOwner_id()), "1", position + "");
                    }
                } else getLogIn(mCtx);
            }
        });

        holder.linearDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(mCtx)) {
                    if (Utils.isNetworkConnected(mCtx, true) && isLogin(mCtx)) {
                        mInvokeCommentSheet.downloadVideo(mFeedList.get(position).getDownload(), mFeedList.get(position).getPost_id(), position);
                    }
                } else getLogIn(mCtx);
            }
        });

        holder.linearComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(mCtx)) {
                    if (((MainDashboardActivity) mCtx).fragmentManager != null) {
                        mInvokeCommentSheet.showCommentSheet(String.valueOf(mFeedList.get(position).getPost_id()), String.valueOf(position));
                    }
                } else getLogIn(mCtx);
            }
        });

        holder.linearShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(mCtx)) {
                    if (((MainDashboardActivity) mCtx).fragmentManager != null) {
                        mInvokeCommentSheet.sharedVideo(String.valueOf(mFeedList.get(position).getDownload()), mFeedList.get(position).getPost_id(), position);
                    }
                } else getLogIn(mCtx);

            }
        });

        holder.linear_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(mCtx)) {
                    if (((MainDashboardActivity) mCtx).fragmentManager != null) {
                        mInvokeCommentSheet.deleteVideo(String.valueOf(mFeedList.get(position).getPost_id()), String.valueOf(position));
                    }
                } else getLogIn(mCtx);
            }
        });
        holder.relativeUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) mCtx).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(mFeedList.get(position).getOwner_id()));
                    ((MainDashboardActivity) mCtx).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });


        // setPlayer(pto, holder, position);
    }


    public Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(Resources.getSystem(), x);
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    public void addData(ArrayList<PostListDTO> mFeedList) {
        this.mFeedList = mFeedList;
    }

    class FeedsViewHolder extends RecyclerView.ViewHolder {
        public ImageView mThumbnail, usericon, img_love, ivFollow, ivDisc, v_thumbnail;
        public TextView mInfo, txt_user_name, txt_time, txt_num_loves, txt_num_comment, txt_description, tvDownloadCount, tv_share_count;

        public LinearLayout linearHeart, linearDownload, linearComments, linearShare, linear_delete;
        public PlayerView playerView;
        public RelativeLayout relativeUserProfile;

        public FeedsViewHolder(View itemView) {
            super(itemView);
            ivDisc = itemView.findViewById(R.id.ivDisc);

            RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(10000);
            rotate.setRepeatCount(Animation.INFINITE);
            rotate.setFillAfter(true);
            rotate.setInterpolator(new LinearInterpolator());
            ivDisc.startAnimation(rotate);

            v_thumbnail = (ImageView) itemView.findViewById(R.id.v_thumbnail);
            ivFollow = (ImageView) itemView.findViewById(R.id.ivFollow);
            relativeUserProfile = (RelativeLayout) itemView.findViewById(R.id.relative_image_container);
            linearShare = (LinearLayout) itemView.findViewById(R.id.linear_share);
            linearComments = (LinearLayout) itemView.findViewById(R.id.linear_comment);
            tvDownloadCount = (TextView) itemView.findViewById(R.id.tv_download_count);
            tv_share_count = (TextView) itemView.findViewById(R.id.tv_share_count);
            linearDownload = (LinearLayout) itemView.findViewById(R.id.linear_download);
            playerView = itemView.findViewById(R.id.playerview);
            linearHeart = (LinearLayout) itemView.findViewById(R.id.linear_heart);
            linear_delete = (LinearLayout) itemView.findViewById(R.id.linear_delete);
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            img_love = (ImageView) itemView.findViewById(R.id.img_love);
            usericon = (ImageView) itemView.findViewById(R.id.usericon);
            mInfo = (TextView) itemView.findViewById(R.id.info);
            txt_num_loves = (TextView) itemView.findViewById(R.id.txt_num_loves);
            txt_num_comment = (TextView) itemView.findViewById(R.id.txt_num_comment);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_user_name = (TextView) itemView.findViewById(R.id.txt_user_name);
        }

    }

    class LikeTask1 extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int index;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            index = Integer.parseInt(params[4]);
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            postDataParams.put("post_id", params[1]);
            postDataParams.put("owner_id", params[2]);
            postDataParams.put("type", params[3]);

            return HTTPUrlConnection.getInstance().load(mCtx, Config.ADD_LIKE, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    Log.e("success", result);
                } else {
                    Log.e("failure", result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}
