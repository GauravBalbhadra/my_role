package com.picker.gallery.view

import android.content.Context
import com.picker.gallery.model.GalleryAlbums
import com.picker.gallery.model.GalleryVideo

interface VideoPickerContract {

    fun initRecyclerViews()
    fun galleryOperation()
    fun toggleDropdown()
    fun getPhoneAlbums(context: Context, listener: OnPhoneVideosObtained)
    fun updateTitle(galleryAlbums: GalleryAlbums = GalleryAlbums())
    fun updateSelectedVideos(selectedlist: ArrayList<GalleryVideo> = ArrayList())

}