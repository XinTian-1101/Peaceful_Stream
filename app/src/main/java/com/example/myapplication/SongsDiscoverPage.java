package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.Adapters.DiscoverRecViewAdapter;
import com.example.myapplication.Models.Playlist;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class SongsDiscoverPage extends AppCompatActivity implements RecViewClickListener{
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private CollectionReference playlistCollection;
    private List<Playlist> playlistList;
    private RecyclerView recyclerViewDiscover;
    private SearchView searchDiscover;
    private DiscoverRecViewAdapter adapter;
    private ExtendedFloatingActionButton fabCreatePlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_discover_page);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fabCreatePlaylist = findViewById(R.id.fabCreatePlaylist);
        playlistCollection = db.collection(getString(R.string.user_collection_name)).document(user.getDisplayName())
                .collection(getString(R.string.playlist_collection_name));
        recyclerViewDiscover = findViewById(R.id.discoverRecView);
        searchDiscover = findViewById(R.id.searchDiscover);
        playlistList = new ArrayList<>();

        adapter = new DiscoverRecViewAdapter(playlistList , this , this);
        recyclerViewDiscover.setAdapter(adapter);
        recyclerViewDiscover.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
        PlaylistEventChangeListener(playlistCollection , playlistList , adapter);

        fabCreatePlaylist.setOnClickListener(view -> showCreatePlaylistDialog());

        searchDiscover.clearFocus();
        searchDiscover.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext() , SearchResultsPage.class);
                intent.putExtra("searchText" , query);
                startActivity(intent);
                Log.d("textSubmit" , "Submitted");
//                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    public void PlaylistEventChangeListener (CollectionReference collectionReference , List<Playlist> playlistList , RecyclerView.Adapter adapter) {
        collectionReference.orderBy("name" , Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getApplicationContext() , "Firestore error" , Toast.LENGTH_LONG).show();
                        return;
                    }
                    assert value != null;
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            playlistList.add(dc.getDocument().toObject(Playlist.class));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SongsDiscoverPage.this , SongsMainPage.class);
        intent.putExtra("playlistName" , playlistList.get(position).getName());
        startActivity(intent);
//        finish();
    }

    public void showCreatePlaylistDialog () {
        CreatePlaylistDialog dialog = new CreatePlaylistDialog(this);
        dialog.show(getSupportFragmentManager() ,"Create Playlist");
    }


}
