package com.myrole.data;

/**
 * Created by Rakesh on 10/22/2016.
 */

public class ContactDemoDTO {
    public String mobile = "";
    public String id = "";
    public String username = "";
    public String name = "";
    public String phone = "";
    public String email = "";
    public String private_user = "";

    public String image = "";
    public String type = "";
    public String status = "INVITE";
    public String follow = "0";

    public ContactDemoDTO(){}

    public ContactDemoDTO(String mobile,String id,String username,String name,String phone,String email,String private_user,String image,String type,
                          String status, String follow){

        this.mobile=mobile;
        this.id=id;
        this.username=username;
        this.name=name;
        this.phone=phone;
        this.email=email;
        this.private_user=private_user;
        this.image=image;
        this.type=type;
        this.status=status;
        this.follow=follow;

    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrivate_user() {
        return private_user;
    }

    public void setPrivate_user(String private_user) {
        this.private_user = private_user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }
}
