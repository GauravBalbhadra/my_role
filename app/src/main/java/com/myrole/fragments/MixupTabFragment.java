package com.myrole.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;
import com.myrole.adapter.MyMixUpListAdapter;
import com.myrole.fragments.viewmodel.MixupTabViewModel;
import com.myrole.listeners.SongClickCallback;
import com.myrole.model.Song;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class MixupTabFragment extends Fragment {

    private static final String TAG = "MixupTabFragment";
    private static final Boolean DEBUG = true;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private List<Song> songItemList;
    private RecyclerView mRecyclerView;
    private SongClickCallback mSongClickCallback;
    private MixupTabViewModel mMixupTabViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MixupTabFragment(SongClickCallback songClickCallback) {
        mSongClickCallback = songClickCallback;
    }

    @SuppressWarnings("unused")
    public static MixupTabFragment newInstance(SongClickCallback songClickCallback) {
        return new MixupTabFragment(songClickCallback);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mixup_list, container, false);
        // Set the adapter

        mRecyclerView = view.findViewById(R.id.mixuplist);
        initViewModel();
        songItemList = mMixupTabViewModel.songItemList;
        initRecyclerView();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(DEBUG) Log.d(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(DEBUG) Log.d(TAG, "onResume: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(DEBUG) Log.d(TAG, "onDestroyView: ");
        if(DEBUG) Log.d(TAG, "stopping audio if playing: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(DEBUG) Log.d(TAG, "onDestroy: ");
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MyMixUpListAdapter(mSongClickCallback, songItemList));

    }

    private void initViewModel() {
        mMixupTabViewModel = ViewModelProviders.of(this).get(MixupTabViewModel.class);
    }

}
