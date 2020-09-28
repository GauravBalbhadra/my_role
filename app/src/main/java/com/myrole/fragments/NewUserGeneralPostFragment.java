package com.myrole.fragments;//package com.myrole.fragments;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.getbase.floatingactionbutton.FloatingActionButton;
//import com.getbase.floatingactionbutton.FloatingActionsMenu;
//import com.myrole.BaseFragment;
//import com.myrole.R;
//import com.myrole.adapter.UserPostAdapter;
//import com.myrole.adapter.UserPostAdapter2;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.vo.Post;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Created by vikesh.kumar on 6/20/2016.
// */
//public class NewUserGeneralPostFragment extends BaseFragment  {
//
//
//    public static final String TAG = "Toro:Facebook";
//    private static ArrayList<Post> generalList = new ArrayList<>();
//    GridLayoutManager layoutManager;
//    private OnFragmentInteractionListener mListener;
//    private SwipeRefreshLayout swipeView;
//    private boolean isRefreshing;
//    private String categoryID,userID;
//    public RecyclerView recyclerView;
//    private UserPostAdapter generalPostAdapter;
//    private UserPostAdapter2 generalPostAdapter1;
//
//    public NewUserGeneralPostFragment() {
//    }
//
//    public static NewUserGeneralPostFragment newInstance(String categoryID,String userID) {
//        NewUserGeneralPostFragment fragment = new NewUserGeneralPostFragment();
//        Bundle bndl = new Bundle();
//        bndl.putString("CATEGORYID", categoryID);
//        bndl.putString("USERID", userID);
//        fragment.setArguments(bndl);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        categoryID = getArguments().getString("CATEGORYID","");
//        userID = getArguments().getString("USERID");
//        //new GetTodayRole().execute(categoryID);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
//        generalList.clear();
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        layoutManager = new GridLayoutManager(getActivity(),3);
//        recyclerView.setLayoutManager(layoutManager);
//        generalPostAdapter = new UserPostAdapter(generalList, getActivity());
//        recyclerView.setAdapter(generalPostAdapter);
//
//        final FloatingActionsMenu rightLabels = (FloatingActionsMenu) view.findViewById(R.id.right_labels);
//        FloatingActionButton addedOnce = (FloatingActionButton) view.findViewById(R.id.list_b);
//        FloatingActionButton addedtwo = (FloatingActionButton) view.findViewById(R.id.grid_b);
//        addedOnce.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rightLabels.collapse();
//               LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                recyclerView.setLayoutManager(layoutManager);
//                generalPostAdapter1 = new UserPostAdapter2(generalList, getActivity(),NewUserGeneralPostFragment.this);
//                recyclerView.setAdapter(generalPostAdapter1);
//            }
//        });
//        addedtwo.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rightLabels.collapse();
//                layoutManager = new GridLayoutManager(getActivity(),3);
//                recyclerView.setLayoutManager(layoutManager);
//                generalPostAdapter = new UserPostAdapter(generalList, getActivity());
//                recyclerView.setAdapter(generalPostAdapter);
//            }
//        });
//
////        if (generalList.size() == 0 && Utils.isNetworkConnected(getActivity(), true))
//            new GetPostsTask().execute();
//        return view;
//    }
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//    }
//
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(String command);
//    }
//
//
//
//    class GetPostsTask extends AsyncTask<String, Void, String> {
//        HashMap<String, String> postDataParams;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            //showProgessDialog();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String response = "";
//            if(getActivity() != null) {
//                postDataParams = new HashMap<String, String>();
//                AppPreferences preferences = AppPreferences.getAppPreferences(getActivity());
//                postDataParams.put("user_id", userID);
//
//                if (!categoryID.isEmpty()) {
//                    postDataParams.put("category_id", categoryID);
//                    response = HTTPUrlConnection.getInstance().load(getContext(),Config.GET_USER_POST_BY_CATEGORY, postDataParams);
//                } else {
//                    response = HTTPUrlConnection.getInstance().load(getContext(),Config.GET_USER_UPLOADED_POST, postDataParams);
//                }
//            }
//
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            //dismissProgressDialog();
//            try {
//                JSONObject object = new JSONObject(result);
//                if (object.getBoolean("status")) {
//                    Log.v("Result", result);
//                    generalList.clear();
//                    JSONArray arr = object.getJSONArray("data");
//
//                    for (int i = 0; i < arr.length(); i++) {
//                        JSONObject postDetail = arr.getJSONObject(i);
//                        Post post = new Post();
//                        post.id = postDetail.getString("post_id");
//                        post.username = postDetail.getString("owner_name");
//                        post.postImage = postDetail.getString("url");
//                        post.ownerId = postDetail.getString("owner_id");
//                        post.categoryName = postDetail.getString("cat_name");
//                        post.description = postDetail.getString("description");
//                        post.numLoves = postDetail.getString("post_likes");
//                        post.islike = postDetail.getString("islike");
//                        post.comment_count = postDetail.getString("comment_count");
//                        post.numComment = postDetail.getJSONArray("comments").length() + "";
//                        //post.image = R.drawable.singer_bg;
//                        generalList.add(post);
//                    }
//                    generalPostAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//}
