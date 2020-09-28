package com.myrole.holders;

import com.myrole.data.CommentsDTO;

import java.util.ArrayList;

/**
 * Created by Rakesh on 8/21/2016.
 */

public class PostDetailsDTO {
    private boolean status;
    private String message;
    private ArrayList<PostDetailsDTO> data;
    private ArrayList<CommentsDTO> comments;
    private int post_id;
    private String url;
    private String description;
    private String post_time;
    private String owner_name;
    private String owner_username;
    private String owner_image;
    private String owner_id;
    private String islike;
    private String role_name;
    private String role_description;
    private String role_description_hindi;
    private String role_id;
    private String cat_name;
    private String cat_slug;
    private String cat_icon;
    private String cat_id;
    private String post_likes;
    private String comment_count;
    private String gener;
    private String tanding_till;
    private String participating;
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getParticipating() {
        return participating;
    }

    public void setParticipating(String participating) {
        this.participating = participating;
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

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
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

    public ArrayList<PostDetailsDTO> getData() {
        return data;
    }

    public void setData(ArrayList<PostDetailsDTO> data) {
        this.data = data;
    }

    public ArrayList<CommentsDTO> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentsDTO> comments) {
        this.comments = comments;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_username() {
        return owner_username;
    }

    public void setOwner_username(String owner_username) {
        this.owner_username = owner_username;
    }

    public String getOwner_image() {
        return owner_image;
    }

    public void setOwner_image(String owner_image) {
        this.owner_image = owner_image;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_description() {
        return role_description;
    }

    public void setRole_description(String role_description) {
        this.role_description = role_description;
    }

    public String getRole_description_hindi() {
        return role_description_hindi;
    }

    public void setRole_description_hindi(String role_description_hindi) {
        this.role_description_hindi = role_description_hindi;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_slug() {
        return cat_slug;
    }

    public void setCat_slug(String cat_slug) {
        this.cat_slug = cat_slug;
    }

    public String getCat_icon() {
        return cat_icon;
    }

    public void setCat_icon(String cat_icon) {
        this.cat_icon = cat_icon;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getPost_likes() {
        return post_likes;
    }

    public void setPost_likes(String post_likes) {
        this.post_likes = post_likes;
    }
}
