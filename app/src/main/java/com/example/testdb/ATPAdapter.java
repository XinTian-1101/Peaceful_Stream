package com.example.testdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ATPAdapter extends RecyclerView.Adapter<ATPAdapter.MyViewHolder>{
    private Context context;
    private ArrayList<PlaylistModel> playlist;
    private rvInterface rv;

    public ATPAdapter(Context context, ArrayList playlist, rvInterface rv){
        this.context = context;
        this.playlist = playlist;
        this.rv=rv;
    }

    @NonNull
    @Override
    public ATPAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_playlist_card,parent, false);

        return new ATPAdapter.MyViewHolder(view, rv);
    }

    @Override
    public void onBindViewHolder(@NonNull ATPAdapter.MyViewHolder holder, int position) {
        holder.titleTxt.setText(playlist.get(position).getPlaylistTitle());
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titleTxt;

        public MyViewHolder(@NonNull View itemView, rvInterface rv) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.playlist_name_adp);

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
