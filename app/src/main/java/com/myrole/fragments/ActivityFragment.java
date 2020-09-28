package com.myrole.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.myrole.BaseFragment;
import com.myrole.R;
import com.myrole.adapter.ExpandableListAdapter;
import com.myrole.utils.Config;
import com.myrole.utils.ExpandableListDataPump;
import com.myrole.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityFragment extends BaseFragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private OnFragmentInteractionListener mListener;

    public ActivityFragment() {}

    public static ActivityFragment newInstance() {

        ActivityFragment fragment = new ActivityFragment();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view   =   inflater.inflate(R.layout.fragment_activity, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        int nogroup = expandableListAdapter.getGroupCount();
        for (int n = 0; n < nogroup; n++)
            expandableListView.expandGroup(n);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getActivity(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();*/

            }


        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getActivity(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {


            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                {
                    /*Toast.makeText(
                            getActivity(),
                            expandableListTitle.get(groupPosition)
                                    + " -> "
                                    + expandableListDetail.get(
                                    expandableListTitle.get(groupPosition)).get(
                                    groupPosition), Toast.LENGTH_SHORT
                    ).show();*/

                    return false;
                }


            }
        });

        applyFont(view);
        return view;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String command);
    }

    private void applyFont(View view) {
        Utils.setTypeface(getActivity(),(TextView)view.findViewById(R.id.txt_title), Config.NEXA, Config.BOLD);
    }
}
