package com.example.myapplication.WellnessModule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class M1_Adapter extends RecyclerView.Adapter<M1_Adapter.MyViewHolder> {

    private Context context;
    private ArrayList<M1_PlaylistModel> title;
    private M1_rvInterface rv;

    public M1_Adapter(Context context, ArrayList title, M1_rvInterface rv){
        this.context = context;
        this.title = title;
        this.rv=rv;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.m1_playlist_card,parent, false);

        return new MyViewHolder(view, rv);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titleTxt.setText(title.get(position).getPlaylistTitle());
        if(position==0) //liked song in recyclerview
            holder.iconPlaylist.setImageResource(R.drawable.m1_likedsong_ic);
        else if(position==1) //you are the best in recyclerview
            holder.iconPlaylist.setImageResource(R.drawable.m1_urthebest_ic);
        else //new playlist
            holder.iconPlaylist.setImageResource(R.drawable.m1_extra_pl_ic2);
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titleTxt; //playlist title
        ImageView iconPlaylist; //playlist icon

        public MyViewHolder(@NonNull View itemView, M1_rvInterface rv) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.playlist_name);
            iconPlaylist = itemView.findViewById(R.id.playlist_icon);

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
        }
    }
}
