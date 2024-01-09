package com.example.myapplication.MusicModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.Adapters.SongsRecViewAdapter;
import com.example.myapplication.Models.Song;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.EventListeners;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage extends AppCompatActivity implements RecViewClickListener {
    private SongsMainPage songsMainPage;
    private FirebaseFirestore db;
    private DocumentReference favSongsDoc;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ExoPlayer player;
    private RecyclerView searchResultsRecView;
    private View customPlayerSearchResultsPage;
    private Toolbar toolbar;
    private List<Song> searchSongList;
    private SongsRecViewAdapter adapter;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_page);
        songsMainPage = new SongsMainPage();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
                searchSongList = new ArrayList<>();
        searchText = getIntent().getExtras().getString("searchText");
        favSongsDoc = FirebaseUtil.getFavSongReference();
        player = new ExoPlayer.Builder(this).build();
        customPlayerSearchResultsPage = findViewById(R.id.customPlayerSearchResultsPage);
        toolbar = findViewById(R.id.searchResultsToolbar);
        AndroidUtil.setToolbar(this , toolbar);
        adapter = new SongsRecViewAdapter(this , getSupportFragmentManager() , searchSongList , false , null ,this);
        searchResultsRecView = findViewById(R.id.searchResultsRecView);
        searchResultsRecView.setAdapter(adapter);
        searchResultsRecView.setLayoutManager(new LinearLayoutManager(this));
        EventListeners.SearchEventChangeListener(searchSongList , adapter , searchText);
        songsMainPage.FavouriteSongsEventChangeListener(favSongsDoc , adapter);
    }

    @Override
    public void onItemClick(int position) {
        songsMainPage.initiateCustomPlayerAndBottomSheet(SearchResultsPage.this ,
                customPlayerSearchResultsPage , position , searchSongList , player ,
                db.collection(getString(R.string.user_collection_name)).document(user.getDisplayName())
                .collection(getString(R.string.playlist_collection_name))
                .document(getString(R.string.fav_songs_document_name)) , adapter , getSupportFragmentManager());
    }


}