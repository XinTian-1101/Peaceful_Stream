package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.Playlist;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.example.myapplication.Models.Song;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DiscoverRecViewAdapter extends RecyclerView.Adapter<DiscoverRecViewAdapter.DiscoverViewHolder> {
    private List <Playlist> playlistList;
    private Context context;
    private RecViewClickListener recViewClickListener;

    public DiscoverRecViewAdapter(List<Playlist> playlistList, Context context, RecViewClickListener recViewClickListener) {
        this.playlistList = playlistList;
        this.context = context;
        this.recViewClickListener = recViewClickListener;
    }

    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discover_playlist_item, parent , false);
        DiscoverViewHolder viewHolder = new DiscoverViewHolder(view , recViewClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position) {
        holder.playlistNameDiscover.setText(playlistList.get(position).getName());
        LinearLayout viewStubLayout = holder.viewStubLayout;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseUtil.getOnePlaylistReference(playlistList.get(position).getName()).get().addOnSuccessListener(documentSnapshot -> {
                    List <String> songArr = (List<String>) documentSnapshot.get("songArr");
                    viewStubLayout.removeAllViews();
                    if (songArr.isEmpty()) {
                        View inflatedView = LayoutInflater.from(context).inflate(R.layout.single_playlist_image , viewStubLayout);
                        ImageView playlistImg = inflatedView.findViewById(R.id.playlistImg);
                        Glide.with(context).asBitmap().load(context.getString(R.string.default_music_icon_image_url)).into(playlistImg);
                    }
                    else if (songArr.size() < 4) {
                        View inflatedView = LayoutInflater.from(context).inflate(R.layout.single_playlist_image , viewStubLayout);
                        FirebaseUtil.getOneSong(songArr.get(0))
                                .get().addOnSuccessListener(documentSnapshot1 -> {
                                    Song songInfo = documentSnapshot1.toObject(Song.class);
                                        ImageView playlistImg = inflatedView.findViewById(R.id.playlistImg);
                                        Glide.with(context).asBitmap().load(songInfo.getImageUrl()).into(playlistImg);
                                });
                    }
                    else {
                        View inflatedView = LayoutInflater.from(context).inflate(R.layout.four_playlist_image , viewStubLayout);
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
                                        Glide.with(context).asBitmap().load(songInfo.getImageUrl()).into(playlistImg);
                                    });
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder {
        TextView playlistNameDiscover;
        LinearLayout viewStubLayout;

        public DiscoverViewHolder(@NonNull View itemView , RecViewClickListener recViewClickListener) {
            super(itemView);
            playlistNameDiscover = itemView.findViewById(R.id.playlistNameDiscover);
            viewStubLayout = itemView.findViewById(R.id.viewStubLayout);

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
