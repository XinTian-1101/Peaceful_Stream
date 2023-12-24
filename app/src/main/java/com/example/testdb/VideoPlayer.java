package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class VideoPlayer extends AppCompatActivity {
    MyDatabaseHelper mdh;
    TextView videoTitleTV, artistNameTV;
    Button nextBtn, prevBtn;
    String title;
    private ArrayList<VideolistModel> playlist = new ArrayList<>();
    private int currentVideoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

//        VideoView vv = findViewById(R.id.videoView);
//        String path = "android.resource://"+getPackageName()+"/"+R.raw.how_to_meditate_1;
//        Uri uri = Uri.parse(path);
//        vv.setVideoURI(uri);
//        vv.start();
//
//        MediaController mc = new MediaController(this);
//        vv.setMediaController(mc);
//        mc.setAnchorView(vv);

        title = getIntent().getStringExtra("VIDEO TITLE");
        String artist_name = getIntent().getStringExtra("ARTIST NAME");

        videoTitleTV = findViewById(R.id.videoTitle);
        artistNameTV = findViewById(R.id.artistName);

        nextBtn = findViewById(R.id.btnNext);
        prevBtn = findViewById(R.id.btnPrevious);

        videoTitleTV.setText(title);
        artistNameTV.setText(artist_name);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextVideo();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentVideoIndex > 0) {
                    currentVideoIndex--;
                    playVideo(currentVideoIndex);
                } else {
                    // If at the beginning of the playlist, play the last song
                    currentVideoIndex = playlist.size() - 1;
                    playVideo(currentVideoIndex);
                }
            }
        });

        mdh = new MyDatabaseHelper(this);

        playlist = getPlaylistFromDatabase();

        playVideo(currentVideoIndex);
    }

    private ArrayList<VideolistModel> getPlaylistFromDatabase() {
        // Retrieve the playlist from the database
        // Implement this method based on your database structure
        // For simplicity, I'm assuming a method that returns all songs in the database
        // You might want to implement a more specific query based on your needs
        SQLiteDatabase db = mdh.getReadableDatabase();
        addPriority();
        Cursor cursor = db.rawQuery("select video_title, artist_name, url from video_playlist",null);
//            Cursor prioritize = db.rawQuery("select song_title, artist_name, url from song_playlist where playlist_id = "+id+" and song_title = '"+title+"'",null);
//
//            String priorTitle = prioritize.getString(0);
//            String priorArtist = prioritize.getString(1);
//            int priorUrl = prioritize.getInt(2);
//            playlist.add(new SonglistModel(priorTitle, priorArtist, priorUrl));
        // fill the song model
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                int url = cursor.getInt(2);
                playlist.add(new VideolistModel(title, artist, url));
            } while (cursor.moveToNext());

//                playlist.remove(new SonglistModel(priorTitle, priorArtist, priorUrl));
            Toast.makeText(this, "Ini punca",Toast.LENGTH_LONG).show();

            cursor.close();
        }

        db.close();
        return playlist;
    }

    private void playVideo(int videoIndex) {
//                if (songIndex < 0 || songIndex >= playlist.size()) {
////
////                    Toast.makeText(this, "LAGU TAK PLAY "+songIndex,Toast.LENGTH_LONG).show();
////                    // Invalid song index
////                    return;
////                }
//
//            // Reset MediaPlayer
//                mediaPlayer.reset();


        VideoView vv = findViewById(R.id.videoView);
        String path = "android.resource://"+getPackageName()+"/"+playlist.get(videoIndex).getUrl();
        Uri uri = Uri.parse(path);


        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);
        mc.setAnchorView(vv);

        vv.setVideoURI(uri);
        vv.start();
//
        // Set the data source and prepare the MediaPlayer
//        int resid = playlist.get(videoIndex).getUrl();
//                try {
//
//        mediaPlayer = MediaPlayer.create(this, resid);
//        mediaPlayer.setOnCompletionListener(this);
        videoTitleTV.setText(playlist.get(videoIndex).getVideoName());
        artistNameTV.setText(playlist.get(videoIndex).getArtistName());
////                mediaPlayer.prepare();
//        seekBar.setMax(mediaPlayer.getDuration());
//        updateSeekBar();
//        // Start playing the song
//        mediaPlayer.start();
        // Update the current song index
        currentVideoIndex = videoIndex;

        vv.setOnCompletionListener(mediaPlayer -> playNextVideo());

//                    mediaPlayer.reset();
//                }catch(IOException e){
//                    e.printStackTrace();
//                }
    }

    private void playNextVideo() {
        currentVideoIndex++;
        if (currentVideoIndex < playlist.size()) {
            // Play the next song
            playVideo(currentVideoIndex);
        } else {
            // Playlist finished, you might want to handle this case
            // For simplicity, I'm resetting the playlist to play from the beginning
            currentVideoIndex = 0;
            playVideo(currentVideoIndex);
        }
    }

    void addPriority(){
        SQLiteDatabase db = mdh.getReadableDatabase();

        Cursor prioritize = db.rawQuery("select video_title, artist_name, url from video_playlist where video_title = '"+title+"'",null);



        if (prioritize != null && prioritize.moveToFirst()) {
//
            do {
//                    String path = "android.resources://"+getPackageName();
                String priorTitle = prioritize.getString(0);
                String priorArtist = prioritize.getString(1);
                int priorUrl = prioritize.getInt(2);
                playlist.add(new VideolistModel(priorTitle, priorArtist, priorUrl));
            } while (prioritize.moveToNext());

            prioritize.close();
        }
    }
}