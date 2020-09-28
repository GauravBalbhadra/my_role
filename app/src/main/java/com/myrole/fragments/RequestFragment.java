package com.myrole.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.RequestAdapter;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.vo.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private RequestAdapter requestAdapter;
    private OnFragmentInteractionListener mListener;
    private ProgressBarHandler progressBarHandler;
    private View view;
    SwipeRefreshLayout swipe_refresh;


    public RequestFragment() {}

    public static RequestFragment newInstance( ) {
        RequestFragment fragment = new RequestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request, container, false);
        progressBarHandler = new ProgressBarHandler(getActivity());

//        view.findViewById(R.id.btn_back).setOnClickListener(this);
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        requestAdapter = new RequestAdapter(contactList,getContext(), getActivity());
        recyclerView.setAdapter(requestAdapter);

        String userId =  AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        new GetFollowerRequestTask().execute(userId);
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
        String userId =  AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        new GetFollowerRequestTask().execute(userId);
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


    class GetFollowerRequestTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
          //  progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            return HTTPUrlConnection.getInstance().load(getActivity(), Config.GET_FOLLOWER_REQUEST, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            //progressBarHandler.hide();
            try {
                contactList.clear();
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONArray userArray = object.getJSONArray("data");
                    if (userArray.length()>0){
                        view.findViewById(R.id.no_requests).setVisibility(View.GONE);
                    }
                    for (int i = 0; i < userArray.length(); i++) {
                        Contact contact = new Contact();
                        contact.id = userArray.getJSONObject(i).getString("id");
                        contact.name = userArray.getJSONObject(i).getString("name");
                        contact.image = userArray.getJSONObject(i).getString("image");
//                        contact.follow = userArray.getJSONObject(i).getString("follow");
                        contactList.add(contact);
                    }
                    requestAdapter.notifyDataSetChanged();
                } else {
                    view.findViewById(R.id.no_requests).setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
