package com.myrole.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.myrole.BaseFragment;
import com.myrole.BuildConfig;
import com.myrole.R;
import com.myrole.adapter.NewUserGeneralFeedAdapter;
import com.myrole.dashboard.interfaces.InvokedFeedsFunctions;
import com.myrole.data.SimpleVideoObject;
import com.myrole.holders.PostListDTO;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.Utils;
import com.myrole.vo.Post;
import com.myrole.widget.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class NewUserGeneralPostFragment1 extends BaseFragment implements InvokedFeedsFunctions, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "GeneralPostFragment1";
    public static final int RESUME_REQUEST_CODE = 1024;
    private static ArrayList<Post> generalList = new ArrayList<>();
    public RecyclerView recyclerView;
    private CardView cardViewNoDataMsg;
    protected RecyclerView.Adapter mAdapter;
    GridLayoutManager layoutManager;
    PostListDTO postListDTO;
    private ArrayList<PostListDTO> mFeedList = new ArrayList<PostListDTO>();
    SwipeRefreshLayout swipe_refresh;
    List<SimpleVideoObject> mVideos1;
    int width;
    String usrID;
    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView.Adapter adapter, RecyclerView.ViewHolder viewHolder,
                                View view, int adapterPosition, long itemId) {

        }
    };

    public static NewUserGeneralPostFragment1 newInstance(String categoryID, String userID) {
        NewUserGeneralPostFragment1 fragment = new NewUserGeneralPostFragment1();
        Bundle bndl = new Bundle();
        bndl.putString("CATEGORYID", categoryID);
        bndl.putString("USERID", userID);
        fragment.setArguments(bndl);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mFeedList.clear();
        if(BuildConfig.DEBUG)
         postListDTO = new PostListDTO();
        generalList.clear();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        cardViewNoDataMsg = view.findViewById(R.id.cardViewNoDataMsg);
        recyclerView.setHasFixedSize(true);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(this);
        String userID = AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID);

        if(Utils.isFromProfile) {
            usrID = AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID);
        }else{
            String otherID = OtherProfileFragment.OtherUserID ;
            Utils.isFromProfile=userID.equals(otherID);
            if (Utils.isFromProfile)
                usrID=userID;
            else
                usrID=otherID;
        }

        if (Utils.isNetworkConnected(getActivity(), true)) {
            callServiceForGeneralFeed(false, getActivity());
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utils.isNetworkConnected(getActivity(), true)) {
                callServiceForGeneralFeed(false, getActivity());
            }
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        mContext.registerReceiver(receiver,new IntentFilter("REFRESH_PROFILE"));
    }


    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void callServiceForGeneralFeed(final boolean isSwipeToRefresh, Activity activity) {

        if(Utils.isFromProfile) {
            usrID = AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID);
        }else{
            usrID = OtherProfileFragment.OtherUserID ;
        }
        //Toast.makeText(activity, "USer ID  "+usrID, Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(Config.GET_USER_UPLOADED_POST)
                .setTag(this)
                .addBodyParameter("user_id", usrID)
                .addBodyParameter("category_id", "")//getArguments().getString("CATEGORYID"))
                .addBodyParameter("width", String.valueOf(width))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " URL : " + Config.GET_USER_UPLOADED_POST);
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse object1 : " + response);
                        Log.d(TAG, "onResponse isMainThread : " + (Looper.myLooper() == Looper.getMainLooper()));
                        mVideos1 = new ArrayList<>();
                        postListDTO = new Gson().fromJson(response, PostListDTO.class);
                        if (postListDTO.getData().size() > 0) {
                            mFeedList.clear();
                            mFeedList = postListDTO.getData() ;
                            layoutManager = new GridLayoutManager(getActivity(), 3);
                            recyclerView.setLayoutManager(layoutManager);
                            mAdapter = new NewUserGeneralFeedAdapter(getContext(), mFeedList, NewUserGeneralPostFragment1.this);
                            recyclerView.setAdapter(mAdapter);
                            cardViewNoDataMsg.setVisibility(View.GONE);
                            //my_post_fragment.setvi
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            try {

                                cardViewNoDataMsg.setVisibility(View.VISIBLE);
                                //Toast.makeText(getContext(), "data not found.", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESUME_REQUEST_CODE) {

        }    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

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

    @Override
    public void onRefresh() {

        if (Utils.isNetworkConnected(getActivity(), true)) {
            callServiceForGeneralFeed(false, getActivity());
        }
        hide();
    }
    private void hide(){
        swipe_refresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(false);
            }
        },2000);
    }
//    private static final int REQUEST_ADD_COMMENTS = 678;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }

}
