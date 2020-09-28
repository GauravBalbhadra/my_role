package com.myrole.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
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
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.MyRoleDeatilAdapter;
import com.myrole.dashboard.interfaces.InvokedFeedsFunctions;
import com.myrole.data.SimpleVideoObject;
import com.myrole.holders.PostListDTO;
import com.myrole.holders.TEMPPostListDTO;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.EndlessRecyclerOnScrollListener;
import com.myrole.utils.Utils;
import com.myrole.vo.Post;
import com.myrole.widget.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyRoleDeatilPostFragment extends BaseFragment implements InvokedFeedsFunctions {
    public static final String TAG = "Toro:Facebook";
    public static final int RESUME_REQUEST_CODE = 1024;
    private static ArrayList<Post> generalList = new ArrayList<>();
    protected RecyclerView.Adapter mAdapter;
    LinearLayoutManager layoutManager;
    PostListDTO postListDTO;
    private String categoryID, userID;
    private OnFragmentInteractionListener mListener;
    int width;
    private int firstVideoPosition;
    int LAST_POST_ID = 0;
    int LIMIT = 10;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar progressBar;

    boolean allowRefresh = true;
    private AppPreferences pref;
    public Activity mactivity;
    public static final int REQUEST_ADD_COMMENTS = 678;
    public RecyclerView mRecyclerView;
    HashMap<String, String> postDataParams;
    private int TOTAL_ITEM_COUNT;
    private boolean mIsLoading = false;
    private int mIsLastPage = 9;

    private View view, view2;
    private static int firstVisibleInListview;
   /* private FrameLayout main_frame;
    private BottomBar bottom_bar;*/
    TabLayout tabLayout;
    int distanse;


    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder,
                                View view, int adapterPosition, long itemId) {
            /*SimpleVideoObject initItem = null;
            long initPosition = 0;
            long initDuration = 0;
            final ToroPlayer player;
            if (adapter instanceof GeneralFeedAdapter) {
                player = ((GeneralFeedAdapter) adapter).getPlayer();
                initItem = (SimpleVideoObject) ((GeneralFeedAdapter) adapter).getItem(adapterPosition);
                if (player != null) {
                    initPosition = player.getCurrentPosition();
                    initDuration = player.getDuration();
                }
            }

            if (initItem != null) {
                Bundle extras = new Bundle();
                extras.putLong(FbPLayerDialogFragment.ARGS_INIT_DURATION, initDuration);
                extras.putLong(FbPLayerDialogFragment.ARGS_INIT_POSITION, initPosition);
                extras.putParcelable(FbPLayerDialogFragment.ARGS_INIT_VIDEO, initItem);

                FbPLayerDialogFragment playlist =
                        FbPLayerDialogFragment.newInstance(initItem, initPosition, initDuration);
                playlist.setTargetFragment(MyRoleDeatilPostFragment.this, RESUME_REQUEST_CODE);
                playlist.show(getChildFragmentManager(), FbPLayerDialogFragment.TAG);
            }*/
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        categoryID = getArguments().getString("CATEGORYID", "");
        userID = getArguments().getString("USERID");
        try {
           /* bottom_bar = getActivity().findViewById(R.id.bottom_bar);
            main_frame = getActivity().findViewById(R.id.main_frame);*/

            String CHECK_TAB_OPEN = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.CHECK_TAB_OPEN);
            if (CHECK_TAB_OPEN.equals("0")) {

            } /*else {
                bottom_bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0);
            }*/
          //  main_frame.setPadding(0, 0, 0, bottom_bar.getHeight());
//            bottom_bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0).setDuration(380);
        } catch (Exception e) {
        }
        //new GetTodayRole().execute(categoryID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.generic_recycler_view2, container, false);

        try {

            String CHECK_TAB_OPEN = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.CHECK_TAB_OPEN);
           /* if (CHECK_TAB_OPEN.equals("0")) {

            } else {
                bottom_bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0);
            }*/
          //  main_frame.setPadding(0, 0, 0, bottom_bar.getHeight());

        } catch (Exception e) {
            /*try {
                main_frame.setPadding(0, 0, 0, 170);
            } catch (Exception e2) {
            }*/
        }

        postListDTO = new PostListDTO();
        generalList.clear();
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        view.findViewById(R.id.btn_back).setOnClickListener(this);
        if (Utils.isNetworkConnected(getContext(), true)) {
            callServiceForGeneralFeed(false, getActivity(), 1);
        }
       /* mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callServiceForGeneralFeed(true, getActivity(), 1);
                //  LAST_POST_ID = 0;
                swiper();
            }
        });

        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                HashMap<String, String> pair = new HashMap<>();
                if (Utils.isNetworkConnected(getContext(), true)) {
                    callServiceForGeneralFeed(false, getActivity(), 1);
                }
            }
        });*/
