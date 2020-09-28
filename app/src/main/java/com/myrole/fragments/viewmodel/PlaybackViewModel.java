package com.myrole.fragments.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlaybackViewModel extends ViewModel {

    private final MutableLiveData<String> trimmedPath = new MutableLiveData<>();

}
