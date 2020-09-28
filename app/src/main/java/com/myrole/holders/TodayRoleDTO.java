package com.myrole.holders;

import java.util.ArrayList;

/**
 * Created by Rakesh on 8/18/2016.
 */

public class TodayRoleDTO {
    private boolean status;
    private String message;
    private ArrayList<TodayRoleDTO> data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getReq_image() {
        return req_image;
    }

    public void setReq_image(int req_image) {
        this.req_image = req_image;
    }

    public int getReq_video() {
        return req_video;
    }

    public void setReq_video(int req_video) {
        this.req_video = req_video;
    }

    public String getTanding_till() {
        return tanding_till;
    }

    public void setTanding_till(String tanding_till) {
        this.tanding_till = tanding_till;
    }

    public String getGener() {
        return gener;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription_hindi() {
        return description_hindi;
    }

    public void setDescription_hindi(String description_hindi) {
        this.description_hindi = description_hindi;
    }

    public String getGift_count() {
        return gift_count;
    }

    public void setGift_count(String gift_count) {
        this.gift_count = gift_count;
    }

    public String getGift_base() {
        return gift_base;
    }

    public void setGift_base(String gift_base) {
        this.gift_base = gift_base;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    private int id;
    private int category_id;
    private int req_image;
    private int req_video;
    private String tanding_till;
    private String gener;
    private String name;
    private String description;
    private String description_hindi;
    private String gift_count;
    private String gift_base;
    private String start_date;
    private String end_date;
    private String created_at;
    private String updated_at;

    public ArrayList<TodayRoleDTO> getData() {
        return data;
    }

    public void setData(ArrayList<TodayRoleDTO> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
