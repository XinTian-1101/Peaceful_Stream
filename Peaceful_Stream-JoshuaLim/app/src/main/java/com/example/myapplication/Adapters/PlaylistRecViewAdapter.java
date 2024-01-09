package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.Playlist;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.example.myapplication.Utils.AndroidUtil;

import java.util.List;

public class PlaylistRecViewAdapter extends RecyclerView.Adapter<PlaylistRecViewAdapter.PlaylistViewHolder> {
    private Context context;
    private List<Playlist> playlistList;
    private RecViewClickListener recViewClickListener;

    public PlaylistRecViewAdapter(Context context, List<Playlist> playlistList, RecViewClickListener recViewClickListener) {
        this.context = context;
        this.playlistList = playlistList;
        this.recViewClickListener = recViewClickListener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_item , parent , false);
        PlaylistViewHolder holder = new PlaylistViewHolder(view , recViewClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.playlistNameAdd.setText(playlistList.get(position).getName());
        AndroidUtil.logMsg("playlist Image" , playlistList.get(position).getImageUrl());
        Glide.with(context).asBitmap().load(playlistList.get(position).getImageUrl()).into(holder.playlistImageAdd);
    }
    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView playlistNameAdd;
        ImageView playlistImageAdd;
        public PlaylistViewHolder(@NonNull View itemView , RecViewClickListener recViewClickListener) {
            super(itemView);
            playlistNameAdd = itemView.findViewById(R.id.playlistNameAdd);
            playlistImageAdd = itemView.findViewById(R.id.playlistImageAdd);

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
