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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

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
import com.myrole.R;
import com.myrole.adapter.AdapterSahreApp;
import com.myrole.dashboard.Fragment_Data_Send;
import com.myrole.dashboard.Functions;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.dashboard.MyWatchVideoAdapter;
import com.myrole.dashboard.ReleaseListner;
import com.myrole.dashboard.VideoCache;
import com.myrole.dashboard.WatchMyVideosActivity;
import com.myrole.dashboard.interfaces.InvokedFeedsFunctions;
import com.myrole.fragments.CommentsFragment;
import com.myrole.holders.PostListDTO;
import com.myrole.model.ModelDataAddShareCount;
import com.myrole.networking.ApiService;
import com.myrole.networking.SongServiceClient;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;

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
import static com.myrole.dashboard.MainDashboardActivity.buttonCancel;
import static com.myrole.dashboard.WatchMyVideosActivity.closeShare;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MyWatchFragment extends Fragment implements Player.EventListener, Fragment_Data_Send, ReleaseListner, InvokedFeedsFunctions, Functions.DownloadDialogListener {

    public static final String TAG = "Feed Screen";
    public static String url = "";
    static MyWatchFragment fragment;
    public int vID;
    Context mContext;
    RecyclerView mRecyclerView;
    PostListDTO postListDTO;
    ArrayList<PostListDTO> mFeedList = new ArrayList<PostListDTO>();
    int mCurrentPage = -1;
    LinearLayoutManager mLayoutManager;
    ProgressBar p_bar;
    public static SimpleExoPlayer previous_player;
    boolean is_visible_to_user;
    int position = 0;
    int LIMIT = 10;
    int mWidth = 0;
    private boolean isSharedDownload = false;
    private CommentsFragment commentBottomSheet = null;
    private DownloadRequest prDownloader;
    int positionSahreApp;
    List<ResolveInfo> launchables;

    public MyWatchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mWidth = displayMetrics.widthPixels;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        is_visible_to_user = isVisibleToUser;
        if (previous_player != null && isVisibleToUser) {
            previous_player.setPlayWhenReady(true);
        } else if (previous_player != null && !isVisibleToUser) {
            previous_player.setPlayWhenReady(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_watch, container, false);
        mFeedList = WatchMyVideosActivity.mFeedList;
        position = WatchMyVideosActivity.position;
        mContext = getActivity();
        p_bar = v.findViewById(R.id.p_bar);
        mRecyclerView = v.findViewById(R.id.recylerview);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        initData();

        ((ImageView) v.findViewById(R.id.ivBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return v;
    }

    public void releasePreviousPlayer() {
        if (previous_player != null) {
            previous_player.setPlayWhenReady(false);
            previous_player.removeListener(this);
            previous_player.release();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (previous_player != null) {
            previous_player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (previous_player != null) {
            previous_player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (previous_player != null) {
            previous_player.release();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        is_visible_to_user = true;
    }

    MyWatchVideoAdapter mAdapter;
    SnapHelper snapHelper;

    public void initData() {

        mAdapter = new MyWatchVideoAdapter(getActivity(), this.mFeedList, this);
        snapHelper = new PagerSnapHelper();
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

                    previous_player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);//(SimpleExoPlayer) playerView.getPlayer();
                    DataSource dataSource = new DefaultDataSource(getActivity(), Util.getUserAgent(getActivity(), "MyRole"), false);

                    //previous_player.setVideoScalingMode(VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    CacheDataSource cacheDataSource = new CacheDataSource(VideoCache.getInstance(getActivity()), dataSource);
                    HlsDataSourceFactory fg = new HlsDataSourceFactory() {
                        @Override
                        public DataSource createDataSource(int dataType) {
                            return cacheDataSource;
                        }
                    };

                    MediaSource videoSource = new HlsMediaSource.Factory(fg)
                            .createMediaSource(Uri.parse(mFeedList.get(mCurrentPage).getUrl()));
                    previous_player.prepare(videoSource, true, true);
                    previous_player.setRepeatMode(Player.REPEAT_MODE_ALL);

                    View layout = mLayoutManager.findViewByPosition(mCurrentPage);
                    final PlayerView playerView = layout.findViewById(R.id.playerview);
                    final ImageView ivThumbnail = layout.findViewById(R.id.v_thumbnail);

                    playerView.setPlayer(previous_player);
                    previous_player.setPlayWhenReady(is_visible_to_user);
                    //playerView.setKeepContentOnPlayerReset(true);
                    playerView.getPlayer().addListener(new Player.EventListener() {
                        @Override
                        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

                        }

                        @Override
                        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                        }

                        @Override
                        public void onLoadingChanged(boolean isLoading) {
                            // Log.e("on load ", " : " + isLoading);
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

            }
        });


        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(position);
    }


    @Override
    public void onDataSent(String yourData) {
        //Log.e("ways "," to ways ");
    }

    @Override
    public void onEvent(SimpleExoPlayer data) {
        previous_player = data;
    }

    @Override
    public void showCommentSheet(String id, String pos) {
        // previous_player.setPlayWhenReady(false);
        commentBottomSheet =
                CommentsFragment.newInstance(id, pos, this);
        commentBottomSheet.show(((WatchMyVideosActivity) mContext).getSupportFragmentManager(),
                "CBS");
    }

    public void Save_Video(final String url, int id) {
        /*Functions.Show_determinent_loader(mContext, false, false, this);
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
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(com.downloader.Progress progress) {
                        int prog = (int) ((progress.currentBytes * 100) / progress.totalBytes);
                        Functions.Show_loading_progress(prog / 2);
                    }

                });


        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                Functions.cancel_determinent_loader();
                if (isSharedDownload) {
                    shareVideo(url, tempPath);
                } else {
                    ApplyWatermark(id);
                }

            }

            @Override
            public void onError(Error error) {
                Delete_file_no_watermark(id);
                Log.e("download error ", " : " + error.getResponseCode());
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                Functions.cancel_determinent_loader();
            }
        });*/
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
                //positionSahreApp = id;

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

                                try {
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
                                } catch (Exception e){
                                    Toast.makeText(getContext(), "Please ty again.\n Data is loading", Toast.LENGTH_SHORT).show();
                                }

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

    public void Delete_file_no_watermark(int id) {
        File file = new File(Environment.getExternalStorageDirectory() + "/MyRole/" + id + "no_watermark" + ".mp4");
        if (file.exists()) {
            file.delete();
        }
    }

    public void ApplyWatermark(int id) {
        Bitmap myLogo = ((BitmapDrawable) getResources().getDrawable(R.drawable.myrole_logo)).getBitmap();
        Bitmap bitmap_resize = Bitmap.createScaledBitmap(myLogo, 100, 100, false);
        GlWatermarkFilter filter = new GlWatermarkFilter(bitmap_resize, GlWatermarkFilter.Position.RIGHT_TOP);
        new GPUMp4Composer(Environment.getExternalStorageDirectory() + "/MyRole/" + id + "no_watermark" + ".mp4",
                Environment.getExternalStorageDirectory() + "/MyRole/" + id + ".mp4")
                .filter(filter)

                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {

                        // Log.d("resp", "" + (int) (progress * 100));
                        Functions.Show_loading_progress((int) ((progress * 100) / 2) + 50);

                    }

                    @Override
                    public void onCompleted() {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Functions.cancel_determinent_loader();
                                Delete_file_no_watermark(id);
                                Scan_file(id);
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
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);


                    }
                });
    }

    public void shareVideo(final String title, String path) {
        Uri u = Uri.fromFile(new File(path));
        Intent shareIntent = new Intent();
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, u);
        shareIntent.setType("video/*");
        startActivity(Intent.createChooser(shareIntent, "MyRole"));
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
                Save_Video(url, vID);
                addDownloadCount(vID);
                int count = mFeedList.get(position).getDownload_cnt();
                mFeedList.get(position).setDownload_cnt(count + 1);
                mAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(mContext, "Exception : "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                /*Save_Video(url, vID);
                addShareCount(vID);
                int count = mFeedList.get(position).getShare_cnt();
                mFeedList.get(position).setShare_cnt(count + 1);
                mAdapter.notifyDataSetChanged();*/
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

            WatchMyVideosActivity.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
            WatchMyVideosActivity.recyclerView.setAdapter(adapterSahreApp);
            adapterSahreApp.setOnItemClickListener(new AdapterSahreApp.OnItemClickListener() {
                @Override
                public void onItemClick(int position1) {
                    positionSahreApp = position1;
                    closeShare(BottomSheetBehavior.STATE_HIDDEN);
                    positionSahreApp = position1;
                    Save_Video(url, vID);
                    addShareCount(vID);
                    int count = mFeedList.get(position).getShare_cnt();
                    mFeedList.get(position).setShare_cnt(count + 1);
                    mAdapter.notifyDataSetChanged();
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

    private void addShareCount(int id) {
        String userId = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        Retrofit retrofit = SongServiceClient.getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap();
        map.put("post_id", "" + id);
        map.put("user_id", userId);

        Call<ModelDataAddShareCount> call = apiService.addShareCount(map);
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
    @Override
    public void dismissCommentSheet() {
        if (commentBottomSheet != null) {
            if (commentBottomSheet.isVisible()) {
                commentBottomSheet.dismiss();
            }
        }
    }

    public void onPopupButtonClick(View button, final int position, String post_id) {
        PopupMenu popup = new PopupMenu(mContext, button);
        AppPreferences preferences = AppPreferences.getAppPreferences(mContext);
        PostListDTO postListDTO = mFeedList.get(position);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        if (postListDTO.getOwner_id() == Integer.parseInt(preferences.getStringValue(AppPreferences.USER_ID))) {
            MenuItem item = popup.getMenu().findItem(R.id.Report);
            item.setVisible(false);
            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
            item1.setVisible(true);
        } else {
            MenuItem item = popup.getMenu().findItem(R.id.Report);
            item.setVisible(true);
            MenuItem item1 = popup.getMenu().findItem(R.id.delete);
            item1.setVisible(false);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Report:
                        optionDialog(position);
                        break;
                    case R.id.delete:
                        AlertDialog(position, post_id);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public void optionDialog(final int pos) {

        CharSequence[] options = new CharSequence[]{"It’s Spam", "It’s Inappropriate"};
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
                        Log.d("TAG", " response : " + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {
                                String msg = object.getString("message");
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    @Override
    public void deleteVideo(final String post_id, String pos) {
        // AlertDialog(Integer.parseInt(pos),post_id);
        View v = mRecyclerView.findViewHolderForAdapterPosition(Integer.parseInt(pos)).itemView.findViewById(R.id.linear_delete);
        onPopupButtonClick(v, Integer.parseInt(pos), post_id);
    }

    public void AlertDialog(final int pos, String post_id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("MY Role");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callServiceForFeeds(post_id, position);

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

    private void callServiceForFeeds(String post_id, int position) {

        AppPreferences preferences = AppPreferences.getAppPreferences(mContext);
        AndroidNetworking.post(Config.DELETE_USER_POST)
                .setTag(this)
                .addBodyParameter("user_id", preferences.getStringValue(AppPreferences.USER_ID))
                .addBodyParameter("post_id", String.valueOf(post_id))
                .setPriority(Priority.HIGH)
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
                        deleteRecord(position);
                        mContext.sendBroadcast(new Intent("REFRESH_PROFILE"));
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
        Log.d("mFeedList", String.valueOf(mFeedList.size()));
        this.mFeedList.remove(Pos);
        mAdapter.addData(mFeedList);
        mAdapter.notifyDataSetChanged();
        Log.d("mFeedList", String.valueOf(mFeedList.size()));
        int selectedPosition = Pos + 1;
        mRecyclerView.scrollToPosition(selectedPosition);
        mRecyclerView.post(() -> {
            View view = mLayoutManager.findViewByPosition(selectedPosition);
            if (view == null) {
                //Log.e(WingPickerView.class.getSimpleName(), "Cant find target View for initial Snap");
                return;
            }

            int[] snapDistance = snapHelper.calculateDistanceToFinalSnap(mLayoutManager, view);
            if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                mRecyclerView.scrollBy(snapDistance[0], snapDistance[1]);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    Save_Video(url, vID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDismissDialog() {
        if (prDownloader != null) {
            prDownloader.cancel();
        }
    }
}