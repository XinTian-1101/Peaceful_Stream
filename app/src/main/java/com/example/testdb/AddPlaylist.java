package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPlaylist extends AppCompatActivity {

    EditText playlist_et;
    Toolbar toolbar;
    Button create, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);

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
                Intent intent = new Intent(AddPlaylist.this,MainActivity.class);
                String playlist_title  = playlist_et.getText().toString();
                MyDatabaseHelper db = new MyDatabaseHelper(AddPlaylist.this);
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

//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // Navigate back to the previous activity
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}