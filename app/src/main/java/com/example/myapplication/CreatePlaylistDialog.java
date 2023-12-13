package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myapplication.Models.Playlist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CreatePlaylistDialog extends AppCompatDialogFragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private CollectionReference playlistCollection;
    private Context context;

    public CreatePlaylistDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        playlistCollection = db.collection(getString(R.string.user_collection_name)).document(user.getDisplayName())
                .collection(getString(R.string.playlist_collection_name));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_playlist_dialog , null);
        EditText playlistName = view.findViewById(R.id.playlistNameCreatePlaylist);
        EditText playlistDesc = view.findViewById(R.id.playlistDescCreatePlaylist);

        builder.setView(view).setTitle("Create New Playlist")
                .setPositiveButton("Create", (dialog, which) -> {
                    SignUpPage signUpPage = new SignUpPage();
                    Playlist playlist = new Playlist(playlistName.getText().toString() , signUpPage.getCurrentDate() ,
                            playlistDesc.getText().toString() , "" , new ArrayList<>());
                    playlistCollection.document(playlistName.getText().toString()).set(playlist)
                            .addOnSuccessListener(documentReference -> Toast.makeText(context ,
                                    "Playlist added" , Toast.LENGTH_SHORT));
                })
                .setNegativeButton("Cancel", (dialog, which) -> {});
        return builder.create();
    }
}