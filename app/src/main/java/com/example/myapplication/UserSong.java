package com.example.myapplication;

import com.example.myapplication.Models.Song;

public class UserSong {
    private Song song;
    private boolean favourite;

    public UserSong(Song song, boolean favourite) {
        this.song = song;
        this.favourite = favourite;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