//        Toro.register(mRecyclerView);

        return view;
    }

    public void swiper() {
        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                HashMap<String, String> pair = new HashMap<>();
                if (Utils.isNetworkConnected(getContext(), true)) {
                    callServiceForGeneralFeed(false, getActivity(), 0);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser)
//            callServiceForGeneralFeed(false, getActivity());
        if (isVisibleToUser) {
            notifyTabSelected(isVisibleToUser);
        } else {
            notifyTabSelected(isVisibleToUser);
        }
    }

    /**
     * this is ues to  tab change dected on recycle view and recreate the video and play again
     * called when this fragment (tab) becomes active or inactive
     */
    public void notifyTabSelected(boolean isSelected) {
       /* final ToroScrollListener scrollListener = Toro.getScrollListener(mRecyclerView);
        if (scrollListener == null) {
            // should never happen as long as Toro.register() was called
            return;
        }
        VideoPlayerManager manager = scrollListener.getManager();
        if (isSelected && manager.getPlayer() == null) {
            // NOTE: first time a tab/fragment is selected it doesn't start to playback until user starts to scroll
            //   > if we can just simulate a scroll event to ToroScrollListener once tab is visible this issue is fixed
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollListener.onScrollStateChanged(mRecyclerView, RecyclerView.SCROLL_STATE_IDLE);
                }
            }, 100);
        } else if (isSelected) {
            // resume video playback
            manager.startPlayback();
        } else {
            // pause video playback
            manager.pausePlayback();
        }*/
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void callServiceForGeneralFeed(final boolean isSwipeToRefresh, Context activity, final int i) {
        if (isSwipeToRefresh) {
            LAST_POST_ID = 0;
            Log.d(TAG, " swipe refresh  ");
        } else {
//            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, " endlless scroll view : ");
        }
        final AppPreferences preferences = AppPreferences.getAppPreferences(activity);


//        AndroidNetworking.post(Config.GET_USER_UPLOADED_POST)
        AndroidNetworking.post(Config.GET_MYROLE_POST)
                .setTag(this)
//                .addBodyParameter("user_id", getArguments().getString("USERID"))
                .addBodyParameter("user_id", preferences.getStringValue(AppPreferences.USER_ID))
                .addBodyParameter("post_last_id", String.valueOf(LAST_POST_ID))
                .addBodyParameter("limit", String.valueOf(LIMIT))
                .addBodyParameter("category_id", getArguments().getString("CATEGORYID"))
                .addBodyParameter("width", String.valueOf(width))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " category_id : " + getArguments().getString("CATEGORYID"));
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(TAG, "result " + response);
                        // Log.d(TAG, "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
//                        mVideos1 = new ArrayList<>();

                        try {
                            JSONObject response2 = new JSONObject(response);
                            JSONArray paging = response2.getJSONArray("paging");
                            // Retrieves the string labeled "colorName" and "description" from
                            //the response JSON Object
                            //and converts them into javascript objects
                            JSONObject c1 = paging.getJSONObject(1);
                            String before = c1.getString("before");
                            LAST_POST_ID = Integer.valueOf(before);

                        } catch (JSONException e) {
                        }
                        if (isSwipeToRefresh) {
                            mSwipeRefreshLayout.setRefreshing(false);

                        } else {
//                            progressBar.setVisibility(View.GONE);
                        }
                        if (i == 0) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

//                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onResponse object : " + response);
                        Log.d(TAG, "onResponse isMainThread : " + (Looper.myLooper() == Looper.getMainLooper()));
                        Log.d(TAG, "last post id : " + LAST_POST_ID);

                        List<SimpleVideoObject> mVideos1 = new ArrayList<>();

                        if (LAST_POST_ID == 0) {
                            postListDTO = new Gson().fromJson(response, PostListDTO.class);
                            if (postListDTO.getData().size() > 0) {
                                //  for (int i = 0; i < postListDTO.getData().size(); i++) {
                                  /*  mVideos1.add(new SimpleVideoObject(
                                            postListDTO.getData().get(i).getPost_id(),
                                            postListDTO.getData().get(i).getUrl(),
//                                            "https://s3.ap-south-1.amazonaws.com/myrole-test/videos/example2/example.mp4_master.m3u8",
                                            postListDTO.getData().get(i).getDescription(),
                                            postListDTO.getData().get(i).getPost_time(), postListDTO.getData().get(i).getOwner_name(),
                                            postListDTO.getData().get(i).getOwner_username(), postListDTO.getData().get(i).getOwner_image(),
                                            postListDTO.getData().get(i).getOwner_id(), postListDTO.getData().get(i).getIslike(),
                                            postListDTO.getData().get(i).getRole_name(), postListDTO.getData().get(i).getRole_description(),
                                            postListDTO.getData().get(i).getRole_description_hindi(), postListDTO.getData().get(i).getRole_id(),
                                            postListDTO.getData().get(i).getCat_id(), postListDTO.getData().get(i).getCat_name(),
                                            postListDTO.getData().get(i).getCat_slug(), postListDTO.getData().get(i).getCat_icon(),
                                            postListDTO.getData().get(i).getPost_likes(), postListDTO.getData().get(i).getThumbnail()));

                                }*/
                                mAdapter = new MyRoleDeatilAdapter(getActivity(), postListDTO.getData(), MyRoleDeatilPostFragment.this);
                                mRecyclerView.setAdapter(mAdapter);

                            }
                        } else {
                            mVideos1.clear();
                            TEMPPostListDTO TEMPPostListDTO = new Gson().fromJson(response, com.myrole.holders.TEMPPostListDTO.class);
                            if (TEMPPostListDTO.getPaging().get(0).isdataavailable()) {
                                postListDTO.getData().addAll(TEMPPostListDTO.getData());

                                if (postListDTO.getData().size() > 0) {
                                    for (int i = 0; i < postListDTO.getData().size(); i++) {
                                        mVideos1.add(new SimpleVideoObject(
                                                postListDTO.getData().get(i).getPost_id(), postListDTO.getData().get(i).getUrl(),
                                                postListDTO.getData().get(i).getDescription(),
                                                postListDTO.getData().get(i).getPost_time(), postListDTO.getData().get(i).getOwner_name(),
                                                postListDTO.getData().get(i).getOwner_username(), postListDTO.getData().get(i).getOwner_image(),
                                                postListDTO.getData().get(i).getOwner_id(), postListDTO.getData().get(i).getIslike(),
                                                postListDTO.getData().get(i).getRole_name(), postListDTO.getData().get(i).getRole_description(),
                                                postListDTO.getData().get(i).getRole_description_hindi(), postListDTO.getData().get(i).getRole_id(),
                                                postListDTO.getData().get(i).getCat_id(), postListDTO.getData().get(i).getCat_name(),
                                                postListDTO.getData().get(i).getCat_slug(), postListDTO.getData().get(i).getCat_icon(),
                                                postListDTO.getData().get(i).getPost_likes(), postListDTO.getData().get(i).getThumbnail()));

                                    }
                                }
                                mIsLastPage = postListDTO.getData().size() - 1;
                                int scrool_position = postListDTO.getData().indexOf(TEMPPostListDTO.getData().get(0));
                                mAdapter = new MyRoleDeatilAdapter(getActivity(), postListDTO.getData(), MyRoleDeatilPostFragment.this);
                                mRecyclerView.setAdapter(mAdapter);
                                layoutManager.scrollToPositionWithOffset(scrool_position - 1, 10);
                                //mmRecyclerView.getLayoutManager().smoothScrollToPosition(mmRecyclerView, null, scrool_position - 2);
                                LAST_POST_ID = TEMPPostListDTO.getPaging().get(0).getBefore();
                                mIsLoading = postListDTO.getPaging().get(0).isdataavailable();
                                Log.d(TAG, "on last post id" + mIsLoading);
                            } else {
//                            Toast.makeText(getActivity(), "No more data abailable", Toast.LENGTH_LONG).show();
                            }

                        }

                    }

                    @Override
                    public void onError(ANError error) {
//                        progressBar.setVisibility(View.GONE);
                        //dismissProgressDialog();
                        if (error.getErrorCode() != 0) {

                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == RESUME_REQUEST_CODE) {
            Toro.rest(true);
            VideoPlayerManager manager = ((VideoPlayerManager) mAdapter);
            if (manager.getPlayer() != null) {
                long latestPosition = data.getLongExtra(FbPLayerDialogFragment.ARGS_LATEST_TIMESTAMP, 0);
                manager.saveVideoState(manager.getPlayer().getMediaId(), latestPosition,
                        manager.getPlayer().getDuration());
            }
            Toro.rest(false);
        }*/
//        if (resultCode == REQUEST_ADD_COMMENTS){
//            if(requestCode == 678 ) {
//                GeneralFeedAdapter generalFeedAdapter = new  GeneralFeedAdapter();
//                generalFeedAdapter
//            }
//        }
    }

    @Override
    public void onResume() {
        try {
            /*bottom_bar = getActivity().findViewById(R.id.bottom_bar);
            main_frame = getActivity().findViewById(R.id.main_frame);*/
            String CHECK_TAB_OPEN = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.CHECK_TAB_OPEN);
          /*  if (CHECK_TAB_OPEN.equals("0")) {
                bottom_bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0);
            } else {
                bottom_bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(0);
            }*/
           // main_frame.setPadding(0, 0, 0, bottom_bar.getHeight());
        } catch (Exception e) {
        }
        //  Toro.register(mRecyclerView);
        super.onResume();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       /* if (mAdapter instanceof GeneralFeedAdapter) {
            ((GeneralFeedAdapter) mAdapter).setOnItemClickListener(listener);
        }*/
    }

    @Override
    public void showCommentSheet(String id, String pos) {

    }

    @Override
    public void downloadVideo(String url, int id, int position) {

    }

    @Override
    public void sharedVideo(String url, int id, int position) {

    }

    @Override
    public void dismissCommentSheet() {

    }

    @Override
    public void deleteVideo(String id, String pos) {

    }
//    private static final int REQUEST_ADD_COMMENTS = 678;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }

}
