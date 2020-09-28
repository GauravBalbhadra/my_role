package com.myrole.adapter;///*
// * Copyright 2016 eneim@Eneim Labs, nam@ene.im
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *        http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.myrole.adapter;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Looper;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.PopupMenu;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.AnalyticsListener;
//import com.androidnetworking.interfaces.StringRequestListener;
//import com.myrole.AddComments;
//import com.myrole.HomeActivity;
//import com.myrole.R;
//import com.myrole.data.SimpleVideoObject;
//import com.myrole.fragments.CommentsFragment;
//import com.myrole.fragments.GeneralCategoryFeedFragment;
//import com.myrole.fragments.LikerFragment;
//import com.myrole.holders.PostListDTO;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.widget.FbItemViewHolder;
//import com.myrole.widget.OnItemClickListener;
//import com.myrole.widget.OrderedPlayList;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import im.ene.lab.toro.ToroPlayer;
//import im.ene.lab.toro.ToroViewHolder;
//import im.ene.lab.toro.VideoPlayerManager;
//import im.ene.lab.toro.VideoPlayerManagerImpl;
//
//
///**
// * Created by eneim on 5/13/16.
// */
//public class GeneralCategoryFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
//        implements OrderedPlayList, VideoPlayerManager ,View.OnClickListener {
//
//    protected final VideoPlayerManager delegate;
//    protected List<SimpleVideoObject> mVideos1;
//    protected List<SimpleVideoObject> mVideos = new ArrayList<>();
//    ArrayList<PostListDTO> postList;
//    Context mContext;
//    Activity activity;
//    GeneralCategoryFeedFragment gcFragment;
//    private OnItemClickListener clickListener;
//    private String shortUrl,longUrl;
//    public GeneralCategoryFeedAdapter(Context mContext, Activity activity, ArrayList<PostListDTO> postList, List<SimpleVideoObject> mVideos1, GeneralCategoryFeedFragment gcFragment) {
//
//        super();
//        this.mContext = mContext;
//        this.activity = activity;
//        this.postList = postList;
//        this.gcFragment = gcFragment;
//        delegate = new VideoPlayerManagerImpl();
//        setHasStableIds(true);
//        this.mVideos1 = mVideos1;
//
//    }
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.clickListener = onItemClickListener;
//    }
//
//    @Nullable
//    public Object getItem(int position) {
//
//        return mVideos1.get((position));
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return FbItemViewHolder.POST_TYPE_TEXT;
//        } else {
//            String url = postList.get(position).getUrl();
//            Log.e("Sub>>", url.substring(url.length() - 4, url.length()));
//            if (url != null && url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".mp4")) {
//                return FbItemViewHolder.POST_TYPE_VIDEO;
//            } else
//                return FbItemViewHolder.POST_TYPE_PHOTO;
//
//        }
//
//
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                      @FbItemViewHolder.PostType int viewType) {
//        final RecyclerView.ViewHolder viewHolder = FbItemViewHolder.createViewHolder(parent, viewType);
//        if (viewHolder instanceof FbItemViewHolder.VideoPost) {
//            ((FbItemViewHolder.VideoPost) viewHolder).setOnItemClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = viewHolder.getAdapterPosition();
//                    if (pos == RecyclerView.NO_POSITION || clickListener == null) {
//                        return;
//                    }
//
//                    //clickListener.onItemClick(GeneralFeedAdapter.this, viewHolder, v, pos, getItemId(pos));
//                }
//            });
//        }
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        if (holder instanceof FbItemViewHolder.PhotoPost) {
//            ((FbItemViewHolder.PhotoPost) holder).setContructor(mContext);
//            ((FbItemViewHolder.PhotoPost) holder).bind(getItem(position));
//            if (postList.get(position).getIslike() == 1) {
//                ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//            } else {
//                ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
//            }
////            ((FbItemViewHolder.PhotoPost) holder).txt_num_loves.setText(String.valueOf(postList.get(position).getPost_likes()));
////
////            ((FbItemViewHolder.PhotoPost) holder).txt_num_comment.setText(String.valueOf(postList.get(position).getComment_count()));
//            ((FbItemViewHolder.PhotoPost) holder).img_love.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String userId = AppPreferences.getAppPreferences(mContext).getStringValue(AppPreferences.USER_ID);
//                    if (postList.get(position).getIslike() == 0) {
//                        postList.get(position).setPost_likes(postList.get(position).getPost_likes() + 1);
//                        ((FbItemViewHolder.PhotoPost) holder).txt_num_loves.setText(String.valueOf(postList.get(position).getPost_likes()));
//                        ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//                        postList.get(position).setIslike(1);
//                    } else {
//                        postList.get(position).setPost_likes(postList.get(position).getPost_likes() - 1);
//                        ((FbItemViewHolder.PhotoPost) holder).txt_num_loves.setText(String.valueOf(postList.get(position).getPost_likes()));
//                        ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
//                        postList.get(position).setIslike(0);
//                    }
////                    notifyDataSetChanged();
////                    ((FbItemViewHolder.PhotoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//                    new LikeTask().execute(userId, String.valueOf(postList.get(position).getPost_id()),
//                            String.valueOf(postList.get(position).getOwner_id()), "1", position + "");
//                }
//            });
//
//            ((FbItemViewHolder.PhotoPost) holder).txt_num_comment.setText(String.valueOf(postList.get(position).getComment_count()));
//
//
//            ((FbItemViewHolder.PhotoPost) holder).lay_num_loves.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////          Intent in_likes = new Intent(mContext, LikersActivity.class);
////          in_likes.putExtra("post_id",String.valueOf(postList.get(position).getPost_id()));
////          mContext.startActivity(in_likes);
//
//                    if (((HomeActivity) mContext).fragmentManager != null) {
//                        ((HomeActivity) mContext).fragmentManager.beginTransaction()
//                                .add(R.id.main_frame,
//                                        LikerFragment.newInstance(String.valueOf(postList.get(position).getPost_id())),
//                                        "LIKERFRAGMENT")
//                                .addToBackStack("LIKERFRAGMENT")
//                                .commit();
//                    }
//                }
//            });
//            ((FbItemViewHolder.PhotoPost) holder).img_main.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    System.out.println("post_idvideo:-" + postList.get(position).getPost_id());
//                    gcFragment.PostDetails(String.valueOf(postList.get(position).getPost_id()));
//                }
//            });
//
////            ((FbItemViewHolder.PhotoPost) holder).lay_num_comm.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////
////                    Intent in_comment = new Intent(mContext, AddComments.class);
////                    in_comment.putExtra("post_id", String.valueOf(postList.get(position).getPost_id()));
////                    in_comment.putExtra("post_position", position);
////                    mContext.startActivity(in_comment);
////
////
////                }
////            });
//
//            ((FbItemViewHolder.PhotoPost) holder).lay_num_comm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    if (((HomeActivity) mContext).fragmentManager != null) {
//                        ((HomeActivity) mContext).fragmentManager.beginTransaction()
//                                .add(R.id.main_frame,
//                                        CommentsFragment.newInstance(String.valueOf(postList.get(position).getPost_id()),String.valueOf(position)),
//                                        "COMMENTSFRAGMENT")
//                                .addToBackStack("COMMENTSFRAGMENT_123")
//                                .commit();
//                    }    }
//            });
//
//            ((FbItemViewHolder.PhotoPost) holder).tv_more.setTag(position);
//            ((FbItemViewHolder.PhotoPost) holder).tv_more.setOnClickListener(this);
//        } else if (holder instanceof FbItemViewHolder.VideoPost) {
//            ((FbItemViewHolder.VideoPost) holder).setContructor(mContext);
//            ((FbItemViewHolder.VideoPost) holder).bind(this, getItem(position));
//            if (postList.get(position).getIslike() == 1) {
//                ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//            } else {
//                ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
//            }
//
//            ((FbItemViewHolder.VideoPost) holder).txt_num_comment.setText(String.valueOf(postList.get(position).getComment_count()));
//            ((FbItemViewHolder.VideoPost) holder).img_love.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    String userId = AppPreferences.getAppPreferences(mContext).getStringValue(AppPreferences.USER_ID);
//                    if (postList.get(position).getIslike() == 0) {
//                        postList.get(position).setPost_likes(postList.get(position).getPost_likes() + 1);
//                        ((FbItemViewHolder.VideoPost) holder).txt_num_loves.setText(String.valueOf(postList.get(position).getPost_likes()));
//                        ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//                        postList.get(position).setIslike(1);
//                    } else {
//                        postList.get(position).setPost_likes(postList.get(position).getPost_likes() - 1);
//                        ((FbItemViewHolder.VideoPost) holder).txt_num_loves.setText(String.valueOf(postList.get(position).getPost_likes()));
//                        ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
//                        postList.get(position).setIslike(0);
//                    }
////                    notifyDataSetChanged();
////                    ((FbItemViewHolder.VideoPost) holder).img_love.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_liked));
//                    new LikeTask().execute(userId, String.valueOf(postList.get(position).getPost_id()),
//                            String.valueOf(postList.get(position).getOwner_id()), "1", position + "");
//
//                }
//            });
//
//            ((FbItemViewHolder.VideoPost) holder).lay_num_loves.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////          Intent in_likes = new Intent(mContext, LikersActivity.class);
////          in_likes.putExtra("post_id",String.valueOf(postList.get(position).getPost_id()));
////          mContext.startActivity(in_likes);
//
//                    if (((HomeActivity) mContext).fragmentManager != null) {
//                        ((HomeActivity) mContext).fragmentManager.beginTransaction()
//                                .add(R.id.main_frame,
//                                        LikerFragment.newInstance(String.valueOf(postList.get(position).getPost_id())),
//                                        "LIKERFRAGMENT")
//                                .addToBackStack("LIKERFRAGMENT")
//                                .commit();
//                    }
//
//                }
//            });
//            ((FbItemViewHolder.VideoPost) holder).lay_num_comm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    Intent in_comment = new Intent(mContext, AddComments.class);
//                    in_comment.putExtra("post_id", String.valueOf(postList.get(position).getPost_id()));
//                    mContext.startActivity(in_comment);
//                }
//            });
//            ((FbItemViewHolder.VideoPost) holder).videoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // that can stop vedio audio
//                    ((FbItemViewHolder.VideoPost) holder).stop_volume();
//                    //end
//                    System.out.println("post_idvideo:-" + postList.get(position).getPost_id());
//                    gcFragment.PostDetails(String.valueOf(postList.get(position).getPost_id()));
//                }
//            });
//
//        } else if (holder instanceof FbItemViewHolder.TextPost) {
//            ((FbItemViewHolder.TextPost) holder).setContructor(mContext);
//            ((FbItemViewHolder.TextPost) holder).bind(getItem(position));
//            ((FbItemViewHolder.TextPost) holder).tv_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!((FbItemViewHolder.TextPost) holder).tv_dec.getText().toString().equalsIgnoreCase("No Role Found For Today")) {
//                        gcFragment.loadTodayRole();
//                    }
//                }
//            });
//            ((FbItemViewHolder.TextPost) holder).tv_dec.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!((FbItemViewHolder.TextPost) holder).tv_dec.getText().toString().equalsIgnoreCase("No Role Found For Today")) {
//                        gcFragment.loadTodayRole();
//                    }
//
//                }
//            });
//        }
//    }
//
//    @Override
//    public int getItemCount() {
////        System.out.println("post_idvideo:-" +"postlist size  "+postList.size());
//        return postList.size(); // magic number
//    }
//
//    @Override
//    public int firstVideoPosition() {
//        return 1;
//    }
//
//    @Override
//    public ToroPlayer getPlayer() {
//        return delegate.getPlayer();
//    }
//
//    @Override
//    public void setPlayer(ToroPlayer player) {
//        delegate.setPlayer(player);
//    }
//
//    @Override
//    public void onRegistered() {
//        delegate.onRegistered();
//    }
//
//    @Override
//    public void onUnregistered() {
//        delegate.onUnregistered();
//    }
//
//    @Override
//    public void startPlayback() {
//        delegate.startPlayback();
//    }
//
//    @Override
//    public void pausePlayback() {
//        delegate.pausePlayback();
//    }
//
//    @Override
//    public void stopPlayback() {
//        delegate.stopPlayback();
//    }
//
//    @Override
//    public void saveVideoState(String videoId, @Nullable Long position, long duration) {
//        delegate.saveVideoState(videoId, position, duration);
//    }
//
//    @Override
//    public void restoreVideoState(String videoId) {
//        delegate.restoreVideoState(videoId);
//    }
//
//    @Nullable
//    @Override
//    public Long getSavedPosition(String videoId) {
//        return delegate.getSavedPosition(videoId);
//    }
//
//    @Override
//    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
//        if (holder instanceof ToroViewHolder) {
//            ((ToroViewHolder) holder).onAttachedToParent();
//        }
//    }
//
//    @Override
//    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
//        if (holder instanceof ToroViewHolder) {
//            ((ToroViewHolder) holder).onDetachedFromParent();
//        }
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_more:
//                onPopupButtonClick(v, (int) v.getTag());
//                break;
//        }
//    }
//
//
//    public void onPopupButtonClick(View button, final int position) {
//        PopupMenu popup = new PopupMenu(mContext, button);
//        final PostListDTO postListDTO = postList.get(position);
//        longUrl = postListDTO.getUrl();
//        new newShortAsync().execute();
////        final String shortUrl = Utils.urlShortner(mContext,postListDTO.getUrl());
////        Toast.makeText(mContext, "LongUrl:"+longUrl, Toast.LENGTH_SHORT).show();
//        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
//        if (postListDTO.getOwner_id() == Integer.parseInt(AppPreferences.getAppPreferences(mContext).getStringValue(AppPreferences.USER_ID))) {
//            MenuItem item = popup.getMenu().findItem(R.id.Report);
//            item.setVisible(false);
//            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
//            item1.setVisible(true);
//        } else {
//            MenuItem item = popup.getMenu().findItem(R.id.Report);
//            item.setVisible(true);
//            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
//            item1.setVisible(false);
//        }
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.Share:
////                        Toast.makeText(mContext, "ShortUrl:"+shortUrl, Toast.LENGTH_SHORT).show();
//                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//                        share.setType("text/plain");
//                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//                        // Add data to the intent, the receiving app will decide
//                        // what to do with it.
//                        share.putExtra(Intent.EXTRA_SUBJECT, "MY Role");
//                        share.putExtra(Intent.EXTRA_TEXT, shortUrl);
//
//                        mContext.startActivity(Intent.createChooser(share, "Share link!"));
//
//                        break;
//                    case R.id.Report:
//                        optionDialog(position);
//                        break;
//                    case R.id.delete:
//                        callServiceForFeeds(postList.get(position).getPost_id());
//                        AlertDialog(position);
//
//                        break;
//                }
//                return true;
//            }
//        });
//
//        popup.show();
//    }
//
//    public void optionDialog(final int pos) {
//
//        CharSequence options[] = new CharSequence[]{"It’s Spam", "It’s Inappropriate"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setCancelable(true);
//        builder.setTitle("Select your option:");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // the user clicked on options[which]
//                switch (which) {
//                    case 0:
//                        callServiceForReport("It’s Spam");
//                        deleteRecord(pos);
//                        break;
//                    case 1:
//                        callServiceForReport("It’s Inappropriate");
//                        deleteRecord(pos);
//                        break;
//
//                }
//            }
//        });
//
//        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //the user clicked on Cancel
//            }
//        });
//        builder.show();
//    }
//
//    private void callServiceForReport(String s) {
//
//        AppPreferences preferences = AppPreferences.getAppPreferences(mContext);
//        AndroidNetworking.post(Config.REPORT_USER_POST)
//                .setTag(this)
//                .addBodyParameter("user_id", preferences.getStringValue(AppPreferences.USER_ID))
//                .addBodyParameter(" comment", s)
//
//                .setPriority(Priority.IMMEDIATE)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//                        Log.d("TAG", " timeTakenInMillis : " + timeTakenInMillis);
//                        Log.d("TAG", " bytesSent : " + bytesSent);
//                        Log.d("TAG", " bytesReceived : " + bytesReceived);
//                        Log.d("TAG", " isFromCache : " + isFromCache);
//                    }
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        //dismissProgressDialog();
//
//                        Log.d("TAG", "delete_onResponse object : " + response.toString());
//                        Log.d("TAG", "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
//
//
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//
//                        //dismissProgressDialog();
//                        if (error.getErrorCode() != 0) {
//                            // received ANError from server
//                            // error.getErrorCode() - the ANError code from server
//                            // error.getErrorBody() - the ANError body from server
//                            // error.getErrorDetail() - just a ANError detail
//                            Log.d("TAG", "onError errorCode : " + error.getErrorCode());
//                            Log.d("TAG", "onError errorBody : " + error.getErrorBody());
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//
//    }
//
//    public void deleteRecord(int Pos) {
//        postList.remove(Pos);
//        mVideos1.remove(Pos);
//        gcFragment.mRecyclerView.setAdapter(new GeneralCategoryFeedAdapter(mContext,activity, postList, mVideos1, gcFragment));
//        notifyDataSetChanged();
//
//    }
//
//    public void AlertDialog(final int pos) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("MY Role");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Are you sure you want delete this?");
//
//        // Setting Positive "Yes" Button
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                deleteRecord(pos);
//            }
//        });
//
//        // Setting Negative "NO" Button
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // Write your code here to invoke NO event
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//    private void callServiceForFeeds(int post_id) {
//
//        AppPreferences preferences = AppPreferences.getAppPreferences(mContext);
//        AndroidNetworking.post(Config.DELETE_USER_POST)
//                .setTag(this)
//                .addBodyParameter("user_id", preferences.getStringValue(AppPreferences.USER_ID))
//                .addBodyParameter("post_id", String.valueOf(post_id))
//
//                .setPriority(Priority.IMMEDIATE)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//                        Log.d("TAG", " timeTakenInMillis : " + timeTakenInMillis);
//                        Log.d("TAG", " bytesSent : " + bytesSent);
//                        Log.d("TAG", " bytesReceived : " + bytesReceived);
//                        Log.d("TAG", " isFromCache : " + isFromCache);
//                    }
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        //dismissProgressDialog();
//
//                        Log.d("TAG", "delete_onResponse object : " + response.toString());
//                        Log.d("TAG", "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
//
//
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//
//                        //dismissProgressDialog();
//                        if (error.getErrorCode() != 0) {
//                            // received ANError from server
//                            // error.getErrorCode() - the ANError code from server
//                            // error.getErrorBody() - the ANError body from server
//                            // error.getErrorDetail() - just a ANError detail
//                            Log.d("TAG", "onError errorCode : " + error.getErrorCode());
//                            Log.d("TAG", "onError errorBody : " + error.getErrorBody());
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d("TAG", "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//
//    }
//
//    class LikeTask extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
//        int index;
//        ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(mContext);
//            progressDialog.setCancelable(false);
//            progressDialog.setTitle("Please wait...");
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            index = Integer.parseInt(params[4]);
//            postDataParams = new HashMap<String, String>();
//            postDataParams.put("user_id", params[0]);
//            postDataParams.put("post_id", params[1]);
//            postDataParams.put("owner_id", params[2]);
//            postDataParams.put("type", params[3]);
//
//            return HTTPUrlConnection.getInstance().load(mContext, Config.ADD_LIKE, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            progressDialog.dismiss();
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//                    mVideos1.get(index).setPost_likes(mVideos1.get(index).getPost_likes() + 1);
//                    notifyDataSetChanged();
//                    Log.v("MYROLE", result);
//                } else {
//                    Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }
//
//    class newShortAsync extends AsyncTask<Void, Void, String> {
//
//        //            String longUrl="http://stackoverflow.com/questions/18372672/how-do-i-use-the-google-url-shortener-api-on-android/20406915";
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////                progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            System.out.println("JSON RESP:" + s);
//            String response = s;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                shortUrl = jsonObject.getString("id");
//                System.out.println("ID:" + shortUrl);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            BufferedReader reader;
//            StringBuffer buffer;
//            String res = null;
//            String json = "{\"longUrl\": \"" + longUrl + "\"}";
//            try {
//                URL url = new URL("https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyAprTSscKqb7NikDmstT0R4RvkERtBzO5w");
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.setReadTimeout(40000);
//                con.setConnectTimeout(40000);
//                con.setRequestMethod("POST");
//                con.setRequestProperty("Content-Type", "application/json");
//                OutputStream os = con.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(
//                        new OutputStreamWriter(os, "UTF-8"));
//
//                writer.write(json);
//                writer.flush();
//                writer.close();
//                os.close();
//
//                int status = con.getResponseCode();
//                InputStream inputStream;
//                if (status == HttpURLConnection.HTTP_OK)
//                    inputStream = con.getInputStream();
//                else
//                    inputStream = con.getErrorStream();
//
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                buffer = new StringBuffer();
//
//                String line = "";
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line);
//                }
//
//                res = buffer.toString();
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return res;
//        }
//    }
//}
