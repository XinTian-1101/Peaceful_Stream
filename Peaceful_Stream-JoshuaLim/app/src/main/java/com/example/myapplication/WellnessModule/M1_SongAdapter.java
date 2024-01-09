package com.example.myapplication.WellnessModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class M1_SongAdapter extends RecyclerView.Adapter<M1_SongAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<M1_SonglistModel> songModel;

    M1_DatabaseHelper mdh;

    private M1_saInterface rv;

    public M1_SongAdapter(Context context, ArrayList<M1_SonglistModel> songModel, M1_saInterface rv) {
        this.context = context;
        this.songModel = songModel;
        this.rv = rv;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.song_list,parent, false);

        return new MyViewHolder(view, rv);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mdh = new M1_DatabaseHelper(context);

        holder.songTV.setText(songModel.get(position).getSongName());
        holder.artistTV.setText(songModel.get(position).getArtistName());
        holder.songPos.setText(Integer.toString(position+1));

        if(mdh.checkLikedSong(songModel.get(position).getSongName())){
            holder.likeBtn.setImageResource(R.drawable.m1_ic_like_on);
        }
        else{
            holder.likeBtn.setImageResource(R.drawable.m1_ic_like_off);
        }
    }

    @Override
    public int getItemCount() {
        return songModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView songTV, artistTV, songPos;
        ImageButton likeBtn, adpBtn;


        public MyViewHolder(@NonNull View itemView, M1_saInterface rv) {
            super(itemView);

            M1_DatabaseHelper mdh = new M1_DatabaseHelper(context);

            songTV = itemView.findViewById(R.id.song_name);
            artistTV = itemView.findViewById(R.id.artist_name);
            likeBtn = itemView.findViewById(R.id.likeOffButton);
            adpBtn = itemView.findViewById(R.id.adpButton);
            songPos = itemView.findViewById(R.id.songPosition);

//            if(mdh.checkLikedSong(itemView.findViewById(R.id.song_name).toString())){
//                likeBtn.setImageResource(R.drawable.ic_playlist);
//            }
//            else{
//                likeBtn.setImageResource(R.drawable.ic_like_on);
//            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rv!=null){
                        int pos = getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){

                            rv.onItemClick(pos);
                        }
                    }
                }
            });

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rv!=null){
                        int pos = getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){

                            if(mdh.checkLikedSong(songModel.get(pos).getSongName())){
                                likeBtn.setImageResource(R.drawable.m1_ic_like_off);
//                                mdh.removeFromLiked(songModel.get(pos).getSongName(), songModel.get(pos).getArtistName(), songModel.get(pos).getUrl());
                            }
                            else{
                                likeBtn.setImageResource(R.drawable.m1_ic_like_on);
//                                mdh.addIntoLiked(songModel.get(pos).getSongName(), songModel.get(pos).getArtistName(), songModel.get(pos).getUrl());
                            }

                            rv.onLikeButtonClick(pos);
                        }
                    }
                }
            });

            adpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rv!=null){
                        int pos = getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){

                            rv.onAdpButtonClick(pos);
                        }
                    }
                }
            });
        }
    }
}
