package com.myrole;//package com.myrole;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.myrole.adapter.FollowerAdapter;
//import com.myrole.fragments.FollowingFragment;
//import com.myrole.utils.AppPreferences;
//import com.myrole.utils.Config;
//import com.myrole.utils.HTTPUrlConnection;
//import com.myrole.utils.ProgressBarHandler;
//import com.myrole.vo.Contact;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class FollowerActivity extends BaseActivity implements View.OnClickListener {
//
//    private AppPreferences pref;
//
//    public Activity mActivity;
//    public Context mContext;
//    private RecyclerView recyclerView;
//    private List<Contact> contactList;
//    private FollowerAdapter followerAdapter;
//    private View view;
//    private ProgressBarHandler progressBarHandler;
//
//    private FollowingFragment.OnFragmentInteractionListener mListener;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        contactList = new ArrayList<>();
//        setContentView(R.layout.fragment_follower);
//        mActivity = this;
//        mContext = this;
//        String userId = getIntent().getStringExtra("user_id");
//
//        findViewById(R.id.btn_back).setOnClickListener(this);
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
//        recyclerView.setLayoutManager(layoutManager);
//        progressBarHandler = new ProgressBarHandler(mActivity);
//        followerAdapter = new FollowerAdapter(contactList, mContext, mActivity);
//        recyclerView.setAdapter(followerAdapter);
//
//        pref = AppPreferences.getAppPreferences(mActivity);
//        pref.putBooleanValue(AppPreferences.allowRefreshfollower, false);
//
//
//       // String userId = getArguments().getString("USERID");
//        new GetFollowersTask().execute(userId);
//    }
////        @Override
////        public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                Bundle savedInstanceState) {
////            view = inflater.inflate(R.layout.fragment_following, container, false);
////
////            view.findViewById(R.id.btn_back).setOnClickListener(this);
////
////            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
////            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
////            recyclerView.setLayoutManager(layoutManager);
////            progressBarHandler = new ProgressBarHandler(getActivity());
////            followingAdapter = new FollowingAdapter(contactList,getContext(), getActivity());
////            recyclerView.setAdapter(followingAdapter);
////
////            pref = AppPreferences.getAppPreferences(getActivity());
////            pref.putBooleanValue(AppPreferences.allowRefreshfollower, false);
////
////
////            String userId = getArguments().getString("USERID");
////            new FollowingFragment.GetFollowersTask().execute(userId);
////
////            return view;
////        }
//
////        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
////        @Override
////        public void onClick(View v) {
////            super.onClick(v);
////            switch (v.getId()) {}}
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.btn_back:
//                    onBackPressed();
//                    break;
//            }}
//
//
//
//        class GetFollowersTask extends AsyncTask<String, Void, String> {
//            HashMap<String, String> postDataParams;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
////            showProgessDialog();
//                progressBarHandler.show();
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                postDataParams = new HashMap<String, String>();
//                postDataParams.put("user_id", params[0]);
//                postDataParams.put("type_by", "follower");
//
//                return HTTPUrlConnection.getInstance().load(mActivity, Config.GET_FOLLOWER, postDataParams);
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
////            dismissProgressDialog();
//                progressBarHandler.hide();
//                try {
//                    JSONObject object = new JSONObject(result);
//                    if (object.getBoolean("status")) {
//
//                        JSONArray userArray = object.getJSONArray("data");
//                        if (userArray.length() > 0) {
//                            findViewById(R.id.no_follower).setVisibility(View.GONE);
//                        }
//                        contactList.clear();
//                        for (int i = 0; i < userArray.length(); i++) {
//                            Contact contact = new Contact();
//                            contact.id = userArray.getJSONObject(i).getString("id");
//                            contact.name = userArray.getJSONObject(i).getString("name");
//                            contact.image = userArray.getJSONObject(i).getString("image");
//                            contact.follow = userArray.getJSONObject(i).getString("follow");
//                            contactList.add(contact);
//                        }
//                        followerAdapter.notifyDataSetChanged();
//                    } else {
//
//                        findViewById(R.id.no_follower).setVisibility(View.VISIBLE);
//                        //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
