package com.myrole.fragments;

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
import com.myrole.adapter.MySongPopularAdapter;
import com.myrole.fragments.viewmodel.SharedViewModel;
import com.myrole.fragments.viewmodel.SoundTabViewModel;
import com.myrole.listeners.SongClickCallback;
import com.myrole.model.Song;

import java.util.List;

public class SoundPopularFragment extends Fragment {
    private static final String TAG = "SoundPopularFragment";
    private static final Boolean DEBUG = true;

    private View view;
    private List<Song> songItemList;
    private RecyclerView mRecyclerView;
    private SongClickCallback mSongClickCallback;
    private SoundTabViewModel mSongTabViewModel;
    private SharedViewModel mModel;

    public SoundPopularFragment(SongClickCallback songClickCallback) {
        mSongClickCallback = songClickCallback;
    }

    @SuppressWarnings("unused")
    public static SoundPopularFragment newInstance(SongClickCallback songClickCallback) {
        return new SoundPopularFragment(songClickCallback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sound_trending, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_trending_sound);
        initViewModel();
        songItemList = mSongTabViewModel.mSongItemList;
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MySongPopularAdapter(mSongClickCallback, songItemList, mModel));
    }

    private void initViewModel() {
        mSongTabViewModel = ViewModelProviders.of(this).get(SoundTabViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(DEBUG) Log.d(TAG, "onDestroyView: ");
        //mListener.onStopped(true);
    }
}
