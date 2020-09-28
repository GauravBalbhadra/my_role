package com.myrole.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.myrole.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GallerySoundTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GallerySoundTabFragment extends Fragment {

    private MediaPlayer mMediaPlayer;

    public GallerySoundTabFragment(MediaPlayer player) {
        // Required empty public constructor
        mMediaPlayer = player;
    }

    public static GallerySoundTabFragment newInstance(MediaPlayer player) {
       // GallerySoundTabFragment fragment = new GallerySoundTabFragment();
         return new GallerySoundTabFragment(player);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery_sound, container, false);
    }
}
