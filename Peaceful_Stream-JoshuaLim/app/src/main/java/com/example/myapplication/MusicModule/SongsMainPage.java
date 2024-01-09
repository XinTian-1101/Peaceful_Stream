package com.example.myapplication.MusicModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Adapters.SongsRecViewAdapter;
import com.example.myapplication.Models.Song;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SongsMainPage extends AppCompatActivity implements RecViewClickListener {
    private ConstraintLayout layout;
    private RecyclerView recyclerView;
    private List<Song> songList;
    private FirebaseFirestore db;
    private SongsRecViewAdapter adapter;
    private ExoPlayer player;
    private RelativeLayout customPlayer;
    private TextView playlistDisplayName;
    private LinearLayout playlistImage;
    private ImageButton playAll;
    private Toolbar toolbar;
    private View customPlayerSongsMainPage;
    private DocumentReference playlistDoc , favSongsDoc;
    private int repeatMode = 0;
    private String playlistName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_main_page);
        layout = findViewById(R.id.songsMainPageLayout);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.songsRecView);
        songList = new ArrayList<>();
        customPlayerSongsMainPage = findViewById(R.id.customPlayerSongsMainPage);
        customPlayer = customPlayerSongsMainPage.findViewById(R.id.customPlayer);
        playlistDisplayName = findViewById(R.id.playlistName);
        playAll = findViewById(R.id.playAll);
        playlistName = getIntent().getExtras().getString("playlistName");
        playlistImage = findViewById(R.id.playlistImage);


        playlistDisplayName.setText(playlistName);

        player = new ExoPlayer.Builder(SongsMainPage.this).build();

        adapter = new SongsRecViewAdapter(this , getSupportFragmentManager() , songList , true , playlistName ,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        playlistDoc = FirebaseUtil.getOnePlaylistReference(playlistName);
        favSongsDoc = FirebaseUtil.getFavSongReference();

        playlistDoc.get().addOnSuccessListener(documentSnapshot -> {
            List<String> songArr = (List<String>) documentSnapshot.get("songArr");
            playlistImage.removeAllViews();
            if (songArr.isEmpty()) {
                View inflatedView = LayoutInflater.from(SongsMainPage.this).inflate(R.layout.single_playlist_image , playlistImage);
                ImageView playlistImg = inflatedView.findViewById(R.id.playlistImg);
                Glide.with(SongsMainPage.this).asBitmap().load(getString(R.string.default_music_icon_image_url)).into(playlistImg);
            }
            else if (songArr.size() < 4) {
                View inflatedView = LayoutInflater.from(SongsMainPage.this).inflate(R.layout.single_playlist_image , playlistImage);
                FirebaseUtil.getOneSong(songArr.get(0))
                        .get().addOnSuccessListener(documentSnapshot1 -> {
                            Song songInfo = documentSnapshot1.toObject(Song.class);
                            ImageView playlistImg = inflatedView.findViewById(R.id.playlistImg);
                            Glide.with(SongsMainPage.this).asBitmap().load(songInfo.getImageUrl()).into(playlistImg);
                        });
            }
            else {
                View inflatedView = LayoutInflater.from(SongsMainPage.this).inflate(R.layout.four_playlist_image , playlistImage);
                for (int i = 0 ; i < 4 ; i++) {
                    int finalI = i;
                    FirebaseUtil.getOneSong(songArr.get(i))
                            .get().addOnSuccessListener(documentSnapshot12 -> {
                                Song songInfo = documentSnapshot12.toObject(Song.class);
                                ImageView playlistImg;
                                if (finalI == 0) playlistImg = inflatedView.findViewById(R.id.playlistImageOne);
                                else if (finalI == 1) playlistImg = inflatedView.findViewById(R.id.playlistImageTwo);
                                else if (finalI == 2) playlistImg = inflatedView.findViewById(R.id.playlistImageThree);
                                else playlistImg = inflatedView.findViewById(R.id.playlistImageFour);
                                Glide.with(SongsMainPage.this).asBitmap().load(songInfo.getImageUrl()).into(playlistImg);
                            });
                }
            }
        });

        PlaylistSongEventChangeListener(playlistDoc , songList , adapter);
        FavouriteSongsEventChangeListener(favSongsDoc , adapter);

        playAll.setOnClickListener(v -> {
            initiateCustomPlayerAndBottomSheet(SongsMainPage.this , customPlayerSongsMainPage ,
                    0 , songList , player , favSongsDoc , adapter , getSupportFragmentManager());
        });
    }

    // method for playing and pausing music
    public void playPauseMusic (ExoPlayer player) {
        if (player.isPlaying()) player.pause();
        else player.play();
    }

    // updating seek bar progress as music plays
    private void updatePlayerPositionProgress(ExoPlayer player , TextView currTimeBtmSheet ,
                                              SeekBar seekBarBtmSheet) {
        new Handler().postDelayed(() -> {
            if (player.isPlaying()) {
                currTimeBtmSheet.setText(getReadableTime((int) player.getCurrentPosition()));
                seekBarBtmSheet.setProgress((int) player.getCurrentPosition());
            }
            updatePlayerPositionProgress(player , currTimeBtmSheet , seekBarBtmSheet);
        }, 1000);
    }

    // converting int returned by player methods to mm:ss format
    String getReadableTime (int duration) {
        String time , secString;
        int hrs = duration / (1000*60*60);
        int min = (duration % (1000*60*60)) / (1000*60);
        int sec = (((duration % (1000*60*60)) % (1000*60*60)) % (1000*60)) / 1000;
        if (sec < 10) {
            secString = "0" + sec;
        }
        else secString = String.valueOf(sec);

        if (hrs < 1) time = min + ":" + secString;
        else time = hrs + ":" + min + ":" + secString;

        return time;
    }

    // listens to changes to songs inside playlist
    public void PlaylistSongEventChangeListener(DocumentReference docRef , List <Song> songList , RecyclerView.Adapter adapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        docRef.addSnapshotListener((value, error) -> {
            if (error != null) Toast.makeText(SongsMainPage.this , "Firestore error" , Toast.LENGTH_SHORT).show();
            else {
                List <String> songArr = (List<String>) value.get("songArr");
                for (Song song : songList) {
                    if (!songArr.contains(song.getTitle())) {
                        songList.remove(song);
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
                for (String song : songArr) {
                    DocumentReference oneSong = FirebaseUtil.getOneSong(song);
                    oneSong.get().addOnSuccessListener(documentSnapshot -> {
                        Song currSong = documentSnapshot.toObject(Song.class);
                        if (!songList.contains(currSong)) {
                            songList.add(currSong);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    public void FavouriteSongsEventChangeListener (DocumentReference favSongsDoc , SongsRecViewAdapter adapter) {
        favSongsDoc.addSnapshotListener((value, error) -> {
            if (error != null) Toast.makeText(SongsMainPage.this , "Firestore error" , Toast.LENGTH_SHORT).show();
            else adapter.notifyDataSetChanged();
        });
    }

    // when clicking a song
    @Override
    public void onItemClick(int position) {
        initiateCustomPlayerAndBottomSheet(SongsMainPage.this , customPlayerSongsMainPage ,
                position , songList , player , favSongsDoc , adapter , getSupportFragmentManager());
    }

    // play the clicked song
    public void playClickedSong (int position , List <Song> songList , ExoPlayer player , Context context
            , RelativeLayout customPlayer) {
        player.setMediaItems(getMediaItems(songList) , position , 0);
        player.prepare();
        player.play();
        if (customPlayer.getVisibility() == View.GONE) customPlayer.setVisibility(View.VISIBLE);
        Toast.makeText(context, songList.get(position).getTitle() + " is playing", Toast.LENGTH_SHORT).show();
    }

    // returns song list as list of media items
    public List <MediaItem> getMediaItems (List<Song> songList) {
        List <MediaItem> mediaItemList = new ArrayList<>();
        for (Song song : songList) {
            MediaItem mediaItem = new MediaItem.Builder().setUri(song.getSongUrl())
                    .setMediaMetadata(new MediaMetadata.Builder().setTitle(song.getTitle())
                            .setArtist(song.getArtist()).setArtworkUri(Uri.parse(song.getImageUrl()))
                            .build()).build();
            mediaItemList.add(mediaItem);
        }
        return mediaItemList;
    }

    // reveals song bottom sheet
    public void showSongBottomSheet (View bottomSheetView , BottomSheetDialog bottomSheetDialog) {
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View)bottomSheetView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        ConstraintLayout layout = bottomSheetDialog.findViewById(R.id.bottomSheetLayout);
        assert layout != null;
        layout.setMinHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        bottomSheetDialog.show();
    }

    public void addToFav (Context context , String songTitle , DocumentReference favSongsDoc) {
        favSongsDoc.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> songArr = (List<String>) documentSnapshot.get("songArr");
                        assert songArr != null;
                        if (!songArr.contains(songTitle)) {
                            songArr.add(songTitle);
                            Toast.makeText(context, "Song added to favourite", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            songArr.remove(songTitle);
                            Toast.makeText(context, "Song removed from favourite", Toast.LENGTH_SHORT).show();
                        }
                        favSongsDoc.update("songArr" , songArr);
                    }
                    else Toast.makeText(context , "Failed to favourite song!" , Toast.LENGTH_SHORT).show();
                });
    }

    // initiates everything including variables, listeners, onclicks etc. for custom player and bototm sheet
    public void initiateCustomPlayerAndBottomSheet (Context context , View includedCustomPlayer , int position , List<Song> songList ,
                                                    ExoPlayer player , DocumentReference favSongsDoc , SongsRecViewAdapter adapter ,
                                                    FragmentManager fragmentManager ) {
        if (player.isPlaying() || player.getCurrentMediaItem() != null) {
            playClickedSong(position , songList , player , context , customPlayer);
            return;
        }
        final int[] repeatMode = {0};
        ImageButton playPauseBtnPlayer = includedCustomPlayer.findViewById(R.id.playPauseBtnPlayer);
        playPauseBtnPlayer.setOnClickListener(view -> playPauseMusic(player));
        RelativeLayout customPlayer = includedCustomPlayer.findViewById(R.id.customPlayer);
        TextView songNamePlayer = includedCustomPlayer.findViewById(R.id.songNamePlayer);
        ImageView songImagePlayer = includedCustomPlayer.findViewById(R.id.songImagePlayer);
        ImageButton favouriteBtnPlayer = includedCustomPlayer.findViewById(R.id.favouriteBtnPlayer);
        ImageButton playlistAddPlayer = includedCustomPlayer.findViewById(R.id.playlistAddPlayer);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.song_bottom_sheet , null);
        bottomSheetDialog.setContentView(bottomSheetView);
        TextView songTitleBtmSheet = bottomSheetDialog.findViewById(R.id.songTitleBtmSheet);
        TextView songArtistBtmSheet = bottomSheetDialog.findViewById(R.id.songArtistBtmSheet);
        TextView currTimeBtmSheet = bottomSheetDialog.findViewById(R.id.currTimeBtmSheet);
        TextView totalDurationBtmSheet = bottomSheetDialog.findViewById(R.id.totalDurationBtmSheet);
        ImageView songImageBtmSheet = bottomSheetDialog.findViewById(R.id.songImageBtmSheet);
        ImageButton playPauseBtmSheet = bottomSheetDialog.findViewById(R.id.playPauseBtmSheet);
        ImageButton nextSongBtmSheet = bottomSheetDialog.findViewById(R.id.nextSongBtmSheet);
        ImageButton prevSongBtmSheet = bottomSheetDialog.findViewById(R.id.prevSongBtmSheet);
        ImageButton shuffleBtmSheet = bottomSheetDialog.findViewById(R.id.shuffleBtmSheet);
        ImageButton repeatBtmSheet = bottomSheetDialog.findViewById(R.id.repeatBtmSheet);
        ImageButton favouriteBtmSheet = bottomSheetDialog.findViewById(R.id.favouriteBtmSheet);
        ImageButton playlistAddBtmSheet = bottomSheetDialog.findViewById(R.id.addToPlaylistBtmSheet);
        SeekBar seekBarBtmSheet = bottomSheetDialog.findViewById(R.id.seekBarBtmSheet);


        playClickedSong(position , songList , player , context , customPlayer);

        playPauseBtmSheet.setOnClickListener(view -> playPauseMusic(player));
        nextSongBtmSheet.setOnClickListener(view -> player.seekToNext());
        prevSongBtmSheet.setOnClickListener(view -> player.seekToPrevious());
        customPlayer.setOnClickListener(view -> showSongBottomSheet(bottomSheetView , bottomSheetDialog));

        player.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                assert mediaItem != null;
                songNamePlayer.setText(mediaItem.mediaMetadata.title);
                songTitleBtmSheet.setText(mediaItem.mediaMetadata.title);
                songArtistBtmSheet.setText(mediaItem.mediaMetadata.artist);
                currTimeBtmSheet.setText(getReadableTime((int) player.getCurrentPosition()));
                seekBarBtmSheet.setProgress((int) player.getCurrentPosition());
                seekBarBtmSheet.setMax((int)player.getDuration());
                totalDurationBtmSheet.setText(getReadableTime((int)player.getDuration()));
                Glide.with(context).asBitmap().load(mediaItem.mediaMetadata.artworkUri).into(songImagePlayer);
                Glide.with(context).asBitmap().load(mediaItem.mediaMetadata.artworkUri).into(songImageBtmSheet);
                updatePlayerPositionProgress(player , currTimeBtmSheet , seekBarBtmSheet);
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if (playbackState == Player.STATE_READY) {
                    songNamePlayer.setText(player.getCurrentMediaItem().mediaMetadata.title);
                    songTitleBtmSheet.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title);
                    songArtistBtmSheet.setText(player.getCurrentMediaItem().mediaMetadata.artist);
                    currTimeBtmSheet.setText(getReadableTime((int) player.getCurrentPosition()));
                    seekBarBtmSheet.setProgress((int) player.getCurrentPosition());
                    seekBarBtmSheet.setMax((int)player.getDuration());
                    totalDurationBtmSheet.setText(getReadableTime((int)player.getDuration()));
                    Glide.with(context).asBitmap().load(player.getCurrentMediaItem()
                            .mediaMetadata.artworkUri).into(songImagePlayer);
                    Glide.with(context).asBitmap().load(player.getCurrentMediaItem()
                            .mediaMetadata.artworkUri).into(songImageBtmSheet);
                    updatePlayerPositionProgress(player , currTimeBtmSheet , seekBarBtmSheet);
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if (isPlaying) {
                    playPauseBtnPlayer.setImageResource(R.drawable.ic_pause);
                    playPauseBtmSheet.setImageResource(R.drawable.ic_pause_white);
                }
                else {
                    playPauseBtnPlayer.setImageResource(R.drawable.ic_play);
                    playPauseBtmSheet.setImageResource(R.drawable.ic_play_white);
                }
            }
        });

        FavouriteSongsEventChangeListener(favSongsDoc , adapter);

        seekBarBtmSheet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (player.getPlaybackState() == Player.STATE_READY) {
                    seekBar.setProgress(progressValue);
                    currTimeBtmSheet.setText(getReadableTime(progressValue));
                    player.seekTo(progressValue);
                }
            }
        });

        shuffleBtmSheet.setOnClickListener(view -> {
            if (player.getShuffleModeEnabled()) {
                player.setShuffleModeEnabled(false);
                shuffleBtmSheet.setImageResource(R.drawable.ic_shuffle_white);
            }
            else {
                player.setShuffleModeEnabled(true);
                shuffleBtmSheet.setImageResource(R.drawable.ic_shuffle_on_white);
            }
        });

        repeatBtmSheet.setOnClickListener(view -> {
            if (repeatMode[0] == 0) {
                player.setRepeatMode(Player.REPEAT_MODE_ONE);
                repeatMode[0] = 1;
                repeatBtmSheet.setImageResource(R.drawable.m1_repeatedon);

            }
            else if (repeatMode[0] == 1) {
                player.setRepeatMode(Player.REPEAT_MODE_ALL);
                repeatMode[0] = 2;
                repeatBtmSheet.setImageResource(R.drawable.m1_repeatedon);

            }
            else {
                player.setRepeatMode(Player.REPEAT_MODE_OFF);
                repeatMode[0] = 0;
                repeatBtmSheet.setImageResource(R.drawable.m1_ic_repeat);

            }
        });

        favSongsDoc.addSnapshotListener((value, error) -> {
            List <String> songArr = (List<String>) value.get("songArr");
            String currSongTitle = player.getCurrentMediaItem().mediaMetadata.title.toString();
            if (songArr.contains(currSongTitle)) {
                favouriteBtnPlayer.setImageResource(R.drawable.ic_favourite_filled_red);
                favouriteBtmSheet.setImageResource(R.drawable.ic_favourite_filled_red);
            }
            else {
                favouriteBtnPlayer.setImageResource(R.drawable.ic_favourite_red);
                favouriteBtmSheet.setImageResource(R.drawable.ic_favourite_red);
            }
        });

        favouriteBtnPlayer.setOnClickListener(view -> addToFav(context , songList.get(position).getTitle() , favSongsDoc));

        favouriteBtmSheet.setOnClickListener(view -> addToFav(context , songList.get(position).getTitle() , favSongsDoc));

        playlistAddPlayer.setOnClickListener(view -> {
            AddToPlaylistDialog atpd = new AddToPlaylistDialog(context , songList.get(position).getTitle());
            atpd.showAddToPlaylistDialog(context , songList.get(position).getTitle() , fragmentManager);
        });

        playlistAddBtmSheet.setOnClickListener(view -> {
            AddToPlaylistDialog atpd = new AddToPlaylistDialog(context , songList.get(position).getTitle());
            atpd.showAddToPlaylistDialog(context , songList.get(position).getTitle() , fragmentManager);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}