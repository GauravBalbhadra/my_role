package com.myrole.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.SearchContactsAdapter;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.Utils;
import com.myrole.vo.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchPeopleFragment extends BaseFragment implements View.OnClickListener {

    String searchWord = "";
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private SearchContactsAdapter contactsAdapter;
    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBarSearchPeople;
    private View view;


    public SearchPeopleFragment() {
    }

    public static SearchPeopleFragment newInstance() {
        SearchPeopleFragment fragment = new SearchPeopleFragment();
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
        view = inflater.inflate(R.layout.fragment_search_people, container, false);
        progressBarSearchPeople = (ProgressBar) view.findViewById(R.id.progressBarSearchPeople);
        RelativeLayout rel_search  = (RelativeLayout)     view.findViewById(R.id.rel_search);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        contactsAdapter = new SearchContactsAdapter(getContext(), contactList);
        recyclerView.setAdapter(contactsAdapter);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "serach people click", Toast.LENGTH_LONG).show();
                Utils.hideKeybord(getContext(),v);
            }
        });

        return view;
    }


    public void doSearch(String keyword) {
     //   Utils.hideKeybord(getContext(),view);
        searchWord = keyword;
        new SearchTask().execute(searchWord);
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


    class SearchTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (view==null){}else{
            if (getUserVisibleHint())
                view.findViewById(R.id.rel_search).setVisibility(View.VISIBLE);}
              //  view.findViewById(R.id.progressBarSearchPeople).setVisibility(View.VISIBLE);
//            showProgessDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            postDataParams = new HashMap<String, String>();
            postDataParams.put("search", params[0]);
            return HTTPUrlConnection.getInstance().load(getActivity(), Config.USER_SEARCH, postDataParams);

        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            if ( view.findViewById(R.id.rel_search).getVisibility() == View.VISIBLE)
            view.findViewById(R.id.rel_search).setVisibility(View.GONE);
//            if (view.findViewById(R.id.progressBarSearchPeople).getVisibility() == View.VISIBLE)
//                view.findViewById(R.id.progressBarSearchPeople).setVisibility(View.GONE);
////            dismissProgressDialog();
            try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject(result);
                      //  view.findViewById(R.id.txt_no_data).setVisibility(View.GONE);
                        if (object.getBoolean("status")) {
                            contactList.clear();
                            JSONArray userArray = object.getJSONArray("data");
                            if(userArray != null && userArray.length() >0 ){
                                view.findViewById(R.id.txt_no_data).setVisibility(View.GONE);
                               // Toast.makeText(getContext(), " kayi karuu", Toast.LENGTH_LONG).show();
                            for (int i = 0; i < userArray.length(); i++) {
                                Contact contact = new Contact();
                                contact.id = userArray.getJSONObject(i).getString("id");
                                contact.phone = userArray.getJSONObject(i).getString("mobile");
                                contact.name = userArray.getJSONObject(i).getString("name");
                                contact.image = userArray.getJSONObject(i).getString("image");
                                contact.status = "User";
                                contactList.add(contact);
                            }
                            contactsAdapter.notifyDataSetChanged();

                            }else {
//                                Toast.makeText(getContext(), "khali he tho mhu kayi karuu", Toast.LENGTH_LONG).show();
//                                  view.findViewById(R.id.txt_no_data).setVisibility(View.VISIBLE);

                            }
                        } else {
                            contactList.clear();
                            contactsAdapter.notifyDataSetChanged();

                            // Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                            view.findViewById(R.id.txt_no_data).setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });}catch (Exception e){}

        }
    }
}
