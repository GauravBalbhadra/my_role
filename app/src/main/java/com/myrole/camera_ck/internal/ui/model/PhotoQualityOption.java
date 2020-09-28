package com.myrole.camera_ck.internal.ui.model;

import com.myrole.camera_ck.configuration.Configuration;
import com.myrole.camera_ck.internal.utils.Size;

/*
 * Created by memfis on 12/1/16.
 */

public class PhotoQualityOption implements CharSequence {

    @Configuration.MediaQuality
    private int mediaQuality;
    private String title;

    public PhotoQualityOption(@Configuration.MediaQuality int mediaQuality, Size size) {
        this.mediaQuality = mediaQuality;

        title = size.getWidth() + " x " + size.getHeight();
    }

    @Configuration.MediaQuality
    public int getMediaQuality() {
        return mediaQuality;
    }

    @Override
    public int length() {
        return title.length();
    }

    @Override
    public char charAt(int index) {
        return title.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return title.subSequence(start, end);
    }

    @Override
    public String toString() {
        return title;
    }
}
