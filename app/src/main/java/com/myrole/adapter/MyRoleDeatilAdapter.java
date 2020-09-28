/*
 * Copyright 2016 eneim@Eneim Labs, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myrole.adapter;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.myrole.R;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.dashboard.interfaces.InvokedFeedsFunctions;
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

public class MyRoleDeatilAdapter extends RecyclerView.Adapter<MyRoleDeatilAdapter.FeedsViewHolder> {

    public Context mCtx;
    private ArrayList<PostListDTO> mFeedList;
    Cache cache;
    InvokedFeedsFunctions mInvokeCommentSheet ;

    public MyRoleDeatilAdapter(Context context, ArrayList<PostListDTO> dataList, InvokedFeedsFunctions activity) {
        this.mCtx = context;
        mInvokeCommentSheet = activity ;
        this.mFeedList = dataList;
        if(cache == null)
        cache = new SimpleCache(new File(context.getCacheDir(), "exCache"), new LeastRecentlyUsedCacheEvictor(1024 * 1024 * 10));
    }

    @NonNull
    @Override
    public MyRoleDeatilAdapter.FeedsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vh_fb_feed_post_video, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        MyRoleDeatilAdapter.FeedsViewHolder viewHolder = new MyRoleDeatilAdapter.FeedsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRoleDeatilAdapter.FeedsViewHolder holder, int position) {
        PostListDTO pto = mFeedList.get(position);
        holder.txt_num_comment.setText(String.valueOf(pto.getComment_count()));
        holder.txt_user_name.setText(pto.getOwner_name());
        holder.txt_time.setText(Utils.timeCalculation(pto.getPost_time()));
        
        if (pto != null && pto.getOwner_image() != null) {
            Picasso.get().load(pto.getOwner_image()).into(holder.usericon);
        } else {
            Picasso.get().load(R.drawable.default_avatar).into(holder.usericon);
        }
        Log.e("is liked ", "");
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
                    new MyRoleDeatilAdapter.LikeTask1().execute(userId, String.valueOf(mFeedList.get(position).getPost_id()), String.valueOf(mFeedList.get(position).getOwner_id()), "1", position + "");
                }
            }
        });

        holder.linearDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mFeedList.get(position).getDownload();
                Log.e("url download ", " : " + url);
                if (Utils.isNetworkConnected(mCtx, true)) {
                    new MyRoleDeatilAdapter.DownloadFile_video().execute(url);
                }
            }
        });

        holder.linearComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((MainDashboardActivity) mCtx).fragmentManager != null) {
                    mInvokeCommentSheet.showCommentSheet(String.valueOf(mFeedList.get(position).getPost_id()), String.valueOf(position));
                }
            }
        });

        setPlayer(pto, holder, position);
    }

    public void setPlayer(PostListDTO item, MyRoleDeatilAdapter.FeedsViewHolder holder, int pos) {
        // final PostListDTO item = mFeedList.get(currentPage);

        //Minimum Video you want to buffer while Playing
        int MIN_BUFFER_DURATION = 2000;
        //Max Video you want to buffer during PlayBack
        int MAX_BUFFER_DURATION = 8000;
        //Min Video you want to buffer before start Playing it
        int MIN_PLAYBACK_START_BUFFER = 2000;
        //Min video You want to buffer when user resumes video
        int MIN_PLAYBACK_RESUME_BUFFER = 2000;

        LoadControl loadControl = new DefaultLoadControl.Builder()
                .setAllocator(new DefaultAllocator(true, 16))
                .setBufferDurationsMs(MIN_BUFFER_DURATION, MAX_BUFFER_DURATION, MIN_PLAYBACK_START_BUFFER, MIN_PLAYBACK_RESUME_BUFFER)
                .createDefaultLoadControl();

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mCtx, trackSelector, loadControl);

        DataSource dataSource = new DefaultDataSource(mCtx, Util.getUserAgent(mCtx, "MyRole"), false);

        CacheDataSource cacheDataSource = new CacheDataSource(cache, dataSource);
        HlsDataSourceFactory fg = new HlsDataSourceFactory() {
            @Override
            public DataSource createDataSource(int dataType) {
                return cacheDataSource;
            }
        };

        MediaSource videoSource = new HlsMediaSource.Factory(fg)
                .createMediaSource(Uri.parse(item.getUrl()));

        Log.e("resp", item.getUrl());

        player.prepare(videoSource);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);

        holder.playerView.setPlayer(player);
        player.setPlayWhenReady(false);
        player.seekTo(4000);
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    class FeedsViewHolder extends RecyclerView.ViewHolder {
        public ImageView mThumbnail, usericon, img_love, img_comment, icon_for_click, ivDisc;
        public TextView mInfo, txt_user_name, txt_time, txt_num_loves, txt_num_comment, txt_description, tvDownloadCount;

        public LinearLayout linearHeart, linearDownload, linearComments;
        public PlayerView playerView;

        public FeedsViewHolder(View itemView) {
            super(itemView);
            ivDisc = itemView.findViewById(R.id.ivDisc);

            RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(10000);
            rotate.setRepeatCount(Animation.INFINITE);
            rotate.setFillAfter(true);
            rotate.setInterpolator(new LinearInterpolator());
            ivDisc.startAnimation(rotate);
            linearComments = (LinearLayout) itemView.findViewById(R.id.linear_comment);
            tvDownloadCount = (TextView) itemView.findViewById(R.id.tv_download_count);
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

        /*implements OrderedPlayList, VideoPlayerManager, View.OnClickListener {
    private QuickAction quickAction;
    private OnItemClickListener clickListener;
    protected final VideoPlayerManager delegate;
    MyRoleDeatilPostFragment gffragment;
    Context mContext;
    Activity activity;
    ArrayList<PostListDTO> post_list;
    RecyclerView.ViewHolder holder;
    private String shortUrl,longUrl;
//  private static final int REQUEST_ADD_COMMENTS = 678;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.clickListener = onItemClickListener;
    }

    AppPreferences preferences;
    protected List<SimpleVideoObject> mVideos1;
    protected List<SimpleVideoObject> mVideos = new ArrayList<>();

    public MyRoleDeatilAdapter(Context mContext, Activity activity, ArrayList<PostListDTO> post_list, List<SimpleVideoObject> mVideos1, MyRoleDeatilPostFragment gffragment) {
        super();
        this.mContext = mContext;
        this.activity = activity;
        this.post_list = post_list;
        this.gffragment = gffragment;
        delegate = new VideoPlayerManagerImpl();
        setHasStableIds(true);
        this.mVideos1 = mVideos1;
        preferences = AppPreferences.getAppPreferences(mContext);

    }

    public MyRoleDeatilAdapter(Context mContext) {
        this.mContext = mContext;
        delegate = new VideoPlayerManagerImpl();

    }

    @Nullable
    public Object getItem(int position) {
        return mVideos1.get((position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        String url = post_list.get(position).getUrl();
        Log.e("Sub>>", url.substring(url.length() - 4, url.length()));
        if (url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".mp4")) {
            return FbItemViewHolder.POST_TYPE_VIDEO;
        } else
            return FbItemViewHolder.POST_TYPE_PHOTO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      @FbItemViewHolder.PostType int viewType) {
        final RecyclerView.ViewHolder viewHolder = FbItemViewHolder.createViewHolder(parent, viewType);
        if (viewHolder instanceof FbItemViewHolder.VideoPost) {
            ((FbItemViewHolder.VideoPost) viewHolder).setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getAdapterPosition();
                    if (pos == RecyclerView.NO_POSITION || clickListener == null) {
                        return;
                    }

                    //clickListener.onItemClick(GeneralMyRoleDeatilAdapter.this, viewHolder, v, pos, getItemId(pos));
                }
            });
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FbItemViewHolder.PhotoPost) {
            ((FbItemViewHolder.PhotoPost) holder).setContructor(mContext);
            ((FbItemViewHolder.PhotoPost) holder).bind(getItem(position));
            ((FbItemViewHolder.PhotoPost) holder).txt_num_loves.setText(String.valueOf(post_list.get(position).getPost_likes()));
            if(post_list.get(position).getIslike()==1){
                ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
            }
            else {
                ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
            }
            ((FbItemViewHolder.PhotoPost) holder).img_love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = AppPreferences.getAppPreferences(mContext).getStringValue(AppPreferences.USER_ID);
                    if (post_list.get(position).getIslike() == 0) {
                        post_list.get(position).setPost_likes(post_list.get(position).getPost_likes() + 1);
                        ((FbItemViewHolder.PhotoPost) holder).txt_num_loves.setText(String.valueOf(post_list.get(position).getPost_likes()));
                        ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
                        post_list.get(position).setIslike(1);
                    } else {
                        post_list.get(position).setPost_likes(post_list.get(position).getPost_likes() - 1);
                        ((FbItemViewHolder.PhotoPost) holder).txt_num_loves.setText(String.valueOf(post_list.get(position).getPost_likes()));
                        ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
                        post_list.get(position).setIslike(0);
                    }
//                    notifyDataSetChanged();
//                    ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
                    new LikeTask1().execute(userId, String.valueOf(post_list.get(position).getPost_id()), String.valueOf(post_list.get(position).getOwner_id()), "1", position + "");


                }
            });

            ((FbItemViewHolder.PhotoPost) holder).lay_num_loves.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (((HomeActivity) mContext).fragmentManager != null) {
                        ((HomeActivity) mContext).fragmentManager.beginTransaction()
                                .add(R.id.main_frame,
                                        LikerFragment.newInstance(String.valueOf(post_list.get(position).getPost_id())),
                                        "LIKERFRAGMENT")
                                .addToBackStack("LIKERFRAGMENT_myrole")
                                .commit();
                    }

                }
            });

            ((FbItemViewHolder.PhotoPost) holder).txt_num_comment.setText(String.valueOf(post_list.get(position).getComment_count()));
//            ((FbItemViewHolder.PhotoPost) holder).img_main.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    System.out.println("post_id:-" + post_list.get(position).getPost_id());
//                    gffragment.PostDetails(String.valueOf(post_list.get(position).getPost_id()));
//                }
//            });
            ((FbItemViewHolder.PhotoPost) holder).lay_num_comm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (((HomeActivity) mContext).fragmentManager != null) {
                        ((HomeActivity) mContext).fragmentManager.beginTransaction()
                                .add(R.id.main_frame,
                                        CommentsFragment.newInstance(String.valueOf(post_list.get(position).getPost_id()), String.valueOf(position)),
                                        "COMMENTSFRAGMENT")
                                .addToBackStack("COMMENTSFRAGMENT_myrole")
                                .commit();
                    }

//                    Intent in_comment = new Intent(mContext, AddComments.class);
//                    in_comment.putExtra("post_id", String.valueOf(post_list.get(position).getPost_id()));
//                    in_comment.putExtra("position",String.valueOf(position));
//                    mContext.startActivity(in_comment);
//                  activity.startActivityForResult(in_comment,REQUEST_ADD_COMMENTS);
                }
            });
            ((FbItemViewHolder.PhotoPost) holder).tv_more.setTag(position);
            ((FbItemViewHolder.PhotoPost) holder).tv_more.setOnClickListener(this);
        } else if (holder instanceof FbItemViewHolder.VideoPost) {
            ((FbItemViewHolder.VideoPost) holder).setContructor(mContext);
            ((FbItemViewHolder.VideoPost) holder).bind(this, getItem(position));
            if(mVideos1.get(position).getIslike()==1){
                ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
            }
            else {
                ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
            }
            ((FbItemViewHolder.VideoPost) holder).img_love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = AppPreferences.getAppPreferences(mContext).getStringValue(AppPreferences.USER_ID);
                    if (mVideos1.get(position).getIslike() == 0) {
                        mVideos1.get(position).setPost_likes(mVideos1.get(position).getPost_likes() + 1);
                        ((FbItemViewHolder.VideoPost) holder).txt_num_loves.setText(String.valueOf(mVideos1.get(position).getPost_likes()));
                        ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
                        mVideos1.get(position).setIslike(1);
                    } else {
                        mVideos1.get(position).setPost_likes(mVideos1.get(position).getPost_likes() - 1);
                        ((FbItemViewHolder.VideoPost) holder).txt_num_loves.setText(String.valueOf(mVideos1.get(position).getPost_likes()));
                        ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
                        mVideos1.get(position).setIslike(0);
                    }

//                    notifyDataSetChanged();
//
//                    ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
                    new LikeTask().execute(userId, String.valueOf(post_list.get(position).getPost_id()), String.valueOf(post_list.get(position).getOwner_id()), "1", position + "");


                }
            });
            ((FbItemViewHolder.VideoPost) holder).img_love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((HomeActivity) mContext).fragmentManager != null) {
                        ((HomeActivity) mContext).fragmentManager.beginTransaction()
                                .add(R.id.main_frame,
                                        LikerFragment.newInstance(String.valueOf(post_list.get(position).getPost_id())),
                                        "LIKERFRAGMENT")
                                .addToBackStack("LIKERFRAGMENT_myrole")
                                .commit();
                    }

                }
            });
            ((FbItemViewHolder.VideoPost) holder).txt_num_comment.setText(String.valueOf(post_list.get(position).getComment_count()));
            ((FbItemViewHolder.VideoPost) holder).img_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // that can stop vedio audio
                    ((FbItemViewHolder.VideoPost) holder).stop_volume();
                    //end
                    if (((HomeActivity) mContext).fragmentManager != null) {
                        ((HomeActivity) mContext).fragmentManager.beginTransaction()
                                .add(R.id.main_frame,
                                        CommentsFragment.newInstance(String.valueOf(post_list.get(position).getPost_id()), String.valueOf(position)),
                                        "COMMENTSFRAGMENT")
                                .addToBackStack("COMMENTSFRAGMENT_myrole")
                                .commit();
                    }

//                    Intent in_comment = new Intent(mContext, AddComments.class);
//                    in_comment.putExtra("post_id", String.valueOf(post_list.get(position).getPost_id()));
//                    in_comment.putExtra("position",String.valueOf(position));
//                    mContext.startActivity(in_comment);
//                  activity.startActivityForResult(in_comment,REQUEST_ADD_COMMENTS);
                }
            });
//            ((FbItemViewHolder.VideoPost) holder).rel_11.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // that can stop vedio audio
//                    ((FbItemViewHolder.VideoPost) holder).stop_volume();
//                    //end
//                    System.out.println("post_idvideo:-" + post_list.get(position).getPost_id());
//                    gffragment.PostDetails(String.valueOf(post_list.get(position).getPost_id()));
//
//                }
//            });
//
//            ((FbItemViewHolder.VideoPost) holder).videoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // that can stop vedio audio
//                    ((FbItemViewHolder.VideoPost) holder).stop_volume();
//                    //end
//                    System.out.println("post_idvideo:-" + post_list.get(position).getPost_id());
//                    gffragment.PostDetails(String.valueOf(post_list.get(position).getPost_id()));
//
//                }
//            });

            ((FbItemViewHolder.VideoPost) holder).tv_more.setTag(position);
            ((FbItemViewHolder.VideoPost) holder).tv_more.setOnClickListener(this);

        }
    }

    @Override
    public int getItemCount() {
        return post_list.size(); // magic number
    }

    @Override
    public int firstVideoPosition() {
        return 1;
    }

    @Override
    public ToroPlayer getPlayer() {
        return delegate.getPlayer();
    }

    @Override
    public void setPlayer(ToroPlayer player) {
        delegate.setPlayer(player);
    }

    @Override
    public void onRegistered() {
        delegate.onRegistered();
    }

    @Override
    public void onUnregistered() {
        delegate.onUnregistered();
    }

    @Override
    public void startPlayback() {
        delegate.startPlayback();
    }

    @Override
    public void pausePlayback() {
        delegate.pausePlayback();
    }

    @Override
    public void stopPlayback() {
        delegate.stopPlayback();
    }

    @Override
    public void saveVideoState(String videoId, @Nullable Long position, long duration) {
        delegate.saveVideoState(videoId, position, duration);
    }

    @Override
    public void restoreVideoState(String videoId) {
        delegate.restoreVideoState(videoId);
    }

    @Nullable
    @Override
    public Long getSavedPosition(String videoId) {
        return delegate.getSavedPosition(videoId);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof ToroViewHolder) {
            ((ToroViewHolder) holder).onAttachedToParent();
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof ToroViewHolder) {
            ((ToroViewHolder) holder).onDetachedFromParent();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more:
                onPopupButtonClick(v, (int) v.getTag());
                break;
        }
    }


    public void onPopupButtonClick(View button, final int position) {
        PopupMenu popup = new PopupMenu(mContext, button);
        final PostListDTO postListDTO = post_list.get(position);
        longUrl = postListDTO.getUrl();
        new newShortAsync().execute();
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
        if (postListDTO.getOwner_id() == Integer.parseInt(preferences.getStringValue(AppPreferences.USER_ID))) {
            MenuItem item2 = popup.getMenu().findItem(R.id.download);
            item2.setVisible(false);
            MenuItem item = popup.getMenu().findItem(R.id.Report);
            item.setVisible(false);
            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
            item1.setVisible(true);
        } else {
            MenuItem item2 = popup.getMenu().findItem(R.id.download);
            item2.setVisible(false);
            MenuItem item = popup.getMenu().findItem(R.id.Report);
            item.setVisible(true);
            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
            item1.setVisible(false);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Share:

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                        // Add data to the intent, the receiving app will decide
                        // what to do with it.
                        share.putExtra(Intent.EXTRA_SUBJECT, "MY Role");
                        share.putExtra(Intent.EXTRA_TEXT, shortUrl);

                        mContext.startActivity(Intent.createChooser(share, "Share link!"));

                        break;
                    case R.id.Report:
                        optionDialog(position);
                        break;
                    case R.id.delete:
                        callServiceForFeeds(post_list.get(position).getPost_id());
                        AlertDialog(position);

                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    public void optionDialog(final int pos) {

        CharSequence options[] = new CharSequence[]{"It’s Spam", "It’s Inappropriate"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setTitle("Select your option:");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on options[which]
                switch (which) {
                    case 0:
                        callServiceForReport("It’s Spam");
                        deleteRecord(pos);
                        break;
                    case 1:
                        callServiceForReport("It’s Inappropriate");
                        deleteRecord(pos);
                        break;

                }
            }
        });

        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //the user clicked on Cancel
            }
        });
        builder.show();
    }

    private void callServiceForReport(String s) {

        AppPreferences preferences = AppPreferences.getAppPreferences(mContext);
        AndroidNetworking.post(Config.REPORT_USER_POST)
                .setTag(this)
                .addBodyParameter("user_id", preferences.getStringValue(AppPreferences.USER_ID))
                .addBodyParameter(" comment", s)

                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d("TAG", " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d("TAG", " bytesSent : " + bytesSent);
                        Log.d("TAG", " bytesReceived : " + bytesReceived);
                        Log.d("TAG", " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        //dismissProgressDialog();

                        Log.d("TAG", "delete_onResponse object : " + response.toString());
                        Log.d("TAG", "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));


                    }

                    @Override
                    public void onError(ANError error) {

                        //dismissProgressDialog();
                        if (error.getErrorCode() != 0) {
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail
                            Log.d("TAG", "onError errorCode : " + error.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + error.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    public void deleteRecord(int Pos) {
        post_list.remove(Pos);
        mVideos1.remove(Pos);
        gffragment.mRecyclerView.setAdapter(new MyRoleDeatilAdapter(mContext,activity, post_list, mVideos1, gffragment));
        notifyDataSetChanged();
    }

    public void AlertDialog(final int pos) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("MY Role");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteRecord(pos);
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    class LikeTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int index;
        ProgressDialog progressDialog;

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

            return HTTPUrlConnection.getInstance().load(mContext,Config.ADD_LIKE, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // progressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {

                    Log.v("MYROLE", result);
                } else {
                    // Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class LikeTask1 extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;
        int index;
        ProgressDialog progressDialog;

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

            return HTTPUrlConnection.getInstance().load(mContext,Config.ADD_LIKE, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // progressDialog.dismiss();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {

                    Log.v("MYROLE", result);
                } else {
                    // Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void callServiceForFeeds(int post_id) {

        AppPreferences preferences = AppPreferences.getAppPreferences(mContext);
        AndroidNetworking.post(Config.DELETE_USER_POST)
                .setTag(this)
                .addBodyParameter("user_id", preferences.getStringValue(AppPreferences.USER_ID))
                .addBodyParameter("post_id", String.valueOf(post_id))

                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d("TAG", " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d("TAG", " bytesSent : " + bytesSent);
                        Log.d("TAG", " bytesReceived : " + bytesReceived);
                        Log.d("TAG", " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        //dismissProgressDialog();

                        Log.d("TAG", "delete_onResponse object : " + response.toString());
                        Log.d("TAG", "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));


                    }

                    @Override
                    public void onError(ANError error) {

                        //dismissProgressDialog();
                        if (error.getErrorCode() != 0) {
                            // received ANError from server
                            // error.getErrorCode() - the ANError code from server
                            // error.getErrorBody() - the ANError body from server
                            // error.getErrorDetail() - just a ANError detail
                            Log.d("TAG", "onError errorCode : " + error.getErrorCode());
                            Log.d("TAG", "onError errorBody : " + error.getErrorBody());
                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    class newShortAsync extends AsyncTask<Void, Void, String> {

        //            String longUrl="http://stackoverflow.com/questions/18372672/how-do-i-use-the-google-url-shortener-api-on-android/20406915";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//                progressBar.setVisibility(View.GONE);
            System.out.println("JSON RESP:" + s);
            String response = s;
            try {
                JSONObject jsonObject = new JSONObject(response);
                shortUrl = jsonObject.getString("id");
//                Toast.makeText(mContext, "ShortUrl:"+shortUrl, Toast.LENGTH_SHORT).show();
                System.out.println("ID:" + shortUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            BufferedReader reader;
            StringBuffer buffer;
            String res = null;
            String json = "{\"longUrl\": \"" + longUrl + "\"}";
            try {
                URL url = new URL("https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyAprTSscKqb7NikDmstT0R4RvkERtBzO5w");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(40000);
                con.setConnectTimeout(40000);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(json);
                writer.flush();
                writer.close();
                os.close();

                int status = con.getResponseCode();
                InputStream inputStream;
                if (status == HttpURLConnection.HTTP_OK)
                    inputStream = con.getInputStream();
                else
                    inputStream = con.getErrorStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                res = buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }
    }
}
*/