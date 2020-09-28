package com.myrole.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.myrole.R;
import com.myrole.adapter.MySoundListAdapter;
import com.myrole.databinding.FragmentSoundListBinding;
import com.myrole.fragments.dummy.DummyContent;
import com.myrole.fragments.viewmodel.SharedViewModel;
import com.myrole.fragments.viewmodel.SoundTabViewModel;
import com.myrole.listeners.OnDecodedSong;
import com.myrole.listeners.OnDownloadedSong;
import com.myrole.listeners.SongApplyClickCallback;
import com.myrole.listeners.SongClickCallback;
import com.myrole.model.Song;
import com.v9kmedia.v9krecorder.utils.V9krecorderutil;

import io.gresse.hugo.vumeterlibrary.VuMeterView;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class SoundTabFragment extends Fragment implements OnDownloadedSong, OnDecodedSong {

    private static final String TAG = "SoundTabFragment";
    private static final Boolean DEBUG = true;

    private FragmentSoundListBinding mFragmentSoundListBinding;
    private NavController mNavController;
    private FragmentManager mFragmentManager;
    private TabLayout mSoundTabLayout;
    private AppCompatTextView mApplyButton;
    private SoundTabViewModel mSoundTabViewModel;
    private OnBackPressedCallback mCallback;
    private View view;
    private SharedViewModel mModel;
    private long mDuration;
    private boolean isFromHome;
    private boolean isPlaying;
    private VuMeterView mVumeter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SoundTabFragment(boolean fromHome, long duration) {
        if(DEBUG) Log.d(TAG, "fromHome: " + fromHome + " duration: " + duration);
        isFromHome = fromHome;
        mDuration = duration;
    }

    private void init() {
        mFragmentManager = getActivity().getSupportFragmentManager();

        mSoundTabLayout = mFragmentSoundListBinding.mSoundTabLayout;

        mSoundTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeFragments(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    private void changeFragments(int position) {
        switch (position) {
            case 0:
                if (mFragmentManager != null) {
                    mSoundTabLayout.getTabAt(position).select();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.main,
                                    SoundTrendingFragment.newInstance(mSongClickCallback, mSongApplyClickCallback),
                                    "Trending")
                            .commit();
                }
                break;
            case 1:
//                if (mFragmentManager != null) {
//                    mSoundTabLayout.getTabAt(position).select();
//                    mFragmentManager.beginTransaction()
//                            .replace(R.id.main,
//                                    SoundPopularFragment.newInstance(mSongClickCallback),
//                                    "popular")
//                            .commit();
//                }
                break;
            case 2:
//                if (mFragmentManager != null) {
//                    mSoundTabLayout.getTabAt(position).select();
//                    mFragmentManager.beginTransaction()
//                            .replace(R.id.main,
//                                    SoundCurrentFragment.newInstance(mSongClickCallback),
//                                    "current")
//                            .commit();
//                }
                break;
        }
    }

    public static SoundTabFragment newInstance(boolean isFromHome, long duration) {
        return new SoundTabFragment(isFromHome, duration);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        mCallback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(mSoundTabViewModel.mIsPlaying) {
                    mSoundTabViewModel.stopSong();
                }
                back();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, mCallback);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);

        mSoundTabViewModel = new ViewModelProvider((this)).get(SoundTabViewModel.class);

        mSoundTabViewModel.getApplyBtnState().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean s) {
                if(s) {
                    mApplyButton.setVisibility(View.VISIBLE);
                    mVumeter.setVisibility(View.VISIBLE);
                } else {
                    mApplyButton.setVisibility(View.GONE);
                    mVumeter.setVisibility(View.GONE);
                }
            }
        });

        mModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        Context context = view.getContext();
        init();
        changeFragments(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentSoundListBinding = FragmentSoundListBinding.inflate(inflater, container, false);
        return mFragmentSoundListBinding.getRoot();
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
    public void onStop() {
        if(DEBUG) Log.d(TAG, "onStop: ");
        super.onStop();
        mSoundTabViewModel.stopSong();
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

    private void back() {
        mCallback.setEnabled(false);
        requireActivity().getOnBackPressedDispatcher().onBackPressed();
    }

    private final SongClickCallback mSongClickCallback = new SongClickCallback() {
        @Override
        public void onClick(Song songToPlay, View songView) {

            mVumeter = songView.findViewById(R.id.vumeter);
            mApplyButton = songView.findViewById(R.id.btnApply);

            mSoundTabViewModel.downloadSong(
                    songToPlay.getAbsoluteSongPath(),
                    V9krecorderutil.getCacheDir(Environment.DIRECTORY_MOVIES),
                    songToPlay.getSongName().replaceAll(" ", "_").toLowerCase(),
                    SoundTabFragment.this,
                    songToPlay.getSongId()
            );


            mModel.setSelectedSongPath(songToPlay.getAbsoluteSongPath());

        }
    };

    private final SongApplyClickCallback mSongApplyClickCallback = new SongApplyClickCallback() {

        @Override
        public void onApply(Song songToApply, View songView) {
            if(!isFromHome) {
                mModel.setSelectedSongPath(songToApply.getAbsoluteSongPath());
                songView.findViewById(R.id.btnApply).setVisibility(View.GONE);
                mSoundTabViewModel.stopSong();
                mSoundTabViewModel.trimSong(mSoundTabViewModel.getDownloadedSong().getValue(), mDuration);
                mNavController.popBackStack(R.id.playbackFragment, false);
            } else {
                songView.findViewById(R.id.btnApply).setVisibility(View.GONE);
                mModel.setSelectedSongName(songToApply.getSongName());
                mModel.setSelectedSongId(songToApply.getSongId());
                mSoundTabViewModel.stopSong();
                mNavController.popBackStack(R.id.cameraFragment, false);
            }
        }

    };

    @Override
    public void onDownloadedSong(String downloadedSong, int downloadedSongId) {
        Log.d(TAG, "now playing downloaded song: " + downloadedSong + " with id: " + downloadedSongId);
        mApplyButton.setVisibility(View.GONE);
        mVumeter.setVisibility(View.GONE);
        mSoundTabViewModel.stopSong();
        mSoundTabViewModel.playSong(downloadedSong, downloadedSongId, mApplyButton, mVumeter);
    }

    @Override
    public void onDecodedSong(String decodedSong, int decodedSongId) {
        Log.d(TAG, "decoded song at: " + decodedSong + " with id: " + decodedSongId);
        mModel.setSelectedDecodedSongPath(decodedSong);
    }
}
