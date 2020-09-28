/*
 * Copyright 2016 eneim@Eneim Labs, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myrole.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.myrole.holders.TodayRoleDTO;

/**
 * Created by eneim on 1/30/16.
 */
public class SimpleVideoObject implements Parcelable {

    public static final Creator<SimpleVideoObject> CREATOR = new Creator<SimpleVideoObject>() {
        @Override
        public SimpleVideoObject createFromParcel(Parcel in) {
            return new SimpleVideoObject(in);
        }

        @Override
        public SimpleVideoObject[] newArray(int size) {
            return new SimpleVideoObject[size];
        }
    };
    private int post_id;
    private String url;
    private String thumbnail;
    private String description;
    private String post_time;
    private String owner_name;
    private String owner_username;
    private String owner_image;
    private int owner_id;
    private int islike;
    private int comment_count;
    private String role_name;
    private String role_description;
    private String role_description_hindi;
    private int role_id;
    private int cat_id;
    private String cat_name;
    private String cat_slug;
    private String cat_icon;
    private int post_likes;
    private TodayRoleDTO todayRoleDTO;

    protected SimpleVideoObject(Parcel in) {
        post_id = in.readInt();
        url = in.readString();
        description = in.readString();
        post_time = in.readString();
        owner_name = in.readString();
        owner_username = in.readString();
        owner_image = in.readString();
        owner_id = in.readInt();
        islike = in.readInt();
        comment_count = in.readInt();
        role_name = in.readString();
        role_description = in.readString();
        role_description_hindi = in.readString();
        role_id = in.readInt();
        cat_id = in.readInt();
        cat_name = in.readString();
        cat_slug = in.readString();
        cat_icon = in.readString();
        post_likes = in.readInt();
    }

    public SimpleVideoObject(int id, TodayRoleDTO todayRoleDTO) {
        this.todayRoleDTO = todayRoleDTO;
    }

    public SimpleVideoObject(@NonNull String video) {
        this.url = video;
    }

    public SimpleVideoObject(int post_id,
                             String url,
                             String description,
                             String post_time,
                             String owner_name,
                             String owner_username,
                             String owner_image,
                             int owner_id,
                             int islike,
                             String role_name,
                             String role_description,
                             String role_description_hindi,
                             int role_id,
                             int cat_id,
                             String cat_name,
                             String cat_slug,
                             String cat_icon,
                             int post_likes,
                             String thumbnail) {

        this.post_id = post_id;
        this.url = url;
        this.description = description;
        this.post_time = post_time;
        this.owner_name = owner_name;
        this.owner_username = owner_username;
        this.owner_image = owner_image;
        this.owner_id = owner_id;
        this.islike = islike;
        this.role_name = role_name;
        this.role_description = role_description;
        this.role_description_hindi = role_description_hindi;
        this.role_id = role_id;
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.cat_slug = cat_slug;
        this.cat_icon = cat_icon;
        this.post_likes = post_likes;
        this.thumbnail = thumbnail;

    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public TodayRoleDTO getTodayRoleDTO() {
        return todayRoleDTO;
    }

    public void setTodayRoleDTO(TodayRoleDTO todayRoleDTO) {
        this.todayRoleDTO = todayRoleDTO;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(post_id);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(post_time);
        dest.writeString(owner_name);
        dest.writeString(owner_username);
        dest.writeString(owner_image);
        dest.writeInt(owner_id);
        dest.writeInt(islike);
        dest.writeInt(comment_count);
        dest.writeString(role_name);
        dest.writeString(role_description);
        dest.writeString(role_description_hindi);
        dest.writeInt(role_id);
        dest.writeInt(cat_id);
        dest.writeString(cat_name);
        dest.writeString(cat_slug);
        dest.writeString(cat_icon);
        dest.writeInt(post_likes);
    }
}
