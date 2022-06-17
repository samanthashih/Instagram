package com.example.instagram;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Like extends ParseObject {
    public static final String KEY_POST = "post";
    public static final String KEY_USER = "user";

    public void setPost(Post post) {put(KEY_POST, post);}

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}

