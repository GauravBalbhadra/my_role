package com.picker.gallery.presenter

interface VideosPresenter {
    fun getPhoneAlbums()
    fun trimVideo(videoToTrim: String?, length: Long)
}