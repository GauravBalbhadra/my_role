package com.myrole.dashboard.frags;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.egl.filter.GlWatermarkFilter;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.request.DownloadRequest;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.myrole.BuildConfig;
import com.myrole.R;
import com.myrole.adapter.AdapterSahreApp;
import com.myrole.adapter.StatusFeedAdapter;
import com.myrole.adapter.VideoCategoryAdapter;
import com.myrole.dashboard.Fragment_Data_Send;
import com.myrole.dashboard.Functions;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.dashboard.ReleaseListner;
import com.myrole.dashboard.StatusVideoCategory;
import com.myrole.dashboard.UserStatusActivity;
import com.myrole.dashboard.VideoCache;
import com.myrole.dashboard.interfaces.InvokedFeedsFunctions;
import com.myrole.dashboard.interfaces.OnStatusItemSelected;
import com.myrole.dashboard.onfragmentback.RootFragment;
import com.myrole.fragments.CommentsFragment;
import com.myrole.holders.PostListDTO;
import com.myrole.model.ModelDataAddShareCount;
import com.myrole.model.ShareDownloadCountModel;
import com.myrole.networking.ApiService;
import com.myrole.networking.SongServiceClient;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.google.android.exoplayer2.C.VIDEO_SCALING_MODE_SCALE_TO_FIT;
import static com.myrole.dashboard.UserStatusActivity.buttonCancel;
import static com.myrole.dashboard.UserStatusActivity.closeShare;

