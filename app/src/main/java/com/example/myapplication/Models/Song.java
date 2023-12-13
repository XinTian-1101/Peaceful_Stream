package com.example.myapplication.Models;

public class Song {
    private String mediaId;
    private String title;
    private String artist;
    private String songUrl;
    private String imageUrl;
    private String genre;

    public Song () {}

    public Song(String mediaId, String title, String artist, String songUrl, String imageUrl, String genre) {
        this.mediaId = mediaId;
        this.title = title;
        this.artist = artist;
        this.songUrl = songUrl;
        this.imageUrl = imageUrl;
        this.genre = genre;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
