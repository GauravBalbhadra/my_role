package com.myrole.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.utils.Utils;
import com.myrole.vo.Category;
import com.myrole.vo.Contact;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FindFragment extends BaseFragment implements View.OnClickListener {
    public static ArrayList<Contact> searchPeopleList = new ArrayList<>();
    ArrayList<Category> categoryList;
    TabLayout tabLayout;
    ViewPager pager;
    ViewPagerAdapter adapter;
    EditText searchText = null;
    ImageView back;
    SearchPeopleFragment searchPeopleFragment;
    private OnFragmentInteractionListener mListener;
    private InputMethodManager inputMethodManager;
    private View view;

    public FindFragment() {
    }

    public static FindFragment newInstance() {
        FindFragment fragment = new FindFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryList = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = (ViewPager) view.findViewById(R.id.pager);
        searchText = (EditText) view.findViewById(R.id.search_text);
        back = (ImageView) view.findViewById(R.id.btn_back);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        inputMethodManager = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        searchText.requestFocus();

        back.setOnClickListener(this);
        ( (RelativeLayout) view.findViewById(R.id.rel_pager)).setOnClickListener(this);
       // adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        //adapter = new ViewPagerAdapter(getActivity().getgetSupportChildFragmentManager() );

       // adapter = new ViewPagerAdapter(getFragmentManager());
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                switch ((tab.getPosition())) {
                    case 0:
//                        searchPostFragment = SearchPostFragment.newInstance();

                       /* if (!searchText.getText().toString().equals("") && searchText != null) {
                           // Toast.makeText(getContext(), "searchPostFragment", Toast.LENGTH_SHORT).show();
                            searchPostFragment.doSearch(searchText.getText().toString());
                        }*/
                        break;
                    case 1:
//                        searchPeopleFragment = SearchPeopleFragment.newInstance();

                        if (!searchText.getText().toString().equals("") && searchText != null) {
                           // Toast.makeText(getContext(), "searchPeopleFragment", Toast.LENGTH_SHORT).show();
                            searchPeopleFragment.doSearch(searchText.getText().toString());
                        }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        categoryList.clear();
        Category category = new Category();
        category.name = "POST";
        categoryList.add(category);
        category = new Category();
        category.name = "PEOPLE";
        categoryList.add(category);

        adapter.notifyDataSetChanged();

        searchText.addTextChangedListener(
                new TextWatcher() {

                    private final long DELAY = 1000; // milliseconds
                    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
                    Runnable workRunnable;
                    private Timer timer = new Timer();

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(final CharSequence s, int start, int before, int count) {

                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // TODO: do what you need here (refresh list)
                                        // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                                        String str = s.toString().toLowerCase();

                                        if ((str.length()) >= 2) {
                                            if (tabLayout.getSelectedTabPosition() == 0) {
                                                //searchPostFragment.doSearch(str);
                                            } else {
                                                searchPeopleFragment.doSearch(str);
                                            }
                                        }
                                    }
                                },
                                DELAY
                        );

                    }

                    @Override
                    public void afterTextChanged(final Editable s) {
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // TODO: do what you need here (refresh list)
                                        // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String str = s.toString().toLowerCase();
                                                if ((str.length()) >= 2) {
                                                    if (tabLayout.getSelectedTabPosition() == 0) {
                                                       // searchPostFragment.doSearch(str);
                                                    } else {
                                                        searchPeopleFragment.doSearch(str);
                                                    }
                                                }
                                            }
                                        });

                                    }
                                },
                                DELAY
                        );
                    }
                }
        );

//        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    String str = searchText.getText().toString().trim();
//                    if(!str.isEmpty())
//                        if(tabLayout.getSelectedTabPosition() == 0) {
//                            searchPostFragment.doSearch(str);
//                        }
//                    else  {
//                            searchPeopleFragment.doSearch(str);
//                        }
//                    return true;
//                }
//                return false;
//            }
//        });
        return view;
    }
//    @Override
//    public void onResume(){
//        super.onResume();
//        initViewPagerAdapter();
//    }
//    private void initViewPagerAdapter(){
//        adapter = new ViewPagerAdapter(getChildFragmentManager());

      //  mAdapter = new ViewPagerAdapter(getFragmentManager(), cursor);
       // new setAdapterTask().execute();
//    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Utils.hideKeybord(getContext(),v);
                getActivity().onBackPressed();
                break;
            case R.id.rel_pager:
                Toast.makeText(getActivity(), "pager click", Toast.LENGTH_LONG).show();
                Utils.hideKeybord(getContext(),v);
               // getActivity().onBackPressed();

                break;
        }

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (!(adapter == null)) {
//
//            adapter.notifyDataSetChanged();
//
//
//        }
//    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

        }


        public Fragment getItem(int num) {
          /*  if (num == 0){
                searchPostFragment = SearchPostFragment.newInstance();

                if (!searchText.getText().toString().equals("") && searchText != null){
                    searchPostFragment.doSearch(searchText.getText().toString());
                }

                return searchPostFragment;
            }*/
            searchPeopleFragment = SearchPeopleFragment.newInstance();

            if (!searchText.getText().toString().equals("") && searchText != null){
                searchPeopleFragment.doSearch(searchText.getText().toString());
            }


            return searchPeopleFragment;
        }

        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categoryList.get(position).name;
        }

    }

}
