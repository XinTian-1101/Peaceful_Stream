package com.example.myapplication.WellnessModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class M1_VideoPlayer extends AppCompatActivity {
    M1_DatabaseHelper mdh;
    TextView videoTitleTV, artistNameTV;
    Button nextBtn, prevBtn;
    VideoView vv;
    String title;
    Toolbar toolbar;
    private ArrayList<M1_VideolistModel> playlist = new ArrayList<>();
    private int currentVideoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1_activity_video_player);

        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);

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
                    currentVideoIndex = playlist.size() - 1;
                    playVideo(currentVideoIndex);
                }
            }
        });

        mdh = new M1_DatabaseHelper(this);

        playlist = getPlaylistFromDatabase();

        playVideo(currentVideoIndex);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                // Pause the MediaPlayer
                if (vv.isPlaying()) {
                    vv.pause();
                }

                // Navigate back to the previous activity
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<M1_VideolistModel> getPlaylistFromDatabase() {
        SQLiteDatabase db = mdh.getReadableDatabase();
        addPriority();
        Cursor cursor = db.rawQuery("select video_title, artist_name, url from video_playlist",null);

        // fill the song model
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                int url = cursor.getInt(2);
                playlist.add(new M1_VideolistModel(title, artist, url));
            } while (cursor.moveToNext());


            cursor.close();
        }

        db.close();
        return playlist;
    }

    private void playVideo(int videoIndex) {
        vv = findViewById(R.id.videoView);
        String path = "android.resource://"+getPackageName()+"/"+playlist.get(videoIndex).getUrl();
        Uri uri = Uri.parse(path);


        MediaController mc = new MediaController(this);
        vv.setMediaController(mc);
        mc.setAnchorView(vv);

        vv.setVideoURI(uri);
        vv.start();

        videoTitleTV.setText(playlist.get(videoIndex).getVideoName());
        artistNameTV.setText(playlist.get(videoIndex).getArtistName());

        // Update the current song index
        currentVideoIndex = videoIndex;

        vv.setOnCompletionListener(mediaPlayer -> playNextVideo());
    }

    private void playNextVideo() {
        currentVideoIndex++;
        if (currentVideoIndex < playlist.size()) {
            // Play the next song
            playVideo(currentVideoIndex);
        } else {
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
                String priorTitle = prioritize.getString(0);
                String priorArtist = prioritize.getString(1);
                int priorUrl = prioritize.getInt(2);
                playlist.add(new M1_VideolistModel(priorTitle, priorArtist, priorUrl));
            } while (prioritize.moveToNext());

            prioritize.close();
        }
    }
}