package com.myrole.data;

import java.util.ArrayList;

/**
 * Created by Rakesh on 10/22/2016.
 */

public class CommentsDTO {
    private boolean status;
    private String msg;
    private String comments;
    private String name;
    private String username;
    private String created_at;
    private String user_id;
    private String comment_id;
    private String post_user_id;
    private String image;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    private boolean isDelete = false;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(String post_user_id) {
        this.post_user_id = post_user_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<CommentsDTO> getData() {
        return data;
    }

    public void setData(ArrayList<CommentsDTO> data) {
        this.data = data;
    }

    private ArrayList<CommentsDTO> data;
}
