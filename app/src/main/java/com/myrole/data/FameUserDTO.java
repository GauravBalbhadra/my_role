package com.myrole.data;

import java.util.ArrayList;

/**
 * Created by Rakesh on 10/8/2016.
 */

public class FameUserDTO {

    private boolean status;
    private String message;
    private ArrayList<FameUserDTO> data;
    private int id;
    private String username;
    private String name;
    private String image;
    private String winner;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
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

    public ArrayList<FameUserDTO> getData() {
        return data;
    }

    public void setData(ArrayList<FameUserDTO> data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
