package com.myrole.fragments;//package com.myrole.fragments;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.myrole.BaseFragment;
//import com.myrole.HomeActivity;
//import com.myrole.R;
//import com.myrole.adapter.GeneralCategoryPostAdapter;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.utils.Utils;
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
//public class CategoryFragment extends BaseFragment implements BaseFragment.OnFragmentInteractionListener,View.OnClickListener {
//    private static ArrayList<Post> generalList = new ArrayList<>();
//    LinearLayoutManager layoutManager;
//
//
//    private OnFragmentInteractionListener mListener;
//    TextView textViewrole,tv_role_desc;
//    private String categoryID;
//    private RecyclerView recyclerView;
//    private GeneralCategoryPostAdapter categoryPostAdapter;
//    private ProgressBar progressBar;
//    public CategoryFragment() {
//    }
//
//    public static CategoryFragment newInstance(String categoryID) {
//        CategoryFragment fragment = new CategoryFragment();
//        Bundle bndl = new Bundle();
//        bndl.putString("CATEGORYID", categoryID);
//        fragment.setArguments(bndl);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        categoryID = getArguments().getString("CATEGORYID","");
//
//        //new GetTodayRole().execute(categoryID);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.layout_recycler_view_cat, container, false);
//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        textViewrole = (TextView)getView().findViewById(R.id.txt_today);
//        tv_role_desc = (TextView)getView().findViewById(R.id.txt_description);
//        textViewrole.setOnClickListener(this);
//        tv_role_desc.setOnClickListener(this);
//    }
//
//    @Override
//    public void setMenuVisibility(final boolean visible) {
//        super.setMenuVisibility(visible);
//        if (visible) {
//            if (Utils.isNetworkConnected(getActivity(), true))
//                new GetPostsTask().execute();
//        }
//    }
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//
//        switch (v.getId()){
//
//            case R.id.txt_today:
//                loadTodayRole();
//                break;
//            case R.id.txt_description:
//                loadTodayRole();
//                break;
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public void loadTodayRole() {
//        if (((HomeActivity) getActivity()).fragmentManager != null) {
//            ((HomeActivity) getActivity()).fragmentManager.beginTransaction()
//                    .add(R.id.main_frame,
//                            TodayRoleFragment.newInstance(categoryID),
//                            "TODAYROLEFRAGMENT")
//                    .addToBackStack(null)
//                    .commit();
//        }
//    }
//
//
//
//    @Override
//    public void onFragmentInteraction(String command) {
//        if (command.equalsIgnoreCase("BACK")) {
//
//        }
//    }
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
////            showProgessDialog();
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            postDataParams = new HashMap<String, String>();
//            if(getActivity() == null ) return null;
//            AppPreferences preferences = AppPreferences.getAppPreferences(getActivity());
//            postDataParams.put("user_id", preferences.getStringValue(AppPreferences.USER_ID));
//            //postDataParams.put("category_id", categoryID);
//            return HTTPUrlConnection.getInstance().load(getContext(),Config.GET_USER_POST, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////            dismissProgressDialog();
//            progressBar.setVisibility(View.GONE);
//            if(result != null) {
//                try {
//                    JSONObject object = new JSONObject(result);
//                    if (object.getBoolean("status")) {
//                        Log.v("Result", result);
//                        JSONArray arr = object.optJSONArray("data");
//                        generalList.clear();
//                    /*dummy data*/
//                        Post post1 = new Post();
//                        post1.description = "It is dummy data to test the functionality of my role cell.";
//                        post1.type = "ROLE";
//                        //generalList.add(post1);
//                    /*dummy end*/
//                        if (arr != null) {
//                            for (int i = 0; i < arr.length(); i++) {
//                                JSONObject postDetail = arr.getJSONObject(i);
//                                Post post = new Post();
//                                post.id = postDetail.getString("post_id");
//                                post.username = postDetail.getString("owner_name");
//                                post.postImage = postDetail.getString("url");
//                                post.ownerId = postDetail.getString("owner_id");
//                                post.categoryName = postDetail.getString("cat_name");
//                                post.description = postDetail.getString("description");
//                                post.numLoves = postDetail.getString("post_likes");
//                                post.numComment = postDetail.getJSONArray("comments").length() + "";
//                                //post.image = R.drawable.singer_bg;
//                                generalList.add(post);
//                            }
//                        }
//                        //swipeView.setRefreshing(false);
//
//                    } else {
//                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//               /* recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
//                recyclerView.setHasFixedSize(true);
//                layoutManager = new LinearLayoutManager(getActivity());
//                recyclerView.setLayoutManager(layoutManager);
//
//                categoryPostAdapter = new GeneralCategoryPostAdapter(generalList,mVideoPlayerManager, getActivity(),mList);
//                recyclerView.setAdapter(categoryPostAdapter);*/
//               // mItemsPositionGetter = new RecyclerViewItemPositionGetter(layoutManager, recyclerView);
//
//
//                /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//                    @Override
//                    public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                        mScrollState = scrollState;
//                        if(scrollState == RecyclerView.SCROLL_STATE_IDLE && !mList.isEmpty()){
//                                try{
//                                    mVideoVisibilityCalculator.onScrollStateIdle(
//                                            mItemsPositionGetter,
//                                            layoutManager.findFirstVisibleItemPosition(),
//                                            layoutManager.findLastVisibleItemPosition());
//                                }catch (Exception e){
//
//                                }
//
//                        }
//                    }
//
//                    @Override
//                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                       *//* if(!mList.isEmpty()){
//                            mVideoVisibilityCalculator.onScroll(mItemsPositionGetter, mLayoutManager.findFirstVisibleItemPosition(), mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1, mScrollState);
//                        }*//*
//                    }
//                });*/
//            }
//
//        }
//    }
//}
