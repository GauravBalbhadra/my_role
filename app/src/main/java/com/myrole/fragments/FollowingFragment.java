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
import com.myrole.adapter.FollowingAdapter;
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

public class FollowingFragment extends BaseFragment implements View.OnClickListener {

    private AppPreferences pref;


    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private FollowingAdapter followingAdapter;
    private View view;
    private ProgressBarHandler progressBarHandler;
    String getuserId, user_id;
    EditText search_text;
    TextView txt_font_search;
    private OnFragmentInteractionListener mListener;

    public FollowingFragment() {

    }

    public static FollowingFragment newInstance(String userId) {
        FollowingFragment fragment = new FollowingFragment();
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
        view = inflater.inflate(R.layout.fragment_following, container, false);

        user_id = AppPreferences.getAppPreferences(getContext()).getStringValue(AppPreferences.USER_ID);
        getuserId = getArguments().getString("USERID");

        view.findViewById(R.id.btn_back).setOnClickListener(this);
      /*  Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
        txt_font_search = (TextView) view.findViewById(R.id.txt_font_search);
        txt_font_search.setTypeface(font);*/
        search_text = (EditText) view.findViewById(R.id.search_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        progressBarHandler = new ProgressBarHandler(getActivity());
        followingAdapter = new FollowingAdapter(contactList, getContext(), getActivity(),user_id,getuserId);
        recyclerView.setAdapter(followingAdapter);

        pref = AppPreferences.getAppPreferences(getActivity());
        pref.putBooleanValue(AppPreferences.allowRefreshfollower, false);


        new GetFollowersTask().execute(getuserId);

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                followingAdapter.getFilter().filter(s);
                //Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        // mListener = null;
    }

    //
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
            postDataParams.put("type_by", "following");

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
                    if (userArray.length() > 0) {
                        view.findViewById(R.id.no_following).setVisibility(View.GONE);
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
                    followingAdapter.notifyDataSetChanged();
                } else {

                    view.findViewById(R.id.no_following).setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
