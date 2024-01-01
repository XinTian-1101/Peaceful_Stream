package com.example.testdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class M1_VideoAdapter extends RecyclerView.Adapter<M1_VideoAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<M1_VideolistModel> videoModel;

    M1_DatabaseHelper mdh;

    private M1_rvInterface rv;

    public M1_VideoAdapter(Context context, ArrayList<M1_VideolistModel> videoModel, M1_rvInterface rv) {
        this.context = context;
        this.videoModel = videoModel;
        this.rv = rv;
    }

    @NonNull
    @Override
    public M1_VideoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.m1_video_list,parent, false);

        return new M1_VideoAdapter.MyViewHolder(view, rv);
    }

    @Override
    public void onBindViewHolder(@NonNull M1_VideoAdapter.MyViewHolder holder, int position) {
        mdh = new M1_DatabaseHelper(context);

        holder.videoTV.setText(videoModel.get(position).getVideoName());
        holder.artistTV.setText(videoModel.get(position).getArtistName());
        holder.videoPosition.setText(Integer.toString(position+1));

//        if(mdh.checkLikedSong(videoModel.get(position).getVideoName())){
//            holder.likeBtn.setImageResource(R.drawable.ic_like_off);
//        }
//        else{
//            holder.likeBtn.setImageResource(R.drawable.ic_like_on);
//        }
    }

    @Override
    public int getItemCount() {
        return videoModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView videoTV, artistTV, videoPosition;
//        ImageButton likeBtn, adpBtn;

        public MyViewHolder(@NonNull View itemView, M1_rvInterface rv) {
            super(itemView);

//            MyDatabaseHelper mdh = new MyDatabaseHelper(context);

            videoTV = itemView.findViewById(R.id.video_name);
            artistTV = itemView.findViewById(R.id.artist_name);
            videoPosition = itemView.findViewById(R.id.vidPosition);
//            likeBtn = itemView.findViewById(R.id.likeOffButton);
//            adpBtn = itemView.findViewById(R.id.adpButton);

//            if(mdh.checkLikedSong(itemView.findViewById(R.id.song_name).toString())){
//                likeBtn.setImageResource(R.drawable.ic_like_off);
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

//            likeBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(rv!=null){
//                        int pos = getAdapterPosition();
//
//                        if(pos!=RecyclerView.NO_POSITION){
//
//                            if(mdh.checkLikedSong(songModel.get(pos).getSongName())){
//                                likeBtn.setImageResource(R.drawable.ic_like_off);
////                                mdh.removeFromLiked(songModel.get(pos).getSongName(), songModel.get(pos).getArtistName(), songModel.get(pos).getUrl());
//                            }
//                            else{
//                                likeBtn.setImageResource(R.drawable.ic_like_on);
////                                mdh.addIntoLiked(songModel.get(pos).getSongName(), songModel.get(pos).getArtistName(), songModel.get(pos).getUrl());
//                            }
//
//                            rv.onLikeButtonClick(pos);
//                        }
//                    }
//                }
//            });
//
//            adpBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(rv!=null){
//                        int pos = getAdapterPosition();
//
//                        if(pos!=RecyclerView.NO_POSITION){
//
//                            rv.onAdpButtonClick(pos);
//                        }
//                    }
//                }
//            });
        }
    }
}
