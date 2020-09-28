package com.myrole.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryServiceResponse {

    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private Boolean status;
    @SerializedName("data")
    private ArrayList<Album> albumArrayList;

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

    public ArrayList<Album> getAlbumArrayList() {
        return albumArrayList;
    }

    public void setAlbumArrayList(ArrayList<Album> albumArrayList) {
        this.albumArrayList = albumArrayList;
    }

    @Override
    public String toString() {
        return "SongServiceResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", songArrayList=" + albumArrayList +
                '}';
    }
}
