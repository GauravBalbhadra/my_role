package com.picker.gallery.view

import com.picker.gallery.model.GalleryAlbums

interface OnPhoneVideosObtained {

    fun onComplete(albums: ArrayList<GalleryAlbums>)
    fun onError()

}