package com.myrole.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.gson.Gson;
import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.ExploreScreenAdapter;
import com.myrole.dashboard.MainDashboardActivity;
import com.myrole.dashboard.ReleaseListner;
import com.myrole.data.FameUserDTO;
import com.myrole.data.SimpleVideoObject;
import com.myrole.holders.PostListDTO;
import com.myrole.holders.TEMPPostListDTO;
import com.myrole.loadmore.PullCallback;
import com.myrole.loadmore.PullToLoadView;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.Utils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.myrole.utils.ActivityTransactions.getLogIn;
import static com.myrole.utils.AppPreferences.isLogin;


public class ExploreFragment extends BaseFragment implements PullCallback, SwipeRefreshLayout.OnRefreshListener, ReleaseListner {

    View view;
    FameUserDTO fameUserDTO;
    LinearLayout linearLayout;
    ImageView img_crown5, img_crown6, img_crown7, img_crown8, img_crown9, img_crown10;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6;
    int LAST_POST_ID = 0;
    int LIMIT = 13;
    ProgressBar progressBar;
    PostListDTO postListDTO;
    GridLayoutManager mLayoutManager;
    RelativeLayout people, post;
    EditText search_tv;
    CardView card;
    InputMethodManager inputMethodManager;
    private Activity activity;
    private Context context;
    private RecyclerView recyclerView;
    private boolean mIsLoading = false;
    private SlidingUpPanelLayout mLayout;
    private ExploreScreenAdapter adapter;
    private boolean loadmore;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private PullToLoadView pullToLoadView;
    int width;

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }

    public void PostDetails2(String p_id, String user_image, String url_image, String thumbunil) {
       /* if (((MainDashboardActivity) getActivity()).fragmentManager != null) {
            ((MainDashboardActivity) getActivity()).fragmentManager.beginTransaction()
                    .add(R.id.container,
                            PostDetailFragment.newInstance2(p_id,user_image,url_image,thumbunil),
                            "POSTDETAILSFRAGMENT")
                    .addToBackStack("POSTDETAILSFRAGMENT")
                    .commit();
        }*/
    }

    public void PostDetails3(String p_id, String user_image, String thumbnail, String url_video) {
       /* if (((MainDashboardActivity) getActivity()).fragmentManager != null) {
            ((MainDashboardActivity) getActivity()).fragmentManager.beginTransaction()
                    .add(R.id.container,
                            PostDetailFragment.newInstance3(p_id,user_image,thumbnail,url_video),
                            "POSTDETAILSFRAGMENT")
                    .addToBackStack("POSTDETAILSFRAGMENT")
                    .commit();
        }*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
SwipeRefreshLayout swipe_refresh;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.explore, container, false);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(this);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        activity = getActivity();
        context = getContext();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String userId = AppPreferences.getAppPreferences(activity).getStringValue(AppPreferences.USER_ID);

        if (Utils.isNetworkConnected(getActivity(), true)) {
            new GetFameUserTask().execute(userId);
        }
        return view;
    }

   /* public void setAdapter(ArrayList<PostListDTO> post_list) {
        if (recyclerView.getAdapter() == null) {

        }
        adapter.setPost_list(post_list);
    }*/
SimpleExoPlayer previous_player;
    @Override
    public void onPause() {
        super.onPause();
        if (previous_player != null) {
            previous_player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pullToLoadView = (PullToLoadView) view.findViewById(R.id.list);
        recyclerView = pullToLoadView.getRecyclerView();
        linearLayout = (LinearLayout) view.findViewById(R.id.hidden_l);
        card = (CardView) view.findViewById(R.id.card);
        search_tv = (EditText) view.findViewById(R.id.search_tv);

        img_crown5 = (ImageView) view.findViewById(R.id.img_crown5);
        img_crown6 = (ImageView) view.findViewById(R.id.img_crown6);
        img_crown7 = (ImageView) view.findViewById(R.id.img_crown7);
        img_crown8 = (ImageView) view.findViewById(R.id.img_crown8);
        img_crown9 = (ImageView) view.findViewById(R.id.img_crown9);
        img_crown10 = (ImageView) view.findViewById(R.id.img_crown10);

        iv1 = (ImageView) view.findViewById(R.id.imageView5);
        iv2 = (ImageView) view.findViewById(R.id.imageView6);
        iv3 = (ImageView) view.findViewById(R.id.imageView7);
        iv4 = (ImageView) view.findViewById(R.id.imageView8);
        iv5 = (ImageView) view.findViewById(R.id.imageView9);
        iv6 = (ImageView) view.findViewById(R.id.imageView10);

        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        pullToLoadView.onAttachedToWindow();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setPullCallback(this);
        pullToLoadView.initLoad();
        // for fix spacing in gridview
        int spanCount = 3; // 3 columns
        int spacing = 1; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        //end

        if (loadmore) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Toast.makeText(getContext(), "message", Toast.LENGTH_SHORT).show();
                if (((MainDashboardActivity) context).fragmentManager != null) {

                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()

                            .replace(R.id.container,//dont add frgement here only use replace to dont create same frgment more click on search edit text
                                    new SearchFragment(),
                                    "FINDFRAGMENT")
                            .addToBackStack("FINDFRAGMENT_45")
                            //.addToBackStack(null)
                            .commit();
                }
            }
        });
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getContext(), "search_tv", Toast.LENGTH_SHORT).show();
                /*if (((MainDashboardActivity) context).fragmentManager != null) {
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .add(R.id.container,
                                    FindFragment.newInstance(),
                                    "FINDFRAGMENT")
                            .addToBackStack("FINDFRAGMENT")
                            .commit();
                }*/
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .add(R.id.container,
                                    new SearchFragment(),
                                    "FINDFRAGMENT")
                            .addToBackStack("FINDFRAGMENT")
                            .commit();
                }

            }
        });

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                if (slideOffset > 0.6761566) {
                    linearLayout.setVisibility(View.VISIBLE);
                    loadmore = true;
                } else {
                    linearLayout.setVisibility(View.GONE);
                    loadmore = false;
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


    }

    public void click1() {
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    // Toast.makeText(context, "iv 1", Toast.LENGTH_LONG).show();
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(fameUserDTO.getData().get(0).getId()));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });
    }

    public void click2() {
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    //  Toast.makeText(context, "iv 2", Toast.LENGTH_LONG).show();
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(fameUserDTO.getData().get(1).getId()));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });
    }

    public void click3() {
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(fameUserDTO.getData().get(2).getId()));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .replace(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });
    }

    public void click4() {
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(fameUserDTO.getData().get(3).getId()));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .add(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });
    }

    public void click5() {
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(fameUserDTO.getData().get(4).getId()));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .add(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        if (previous_player != null) {
            previous_player.setPlayWhenReady(false);
        }
    }

    public void click6() {
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MainDashboardActivity) context).fragmentManager != null) {
                    OtherProfileFragment fragment = OtherProfileFragment.newInstance(String.valueOf(fameUserDTO.getData().get(5).getId()));
                    ((MainDashboardActivity) context).fragmentManager.beginTransaction()
                            .add(R.id.container,
                                    fragment,
                                    "OTHERPROFILEFRAGMENT")
                            .addToBackStack("OTHERPROFILEFRAGMENT")
                            .commit();
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.hideKeybord(getContext(), view);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Utils.hideKeybord(getContext(), view);
    }

    boolean isFirstInstance = true;
    ArrayList<PostListDTO> list;
    int page = 0;
    private void callServiceForFeeds(final boolean isSwipeToRefresh) {
        isLoading = true;
        AppPreferences preferences = AppPreferences.getAppPreferences(getActivity());
        AndroidNetworking.post(Config.GET_DISCOVER_POST)
                .setTag(this)
                .addBodyParameter("user_id", preferences.getStringValue(AppPreferences.USER_ID))
                //.addBodyParameter("post_last_id", String.valueOf(LAST_POST_ID))
                .addBodyParameter("limit", String.valueOf(LIMIT))
                //.addBodyParameter("width", String.valueOf(width))
                .addBodyParameter("currentPage", String.valueOf(page))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "onResponse object 1: " + response);
                        Log.d(TAG, "onResponse isMainThread : " + (Looper.myLooper() == Looper.getMainLooper()));
                        List<SimpleVideoObject> mVideos1 = new ArrayList<>();

                        if (LAST_POST_ID == 0) {

                            postListDTO = new Gson().fromJson(response, PostListDTO.class);
                            if (postListDTO.getData().size() > 0) {
                                for (int i = 0; i < postListDTO.getData().size(); i++) {
                                    mVideos1.add(new SimpleVideoObject(postListDTO.getData().get(i).getPost_id(), postListDTO.getData().get(i).getUrl(), postListDTO.getData().get(i).getDescription(),
                                            postListDTO.getData().get(i).getPost_time(), postListDTO.getData().get(i).getOwner_name(), postListDTO.getData().get(i).getOwner_username(), postListDTO.getData().get(i).getOwner_image(),
                                            postListDTO.getData().get(i).getOwner_id(), postListDTO.getData().get(i).getIslike(), postListDTO.getData().get(i).getRole_name(), postListDTO.getData().get(i).getRole_description(),
                                            postListDTO.getData().get(i).getRole_description_hindi(), postListDTO.getData().get(i).getRole_id(), postListDTO.getData().get(i).getCat_id(), postListDTO.getData().get(i).getCat_name(),
                                            postListDTO.getData().get(i).getCat_slug(), postListDTO.getData().get(i).getCat_icon(), postListDTO.getData().get(i).getPost_likes(), postListDTO.getData().get(i).getThumbnail()));

                                }

                                if(isFirstInstance){
                                    list = new ArrayList<>();
                                    list.addAll( postListDTO.getData());
                                    adapter = new ExploreScreenAdapter(getContext(), ExploreFragment.this,list);
                                    recyclerView.setAdapter(adapter);
                                    isFirstInstance = false;
                                }else{
                                    list.addAll( postListDTO.getData());
                                    adapter.addData(list);
                                }
                                adapter.notifyDataSetChanged();
                                // setAdapter(postListDTO.getData());
                                try {
                                    LAST_POST_ID = postListDTO.getPaging().get(0).getBefore();
                                    mIsLoading = postListDTO.getPaging().get(0).isdataavailable();
                                } catch (Exception e) {
                                    //e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(context, "data not found.", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            mVideos1.clear();
                            TEMPPostListDTO TEMPPostListDTO = new Gson().fromJson(response, com.myrole.holders.TEMPPostListDTO.class);
                            if (TEMPPostListDTO.getPaging().get(0).isdataavailable()) {
                                postListDTO.getData().addAll(TEMPPostListDTO.getData());

                                if (postListDTO.getData().size() > 0) {
                                    for (int i = 0; i < postListDTO.getData().size(); i++) {
                                        mVideos1.add(new SimpleVideoObject(postListDTO.getData().get(i).getPost_id(), postListDTO.getData().get(i).getUrl(), postListDTO.getData().get(i).getDescription(),
                                                postListDTO.getData().get(i).getPost_time(), postListDTO.getData().get(i).getOwner_name(), postListDTO.getData().get(i).getOwner_username(), postListDTO.getData().get(i).getOwner_image(),
                                                postListDTO.getData().get(i).getOwner_id(), postListDTO.getData().get(i).getIslike(), postListDTO.getData().get(i).getRole_name(), postListDTO.getData().get(i).getRole_description(),
                                                postListDTO.getData().get(i).getRole_description_hindi(), postListDTO.getData().get(i).getRole_id(), postListDTO.getData().get(i).getCat_id(), postListDTO.getData().get(i).getCat_name(),
                                                postListDTO.getData().get(i).getCat_slug(), postListDTO.getData().get(i).getCat_icon(), postListDTO.getData().get(i).getPost_likes(), postListDTO.getData().get(i).getThumbnail()));

                                    }
                                }
                                if(isFirstInstance){
                                    list = new ArrayList<>();
                                    list.addAll(postListDTO.getData());
                                    adapter = new ExploreScreenAdapter(getContext(), ExploreFragment.this,list );
                                    recyclerView.setAdapter(adapter);
                                    isFirstInstance = false;
                                }else{
                                    list.addAll(postListDTO.getData());
                                    adapter.addData(list);
                                }
                                adapter.notifyDataSetChanged();
                                LAST_POST_ID = TEMPPostListDTO.getPaging().get(0).getBefore();
                                mIsLoading = postListDTO.getPaging().get(0).isdataavailable();
                            } else {
                                isHasLoadedAll = true;
                                pullToLoadView.setComplete();
                                //  Toast.makeText(activity, "No more data available", Toast.LENGTH_LONG).show();
                            }

                        }
                        pullToLoadView.setComplete();
                        isLoading = false;

                    }

                    @Override
                    public void onError(ANError error) {
                        progressBar.setVisibility(View.GONE);
                        if (error.getErrorCode() != 0) {
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });

    }


    @Override
    public void onLoadMore() {
        if (Utils.isNetworkConnected(context, true)) {
            page++;
            callServiceForFeeds(false);
        }

    }

    @Override
    public void onRefresh() {
        isFirstInstance = true;
        isHasLoadedAll = false;
        if (Utils.isNetworkConnected(context, true)) {
            callServiceForFeeds(false);
        }
        hide();
    }
    private void hide(){
        swipe_refresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(false);
            }
        },500);
    }
    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return isHasLoadedAll;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onEvent(SimpleExoPlayer data) {
        this.previous_player= data;
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    class GetFameUserTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            return HTTPUrlConnection.getInstance().load(activity, Config.FAME_USER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            fameUserDTO = new Gson().fromJson(result, FameUserDTO.class);
            if (fameUserDTO != null) {

                if (fameUserDTO.isStatus()) {
                    if (fameUserDTO.getData() != null && fameUserDTO.getData().size() > 0) {

                        for (int i = 0; i < fameUserDTO.getData().size(); i++) {
                            if (i == 0) {
                                if (fameUserDTO.getData().get(i).getWinner() != null) {
                                    if (fameUserDTO.getData().get(i).getWinner().equals("0")) {
                                    } else {
                                        img_crown5.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (fameUserDTO.getData().get(i).getImage() != null) {
                                    Glide.with(activity).asBitmap().load(fameUserDTO.getData().get(i).getImage()).centerCrop().into(new BitmapImageViewTarget(iv1) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            iv1.setImageDrawable(circularBitmapDrawable);
                                            click1();
                                        }
                                    });
                                } else {
                                    if (fameUserDTO.getData().get(i).getName() != null) {
                                        click1();
                                    }
                                }
                            } else if (i == 1) {
                                if (fameUserDTO.getData().get(i).getWinner() != null) {
                                    if (fameUserDTO.getData().get(i).getWinner().equals("0")) {
                                    } else {
                                        img_crown6.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (fameUserDTO.getData().get(i).getImage() != null) {
                                    Glide.with(activity).asBitmap().load(fameUserDTO.getData().get(i).getImage()).centerCrop().into(new BitmapImageViewTarget(iv2) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            iv2.setImageDrawable(circularBitmapDrawable);
                                            click2();
                                        }
                                    });
                                } else {
                                    if (fameUserDTO.getData().get(i).getName() != null) {
                                        click2();
                                    }
                                }
                            } else if (i == 2) {
                                if (fameUserDTO.getData().get(i).getWinner() != null) {
                                    if (fameUserDTO.getData().get(i).getWinner().equals("0")) {
                                    } else {
                                        img_crown7.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (fameUserDTO.getData().get(i).getImage() != null) {
                                    Glide.with(activity).asBitmap().load(fameUserDTO.getData().get(i).getImage()).centerCrop().into(new BitmapImageViewTarget(iv3) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            iv3.setImageDrawable(circularBitmapDrawable);
                                            click3();
                                        }
                                    });
                                } else {
                                    if (fameUserDTO.getData().get(i).getName() != null) {
                                        click3();
                                    }
                                }
                            } else if (i == 3) {
                                if (fameUserDTO.getData().get(i).getWinner() != null) {
                                    if (fameUserDTO.getData().get(i).getWinner().equals("0")) {
                                    } else {
                                        img_crown8.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (fameUserDTO.getData().get(i).getImage() != null) {
                                    Glide.with(activity).asBitmap().load(fameUserDTO.getData().get(i).getImage()).centerCrop().into(new BitmapImageViewTarget(iv4) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            iv4.setImageDrawable(circularBitmapDrawable);
                                            click4();
                                        }
                                    });
                                } else {
                                    if (fameUserDTO.getData().get(i).getName() != null) {
                                        click4();
                                    }
                                }
                            } else if (i == 4) {
                                if (fameUserDTO.getData().get(i).getWinner() != null) {
                                    if (fameUserDTO.getData().get(i).getWinner().equals("0")) {
                                    } else {
                                        img_crown9.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (fameUserDTO.getData().get(i).getImage() != null) {
                                    Glide.with(activity).asBitmap().load(fameUserDTO.getData().get(i).getImage()).centerCrop().into(new BitmapImageViewTarget(iv5) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            iv5.setImageDrawable(circularBitmapDrawable);
                                            click5();
                                        }
                                    });
                                } else {
                                    if (fameUserDTO.getData().get(i).getName() != null) {
                                        click5();
                                    }
                                }
                            } else if (i == 5) {
                                if (fameUserDTO.getData().get(i).getWinner() != null) {
                                    if (fameUserDTO.getData().get(i).getWinner().equals("0")) {
                                    } else {
                                        img_crown10.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (fameUserDTO.getData().get(i).getImage() != null) {
                                    Glide.with(activity).asBitmap().load(fameUserDTO.getData().get(i).getImage()).centerCrop().into(new BitmapImageViewTarget(iv6) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            iv6.setImageDrawable(circularBitmapDrawable);
                                            click6();
                                        }
                                    });
                                } else {
                                    if (fameUserDTO.getData().get(i).getName() != null) {
                                        click6();
                                    }
                                }
                            }

                        }
                    }
                }
            }

        }
    }

}
