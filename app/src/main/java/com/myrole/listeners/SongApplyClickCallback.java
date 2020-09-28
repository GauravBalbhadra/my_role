package com.myrole.listeners;

import android.view.View;

import com.myrole.model.Song;

public interface SongApplyClickCallback {

    void onApply(Song songToApply, View songToApplyView);

}
