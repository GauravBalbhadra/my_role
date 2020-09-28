package com.myrole.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.UserPostAdapter;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.Utils;
import com.myrole.vo.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vikesh.kumar on 6/20/2016.
 */
public class UserGeneralPostFragment extends BaseFragment implements  BaseFragment.OnFragmentInteractionListener {

    private static ArrayList<Post> generalList = new ArrayList<>();
    GridLayoutManager layoutManager;
    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeView;
    private boolean isRefreshing;
    private String categoryID;
    private RecyclerView recyclerView;
    private UserPostAdapter generalPostAdapter;

    public UserGeneralPostFragment() {
    }

    public static UserGeneralPostFragment newInstance(String categoryID) {
        UserGeneralPostFragment fragment = new UserGeneralPostFragment();
        Bundle bndl = new Bundle();
        bndl.putString("CATEGORYID", categoryID);
        fragment.setArguments(bndl);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        categoryID = getArguments().getString("CATEGORYID","");
        //new GetTodayRole().execute(categoryID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_recycler_view, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(layoutManager);
        generalPostAdapter = new UserPostAdapter(generalList, getActivity());
        recyclerView.setAdapter(generalPostAdapter);




        if (generalList.size() == 0 && Utils.isNetworkConnected(getActivity(), true))
            new GetPostsTask().execute();
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
    public void onFragmentInteraction(String command) {
        if (command.equalsIgnoreCase("BACK")) {

        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }



    class GetPostsTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            if(getActivity() != null) {
                postDataParams = new HashMap<String, String>();
                AppPreferences preferences = AppPreferences.getAppPreferences(getActivity());
                postDataParams.put("user_id", preferences.getStringValue(AppPreferences.USER_ID));

                if (!categoryID.isEmpty()) {
                    postDataParams.put("category_id", categoryID);
                    response = HTTPUrlConnection.getInstance().load(getContext(), Config.GET_USER_POST_BY_CATEGORY, postDataParams);
                } else {
                    response = HTTPUrlConnection.getInstance().load(getContext(), Config.GET_USER_UPLOADED_POST, postDataParams);
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //dismissProgressDialog();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    Log.v("Result", result);
                    generalList.clear();
                    JSONArray arr = object.getJSONArray("data");

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject postDetail = arr.getJSONObject(i);
                        Post post = new Post();
                        post.id = postDetail.getString("post_id");
                        post.username = postDetail.getString("owner_name");
                        post.postImage = postDetail.getString("url");
                        post.ownerId = postDetail.getString("owner_id");
                        post.categoryName = postDetail.getString("cat_name");
                        post.description = postDetail.getString("description");
                        post.numLoves = postDetail.getString("post_likes");
                        post.numComment = postDetail.getJSONArray("comments").length() + "";
                        //post.image = R.drawable.singer_bg;
                        generalList.add(post);
                    }
                    generalPostAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
