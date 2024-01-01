package com.example.completecnc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.completecnc.Item.M2_ChatItem;
import com.example.completecnc.R;
import com.squareup.picasso.Picasso;

public class M2_ChatAdapter extends RecyclerView.Adapter<M2_ChatAdapter.ChatViewHolder>{
    ArrayList<M2_ChatItem> list;
    Context context;

    public M2_ChatAdapter(Context context, ArrayList<M2_ChatItem> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.m2_chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        M2_ChatItem chatItem = list.get(position);
        holder.counsellorName.setText(chatItem.getName());
        holder.counsellorHospital.setText(chatItem.getHospital());

        String imageUri = null;
        imageUri = chatItem.getImage();
        Picasso.get().load(imageUri).into(holder.counsellorImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView counsellorName, counsellorHospital;
        ImageView counsellorImage;

        public ChatViewHolder(@NonNull View itemView){
            super(itemView);
            counsellorName = itemView.findViewById(R.id.TVCounsellor);
            counsellorHospital = itemView.findViewById(R.id.TVHospital);
            counsellorImage = itemView.findViewById(R.id.IVCounsellor);
        }
    }
}


