package com.picker.gallery.utils;

public class TrimVideoEvent {

    private String responePath;
    public TrimVideoEvent(String messagePath) {
        responePath = messagePath;
    }
    public String getResponsePath() {
        return responePath;
    }

}
