package com.example.myapplication.WellnessModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;

public class M1_PlaylistInside extends AppCompatActivity implements M1_saInterface {

    M1_SongAdapter adapter;
    RecyclerView rv;
    ImageView playlistIcon;
    ImageButton playButton;
    Toolbar toolbar;
    ArrayList<M1_SonglistModel> song_list = new ArrayList<>();
    int playlist_id;
    M1_DatabaseHelper mdh = new M1_DatabaseHelper(M1_PlaylistInside.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1_activity_playlist_inside);

        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);

        TextView playlistNameTV = findViewById(R.id.textView2);
        String playlist_name = getIntent().getStringExtra("PLAYLIST NAME");
        getSupportActionBar().setTitle(playlist_name);
        playlistNameTV.setText(playlist_name);
        playlistIcon = findViewById(R.id.imageView);

        //get which playlist
        playlist_id = getIntent().getIntExtra("PLAYLIST ID",10);

        if(playlist_id<4 || playlist_id >4){
            playlistIcon.setImageResource(getIntent().getIntExtra("PLAYLIST ICON",-1));
        }
//        else{
//            playlistIcon.setImageResource(R.drawable.extra_pl_ic2);
//        }


        //get all songs from playlist id
        rv = findViewById(R.id.recyclerview2);

        //to add into model
        displayData(playlist_id);

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M1_PlaylistInside.this, M1_MusicPlayer.class);

                intent.putExtra("SONG NAME", song_list.get(0).getSongName());
                intent.putExtra("ARTIST NAME", song_list.get(0).getArtistName());
                intent.putExtra("SONG URL", song_list.get(0).getUrl());
                intent.putExtra("PLAYLIST ID", playlist_id);

                startActivity(intent);
            }
        });

        adapter = new M1_SongAdapter(M1_PlaylistInside.this, song_list, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(M1_PlaylistInside.this,LinearLayoutManager.VERTICAL,false));
    }

    //to add all song data into arraylist
    void displayData(int id){
        Cursor cursor = mdh.readPlaylist(id);

        if(cursor.getCount() == 0){
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                int url = cursor.getInt(2);
                song_list.add(new M1_SonglistModel(title, artist, url));
            }
        }
        cursor.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mdh = new M1_DatabaseHelper(M1_PlaylistInside.this);
        song_list = new ArrayList<>();

        displayData(playlist_id);

        adapter = new M1_SongAdapter(M1_PlaylistInside.this, song_list,this);
        rv.setAdapter(adapter);
    }


    public void onItemClick(int position) {
        Intent intent = new Intent(M1_PlaylistInside.this, M1_MusicPlayer.class);

        intent.putExtra("SONG TITLE", song_list.get(position).getSongName());
        intent.putExtra("ARTIST NAME", song_list.get(position).getArtistName());
        intent.putExtra("SONG URL", song_list.get(position).getUrl());
        intent.putExtra("PLAYLIST ID", playlist_id);

        startActivity(intent);
    }

    public void onLikeButtonClick(int position){

        //fetch data
        String title = song_list.get(position).getSongName();
        String artist = song_list.get(position).getArtistName();
        int url = song_list.get(position).getUrl();

        if(mdh.checkLikedSong(title)){ //check if song is liked
            mdh.removeFromLiked(title, artist, url);
        }
        else{
            mdh.addIntoLiked(title, artist, url);
        }
    }

    @Override
    public void onAdpButtonClick(int position) {
        Intent intent = new Intent(M1_PlaylistInside.this, M1_AddSongToPlaylist.class);

        //fetch song data
        intent.putExtra("SONG TITLE", song_list.get(position).getSongName());
        intent.putExtra("ARTIST NAME", song_list.get(position).getArtistName());
        intent.putExtra("SONG URL", song_list.get(position).getUrl());
        intent.putExtra("PLAYLIST ID", playlist_id);

        startActivity(intent);
    }
}