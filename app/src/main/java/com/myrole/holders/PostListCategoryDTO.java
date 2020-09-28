package com.myrole.holders;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rakesh on 8/15/2016.
 */

public class PostListCategoryDTO implements Serializable {
    private boolean status;
    private boolean isdataavailable;
    private String message;
    private ArrayList<PostListCategoryDTO> data;
    private ArrayList<PostListCategoryDTO> paging;
    //private ArrayList<PostListDTO> comments;
    private int post_id;
 //   private int after;
 //   private int before;
 //   private int limit;
 //  private int comment_count;
//    private int owner_id;
    //private int islike;
    private int role_id;
    private int cat_id;
    private int post_likes;
   // private int user_id;
    private String url;
    private String thumbnail;
    private String description;
   // private String post_time;
    private String owner_name;
    private String owner_username;
    private String owner_image;
    private String role_name;
    private String role_description;
    private String role_description_hindi;
    private String cat_name;
    private String cat_slug;
    private String cat_icon;
   // private String comment;
    private String username;
  //  private String name;
  //  private String image;

    public boolean isdataavailable() {
        return isdataavailable;
    }

    public void setIsdataavailable(boolean isdataavailable) {
        this.isdataavailable = isdataavailable;
    }

//    public int getComment_count() {
//        return comment_count;
//    }
//
//    public void setComment_count(int comment_count) {
//        this.comment_count = comment_count;
//    }

    public ArrayList<PostListCategoryDTO> getPaging() {
        return paging;
    }

    public void setPaging(ArrayList<PostListCategoryDTO> paging) {
        this.paging = paging;
    }

//    public int getAfter() {
//        return after;
//    }
//
//    public void setAfter(int after) {
//        this.after = after;
//    }
//
//    public int getBefore() {
//        return before;
//    }
//
//    public void setBefore(int before) {
//        this.before = before;
//    }
//
//    public int getLimit() {
//        return limit;
//    }
//
//    public void setLimit(int limit) {
//        this.limit = limit;
//    }

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

    public ArrayList<PostListCategoryDTO> getData() {
        return data;
    }

    public void setData(ArrayList<PostListCategoryDTO> data) {
        this.data = data;
    }

    /*  public ArrayList<PostListDTO> getComments() {
          return comments;
      }

      public void setComments(ArrayList<PostListDTO> comments) {
          this.comments = comments;
      }
  */
    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

//    public int getOwner_id() {
//        return owner_id;
//    }
//
//    public void setOwner_id(int owner_id) {
//        this.owner_id = owner_id;
//    }
//
//    public int getIslike() {
//        return islike;
//    }
//
//    public void setIslike(int islike) {
//        this.islike = islike;
//    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getPost_likes() {
        return post_likes;
    }

    public void setPost_likes(int post_likes) {
        this.post_likes = post_likes;
    }

//    public int getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(int user_id) {
//        this.user_id = user_id;
//    }

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

//    public String getPost_time() {
//        return post_time;
//    }
//
//    public void setPost_time(String post_time) {
//        this.post_time = post_time;
//    }

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

//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
