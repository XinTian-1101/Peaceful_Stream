package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Adapters.SongsRecViewAdapter;
import com.example.myapplication.Models.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage extends AppCompatActivity implements RecViewClickListener{
    private SongsMainPage songsMainPage;
    private FirebaseFirestore db;
    private CollectionReference songsCollection;
    private DocumentReference favSongsDoc;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ExoPlayer player;
    private RecyclerView searchResultsRecView;
    private View customPlayerSearchResultsPage;
    private List<Song> searchSongList;
    private SongsRecViewAdapter adapter;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results_page);
        songsMainPage = new SongsMainPage();
        db = FirebaseFirestore.getInstance();
        songsCollection = db.collection(getString(R.string.songs_collection_name));
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
                searchSongList = new ArrayList<>();
        searchText = getIntent().getExtras().getString("searchText");
        favSongsDoc = db.collection(getString(R.string.user_collection_name)).document(user.getDisplayName())
                .collection(getString(R.string.playlist_collection_name)).document(getString(R.string.fav_songs_document_name));
        player = new ExoPlayer.Builder(this).build();
        customPlayerSearchResultsPage = findViewById(R.id.customPlayerSearchResultsPage);
        adapter = new SongsRecViewAdapter(this , getSupportFragmentManager() , searchSongList , false , null ,this);
        searchResultsRecView = findViewById(R.id.searchResultsRecView);
        searchResultsRecView.setAdapter(adapter);
        searchResultsRecView.setLayoutManager(new LinearLayoutManager(this));
        SearchEventChangeListener(songsCollection , searchSongList , adapter , searchText);
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

    public void SearchEventChangeListener (CollectionReference songsCollection , List<Song> songList , SongsRecViewAdapter adapter , String searchText) {
        songsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                if (doc.getString("title").contains(searchText)
                        || doc.getString("artist").contains(searchText)) {
                    songList.add(doc.toObject(Song.class));
                    Log.d("SearchEvent" , "Fired");
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}