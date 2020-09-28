package com.picker.gallery.presenter

import com.picker.gallery.model.interactor.VideosInteractorImpl
import com.picker.gallery.view.VideosFragment

class VideosPresenterImpl(var videosFragment: VideosFragment): VideosPresenter {

    var interactor = VideosInteractorImpl(this)

    var selectedVideoPath : String? = null
    var selectedVideoDuration: Int? = 0

    override fun getPhoneAlbums() {
        interactor.getPhoneAlbums()
    }

    override fun trimVideo(videoToTrim: String?, length: Long) {
        interactor.trimVideo(videoToTrim, length)
    }
}