package com.myrole;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.tabs.TabLayout;
import com.myrole.databinding.FragmentMixupBinding;
import com.myrole.fragments.SoundTabFragment;
import com.myrole.fragments.viewmodel.MixupViewModel;
import com.myrole.fragments.viewmodel.SharedViewModel;
import com.myrole.listeners.SongApplyClickCallback;
import com.myrole.listeners.SongClickCallback;
import com.myrole.model.Song;

public class MixupFragment extends Fragment {

    private static final String TAG = "MixupFragment";
    private static final Boolean DEBUG = true;

    private FragmentManager mFragmentManager;

    private NavController mNavController;
    private MixupViewModel mMixupViewModel;

    private SharedViewModel mModel;

    private Button mApplyBtn;

    private FragmentMixupBinding mFragmentMixupBinding;

    private TabLayout mTabLayout;

    public static boolean isFromHome;

    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private String mSelectedSongPath;
    private long mDuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentMixupBinding = FragmentMixupBinding.inflate(inflater, container, false);
        mFragmentMixupBinding.setCallback(mSongApplyClickCallback);
        return mFragmentMixupBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);

        mMixupViewModel = new ViewModelProvider(this).get(MixupViewModel.class);

        mModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //mFragmentMixupBinding.btnApply.setOnClickListener(this);

        int mShowIndex = MixupFragmentArgs.fromBundle(getArguments()).getShowIndex();

        boolean mIsFromHome = MixupFragmentArgs.fromBundle(getArguments()).getIsFromHome();

        mDuration = MixupFragmentArgs.fromBundle(getArguments()).getDuration();

        init(mIsFromHome);

        //TODO get index of the fragment to show here
        changeFragments(mShowIndex);
    }

    private void init(boolean isIsFromHome) {

        //TODO get isFromHome flag from args
        isFromHome = isIsFromHome;

        mFragmentManager = requireActivity().getSupportFragmentManager();
        mTabLayout = requireActivity().findViewById(R.id.tabLayout);

        mTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {


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
                    mTabLayout.getTabAt(position).select();
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frame_main_content,
                                    SoundTabFragment.newInstance(isFromHome, mDuration),
                                    "Sound")
                            .commit();
                }
                break;
            case 1:
//                if (mFragmentManager != null) {
//                    mTabLayout.getTabAt(position).select();
//                    mFragmentManager.beginTransaction()
//                            .replace(R.id.frame_main_content,
//                                    MixupTabFragment.newInstance(mSongClickCallback),
//                                    "MixUp")
//                            .commit();
//                }
                break;
            case 2:
//                if (mFragmentManager != null) {
//                    mTabLayout.getTabAt(position).select();
//                    mFragmentManager.beginTransaction()
//                            .replace(R.id.frame_main_content,
//                                    GallerySoundTabFragment.newInstance(mMediaPlayer),
//                                    "GallerySound")
//                            .commit();
//                }
                break;
        }
    }

    private final SongClickCallback mSongClickCallback = new SongClickCallback() {
        @Override
        public void onClick(Song songToPlay, View songView) {
            Toast.makeText(getContext(), "playing: " + songToPlay.getSongName(), Toast.LENGTH_SHORT).show();
        }
    };

    private final SongApplyClickCallback mSongApplyClickCallback = new SongApplyClickCallback() {

        @Override
        public void onApply(Song songToApply, View songView) {

        }

    };


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnApply:
//                mMixupViewModel.trimSong();
//                if(!isFromHome) {
//                    mNavController.popBackStack(R.id.playbackFragment, false);
//                } else {
//                    mNavController.popBackStack(R.id.cameraFragment, false);
//                }
//                break;
//        }
//    }

}
