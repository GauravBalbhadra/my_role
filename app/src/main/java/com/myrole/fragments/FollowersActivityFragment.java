package com.myrole.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.ActivityFollowingAdapter;
import com.myrole.components.BottomBar;
import com.myrole.data.FollowingDTO;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;

import java.util.HashMap;

public class FollowersActivityFragment extends BaseFragment implements View.OnClickListener  {


    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ActivityFollowingAdapter searchPostAdapter;
    private ProgressBarHandler progressBarHandler;
    private Context context;
    private Activity mActivity;
    private RelativeLayout lay_follower_fragment;
    private TextView txt_no_data;
    private View view;
    BottomBar bottom_bar;
    Fragment mContent;
    int  mCurCheckPosition;

    public static FollowersActivityFragment newInstance() {
        FollowersActivityFragment fragment = new FollowersActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
//        if (savedInstanceState != null) {
//            //Restore the fragment's instance
//         mContent = getFragmentManager().getFragment(savedInstanceState, FollowersActivityFragment.newInstance());
//
//        }
        bottom_bar = getActivity().findViewById(R.id.bottom_bar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //  final View view = inflater.inflate(R.layout.fragment_following_post, container, false);
       view = inflater.inflate(R.layout.fragment_following_post, container, false);
      // txt_no_data= (TextView) view.findViewById(R.id.txt_no_data);
        progressBarHandler = new ProgressBarHandler(getActivity());
        lay_follower_fragment = (RelativeLayout) view.findViewById(R.id.lay_follower_fragment);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        new GetActivityTask().execute();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

//        callServiceForFeeds();

        return view;
    }
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            //Restore the fragment's state here
//            progressBarHandler = new ProgressBarHandler(getActivity());
//            lay_follower_fragment = (RelativeLayout) view.findViewById(R.id.lay_follower_fragment);
//            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//            new GetActivityTask().execute();
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//            recyclerView.setLayoutManager(linearLayoutManager);
//
//        }
//    }
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        //Save the fragment's instance
//        getFragmentManager().putFragment(outState, "myFragmentName", mContent);
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
            lay_follower_fragment.setVisibility(View.GONE);
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID));
            postDataParams.put("type", "you");
            return HTTPUrlConnection.getInstance().load(getContext(), Config.GET_ACTIVITY, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            lay_follower_fragment.setVisibility(View.VISIBLE);

            FollowingDTO followingDTO = new Gson().fromJson(result, FollowingDTO.class);

                if(followingDTO!= null && followingDTO.isStatus() && followingDTO.getData()!= null && followingDTO.getData().size()>0){
                    searchPostAdapter = new ActivityFollowingAdapter(getActivity(),getContext(),followingDTO.getData(),bottom_bar);
                    recyclerView.setAdapter(searchPostAdapter);
                }
                else {
                    view.findViewById(R.id.txt_no_data).setVisibility(View.VISIBLE);
                  //  txt_no_data.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(),"Data not found",Toast.LENGTH_LONG).show();
                }
        }
    }


//    private void callServiceForFeeds() {
//        progressBarHandler.show();
//        AppPreferences preferences = AppPreferences.getAppPreferences(getActivity());
//        AndroidNetworking.post(Config.GET_ACTIVITY)
//                .setTag(this)
//                .addBodyParameter("user_id",preferences.getStringValue(AppPreferences.USER_ID))
//                .addBodyParameter("type","you")
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
//                        FollowingDTO followingDTO = new Gson().fromJson(response,FollowingDTO.class);
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
//                        progressBarHandler.hide();
//                        //dismissProgressDialog();
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
