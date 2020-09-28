package com.myrole.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.gson.Gson;
import com.myrole.BuildConfig;
import com.myrole.R;
import com.myrole.dashboard.UserStatusActivity;
import com.myrole.dashboard.frags.StatusFeedFragment;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static com.myrole.utils.ActivityTransactions.getLogIn;
import static com.myrole.utils.AppPreferences.isLogin;

public class StatusFeedAdapter extends RecyclerView.Adapter<StatusFeedAdapter.FeedsViewHolder> {

    public Context mCtx;
    private ArrayList<PostListDTO> mFeedList;
    Cache cache;
    InvokedFeedsFunctions mInvokeCommentSheet;


    public StatusFeedAdapter(Context context, ArrayList<PostListDTO> dataList, StatusFeedFragment activity) {
        this.mCtx = context;
        mInvokeCommentSheet = activity;
        this.mFeedList = dataList;

    }

    @NonNull
    @Override
    public FeedsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.status_item_layout, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        FeedsViewHolder viewHolder = new FeedsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedsViewHolder holder, int position) {
        PostListDTO pto = mFeedList.get(position);
        if (BuildConfig.DEBUG)
            Log.e("resp : " + position, " : " + new Gson().toJson(pto));

        Glide.with(mCtx).load(pto.getThumbnail())
                .centerCrop().into(holder.v_thumbnail);
        holder.txt_num_comment.setText(String.valueOf(pto.getComment_count()));
        holder.txt_user_name.setText(pto.getOwner_name());
        holder.tvDownloadCount.setText(pto.total_download + "");
        holder.tvShareCount.setText(pto.total_share + "");

        holder.txt_time.setVisibility(View.GONE);

        String userID = AppPreferences.getAppPreferences(mCtx).getStringValue(AppPreferences.USER_ID);

        // if (pto != null && (pto.getOwner_id() == Integer.parseInt(userID))) {
        holder.ivFollow.setVisibility(View.GONE);
       /* } else {
            holder.ivFollow.setVisibility(View.VISIBLE);
        }*/

        if (pto != null && pto.getOwner_image() != null) {
            Picasso.get().load(pto.getOwner_image()).into(holder.usericon);
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
                        new StatusFeedAdapter.LikeTask1().execute(userId, String.valueOf(mFeedList.get(position).getPost_id()), String.valueOf(mFeedList.get(position).getOwner_id()), "1", position + "");
                    }
                } else getLogIn(mCtx);
            }
        });

        holder.linearComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(mCtx)) {
                    if (((UserStatusActivity) mCtx).fragmentManager != null) {
                        mInvokeCommentSheet.showCommentSheet(String.valueOf(mFeedList.get(position).getPost_id()), String.valueOf(position));
                    }
                } else getLogIn(mCtx);
            }
        });

        holder.linearDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(mCtx)) {
                    if (Utils.isNetworkConnected(mCtx, true)) {
                        mInvokeCommentSheet.downloadVideo(mFeedList.get(position).getDownload(), mFeedList.get(position).id, position);
                    }
                } else getLogIn(mCtx);
            }
        });

        holder.linearShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(mCtx)) {
                    if (((UserStatusActivity) mCtx).fragmentManager != null) {
                        mInvokeCommentSheet.sharedVideo(String.valueOf(mFeedList.get(position).getDownload()), mFeedList.get(position).id, position);
                    }
                } else getLogIn(mCtx);
            }
        });

        holder.relativeUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin(mCtx)) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(mFeedList.get(position).getOwner_id()));
                    ((UserStatusActivity) mCtx).fragmentManager.beginTransaction()
                            .replace(R.id.main,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                } else getLogIn(mCtx);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    public void addData(ArrayList<PostListDTO> mFeedList) {
        this.mFeedList = mFeedList;
    }

    static class FeedsViewHolder extends RecyclerView.ViewHolder {
        public ImageView mThumbnail, usericon, img_love, ivFollow, ivDisc, v_thumbnail;
        public TextView mInfo, txt_user_name, txt_time, txt_num_loves, txt_num_comment, txt_description, tvDownloadCount, tvShareCount;

        public LinearLayout linearHeart, linearDownload, linearComments, linearShare;
        public PlayerView playerView;
        public RelativeLayout relativeUserProfile;

        public FeedsViewHolder(View itemView) {
            super(itemView);
            ivDisc = itemView.findViewById(R.id.ivDisc);
            ivDisc.setVisibility(View.GONE);

            v_thumbnail = (ImageView) itemView.findViewById(R.id.v_thumbnail);
            ivFollow = (ImageView) itemView.findViewById(R.id.ivFollow);
            relativeUserProfile = (RelativeLayout) itemView.findViewById(R.id.relative_image_container);
            linearShare = (LinearLayout) itemView.findViewById(R.id.linear_share);
            linearComments = (LinearLayout) itemView.findViewById(R.id.linear_comment);
            tvDownloadCount = (TextView) itemView.findViewById(R.id.tv_download_count);
            tvShareCount = (TextView) itemView.findViewById(R.id.tv_share_count);
            linearDownload = (LinearLayout) itemView.findViewById(R.id.linear_download);
            playerView = itemView.findViewById(R.id.playerview);
            linearHeart = (LinearLayout) itemView.findViewById(R.id.linear_heart);
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

    class DownloadFile_video extends AsyncTask<String, Integer, Long> {
        ProgressDialog mProgressDialog = new ProgressDialog(mCtx);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Downloading");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }

        @Override
        protected Long doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL((String) aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                String uniqueId = UUID.randomUUID().toString();
                String targetFileName = uniqueId + ".mp4";//Change name and subname
                int lenghtOfFile = conexion.getContentLength();
//                String PATH = Environment.getExternalStorageDirectory() + "/" + "MyRoleApp" + "/" + "Images" + "/";
                String PATH = Environment.getExternalStorageDirectory() + "/" + "MyRoleApp" + "/" + "Videos" + "/";
                File folder = new File(PATH);
                if (!folder.exists()) {
                    folder.mkdir();//If there is no folder it will be created.
                }
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(PATH + targetFileName);
                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressDialog.setProgress(progress[0]);
            if (mProgressDialog.getProgress() == mProgressDialog.getMax()) {
                mProgressDialog.dismiss();
                Toast.makeText(mCtx, "File Downloaded", Toast.LENGTH_SHORT).show();
            }
        }

        protected void onPostExecute(String result) {
        }
    }
}

