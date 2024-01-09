package com.example.myapplication.WellnessModule;

public class M1_VideolistModel {
    private String videoName;

    private String artistName;

    private int url;

    public M1_VideolistModel(String videoName, String artistName, int url) {
        this.videoName = videoName;
        this.artistName = artistName;
        this.url = url;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getUrl() {
        return url;
    }
}
