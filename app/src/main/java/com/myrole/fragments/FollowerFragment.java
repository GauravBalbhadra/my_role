package com.myrole.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.FollowerAdapter;
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

public class FollowerFragment extends BaseFragment implements View.OnClickListener {

    private AppPreferences pref;


    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private FollowerAdapter followerAdapter;
    private OnFragmentInteractionListener mListener;
    private View view;
    private ProgressBarHandler progressBarHandler;
    TextView txt_font_search;
    EditText search_text;
    public FollowerFragment() {}

    public static FollowerFragment newInstance(String userId) {
        FollowerFragment fragment = new FollowerFragment();
        Bundle bndl = new Bundle();
        bndl.putString("USERID", userId);
        fragment.setArguments(bndl);
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
        view   =   inflater.inflate(R.layout.fragment_follower, container, false);
        view.findViewById(R.id.btn_back).setOnClickListener(this);

        //Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
       // txt_font_search = (TextView) view.findViewById(R.id.txt_font_search);
        //txt_font_search.setTypeface(font);
        search_text = (EditText) view.findViewById(R.id.search_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBarHandler = new ProgressBarHandler(getActivity());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        followerAdapter = new FollowerAdapter(contactList,getContext(), getActivity());
        recyclerView.setAdapter(followerAdapter);

        pref = AppPreferences.getAppPreferences(getActivity());
        pref.putBooleanValue(AppPreferences.allowRefreshfollower, false);

        String userId = getArguments().getString("USERID");
        new GetFollowersTask().execute(userId);

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                followerAdapter.getFilter().filter(s);
                //Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

                // listening to search query text change
//        search_text.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // filter recycler view when query submitted
//                mAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                mAdapter.getFilter().filter(query);
//                return false;
//            }
//        });

        return view;
    }



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


    class GetFollowersTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showProgessDialog();
            progressBarHandler.show();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("user_id", params[0]);
            postDataParams.put("type_by", "follower");
            return HTTPUrlConnection.getInstance().load(getActivity(), Config.GET_FOLLOWER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
           progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    JSONArray userArray = object.getJSONArray("data");
                    if (userArray.length()>0){
                       view.findViewById(R.id.no_follower).setVisibility(View.GONE);
                    }
                    contactList.clear();
                    for (int i = 0; i < userArray.length(); i++) {
                        Contact contact = new Contact();
                        contact.id = userArray.getJSONObject(i).getString("id");
                        contact.name = userArray.getJSONObject(i).getString("name");
                        contact.image = userArray.getJSONObject(i).getString("image");
                        contact.follow = userArray.getJSONObject(i).getString("follow");
                        contactList.add(contact);
                    }
                    followerAdapter.notifyDataSetChanged();
                } else {

                    view.findViewById(R.id.no_follower).setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
