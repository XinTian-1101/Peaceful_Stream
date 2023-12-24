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

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<VideolistModel> videoModel;

    MyDatabaseHelper mdh;

    private rvInterface rv;

    public VideoAdapter(Context context, ArrayList<VideolistModel> videoModel, rvInterface rv) {
        this.context = context;
        this.videoModel = videoModel;
        this.rv = rv;
    }

    @NonNull
    @Override
    public VideoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_list,parent, false);

        return new VideoAdapter.MyViewHolder(view, rv);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.MyViewHolder holder, int position) {
        mdh = new MyDatabaseHelper(context);

        holder.videoTV.setText(videoModel.get(position).getVideoName());
        holder.artistTV.setText(videoModel.get(position).getArtistName());

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

        TextView videoTV, artistTV;
//        ImageButton likeBtn, adpBtn;

        public MyViewHolder(@NonNull View itemView, rvInterface rv) {
            super(itemView);

//            MyDatabaseHelper mdh = new MyDatabaseHelper(context);

            videoTV = itemView.findViewById(R.id.video_name);
            artistTV = itemView.findViewById(R.id.artist_name);
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
