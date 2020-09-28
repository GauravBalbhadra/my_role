package com.myrole.listeners;

import android.view.View;

import com.myrole.model.Song;

public interface SongClickCallback {
    void onClick(Song songToPlay, View view);
}
