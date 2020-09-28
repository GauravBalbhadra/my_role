package com.myrole.holders;

/**
 * Created by welcome on 19-04-2017.
 */

public class User {
    public String name,hometown;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public User(String name , String hometown) {
        this.name = name;

        this.hometown = hometown;

    }
}
