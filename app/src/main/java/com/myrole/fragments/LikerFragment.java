package com.myrole.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.LikerAdapter;
import com.myrole.utils.AppPreferences;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.utils.ProgressBarHandler;
import com.myrole.vo.Liker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LikerFragment extends BaseFragment implements View.OnClickListener {


    private RecyclerView recyclerView;
    private List<Liker> likertList;
    private LikerAdapter likerAdapter;
    private OnFragmentInteractionListener mListener;
    private String post_id;
    private ProgressBarHandler progressBarHandler;
    private View view;
    /*BottomBar bottom_bar;
    FrameLayout main_frame;*/
    EditText search_text;
    public LikerFragment() {

    }

    public static Fragment newInstance(String post_id) {
        LikerFragment fragment = new LikerFragment();
        Bundle bndl = new Bundle();
        bndl.putString("POSTID", post_id);
        fragment.setArguments(bndl);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        post_id = getArguments().getString("POSTID");
        likertList = new ArrayList<>();
        /*try{
        bottom_bar = getActivity().findViewById(R.id.bottom_bar);
        main_frame = getActivity().findViewById(R.id.main_frame);}catch (Exception e){}*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    /*    try {
            main_frame.setPadding(0, 0, 0, 0);
           // bottom_bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(bottom_bar.getHeight());
        }catch (Exception e){
            try {
                try {
                    main_frame = getActivity().findViewById(R.id.main_frame);
                 //   bottom_bar = getActivity().findViewById(R.id.bottom_bar);
                }catch (Exception e2){}
                main_frame.setPadding(0, 0, 0, 0);
               // bottom_bar.animate().setInterpolator(new AccelerateDecelerateInterpolator()).translationY(bottom_bar.getHeight());
            }catch (Exception e3){
//                main_frame.setPadding(0, 0, 0, 170);
            }
        }*/

        view = inflater.inflate(R.layout.fragment_liker, container, false);
        ImageView back = (ImageView) view.findViewById(R.id.btn_back);
        back.setOnClickListener(this);
        search_text = (EditText) view.findViewById(R.id.search_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBarHandler = new ProgressBarHandler(getActivity());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        likerAdapter = new LikerAdapter(likertList,getContext(), getActivity());
        recyclerView.setAdapter(likerAdapter);

        String userId = AppPreferences.getAppPreferences(getActivity()).getStringValue(AppPreferences.USER_ID);
        new GetLikerTask().execute(userId, post_id);

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                likerAdapter.getFilter().filter(s);
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
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }


    class GetLikerTask extends AsyncTask<String, Void, String> {
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
            postDataParams.put("post_id", params[1]);
            return HTTPUrlConnection.getInstance().load(getContext(), Config.GET_LIKER, postDataParams);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            dismissProgressDialog();
            progressBarHandler.hide();
            try {
                JSONObject object = new JSONObject(result);
                if (object.getBoolean("status")) {
                    likertList.clear();
                    JSONArray userArray = object.getJSONArray("data");
                    if (userArray.length() > 0) {
                        view.findViewById(R.id.no_liker).setVisibility(View.GONE);
                        for (int i = 0; i < userArray.length(); i++) {
                            Liker liker = new Liker();
                            liker.user_id = userArray.getJSONObject(i).getString("user_id");
                            liker.name = userArray.getJSONObject(i).getString("name");
                            liker.image = userArray.getJSONObject(i).getString("image");
                            liker.follows = userArray.getJSONObject(i).getString("follows");
                            likertList.add(liker);
                        }

                        likerAdapter.notifyDataSetChanged();
                    }

                } else {
                    view.findViewById(R.id.no_liker).setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
