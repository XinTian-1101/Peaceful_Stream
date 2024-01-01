package com.example.testdb;

import static com.google.common.reflect.Reflection.getPackageName;

import java.io.Serializable;

public class M1_SonglistModel implements Serializable {
    private String songName;

    private String artistName;

    private int url;
    private int icon;

    public M1_SonglistModel(String songName, String artistName, int url, int icon) {
        this.songName = songName;
        this.artistName = artistName;
        this.url = url;
        this.icon=icon;
    }

    public M1_SonglistModel(String songName, String artistName, int url) {
        this.songName = songName;
        this.artistName = artistName;
        this.url = url;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getUrl() {
        return url;
    }

    public int getIcon(){return icon;}
}
