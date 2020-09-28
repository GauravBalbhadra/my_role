package com.myrole.utilities;

public class TrimSongEvent {

    private String responePath;
    public TrimSongEvent(String messagePath) {
        responePath = messagePath;
    }
    public String getResponsePath() {
        return responePath;
    }

}
