package com.example.myapplication.WellnessModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;

public class M1_MusicPlayer extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;

    TextView artistNameTV, songTitleTV, timeElapsed, totalDuration;
    ImageView icon;

    M1_DatabaseHelper mdh;
    private Handler handler = new Handler();
    private ArrayList<M1_SonglistModel> playlist = new ArrayList<>();
    private int currentSongIndex = 0;

    SeekBar seekBar;
    Toolbar toolbar;
    int id;
    String title;
    ImageButton playBtn, pauseBtn, nextBtn, prevBtn, shuffleBtn, loopBtn, likeBtn, atpBtn;
    private boolean isShuffle = false;
    private boolean isLoop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1_activity_music_player);

        //declare ImageButton variable
        playBtn = findViewById(R.id.playButton);
        pauseBtn = findViewById(R.id.pauseButton);
        prevBtn = findViewById(R.id.prevButton);
        nextBtn = findViewById(R.id.nextButton);
        shuffleBtn = findViewById(R.id.shuffleButton);
        loopBtn = findViewById(R.id.loopButton);
        likeBtn = findViewById(R.id.likeButton);
        atpBtn = findViewById(R.id.addToPlaylistButton);
        songTitleTV = findViewById(R.id.title_song);
        artistNameTV = findViewById(R.id.name_artist);
        seekBar = findViewById(R.id.seekBar);

        icon = findViewById(R.id.imageView2);

        timeElapsed = findViewById(R.id.timeElapsed);
        totalDuration = findViewById(R.id.totalDuration);

        toolbar = findViewById(R.id.toolBar);

        mdh = new M1_DatabaseHelper(this);

        title = getIntent().getStringExtra("SONG TITLE");
        String artist_name = getIntent().getStringExtra("ARTIST NAME");
        int url = getIntent().getIntExtra("SONG URL",-1);

        //setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.m1_ic_back_arrow);

        if(mdh.checkLikedSong(title)){ //if liked, heart filled
            likeBtn.setImageResource(R.drawable.m1_ic_like_on);
        }
        else{ //if not liked, heart empty
            likeBtn.setImageResource(R.drawable.m1_ic_like_off);
        }

        //play button onclick
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()){
                    pauseBtn.setVisibility(View.VISIBLE);
                    playBtn.setVisibility(View.INVISIBLE);
                    //play the media
                    mediaPlayer.start();
                }
                else{

                }
            }
        });

        //pause button onclick
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    playBtn.setVisibility(View.VISIBLE);
                    pauseBtn.setVisibility(View.INVISIBLE);
                    //pause the media
                    mediaPlayer.pause();
                }
            }
        });

        //next song button onclick
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseBtn.setVisibility(View.VISIBLE);
                playBtn.setVisibility(View.INVISIBLE);
                //call method to play next song
                playNextSong();
            }
        });

        //previous song button onclick
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSongIndex--;
                pauseBtn.setVisibility(View.VISIBLE);
                playBtn.setVisibility(View.INVISIBLE);
                if (currentSongIndex >= 0) { //take previous song
                    mediaPlayer.reset();
                    playSong(currentSongIndex);
                }
                else { //take last song in the list
                    currentSongIndex = playlist.size() - 1;
                    mediaPlayer.reset();
                    playSong(currentSongIndex);
                }
            }
        });

        //shuffle song onclick
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toggleShuffle();
                isShuffle = !isShuffle;

                if (isShuffle) {
                    // Shuffle the playlist
                    Collections.shuffle(playlist);
                    Toast.makeText(M1_MusicPlayer.this,"In Shuffle", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(M1_MusicPlayer.this,"Not in Shuffle", Toast.LENGTH_SHORT).show();
            }
        });

        //loop song onclick
        loopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toggleLoop();
                isLoop = !isLoop;
                if(isLoop)
                    Toast.makeText(M1_MusicPlayer.this,"In Loop", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(M1_MusicPlayer.this,"Not In Loop", Toast.LENGTH_SHORT).show();
                mediaPlayer.setLooping(isLoop);
            }
        });

        //like button onclick
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mdh.checkLikedSong(title)){ //check song is liked or not
                    likeBtn.setImageResource(R.drawable.m1_ic_like_off);
                    mdh.removeFromLiked(title, artist_name, url); //if liked, remove from liked song
                }
                else{
                    likeBtn.setImageResource(R.drawable.m1_ic_like_on);
                    mdh.addIntoLiked(title, artist_name, url); //add into liked songs
                }
            }
        });

        //add to playlist onclick
        atpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(M1_MusicPlayer.this, M1_AddSongToPlaylist.class);

                //bring data to next activity
                intent.putExtra("SONG TITLE", title);
                intent.putExtra("ARTIST NAME", artist_name);
                intent.putExtra("SONG URL", url);

                startActivity(intent);
            }
        });

        //to make seekbar can control the song
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    timeElapsed.setText(formatDuration(progress)); //update the elapsed time text
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        songTitleTV.setText(title);
        artistNameTV.setText(artist_name);

        id = getIntent().getIntExtra("PLAYLIST ID",100);
        playlist = getPlaylistFromDatabase(id);
        playSong(currentSongIndex);
    }

    //format for time elapsed and total duration
    private String formatDuration(int duration) {
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void toggleShuffle() {
//        isShuffle = !isShuffle;
//
//        if (isShuffle) {
//            // Shuffle the playlist
//            Collections.shuffle(playlist);
//            Toast.makeText(MusicPlayer.this,"In Shuffle", Toast.LENGTH_SHORT).show();
//        }
//        else
//            Toast.makeText(MusicPlayer.this,"Not in Shuffle", Toast.LENGTH_SHORT).show();
    }

    public void toggleLoop() {
//        isLoop = !isLoop;
//        if(isLoop)
//            Toast.makeText(MusicPlayer.this,"In Loop", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(MusicPlayer.this,"Not In Loop", Toast.LENGTH_SHORT).show();
//        mediaPlayer.setLooping(isLoop);
    }

    //apply back to up button
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                // Pause the MediaPlayer
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //add song into list to play from specific playlist
    private ArrayList<M1_SonglistModel> getPlaylistFromDatabase(int id) {
        SQLiteDatabase db = mdh.getReadableDatabase();

        addPriority();
        Cursor cursor = db.rawQuery("select song_title, artist_name, url, song_icon from song_playlist where playlist_id = "+id,null);
        // fill the song model
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                int url = cursor.getInt(2);
                int icon_song = cursor.getInt(3);

                playlist.add(new M1_SonglistModel(title, artist, url, icon_song));
            } while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return playlist;
    }

    //add selected song to be first in queue
    void addPriority(){
        SQLiteDatabase db = mdh.getReadableDatabase();

        Cursor prioritize = db.rawQuery("select song_title, artist_name, url, song_icon  from song_playlist where playlist_id = "+id+" and song_title = '"+title+"'",null);


        if (prioritize != null && prioritize.moveToFirst()) {
            do {
                String priorTitle = prioritize.getString(0);
                String priorArtist = prioritize.getString(1);
                int priorUrl = prioritize.getInt(2);
                int priorIcon = prioritize.getInt(3);

                playlist.add(new M1_SonglistModel(priorTitle, priorArtist, priorUrl, priorIcon));
            } while (prioritize.moveToNext());

            prioritize.close();
        }
    }
        private void playSong(int songIndex) {
            int resid = playlist.get(songIndex).getUrl();

            mediaPlayer = MediaPlayer.create(this, resid);
            mediaPlayer.setOnCompletionListener(this);

            totalDuration.setText(formatDuration(mediaPlayer.getDuration()));

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

            icon.setImageResource(playlist.get(songIndex).getIcon());
            songTitleTV.setText(playlist.get(songIndex).getSongName());
            artistNameTV.setText(playlist.get(songIndex).getArtistName());
            seekBar.setMax(mediaPlayer.getDuration());
            updateSeekBar();

            mediaPlayer.start();
            currentSongIndex = songIndex;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            playNextSong();
        }

        private void updateSeekBar() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int pos = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(pos);
                        timeElapsed.setText(formatDuration(pos));

                        //always call to always update
                        updateSeekBar();
                    }
                }
            }, 100);
        }

        private void playNextSong() {
            currentSongIndex++;
            if (currentSongIndex < playlist.size()) {
                mediaPlayer.reset();
                playSong(currentSongIndex);
            } else {
                currentSongIndex = 0;
                mediaPlayer.reset();
                playSong(currentSongIndex);
            }
        }

        protected void onStop(){
            super.onStop();
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        }
}
