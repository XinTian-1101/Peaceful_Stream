package com.example.testdb;

public class PlaylistModel {
    public PlaylistModel(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }
    public PlaylistModel(String playlistTitle, int id){
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
