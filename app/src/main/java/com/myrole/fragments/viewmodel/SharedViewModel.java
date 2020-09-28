package com.myrole.fragments.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myrole.model.Song;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> mSelectedSongPath = new MutableLiveData<>();
    private final MutableLiveData<String> mSelectedDecodedSongPath = new MutableLiveData<>();
    private final MutableLiveData<String> mSelectedSongName = new MutableLiveData<>();
    private final MutableLiveData<Integer> mSelectedSongId = new MutableLiveData<>();

    private Song mSong;

    public void setSelectedDecodedSongPath(String decodedSongPath) {
        mSelectedDecodedSongPath.setValue(decodedSongPath);
    }

    public LiveData<String> getSelectedDecodedSongPath() {
        return mSelectedDecodedSongPath;
    }

    public void setSelectedSongPath(String songPath) {
        mSelectedSongPath.setValue(songPath);
    }

    public LiveData<String> getSelectedSongPath() {
        return mSelectedSongPath;
    }

    public void setSelectedSongName(String name) {
        mSelectedSongName.setValue(name);
    }

    public MutableLiveData<String> getSelectedSongName() { return mSelectedSongName; }

    public void setSelectedSongId(int songId) {
        mSelectedSongId.setValue(songId);
    }

    public LiveData<Integer> getSelectedSongId() { return mSelectedSongId; }

}
