package com.myrole.data;

import java.util.ArrayList;

/**
 * Created by pws on 11/4/2016.
 */

public class FollowingDTO {
    private boolean status;
    private String message;
    private String image_to;
    private String p_ids;
    private int id;
    private int activity_id;
    private String btn_value;
    private String msg;
    private String username_to;
    private String image_url;
    private String type;
    private String ago;
    private String activity;
    private String username_for;
    private int user_to;
    private int user_for;
    private ArrayList<FollowingDTO> data;
    private ArrayList<FollowingDTO> images;

    public String getBtn_value() {
        return btn_value;
    }

    public void setBtn_value(String btn_value) {
        this.btn_value = btn_value;
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

    public String getImage_to() {
        return image_to;
    }

    public void setImage_to(String image_to) {
        this.image_to = image_to;
    }

    public String getP_ids() {
        return p_ids;
    }

    public void setP_ids(String p_ids) {
        this.p_ids = p_ids;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername_to() {
        return username_to;
    }

    public void setUsername_to(String username_to) {
        this.username_to = username_to;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAgo() {
        return ago;
    }

    public void setAgo(String ago) {
        this.ago = ago;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getUsername_for() {
        return username_for;
    }

    public void setUsername_for(String username_for) {
        this.username_for = username_for;
    }

    public int getUser_to() {
        return user_to;
    }

    public void setUser_to(int user_to) {
        this.user_to = user_to;
    }

    public int getUser_for() {
        return user_for;
    }

    public void setUser_for(int user_for) {
        this.user_for = user_for;
    }

    public ArrayList<FollowingDTO> getData() {
        return data;
    }

    public void setData(ArrayList<FollowingDTO> data) {
        this.data = data;
    }

    public ArrayList<FollowingDTO> getImages() {
        return images;
    }

    public void setImages(ArrayList<FollowingDTO> images) {
        this.images = images;
    }

    public FollowingDTO(){}

    public FollowingDTO( String message,String image_to,String p_ids,int id,int activity_id,String msg,String username_to,String image_url,
                        String ago,String activity ,String username_for ,int user_to,int user_for ) {
        this.status = status;

        this.message = message;
        this.image_to = image_to;
        this.p_ids = p_ids;
        this.id = id;
        this.activity_id = activity_id;
        this.msg = msg;
        this.username_to = username_to;
        this.image_url = image_url;
        this.ago = ago;
        this.activity = activity;
        this.username_for = username_for;
        this.user_to = user_to;
        this.user_for = user_for;

    }
}