public class StatusFeedFragment extends RootFragment implements
        Player.EventListener, Fragment_Data_Send, ReleaseListner, InvokedFeedsFunctions, OnStatusItemSelected, Functions.DownloadDialogListener {

    View mView;
    public static final String TAG = "Feed Screen";
    Context mContext;
    RecyclerView mRecyclerView;
    PostListDTO postListDTO;
    ArrayList<PostListDTO> mFeedList = new ArrayList<PostListDTO>();
    int mCurrentPage = -1;
    LinearLayoutManager mLayoutManager;
    ProgressBar p_bar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SimpleExoPlayer status_player;
    boolean is_visible_to_user;
    int LAST_POST_ID = 0;
    //int LIMIT = 20;
    int page_no = 0;
    String categoryID = "";
    int mWidth = 0;
    private RecyclerView recycler_video_category;
    private VideoCategoryAdapter videoCategoryAdapter;
    private List<StatusVideoCategory> videoCategoryList = new ArrayList<StatusVideoCategory>();

    private StatusFeedFragment statusFeedFragment;
    List<ResolveInfo> launchables;
    int positionSahreApp;

    private boolean isSharedDownload = false;
    public static String url = "";
    public int vID;

    boolean isLoading = false;
    TextView tvLoading;
    private DownloadRequest prDownloader;

    public StatusFeedFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        is_visible_to_user = isVisibleToUser;
        if (status_player != null && isVisibleToUser) {
            status_player.setPlayWhenReady(true);
        } else if (status_player != null && !isVisibleToUser) {
            status_player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        is_visible_to_user = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        mWidth = displayMetrics.widthPixels;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (status_player != null) {
            status_player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (status_player != null) {
            status_player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (status_player != null) {
            status_player.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (status_player != null && is_visible_to_user) {
            status_player.setPlayWhenReady(true);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.user_status_fragment, container, false);
        mContext = getContext();
        p_bar = mView.findViewById(R.id.p_bar);
        mRecyclerView = mView.findViewById(R.id.status_recycler);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        tvLoading = mView.findViewById(R.id.tv_loading);

        mView.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        recycler_video_category = mView.findViewById(R.id.recycler_video_category);
        recycler_video_category.setLayoutManager(new LinearLayoutManager
                (getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_video_category.setHasFixedSize(false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        // this is the scroll listener of recycler view which will tell the current item number
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no = scrollOffset / height;

                if (page_no != mCurrentPage && mFeedList.size() > 0) {
                    mCurrentPage = page_no;
                    releasePreviousPlayer();

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


                    status_player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);//(SimpleExoPlayer) playerView.getPlayer();
                    //playerView.setPlayer(player);
                    status_player.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    DataSource dataSource = new DefaultDataSource(getActivity(), Util.getUserAgent(getActivity(), "MyRole"), false);

                    CacheDataSource cacheDataSource = new CacheDataSource(VideoCache.getInstance(getActivity()), dataSource);
                    HlsDataSourceFactory fg = new HlsDataSourceFactory() {
                        @Override
                        public DataSource createDataSource(int dataType) {
                            return cacheDataSource;
                        }
                    };

                    MediaSource videoSource = new HlsMediaSource.Factory(fg)
                            .createMediaSource(Uri.parse(mFeedList.get(mCurrentPage).getUrl()));

                    status_player.prepare(videoSource, true, true);
                    status_player.setRepeatMode(Player.REPEAT_MODE_ALL);

                    View layout = mLayoutManager.findViewByPosition(mCurrentPage);
                    final PlayerView playerView = layout.findViewById(R.id.playerview);
                    final ImageView ivThumbnail = layout.findViewById(R.id.v_thumbnail);

                    playerView.setPlayer(status_player);
                    status_player.setPlayWhenReady(is_visible_to_user);
                    // playerView.setKeepContentOnPlayerReset(true);
                    playerView.getPlayer().addListener(new Player.EventListener() {
                        @Override
                        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

                        }

                        @Override
                        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                        }

                        @Override
                        public void onLoadingChanged(boolean isLoading) {
                        }

                        @Override
                        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                            if (playbackState == Player.STATE_BUFFERING) {
                                p_bar.setVisibility(View.VISIBLE);
                            } else if (playbackState == Player.STATE_READY) {
                                p_bar.setVisibility(View.GONE);
                                ivThumbnail.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onIsPlayingChanged(boolean isPlaying) {

                        }

                        @Override
                        public void onRepeatModeChanged(int repeatMode) {

                        }

                        @Override
                        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                        }

                        @Override
                        public void onPlayerError(ExoPlaybackException error) {

                        }

                        @Override
                        public void onPositionDiscontinuity(int reason) {

                        }

                        @Override
                        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                        }

                        @Override
                        public void onSeekProcessed() {

                        }
                    });

                    // setPlayer(mCurrentPage);

                }

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mFeedList.size() - 1) {
                        tvLoading.setVisibility(View.VISIBLE);
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });

        mSwipeRefreshLayout = mView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, 200);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.black);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = -1;
                page_no = 0;
                categoryID = "";
                if (Utils.isNetworkConnected(getContext(), true)) {
                    callServiceForVideoByCategory(true, getActivity(), 1);
                }
            }
        });

        if (Utils.isNetworkConnected(getContext(), true)) {
            callServiceForVideoByCategory(false, getActivity(), 1);
        }

        return mView;
    }

    private void loadMore() {
        page_no = page_no + 1;
        if (Utils.isNetworkConnected(getContext(), true)) {
            callServiceForVideoByCategory(false, getActivity(), 1);
        }
    }

    public void releasePreviousPlayer() {
        if (status_player != null) {
            status_player.setPlayWhenReady(false);
            status_player.removeListener(this);
            status_player.release();
        }
    }

    public void callServiceForVideoCategory(Context activity) {
        final AppPreferences preferences = AppPreferences.getAppPreferences(activity);
        AndroidNetworking.post(Config.STATUS_CATEGORY)
                .setTag(this)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        String user_id = preferences.getStringValue(AppPreferences.USER_ID);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject response2 = new JSONObject(response);
                            if (response2.getString("status").equalsIgnoreCase("true")) {
                                JSONArray jsonArray = response2.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    StatusVideoCategory statusVideoCategory = new StatusVideoCategory();
                                    statusVideoCategory.setVideo_category_id(object.getString("id"));
                                    statusVideoCategory.setVideo_category_name(object.getString("name"));
                                    videoCategoryList.add(statusVideoCategory);
                                }

                                videoCategoryAdapter = new VideoCategoryAdapter(StatusFeedFragment.this, videoCategoryList, getActivity());
                                recycler_video_category.setAdapter(videoCategoryAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("error ", " : " + error.getErrorBody());
                    }
                });
    }

    @Override
    public void onDataSent(String yourData) {

    }

    boolean isFirstInstance = true;
    StatusFeedAdapter mAdap;

    public void callServiceForVideoByCategory(final boolean isSwipeToRefresh, Context activity, int position) {
        final AppPreferences preferences = AppPreferences.getAppPreferences(activity);
        AndroidNetworking.post(Config.GET_STATUS_VIDEOS)
                .setTag(this)
                .addBodyParameter("perPage", "50")
                .addBodyParameter("currentPage", "" + page_no)
                .addBodyParameter("category", categoryID)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        String user_id = preferences.getStringValue(AppPreferences.USER_ID);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject response2 = new JSONObject(response);

                            Log.e("resp  : ", " : " + response2);
                            if (isSwipeToRefresh) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            } else {
                                p_bar.setVisibility(View.GONE);
                            }

                            if (response2.getString("status").equalsIgnoreCase("true")) {
                                postListDTO = new Gson().fromJson(response, PostListDTO.class);
                                if (postListDTO.getData().size() > 0) {
                                    /*if (mFeedList.size() > 0) {
                                        mFeedList.addAll(mFeedList = postListDTO.getData());
                                    } else {
                                        mFeedList = postListDTO.getData();
                                    }

                                    if (mFeedList.size() > 0) {
                                        StatusFeedAdapter mAdap = new StatusFeedAdapter(mContext, mFeedList, StatusFeedFragment.this);
                                        mRecyclerView.setAdapter(mAdap);
                                    } else {
                                        Toast.makeText(activity, "No Status Data Available", Toast.LENGTH_SHORT).show();
                                    }*/

                                    if (isFirstInstance) {
                                        mFeedList = new ArrayList<>();
                                        mFeedList.addAll(postListDTO.getData());
                                        mAdap = new StatusFeedAdapter(mContext, mFeedList, StatusFeedFragment.this);
                                        mRecyclerView.setAdapter(mAdap);
                                        isFirstInstance = false;
                                    } else {
                                        mFeedList.addAll(postListDTO.getData());
                                        mAdap.addData(mFeedList);
                                    }
                                    mAdap.notifyDataSetChanged();
                                    Log.d("Adapter", String.valueOf(mFeedList.size()));
                                }
                            }

                            if (tvLoading.isShown()) {
                                tvLoading.setVisibility(View.GONE);
                                isLoading = false;
                            }


                            if (videoCategoryList.size() == 0) {
                                if (Utils.isNetworkConnected(getContext(), true)) {
                                    callServiceForVideoCategory(getActivity());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (isSwipeToRefresh) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            p_bar.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        // p_bar.setVisibility(View.GONE);

                        if (isSwipeToRefresh) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            p_bar.setVisibility(View.GONE);
                        }

                        if (tvLoading.isShown()) {
                            tvLoading.setVisibility(View.GONE);
                            isLoading = false;
                        }
                    }
                });
    }

    @Override
    public void onEvent(SimpleExoPlayer data) {
        status_player = data;
    }

    @Override
    public void showCommentSheet(String id, String pos) {
        status_player.setPlayWhenReady(false);
        CommentsFragment commentBottomSheet =
                CommentsFragment.newInstance(id, pos, this);
        commentBottomSheet.show(((UserStatusActivity) mContext).fragmentManager,
                "CBS");
    }

    @Override
    public void downloadVideo(String urlData, int id, int position) {
        url = urlData;
        vID = id;
        isSharedDownload = false;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( //Method of Fragment
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101
            );
        } else {
            try {
                Save_Video(url, vID, position);
                //addDownloadCount(vID);
                requestUpdateDownload(vID);
                int count = mFeedList.get(position).total_download;
                mFeedList.get(position).total_download = count + 1;
                //mFeedList.get(position).setDownload_cnt(count + 1);
                mAdap.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addDownloadCount(int id) {
        String userId = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        Retrofit retrofit = SongServiceClient.getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap();
        map.put("post_id", "" + id);
        map.put("user_id", userId);

        Call<ModelDataAddShareCount> call = apiService.addDownloadCount(map);
        call.enqueue(new Callback<ModelDataAddShareCount>() {
            @Override
            public void onResponse(Call<ModelDataAddShareCount> call, Response<ModelDataAddShareCount> response) {
                //Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ModelDataAddShareCount> call, Throwable t) {

            }
        });

    }

    void requestUpdateShareCount(int id) {
        String userId = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        Retrofit retrofit = SongServiceClient.getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap();
        map.put("user_id", userId);
        map.put("video_id", "" + id);
        map.put("type", "share");

        Call<ShareDownloadCountModel> call = apiService.getShareCount(map);
        call.enqueue(new Callback<ShareDownloadCountModel>() {
            @Override
            public void onResponse(Call<ShareDownloadCountModel> call, Response<ShareDownloadCountModel> response) {
                Log.d("resp", "true download video id " + id + " userid " + userId + " " + new Gson().toJson(response.body()));

            }

            @Override
            public void onFailure(Call<ShareDownloadCountModel> call, Throwable t) {
                Log.d("resp", "error ");

            }
        });

    }

    void requestUpdateDownload(int id) {
        String userId = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        Retrofit retrofit = SongServiceClient.getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap();
        map.put("user_id", userId);
        map.put("video_id", "" + id);
        map.put("type", "download");

        Call<ShareDownloadCountModel> call = apiService.getDownloadCount(map);
        call.enqueue(new Callback<ShareDownloadCountModel>() {
            @Override
            public void onResponse(Call<ShareDownloadCountModel> call, Response<ShareDownloadCountModel> response) {
                //Toast.makeText(getContext(), "ResPonse : "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("resp", "true download video id " + id + " userid " + userId + " " + new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<ShareDownloadCountModel> call, Throwable t) {

                Log.d("resp", "error");
            }
        });

    }

    public void Save_Video(final String url, int id, int position1) {
        Functions.Show_determinent_loader(mContext, false, false, this);
        PRDownloader.initialize(getActivity().getApplicationContext());
        String tempPath = Environment.getExternalStorageDirectory() + "/MyRole/" + id + "no_watermark" + ".mp4";
        prDownloader = PRDownloader.download(url, Environment.getExternalStorageDirectory() + "/MyRole/", id + "no_watermark" + ".mp4")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setMessage("Are you sure want to cancel?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Functions.cancel_determinent_loader();
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(com.downloader.Progress progress) {
                        int prog = (int) ((progress.currentBytes * 100) / progress.totalBytes);
                        //Functions.Show_loading_progress(prog / 2);
                        Functions.Show_loading_progress(prog);
                    }

                });


        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                positionSahreApp = position1;

                //TODO NEw
                Bitmap myLogo = ((BitmapDrawable) getResources().getDrawable(R.drawable.logo_for_video)).getBitmap();
                Log.d("resp", " wid " + myLogo.getWidth() + " hei " + myLogo.getHeight());
                Bitmap bitmap_resize = Bitmap.createScaledBitmap(myLogo, 100, 80, true);
                GlWatermarkFilter filter = new GlWatermarkFilter(bitmap_resize, GlWatermarkFilter.Position.LEFT_TOP);
                new GPUMp4Composer(Environment.getExternalStorageDirectory() + "/MyRole/" + id + "no_watermark" + ".mp4",
                        Environment.getExternalStorageDirectory() + "/MyRole/" + id + ".mp4")
                        .filter(filter)

                        .listener(new GPUMp4Composer.Listener() {
                            @Override
                            public void onProgress(double progress) {
                                //Functions.Show_loading_progress((int) ((progress * 100) / 2) + 50);
                            }

                            @Override
                            public void onCompleted() {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Delete_file_no_watermark(id);
                                        if (isSharedDownload) {
                                            ResolveInfo launchable = launchables.get(positionSahreApp);
                                            ActivityInfo activity = launchable.activityInfo;
                                            ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);

                                            if (getContext() != null) {
                                                File file = new File(Environment.getExternalStorageDirectory() + "/MyRole/" + id + ".mp4");
                                                Uri u = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                shareIntent.setComponent(new ComponentName(launchable.activityInfo.packageName, launchable.activityInfo.name));
                                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                shareIntent.setAction(Intent.ACTION_SEND);
                                                shareIntent.addCategory(Intent.ACTION_SEND);
                                                shareIntent.putExtra(Intent.EXTRA_STREAM, u);
                                                shareIntent.setType("video/*");
                                                startActivity(shareIntent);
                                                Functions.cancel_determinent_loader();
                                            }
                                        } else {
                                            Functions.cancel_determinent_loader();
                                            Scan_file(id);
                                            Toast.makeText(getActivity(), "Video download successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCanceled() {
                                Log.d("resp", "onCanceled");
                            }

                            @Override
                            public void onFailed(Exception exception) {

                                Functions.cancel_determinent_loader();
                                Log.d("resp", exception.toString());

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Delete_file_no_watermark(id);
                                            Functions.cancel_determinent_loader();
                                            Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {

                                        }
                                    }
                                });

                            }
                        })
                        .start();
            }

            @Override
            public void onError(Error error) {
                Delete_file_no_watermark(id);
                //Log.e("download error ", " : " + error.getResponseCode());
                Toast.makeText(mContext, "Error"+error.getServerErrorMessage(), Toast.LENGTH_SHORT).show();
                Functions.cancel_determinent_loader();
            }
        });
    }

    /*

    public void Save_Video(final String url, int id) {
        Functions.Show_determinent_loader(mContext, false, false, this);
        PRDownloader.initialize(getActivity().getApplicationContext());
        String tempPath = Environment.getExternalStorageDirectory() + "/MyRole/" + id + "no_watermark" + ".mp4";
        prDownloader = PRDownloader.download(url, Environment.getExternalStorageDirectory() + "/MyRole/", id + "no_watermark" + ".mp4")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setMessage("Are you sure want to cancel?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Functions.cancel_determinent_loader();
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(com.downloader.Progress progress) {
                        int prog = (int) ((progress.currentBytes * 100) / progress.totalBytes);
                        //Functions.Show_loading_progress(prog / 2);
                        Functions.Show_loading_progress(prog);
                    }

                });


        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                //TODO NEw
                Bitmap myLogo = ((BitmapDrawable) getResources().getDrawable(R.drawable.water_mark)).getBitmap();
                Log.d("resp", " wid " + myLogo.getWidth() + " hei " + myLogo.getHeight());
                Bitmap bitmap_resize = Bitmap.createScaledBitmap(myLogo, 100, 80, true);
                GlWatermarkFilter filter = new GlWatermarkFilter(bitmap_resize, GlWatermarkFilter.Position.LEFT_TOP);
                new GPUMp4Composer(Environment.getExternalStorageDirectory() + "/MyRole/" + id + "no_watermark" + ".mp4",
                        Environment.getExternalStorageDirectory() + "/MyRole/" + id + ".mp4")
                        .filter(filter)

                        .listener(new GPUMp4Composer.Listener() {
                            @Override
                            public void onProgress(double progress) {
                                //Functions.Show_loading_progress((int) ((progress * 100) / 2) + 50);
                            }

                            @Override
                            public void onCompleted() {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Delete_file_no_watermark(id);
                                        if (isSharedDownload) {
                                            ResolveInfo launchable = launchables.get(positionSahreApp);
                                            ActivityInfo activity = launchable.activityInfo;
                                            ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                                            if (getContext() != null) {
                                                File file = new File(Environment.getExternalStorageDirectory() + "/MyRole/" + id + ".mp4");
                                                Uri u = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                shareIntent.setComponent(new ComponentName(launchable.activityInfo.packageName, launchable.activityInfo.name));
                                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                shareIntent.setAction(Intent.ACTION_SEND);
                                                shareIntent.addCategory(Intent.ACTION_SEND);
                                                shareIntent.putExtra(Intent.EXTRA_STREAM, u);
                                                shareIntent.setType("video/*");
                                                startActivity(shareIntent);
                                                Functions.cancel_determinent_loader();
                                            }
                                        } else {
                                            Functions.cancel_determinent_loader();
                                            Scan_file(id);
                                            Toast.makeText(getActivity(), "Video download successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCanceled() {
                                Log.d("resp", "onCanceled");
                            }

                            @Override
                            public void onFailed(Exception exception) {

                                Functions.cancel_determinent_loader();
                                Log.d("resp", exception.toString());

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Delete_file_no_watermark(id);
                                            Functions.cancel_determinent_loader();
                                            Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {

                                        }
                                    }
                                });

                            }
                        })
                        .start();
            }

            @Override
            public void onError(Error error) {
                Delete_file_no_watermark(id);
                //Log.e("download error ", " : " + error.getResponseCode());
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                Functions.cancel_determinent_loader();
            }
        });
    }
*/

    public void Delete_file_no_watermark(int id) {
        File file = new File(Environment.getExternalStorageDirectory() + "/MyRole/" + id + "no_watermark" + ".mp4");
        if (file.exists()) {
            file.delete();
        }
    }

    public void ApplyWatermark(int id) {
        Bitmap myLogo = ((BitmapDrawable) getResources().getDrawable(R.drawable.water_mark)).getBitmap();
        Bitmap bitmap_resize = Bitmap.createScaledBitmap(myLogo, 100, 80, true);
        GlWatermarkFilter filter = new GlWatermarkFilter(bitmap_resize, GlWatermarkFilter.Position.LEFT_TOP);
        new GPUMp4Composer(Environment.getExternalStorageDirectory() + "/MyRole/" + id + "no_watermark" + ".mp4",
                Environment.getExternalStorageDirectory() + "/MyRole/" + id + ".mp4")
                .filter(filter)

                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Functions.Show_loading_progress((int) ((progress * 100) / 2) + 50);
                    }

                    @Override
                    public void onCompleted() {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Functions.cancel_determinent_loader();
                                Delete_file_no_watermark(id);
                                if (isSharedDownload) {
                                    shareVideo(id);
                                } else {
                                    Scan_file(id);
                                    Toast.makeText(getActivity(), "Video download successfully", Toast.LENGTH_LONG).show();
                                }
                                isSharedDownload = false;
                            }
                        });
                    }

                    @Override
                    public void onCanceled() {
                        Log.d("resp", "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        Log.d("resp", exception.toString());

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Delete_file_no_watermark(id);
                                    Functions.cancel_determinent_loader();
                                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {

                                }
                            }
                        });

                    }
                })
                .start();
    }

    public void Scan_file(int item) {
        MediaScannerConnection.scanFile(getActivity(),
                new String[]{Environment.getExternalStorageDirectory() + "/MyRole/" + item + ".mp4"},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        if (BuildConfig.DEBUG) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }

                    }
                });
    }

    public void shareVideo(final int id) {
        if (getContext() != null) {
            File file = new File(Environment.getExternalStorageDirectory() + "/MyRole/" + id + ".mp4");
            Uri u = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
            Intent shareIntent = new Intent();
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, u);
            shareIntent.setType("video/*");
            startActivity(Intent.createChooser(shareIntent, "MyRole"));
        }
    }

    @Override
    public void sharedVideo(String urlData, int id, int position) {
        url = urlData;
        vID = id;
        isSharedDownload = true;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( //Method of Fragment
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101
            );
        } else {
            try {
                /*Log.d("resp", "share ");
                int count = mFeedList.get(position).total_share;
                mFeedList.get(position).total_share = (count + 1);
                mAdap.notifyDataSetChanged();
                requestUpdateShareCount(id);
                Save_Video(url, vID);*/

                customShareVideo(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void customShareVideo(int position) {
        try {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            launchables = getActivity().getPackageManager().queryIntentActivities(shareIntent, 0);

            PackageManager pm = getActivity().getPackageManager();
            Intent main = new Intent(Intent.ACTION_MAIN, null);

            main.addCategory(Intent.CATEGORY_LAUNCHER);
            AdapterSahreApp adapterSahreApp = new AdapterSahreApp(getContext(), pm, launchables);

            UserStatusActivity.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
            UserStatusActivity.recyclerView.setAdapter(adapterSahreApp);
            adapterSahreApp.setOnItemClickListener(new AdapterSahreApp.OnItemClickListener() {
                @Override
                public void onItemClick(int position1) {
                    closeShare(BottomSheetBehavior.STATE_HIDDEN);
                    positionSahreApp = position1;
                    Log.d(TAG, "onItemClick: "+url);
                    //Toast.makeText(mContext, ""+url, Toast.LENGTH_SHORT).show();
                    Save_Video(url, vID, position1);
                    requestUpdateShareCount(vID);
                    int count = mFeedList.get(position).total_share;
                    mFeedList.get(position).total_share = count + 1;
                    //mFeedList.get(position).setShare_cnt(count + 1);
                    mAdap.notifyDataSetChanged();
                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeShare(BottomSheetBehavior.STATE_HIDDEN);
                }
            });
            closeShare(BottomSheetBehavior.STATE_EXPANDED);

        } catch (Exception e) {
            Toast.makeText(mContext, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void dismissCommentSheet() {

    }

    @Override
    public void deleteVideo(String id, String pos) {

    }

    @Override
    public void onStatusCategorySelected(String id) {
        releasePreviousPlayer();
        mCurrentPage = -1;
        categoryID = id;
        page_no = 0;
        mFeedList.clear();
        if (Utils.isNetworkConnected(getContext(), true)) {
            callServiceForVideoByCategory(false, getActivity(), 1);
        }
    }

    @Override
    public void onDismissDialog() {
        if (prDownloader != null) {
            prDownloader.cancel();
        }
    }
}

