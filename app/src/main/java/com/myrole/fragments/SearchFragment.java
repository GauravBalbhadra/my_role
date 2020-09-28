package com.myrole.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;
import com.myrole.adapter.SearchContactsAdapter;
import com.myrole.networking.ApiService;
import com.myrole.networking.SongServiceClient;
import com.myrole.utils.Config;
import com.myrole.utils.HTTPUrlConnection;
import com.myrole.vo.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    EditText editTextSearch;
    CardView cardMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        editTextSearch = view.findViewById(R.id.search_text);
        cardMessage = view.findViewById(R.id.cardMessage);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) doSearch(s.toString());
            }
        });

        return view;
    }

    public void doSearch(String keyword) {
        getSearchResult(keyword);
    }

    private void getSearchResult(String keyword) {
        Retrofit retrofit = SongServiceClient.getRetrofitClient();
        ApiService apiService = retrofit.create(ApiService.class);
        HashMap<String, String> map = new HashMap();
        map.put("search", keyword);

        Call<ResponseBody> call = apiService.getSearchResult(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getBoolean("status")) {
                        JSONArray userArray = object.getJSONArray("data");
                        if (userArray.length() > 0) {
                            List<Contact> contactList = new ArrayList<>();
                            for (int i = 0; i < userArray.length(); i++) {
                                Contact contact = new Contact();
                                contact.id = userArray.getJSONObject(i).getString("id");
                                contact.phone = userArray.getJSONObject(i).getString("mobile");
                                contact.name = userArray.getJSONObject(i).getString("name");
                                contact.image = userArray.getJSONObject(i).getString("image");
                                contact.status = "User";
                                contactList.add(contact);
                            }
                            SearchContactsAdapter contactsAdapter = new SearchContactsAdapter(getContext(), contactList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(contactsAdapter);
                        }
                    }

                } catch (IOException | JSONException | NullPointerException e) {
                    Toast.makeText(getContext(), "Exception : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    class SearchTask extends AsyncTask<String, Void, String> {
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(result);
                            Toast.makeText(getContext(), ""+object, Toast.LENGTH_SHORT).show();
                            //  view.findViewById(R.id.txt_no_data).setVisibility(View.GONE);
                            if (object.getBoolean("status")) {
                                //contactList.clear();
                                JSONArray userArray = object.getJSONArray("data");
                                if (userArray.length() > 0) {
                                    //view.findViewById(R.id.txt_no_data).setVisibility(View.GONE);
                                    // Toast.makeText(getContext(), " kayi karuu", Toast.LENGTH_LONG).show();
                                    List<Contact> contactList = new ArrayList<>();
                                    for (int i = 0; i < userArray.length(); i++) {
                                        Contact contact = new Contact();
                                        contact.id = userArray.getJSONObject(i).getString("id");
                                        contact.phone = userArray.getJSONObject(i).getString("mobile");
                                        contact.name = userArray.getJSONObject(i).getString("name");
                                        contact.image = userArray.getJSONObject(i).getString("image");
                                        contact.status = "User";
                                        contactList.add(contact);
                                    }
                                    SearchContactsAdapter contactsAdapter = new SearchContactsAdapter(getContext(), contactList);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    recyclerView.setAdapter(contactsAdapter);

                                } else {
//                                Toast.makeText(getContext(), "khali he tho mhu kayi karuu", Toast.LENGTH_LONG).show();
//                                  view.findViewById(R.id.txt_no_data).setVisibility(View.VISIBLE);

                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Exception : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                Toast.makeText(getContext(), "Exception 1 : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}