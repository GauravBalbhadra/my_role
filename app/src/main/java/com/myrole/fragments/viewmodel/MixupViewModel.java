package com.myrole.fragments.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.myrole.model.Song;
import com.myrole.repository.AppRepository;

import java.util.List;

public class MixupViewModel extends AndroidViewModel {

    public List<Song> songItemList;

    private AppRepository appRepository;

    public MixupViewModel(Application application)
    {
        super(application);
        appRepository = AppRepository.getInstance();
        songItemList = appRepository.songItemList;
    }
}
