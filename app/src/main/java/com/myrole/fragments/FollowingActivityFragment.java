package com.myrole.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.ActivityFollowingAdapter;
import com.myrole.components.BottomBar;
import com.myrole.data.FollowingDTO;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FollowingActivityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ActivityFollowingAdapter searchPostAdapter;
    private ProgressBar progressBarHandler;
    private TextView txt_no_data;
    private  View view;


    BottomBar bottom_bar;
    public FollowingActivityFragment() {
    }

    public static FollowingActivityFragment newInstance() {
        FollowingActivityFragment fragment = new FollowingActivityFragment();
        return fragment;
    }

    public void PostDetails2(String p_id,String user_image,String url_image,String tumbunil) {
       /* if (((HomeActivity) getActivity()).fragmentManager != null) {
            ((HomeActivity) getActivity()).fragmentManager.beginTransaction()
                    .add(R.id.main_frame,
                            PostDetailFragment.newInstance2(p_id,user_image,url_image,tumbunil),
                            "POSTDETAILSFRAGMENT")
                    .addToBackStack("POSTDETAILSFRAGMENT")
                    .commit();
        }*/
    }
    public void PostDetails3(String p_id,String user_image,String thumbnail,String url_video) {
        /*if (((HomeActivity) getActivity()).fragmentManager != null) {
            ((HomeActivity) getActivity()).fragmentManager.beginTransaction()
                    .add(R.id.main_frame,
                            PostDetailFragment.newInstance3(p_id,user_image,thumbnail,url_video),
                            "POSTDETAILSFRAGMENT")
                    .addToBackStack("POSTDETAILSFRAGMENT")
                    .commit();
        }*/
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        bottom_bar = getActivity().findViewById(R.id.bottom_bar);
    }
    SwipeRefreshLayout swipe_refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //  final View view = inflater.inflate(R.layout.fragment_following_post, container, false);
       view = inflater.inflate(R.layout.fragment_following_post, container, false);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(this);
        txt_no_data= (TextView) view.findViewById(R.id.txt_no_data);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBarHandler = (ProgressBar)view.findViewById(R.id.progressBar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
//        callServiceForFeeds();
        if (Utils.isNetworkConnected(getActivity(), true)){
        new GetActivityTask().execute();}
        return view;
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

    @Override
    public void onRefresh() {
        if (Utils.isNetworkConnected(getActivity(), true)){
            new GetActivityTask().execute();}
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }


    class GetActivityTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID));
            //postDataParams.put("type", "me");
            return HTTPUrlConnection.getInstance().load(getContext(), Config.GET_ACTIVITY, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(result);
                FollowingDTO followingDTO = new Gson().fromJson(result, FollowingDTO.class);
                if (followingDTO.isStatus()) {

                    if(followingDTO.isStatus()&&followingDTO.getData()!=null&&followingDTO.getData().size()>0){
                       searchPostAdapter = new ActivityFollowingAdapter(getActivity(),getContext(),followingDTO.getData(),bottom_bar);
                        recyclerView.setAdapter(searchPostAdapter);
                    }
                    else {
                        view.findViewById(R.id.txt_no_data).setVisibility(View.VISIBLE);
                       // txt_no_data.setVisibility(View.VISIBLE);
                     //   Toast.makeText(getActivity(),"Data not found",Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



//    private void callServiceForFeeds() {
//       progressBarHandler.show();
//        AppPreferences preferences = AppPreferences.getAppPreferences(getActivity());
//        AndroidNetworking.post(Config.GET_ACTIVITY)
//                .setTag(this)
//                .addBodyParameter("user_id",preferences.getStringValue(AppPreferences.USER_ID))
//                .addBodyParameter("type","me")
//                .setPriority(Priority.IMMEDIATE)
//                .build()
//                .setAnalyticsListener(new AnalyticsListener() {
//                    @Override
//                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
//                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
//                        Log.d(TAG, " bytesSent : " + bytesSent);
//                        Log.d(TAG, " bytesReceived : " + bytesReceived);
//                        Log.d(TAG, " isFromCache : " + isFromCache);
//                    }
//                })
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressBarHandler.hide();
//                        Log.d(TAG, "onResponse object : " + response.toString());
//                        Log.d(TAG, "onResponse isMainThread : " + String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
//                        FollowingDTO followingDTO= new Gson().fromJson(response,FollowingDTO.class);
//                        if(followingDTO.isStatus()&&followingDTO.getData()!=null&&followingDTO.getData().size()>0){
//                            searchPostAdapter = new ActivityFollowingAdapter(getActivity(),followingDTO.getData());
//                            recyclerView.setAdapter(searchPostAdapter);
//                        }
//                        else {
//                            Toast.makeText(getActivity(),"Data not found",Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                       progressBarHandler.hide();
////                        dismissProgressDialog();
//                        if (error.getErrorCode() != 0) {
//                            // received ANError from server
//                            // error.getErrorCode() - the ANError code from server
//                            // error.getErrorBody() - the ANError body from server
//                            // error.getErrorDetail() - just a ANError detail
//                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
//                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//
//    }
}
