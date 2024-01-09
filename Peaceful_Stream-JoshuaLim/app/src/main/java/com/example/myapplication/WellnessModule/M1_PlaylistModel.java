package com.example.myapplication.WellnessModule;

public class M1_PlaylistModel {
    public M1_PlaylistModel(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }
    public M1_PlaylistModel(String playlistTitle, int id){
        this.playlistTitle = playlistTitle;
        this.id = id;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public int getId() {
        return id;
    }

    private String playlistTitle;
    private int id;
}
