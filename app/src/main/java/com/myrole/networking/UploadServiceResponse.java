package com.myrole.networking;

import com.google.gson.annotations.SerializedName;

public class UploadServiceResponse {

    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("role_id")
    private int role_id;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getRole_id() {
        return role_id;
    }
}
