package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddSongToPlaylist extends AppCompatActivity implements rvInterface{

    RecyclerView rv;
    Button createPlaylistBtn;
    MyDatabaseHelper mdh = new MyDatabaseHelper(AddSongToPlaylist.this);
    ArrayList<PlaylistModel> playlist = new ArrayList<>();
    ATPAdapter adapter;

    String title, artist;
    int url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_to_playlist);

        rv = findViewById(R.id.recyclerview5);

        title = getIntent().getStringExtra("SONG TITLE");
        artist = getIntent().getStringExtra("ARTIST NAME");
        url = getIntent().getIntExtra("SONG URL",100);

        createPlaylistBtn = findViewById(R.id.button2);

        createPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSongToPlaylist.this, AddPlaylist.class);

//                TextView tv = findViewById(R.id.playlist_name2);
//                String playlist = tv.getText().toString();
//                intent.putExtra("PLAYLIST NAME",playlist);
//                Toast.makeText(AddSongToPlaylist.this, playlist, Toast.LENGTH_SHORT).show();
////                int id = syncPlaylistData(playlist);
//                int id = 100;
                intent.putExtra("PLAYLIST ID",playlist.size()+1);
                intent.putExtra("SONG TITLE", title);
                intent.putExtra("ARTIST NAME", artist);
                intent.putExtra("SONG URL", url);

                startActivity(intent);
            }
        });

        displayData();

        adapter = new ATPAdapter(AddSongToPlaylist.this, playlist,this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(AddSongToPlaylist.this,LinearLayoutManager.VERTICAL,false));
    }


    void displayData(){
        Cursor cursor = mdh.readPlaylistData();

        if(cursor.getCount() == 0){
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                playlist.add(new PlaylistModel(title, id));
            }
        }
        cursor.close();
    }

    public void onItemClick(int position) {
        Intent intent = new Intent(AddSongToPlaylist.this, MainActivity.class);
        MyDatabaseHelper mdh = new MyDatabaseHelper(this);
//
        int id = playlist.get(position).getId();

        mdh.addSongIntoPlaylist(title,artist,url,id);
//
//
//
//        TextView tv = findViewById(R.id.playlist_name);
//        String playlist = tv.getText().toString();
//        int id = syncPlaylistData(title);
//        int id = playlist.get(position).getId();

//        intent.putExtra("PLAYLIST ID",id);
//
        startActivity(intent);
        Toast.makeText(AddSongToPlaylist.this,title+" is added to "+playlist.get(position).getPlaylistTitle(),Toast.LENGTH_SHORT).show();
    }
}