package com.example.myapplication.MusicModule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.PlaylistRecViewAdapter;
import com.example.myapplication.Models.Playlist;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddToPlaylistDialog extends AppCompatDialogFragment implements RecViewClickListener {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private CollectionReference playlistCollection;
    private List<Playlist> playlistList;
    private RecyclerView addToPlaylistRecView;
    private PlaylistRecViewAdapter adapter;
    private String songToAdd;
    private Context context;

    public AddToPlaylistDialog (Context context , String songToAdd) {
        this.songToAdd = songToAdd;
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        playlistList = new ArrayList<>();
        playlistCollection = db.collection(getString(R.string.user_collection_name)).document(user.getDisplayName())
                .collection(getString(R.string.playlist_collection_name));
        playlistCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                Playlist playlist = doc.toObject(Playlist.class);
                playlistList.add(playlist);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_to_playlist_dialog , null);
        adapter = new PlaylistRecViewAdapter(context , playlistList , this);
        addToPlaylistRecView = view.findViewById(R.id.addToPlaylistRecView);
        addToPlaylistRecView.setLayoutManager(new LinearLayoutManager(context));
        addToPlaylistRecView.setAdapter(adapter);

        SongsDiscoverPage sdp = new SongsDiscoverPage();
        sdp.PlaylistEventChangeListener(db.collection(getString(R.string.user_collection_name))
                        .document(user.getDisplayName()).collection(getString(R.string.playlist_collection_name)),
                playlistList , adapter);

        builder.setView(view).setTitle("Add To Playlist")
                .setPositiveButton("Create New Playlist", (dialog, which) -> {
                    CreatePlaylistDialog createPlaylistDialog = new CreatePlaylistDialog(context);
                    createPlaylistDialog.show(getActivity().getSupportFragmentManager() ,"Create Playlist");
                })
                .setNegativeButton("Cancel", (dialog, which) -> {});
        return builder.create();
    }

    @Override
    public void onItemClick(int position) {
        playlistCollection.document(playlistList.get(position).getName())
                .get().addOnSuccessListener(documentSnapshot -> {
                    List <String> songArr = (List<String>) documentSnapshot.get("songArr");
                    if (!songArr.contains(songToAdd)) {
                        songArr.add(songToAdd);
                        playlistCollection.document(playlistList.get(position).getName())
                                .update("songArr" , songArr);
                        Toast.makeText(context, "Song added to playlist!", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(context, "Song already exists in selected playlist", Toast.LENGTH_SHORT).show();
                });
        getDialog().dismiss();
    }

    public void showAddToPlaylistDialog (Context context , String songTitle , FragmentManager fragmentManager) {
        AddToPlaylistDialog dialog = new AddToPlaylistDialog(context , songTitle);
        dialog.show(fragmentManager , "Add To Playlist");
    }
}
