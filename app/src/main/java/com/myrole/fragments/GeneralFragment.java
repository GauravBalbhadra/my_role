package com.myrole.fragments;//package com.myrole.fragments;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.myrole.BaseFragment;
//import com.myrole.R;
//import com.myrole.adapter.GeneralCategoryPostAdapter;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.utils.Utils;
//import com.myrole.vo.Post;
//import com.volokh.danylo.visibility_utils.calculator.ListItemsVisibilityCalculator;
//import com.volokh.danylo.visibility_utils.calculator.SingleListViewItemActiveCalculator;
//import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter;
//import com.volokh.danylo.visibility_utils.scroll_utils.RecyclerViewItemPositionGetter;
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
//public class GeneralFragment extends BaseFragment implements  SwipeRefreshLayout.OnRefreshListener {
//    //private final ArrayList<BaseVideoItem> mList = new ArrayList<>();
//    private static ArrayList<Post> generalList = new ArrayList<>();
//    LinearLayoutManager mLayoutManager;
//    private OnFragmentInteractionListener mListener;
//    private SwipeRefreshLayout swipeView;
//    private boolean isRefreshing;
//    //private String categoryID;
//    private RecyclerView recyclerView;
//    private GeneralCategoryPostAdapter generalPostAdapter;
//    private ProgressBar progressBar;
//    /**
//     * Only the one (most visible) view should be active (and playing).
//     * To calculate visibility of views we use {@link SingleListViewItemActiveCalculator}
//     */
///*    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
//           // new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);*/
//
//
//    /**
//     * ItemsPositionGetter is used by {@link ListItemsVisibilityCalculator} for getting information about
//     * items position in the RecyclerView and LayoutManager
//     */
//    private ItemsPositionGetter mItemsPositionGetter;
//
//
//    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
//
//    public GeneralFragment() {
//    }
//
//    public static GeneralFragment newInstance(String categoryID) {
//        GeneralFragment fragment = new GeneralFragment();
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
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
//
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//
//        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
//
//        if (Utils.isNetworkConnected(getActivity(), true))
//            new GetPostsTask().execute();
//
//
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        /*if(!mList.isEmpty()){
//            // need to call this method from list view handler in order to have filled list
//
//            recyclerView.post(new Runnable() {
//                @Override
//                public void run() {
//
//                    mVideoVisibilityCalculator.onScrollStateIdle(
//                            mItemsPositionGetter,
//                            mLayoutManager.findFirstVisibleItemPosition(),
//                            mLayoutManager.findLastVisibleItemPosition());
//
//                }
//            });
//        }*/
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        // we have to stop any playback in onStop
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
////
////    @Override
////    public void onFragmentInteraction(String command) {
////        if (command.equalsIgnoreCase("BACK")) {
////
////        }
////    }
//
//    @Override
//    public void onRefresh() {
//
//    }
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(String command);
//    }
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
//            if (getActivity() == null) return null;
//            AppPreferences preferences = AppPreferences.getAppPreferences(getActivity());
//            postDataParams.put("user_id", preferences.getStringValue(AppPreferences.USER_ID));
//
//            return HTTPUrlConnection.getInstance().load(getActivity(),Config.GET_USER_POST, postDataParams);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////          dismissProgressDialog();
//            progressBar.setVisibility(View.GONE);
//            if (result != null) {
//                try {
//                    JSONObject object = new JSONObject(result);
//                    if (object.getBoolean("status")) {
//                        Log.v("Result", result);
//                        JSONArray arr = object.getJSONArray("data");
//                        generalList.clear();
//                        //mList.clear();
//                        for (int i = 0; i < arr.length(); i++) {
//                            JSONObject postDetail = arr.getJSONObject(i);
//                            Post post = new Post();
//                            post.id = postDetail.getString("post_id");
//                            post.username = postDetail.getString("owner_name");
//                            post.postImage = postDetail.getString("url");
//                            post.owner_image = postDetail.getString("owner_image");
//                            post.ownerId = postDetail.getString("owner_id");
//                            post.categoryName = postDetail.getString("cat_name");
//                            post.description = postDetail.getString("description");
//                            post.numLoves = postDetail.getString("post_likes");
//                            post.numComment = postDetail.getJSONArray("comments").length() + "";
//                            post.postTime = postDetail.getString("post_time");
//                            //post.image = R.drawable.singer_bg;
//                            generalList.add(post);
//                           /* try {
//                               // mList.add(ItemFactory.createItemFromAsset(postDetail.getString("url"), postDetail.getString("owner_image"), getActivity()));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }*/
//                        }
//
//                    } else {
//                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
//                recyclerView.setHasFixedSize(true);
//                mLayoutManager = new LinearLayoutManager(getActivity());
//                recyclerView.setLayoutManager(mLayoutManager);
//
//                // generalPostAdapter = new GeneralCategoryPostAdapter(generalList,mVideoPlayerManager, getActivity(),mList);
//                recyclerView.setAdapter(generalPostAdapter);
//                mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, recyclerView);
//
//
//                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//                    @Override
//                    public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//                       /* mScrollState = scrollState;
//                            if(scrollState == RecyclerView.SCROLL_STATE_IDLE && !mList.isEmpty()){
//                                try{
//
//                                    mVideoVisibilityCalculator.onScrollStateIdle(
//                                            mItemsPositionGetter,
//                                            mLayoutManager.findFirstVisibleItemPosition(),
//                                            mLayoutManager.findLastVisibleItemPosition());
//                                }
//                                catch (Exception e){
//
//                                  e.printStackTrace();
//                                }
//
//
//
//
//
//                            }
//
//
//                    }
//
//                    @Override
//                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                       /* if(!mList.isEmpty()){
//                            mVideoVisibilityCalculator.onScroll(mItemsPositionGetter, mLayoutManager.findFirstVisibleItemPosition(), mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1, mScrollState);
//                        }*/
//                    }
//                });
//            }
//        }
//    }
//
//
//}
