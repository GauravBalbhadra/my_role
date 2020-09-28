package com.myrole.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataAddShareCount {

    @SerializedName("fieldCount")
    @Expose
    private Integer fieldCount;
    @SerializedName("affectedRows")
    @Expose
    private Integer affectedRows;
    @SerializedName("insertId")
    @Expose
    private Integer insertId;
    @SerializedName("serverStatus")
    @Expose
    private Integer serverStatus;
    @SerializedName("warningCount")
    @Expose
    private Integer warningCount;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("protocol41")
    @Expose
    private Boolean protocol41;
    @SerializedName("changedRows")
    @Expose
    private Integer changedRows;

    public Integer getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(Integer fieldCount) {
        this.fieldCount = fieldCount;
    }

    public Integer getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(Integer affectedRows) {
        this.affectedRows = affectedRows;
    }

    public Integer getInsertId() {
        return insertId;
    }

    public void setInsertId(Integer insertId) {
        this.insertId = insertId;
    }

    public Integer getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(Integer serverStatus) {
        this.serverStatus = serverStatus;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getProtocol41() {
        return protocol41;
    }

    public void setProtocol41(Boolean protocol41) {
        this.protocol41 = protocol41;
    }

    public Integer getChangedRows() {
        return changedRows;
    }

    public void setChangedRows(Integer changedRows) {
        this.changedRows = changedRows;
    }

}
