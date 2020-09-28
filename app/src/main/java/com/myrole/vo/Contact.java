package com.myrole.vo;

/**
 * Created by Vikesh on 14-05-2016.
 */
public class Contact implements Comparable<Contact> {
    public String mobile = "";
    public String id = "";
    public String username = "";
    public String name = "";
    public String phone = "";
    public String email = "";
    public String private_user = "";
    public String follow_request = "";

    public String image = "";
    public String type = "";
    public String status = "INVITE";
    public String status_2 = "INVITE";
    public String follow = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int compareTo(Contact other) {
        return status.compareTo(other.status);
    }

}
