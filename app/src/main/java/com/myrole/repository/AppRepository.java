package com.myrole.repository;

import com.myrole.model.Song;

import java.util.List;

public class AppRepository {
    public static final AppRepository ourInstance = new AppRepository();

    public List<Song> songItemList;

    public static AppRepository getInstance() {
        return ourInstance;
    }
    private AppRepository() {
        songItemList = MusicItemProvider.songList;
    }
}