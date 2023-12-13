package com.example.myapplication.Models;

import java.util.List;

public class Playlist {
    private String name , dateCreated , description , imageUrl;
    private List<String> songArr;

    public Playlist(){}

    public Playlist(String name, String dateCreated, String description, String imageUrl, List<String> songArr) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.description = description;
        this.imageUrl = imageUrl;
        this.songArr = songArr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getSongArr() {
        return songArr;
    }

    public void setSongArr(List<String> songArr) {
        this.songArr = songArr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
