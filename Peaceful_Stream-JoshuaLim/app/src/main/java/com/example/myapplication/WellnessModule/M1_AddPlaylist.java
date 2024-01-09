package com.example.myapplication.WellnessModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;

public class M1_AddPlaylist extends AppCompatActivity {

    EditText playlist_et;
    Toolbar toolbar;
    Button create, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1_activity_add_playlist);

        //set up elements
        playlist_et = findViewById(R.id.playlistET);
        create = findViewById(R.id.createButton);
        cancel = findViewById(R.id.cancelButton);

        toolbar = findViewById(R.id.toolBar);

        //create toolbar
        setSupportActionBar(toolbar);

        //create button onclick
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M1_AddPlaylist.this, M1_MainActivity.class);
                String playlist_title  = playlist_et.getText().toString();
                M1_DatabaseHelper db = new M1_DatabaseHelper(M1_AddPlaylist.this);
                //create the playlist in list of playlist
                db.createPlaylist(playlist_title);

                startActivity(intent);
            }
        });

        //cancel button onclick
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}