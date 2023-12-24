package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPlaylist extends AppCompatActivity {

    EditText playlist_et;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);

        playlist_et = findViewById(R.id.playlistET);
        button = findViewById(R.id.createButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playlist_title  = playlist_et.getText().toString();
                MyDatabaseHelper db = new MyDatabaseHelper(AddPlaylist.this);

                //create the playlist in list of playlist
                db.createPlaylist(playlist_title);

                String title = getIntent().getStringExtra("SONG TITLE");
                String artist = getIntent().getStringExtra("ARTIST NAME");
                int url = getIntent().getIntExtra("SONG URL",100);
                int id = getIntent().getIntExtra("PLAYLIST ID",100);
                db.addSongIntoPlaylist(title,artist,url,id);
            }
        });
    }
}