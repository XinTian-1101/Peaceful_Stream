package com.example.myapplication.Models;

import android.net.Uri;

public class User {
    private String username , email;
    private String imageUri;
    private boolean isCounsellor;

    public User() {
    }

    public User(String username, String email, String imageUri, boolean isCounsellor) {
        this.username = username;
        this.email = email;
        this.imageUri = imageUri;
        this.isCounsellor = isCounsellor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isCounsellor() {
        return isCounsellor;
    }

    public void setCounsellor(boolean counsellor) {
        isCounsellor = counsellor;
    }
}
