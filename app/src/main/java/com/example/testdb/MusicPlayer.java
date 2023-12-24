package com.example.testdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MusicPlayer extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;

    TextView artistNameTV, songTitleTV, timeElapsed, totalDuration;

    private MyDatabaseHelper mdh;
    private Handler handler = new Handler();
    private ArrayList<SonglistModel> playlist = new ArrayList<>();
    private int currentSongIndex = 0;

    SeekBar seekBar;
    int id;
    String title;
    ImageButton playBtn, pauseBtn, nextBtn, prevBtn, shuffleBtn, loopBtn;
    private boolean isShuffle = false;
    private boolean isLoop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        //declare ImageButton variable
        playBtn = findViewById(R.id.playButton);
        pauseBtn = findViewById(R.id.pauseButton);
        prevBtn = findViewById(R.id.prevButton);
        nextBtn = findViewById(R.id.nextButton);
        shuffleBtn = findViewById(R.id.shuffleButton);
        loopBtn = findViewById(R.id.loopButton);

        timeElapsed = findViewById(R.id.timeElapsed);
        totalDuration = findViewById(R.id.totalDuration);



        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()){
                    pauseBtn.setVisibility(View.VISIBLE);
                    playBtn.setVisibility(View.INVISIBLE);
                    mediaPlayer.start();
                }
                else{

                }
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    playBtn.setVisibility(View.VISIBLE);
                    pauseBtn.setVisibility(View.INVISIBLE);
                    mediaPlayer.pause();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSongIndex--;
                if (currentSongIndex >= 0) {
                    // Play the previous song
                    mediaPlayer.reset();
                    playSong(currentSongIndex);
                } else {
                    // If at the beginning of the playlist, play the last song
                    currentSongIndex = playlist.size() - 1;
                    mediaPlayer.reset();
                    playSong(currentSongIndex);
                }
            }
        });

        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShuffle();
            }
        });

        loopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLoop();
            }
        });
        title = getIntent().getStringExtra("SONG TITLE");
        String artist_name = getIntent().getStringExtra("ARTIST NAME");

        songTitleTV = findViewById(R.id.title_song);
        artistNameTV = findViewById(R.id.name_artist);
        seekBar = findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });

        songTitleTV.setText(title);
        artistNameTV.setText(artist_name);

        mdh = new MyDatabaseHelper(this);
        id = getIntent().getIntExtra("PLAYLIST ID",100);
//        Toast.makeText(MusicPlayer.this,"Di sini masih belum",Toast.LENGTH_LONG).show();
        playlist = getPlaylistFromDatabase(id);
//        Toast.makeText(this,playlist.size(),Toast.LENGTH_LONG).show();
////        // Example: Play the first song in the playlist
        playSong(currentSongIndex);
//        playSong();
    }

    private String formatDuration(int duration) {
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void toggleShuffle() {
        isShuffle = !isShuffle;

        if (isShuffle) {
            // Shuffle the playlist
            Collections.shuffle(playlist);
            Toast.makeText(MusicPlayer.this,"In Shuffle", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(MusicPlayer.this,"Not in Shuffle", Toast.LENGTH_SHORT).show();
    }

    public void toggleLoop() {
        isLoop = !isLoop;
        if(isLoop)
            Toast.makeText(MusicPlayer.this,"In Loop", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MusicPlayer.this,"Not In Loop", Toast.LENGTH_SHORT).show();
        mediaPlayer.setLooping(isLoop);
    }
    private ArrayList<SonglistModel> getPlaylistFromDatabase(int id) {
        // Retrieve the playlist from the databaseific query based on your needs
        SQLiteDatabase db = mdh.getReadableDatabase();

        addPriority();
        Cursor cursor = db.rawQuery("select song_title, artist_name, url from song_playlist where playlist_id = "+id,null);
        // fill the song model
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String artist = cursor.getString(1);
                int url = cursor.getInt(2);
                playlist.add(new SonglistModel(title, artist, url));
            } while (cursor.moveToNext());

            Toast.makeText(this, "Ini punca",Toast.LENGTH_LONG).show();

            cursor.close();
        }
        db.close();
        return playlist;
    }

    void addPriority(){
        SQLiteDatabase db = mdh.getReadableDatabase();

        Cursor prioritize = db.rawQuery("select song_title, artist_name, url from song_playlist where playlist_id = "+id+" and song_title = '"+title+"'",null);



        if (prioritize != null && prioritize.moveToFirst()) {
            do {
                String priorTitle = prioritize.getString(0);
                String priorArtist = prioritize.getString(1);
                int priorUrl = prioritize.getInt(2);
                playlist.add(new SonglistModel(priorTitle, priorArtist, priorUrl));
            } while (prioritize.moveToNext());

            prioritize.close();
        }
    }
        private void playSong(int songIndex) {
        // Set the data source and prepare the MediaPlayer
            int resid = playlist.get(songIndex).getUrl();

            mediaPlayer = MediaPlayer.create(this, resid);
            mediaPlayer.setOnCompletionListener(this);

            totalDuration.setText(formatDuration(mediaPlayer.getDuration()));

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }

            songTitleTV.setText(playlist.get(songIndex).getSongName());
            artistNameTV.setText(playlist.get(songIndex).getArtistName());
            seekBar.setMax(mediaPlayer.getDuration());
            updateSeekBar();
            // Start playing the song
            mediaPlayer.start();
            // Update the current song index
            currentSongIndex = songIndex;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            // Called when the current song finishes playing
            // Play the next song in the playlist
            mp.reset();
            playNextSong();
        }

        private void updateSeekBar() {
            // Update SeekBar and duration text every 100 milliseconds
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        // Update SeekBar progress
                        int pos = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(pos);
                        timeElapsed.setText(formatDuration(pos));

                        // Call recursively to keep updating
                        updateSeekBar();
                    }
                }
            }, 100);
        }

        private void playNextSong() {
            currentSongIndex++;
            if (currentSongIndex < playlist.size()) {
                // Play the next song
                mediaPlayer.reset();
                playSong(currentSongIndex);
            } else {
                // Playlist finished
                currentSongIndex = 0;
                mediaPlayer.reset();
                playSong(currentSongIndex);
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            // Release the MediaPlayer resources when destroyed
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
        }


}
