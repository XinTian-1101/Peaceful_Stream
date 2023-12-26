package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MusicModule.AddToPlaylistDialog;
import com.example.myapplication.Models.Playlist;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.example.myapplication.Models.Song;
import com.example.myapplication.MusicModule.SongsMainPage;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SongsRecViewAdapter extends RecyclerView.Adapter<SongsRecViewAdapter.SongsViewHolder>{
    private Context context;
    private List<Song> songList;
    private RecViewClickListener recViewClickListener;
    private FragmentManager fragmentManager;
    private boolean songInPlaylist;
    private String playlistName;

    public SongsRecViewAdapter(Context context, FragmentManager fragmentManager , List<Song> songList , boolean songInPlaylist, String playListName , RecViewClickListener recViewClickListener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.songList = songList;
        this.songInPlaylist = songInPlaylist;
        this.playlistName = playListName;
        this.recViewClickListener = recViewClickListener;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item , parent , false);
        SongsViewHolder viewHolder = new SongsViewHolder(view , recViewClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        String songTitle = songList.get(position).getTitle();
        holder.number.setText(String.valueOf(position + 1));
        holder.title.setText(songTitle);
        holder.artist.setText(songList.get(position).getArtist());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference favSongsDoc = FirebaseUtil.getFavSongReference();
        favSongsDoc.get().addOnSuccessListener(documentSnapshot -> {
            List<String> songArr = documentSnapshot.toObject(Playlist.class).getSongArr();
            if (songArr.contains(songTitle)) holder.favouriteBtn.setImageResource(R.drawable.ic_favourite_filled_red);
            else holder.favouriteBtn.setImageResource(R.drawable.ic_favourite_red);
        });
        favSongsDoc.addSnapshotListener((value, error) -> {
            List <String> songArr = (List<String>) value.get("songArr");
            if (songArr.contains(songTitle))
                holder.favouriteBtn.setImageResource(R.drawable.ic_favourite_filled_red);
            else holder.favouriteBtn.setImageResource(R.drawable.ic_favourite_red);
        });
        holder.playlistAdd.setOnClickListener(view -> {
            AddToPlaylistDialog atpd = new AddToPlaylistDialog(context , songTitle);
            atpd.showAddToPlaylistDialog(context , songTitle , fragmentManager);
        });
        holder.favouriteBtn.setOnClickListener(view -> {
            SongsMainPage smp = new SongsMainPage();
            smp.addToFav(context , songTitle , favSongsDoc);
        });
        if (songInPlaylist) {
            holder.removeFromPlaylist.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.playlistAdd.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.LEFT_OF , R.id.removeFromPlaylist);
            holder.playlistAdd.setLayoutParams(params);
        }
        if (playlistName != null) {
            DocumentReference playlist = db.collection(context.getString(R.string.user_collection_name))
                    .document(user.getDisplayName()).collection(context.getString(R.string.playlist_collection_name))
                    .document(playlistName);
            holder.removeFromPlaylist.setOnClickListener(view -> {
                playlist.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> songArr = (List<String>) documentSnapshot.get("songArr");
                        songArr.remove(songTitle);
                        playlist.update("songArr" , songArr);
                        Toast.makeText(context, "Song removed from playlist", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongsViewHolder extends RecyclerView.ViewHolder {
        TextView title , artist , number;
        ImageButton playlistAdd, favouriteBtn , removeFromPlaylist;

        public SongsViewHolder(@NonNull View itemView , RecViewClickListener recViewClickListener) {
            super(itemView);
            number = itemView.findViewById(R.id.numbering);
            title = itemView.findViewById(R.id.title);
            artist = itemView.findViewById(R.id.artist);
            playlistAdd = itemView.findViewById(R.id.playlistAdd);
            favouriteBtn = itemView.findViewById(R.id.favouriteBtn);
            removeFromPlaylist = itemView.findViewById(R.id.removeFromPlaylist);

            itemView.setOnClickListener(view -> {
                if (recViewClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recViewClickListener.onItemClick(position);
                    }
                }
            });
        }
    }
}

