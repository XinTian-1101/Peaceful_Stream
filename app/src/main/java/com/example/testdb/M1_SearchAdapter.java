package com.example.testdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class M1_SearchAdapter extends RecyclerView.Adapter<M1_SearchAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<M1_SonglistModel> filteredSong;
    private ArrayList<M1_SonglistModel> original_data;
    M1_DatabaseHelper mdh;
    private M1_saInterface rv;

    public M1_SearchAdapter(Context context, M1_saInterface rv, ArrayList<M1_SonglistModel> original_data) {
        this.context = context;
        this.original_data = original_data;
        this.filteredSong = new ArrayList<>();
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

        holder.songTV.setText(filteredSong.get(position).getSongName());
        holder.artistTV.setText(filteredSong.get(position).getArtistName());
        holder.songPos.setText(Integer.toString(position+1));

        if(mdh.checkLikedSong(filteredSong.get(position).getSongName())){
            holder.likeBtn.setImageResource(R.drawable.m1_ic_like_on);
        }
        else{
            holder.likeBtn.setImageResource(R.drawable.m1_ic_like_off);
        }
    }

    @Override
    public int getItemCount() {
        return filteredSong.size();
    }

    public void filterList(ArrayList<M1_SonglistModel> filteredList){
        filteredSong = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView songTV, artistTV, songPos;
        ImageButton likeBtn, adpBtn;

        public MyViewHolder(@NonNull View itemView, M1_saInterface rv) {
            super(itemView);

            M1_DatabaseHelper mdh = new M1_DatabaseHelper(context);

            songTV = itemView.findViewById(R.id.song_name);
            artistTV = itemView.findViewById(R.id.artist_name);
            songPos = itemView.findViewById(R.id.songPosition);
            likeBtn = itemView.findViewById(R.id.likeOffButton);
            adpBtn = itemView.findViewById(R.id.adpButton);

            if(mdh.checkLikedSong(itemView.findViewById(R.id.song_name).toString())){
                likeBtn.setImageResource(R.drawable.m1_ic_like_on);
            }
            else{
                likeBtn.setImageResource(R.drawable.m1_ic_like_off);
            }

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

                            if(mdh.checkLikedSong(filteredSong.get(pos).getSongName())){
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

    public void filter(String query) {
        filteredSong.clear();
        if (query.isEmpty()) {
            for(int i = 0; i<original_data.size();i++){
                String title = original_data.get(i).getSongName();
                String artist = original_data.get(i).getArtistName();
                int url = original_data.get(i).getUrl();
                filteredSong.add(new M1_SonglistModel(title,artist,url));
            }
        } else {
//            for (SonglistModel item : original_data) {
//                if (item.getSongName().toLowerCase().contains(query.toLowerCase())) {
//                    filteredSong.add(item);
//                }
//            }
            for(int i = 0; i<original_data.size();i++){
                String title = original_data.get(i).getSongName();
                String artist = original_data.get(i).getArtistName();
                int url = original_data.get(i).getUrl();
                String compare = query.toLowerCase();
                if(title.toLowerCase().contains(compare)){
                    filteredSong.add(new M1_SonglistModel(title,artist,url));
                }
            }
        }
        notifyDataSetChanged();
    }
}
