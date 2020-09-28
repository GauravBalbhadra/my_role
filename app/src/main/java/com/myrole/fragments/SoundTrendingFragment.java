package com.myrole.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myrole.R;
import com.myrole.adapter.MySoundTrendingAdapter;
import com.myrole.fragments.viewmodel.SharedViewModel;
import com.myrole.fragments.viewmodel.SoundTabViewModel;
import com.myrole.listeners.SongApplyClickCallback;
import com.myrole.listeners.SongClickCallback;
import com.myrole.model.Song;
import com.myrole.model.SongServiceResponse;
import com.myrole.networking.SongService;
import com.myrole.networking.SongServiceClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoundTrendingFragment extends Fragment implements Callback<SongServiceResponse> {

    private static final String TAG = "SoundTrendingFragment";
    private static final Boolean DEBUG = true;

    private View view;
    private RecyclerView mRecyclerView;
    private SongClickCallback mSongClickCallback;
    private SongApplyClickCallback mSongApplyClickCallback;
    private SoundTabViewModel mSongTrendingViewModel;
    private SharedViewModel mModel;

    public SoundTrendingFragment(SongClickCallback songClickCallback, SongApplyClickCallback songApplyClickCallback) {
        mSongClickCallback = songClickCallback;
        mSongApplyClickCallback = songApplyClickCallback;
    }

    @SuppressWarnings("unused")
    public static SoundTrendingFragment newInstance(SongClickCallback songClickCallback, SongApplyClickCallback songApplyClickCallback) {
        return new SoundTrendingFragment(songClickCallback, songApplyClickCallback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetSong();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sound_trending, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_trending_sound);

        initViewModel();

        fetSong();

        return view;
    }

    private void initRecyclerView(ArrayList<Song> songItemList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new MySoundTrendingAdapter(mSongClickCallback, mSongApplyClickCallback, songItemList, mModel));
    }

    private void initViewModel() {
        mSongTrendingViewModel = ViewModelProviders.of(this).get(SoundTabViewModel.class);
    }

    private void fetSong() {


        Retrofit retrofit = SongServiceClient.getRetrofitClient();

        SongService songService = retrofit.create(SongService.class);

        Call<SongServiceResponse> call = songService.getSongs();

        call.enqueue(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(DEBUG) Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onResponse(Call<SongServiceResponse> call, Response<SongServiceResponse> response) {
        if(response.body() != null) {
            initRecyclerView(response.body().getSongArrayList());
        }
    }

    @Override
    public void onFailure(Call<SongServiceResponse> call, Throwable t) {
        Log.d(TAG, "onFailure: " + t.getMessage() + t.getStackTrace() + t.getCause() );
    }

}