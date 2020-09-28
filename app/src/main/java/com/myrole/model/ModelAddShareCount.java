package com.myrole.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelAddShareCount {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ModelDataAddShareCount modelDataAddShareCount;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ModelDataAddShareCount getModelDataAddShareCount() {
        return modelDataAddShareCount;
    }

    public void setModelDataAddShareCount(ModelDataAddShareCount modelDataAddShareCount) {
        this.modelDataAddShareCount = modelDataAddShareCount;
    }

}