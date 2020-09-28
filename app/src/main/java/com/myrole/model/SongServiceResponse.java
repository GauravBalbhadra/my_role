package com.myrole.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SongServiceResponse {

    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private Boolean status;
    @SerializedName("data")
    private ArrayList<Song> songArrayList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ArrayList<Song> getSongArrayList() {
        return songArrayList;
    }

    public void setSongArrayList(ArrayList<Song> songArrayList) {
        this.songArrayList = songArrayList;
    }

    @Override
    public String toString() {
        return "SongServiceResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", songArrayList=" + songArrayList +
                '}';
    }
}
