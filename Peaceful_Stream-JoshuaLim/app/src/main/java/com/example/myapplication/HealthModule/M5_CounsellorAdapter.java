package com.example.myapplication.HealthModule;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class M5_CounsellorAdapter extends RecyclerView.Adapter<M5_CounsellorAdapter.CounsellorViewHolder> {

    Context context;

    ArrayList<M5_Counsellor> list;

    public M5_CounsellorAdapter(Context context, ArrayList<M5_Counsellor> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CounsellorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.m5_item,parent,false);
        return new CounsellorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CounsellorViewHolder holder, int position) {

        M5_Counsellor counsellor = list.get(position);
        holder.name.setText(counsellor.getName());
        holder.experience.setText(counsellor.getWorkingExperience());
        holder.university.setText(counsellor.getUniversity());
        holder.language.setText(counsellor.getLanguage());
        holder.star.setRating(counsellor.getStar());

        // Load the image into the ImageView using Picasso
        Picasso.get()
                .load(counsellor.getImageLink())
                .placeholder(R.drawable.m5_placeholder_image)
                .error(R.drawable.m5_error_image)
                .into(holder.imageView);

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start ChatActivity when the chat button is clicked
                Context context = view.getContext();
                Intent intent = new Intent(context, M5_ChatActivity.class);
                int counsellorId = list.get(position).getID(); // Example data
                intent.putExtra("COUNSELLOR_ID", Integer.toString(counsellorId));
                context.startActivity(intent);
            }
        });

    }

    public void sortListByRating() {
        Collections.sort(list, new Comparator<M5_Counsellor>() {
            @Override
            public int compare(M5_Counsellor c1, M5_Counsellor c2) {
                // Sort in descending order based on the star rating
                return Integer.compare(c2.getStar(), c1.getStar());
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CounsellorViewHolder extends RecyclerView.ViewHolder{

        TextView name, experience, university, language;
        RatingBar star;
        ImageView imageView;
        Button btnChat;
        public CounsellorViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvname);
            experience = itemView.findViewById(R.id.tvexperience);
            university = itemView.findViewById(R.id.tvuniversity);
            language = itemView.findViewById(R.id.tvlanguage);
            star = itemView.findViewById(R.id.ratingBar);
            imageView = itemView.findViewById(R.id.imageViewCounsellor);
            btnChat = itemView.findViewById(R.id.btnChat);
        }
    }



}