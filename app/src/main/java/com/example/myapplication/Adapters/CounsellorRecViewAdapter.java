package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.Counsellor;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;

import java.util.List;

public class CounsellorRecViewAdapter extends RecyclerView.Adapter<CounsellorRecViewAdapter.CounsellorViewHolder> {
    private List<Counsellor> counsellorList;
    private Context context;
    private RecViewClickListener recViewClickListener;

    public CounsellorRecViewAdapter(List<Counsellor> counsellorList, Context context, RecViewClickListener recViewClickListener) {
        this.counsellorList = counsellorList;
        this.context = context;
        this.recViewClickListener = recViewClickListener;
    }

    @NonNull
    @Override
    public CounsellorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.counsellor_item , null , false);
        CounsellorViewHolder viewHolder = new CounsellorViewHolder(view , recViewClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CounsellorViewHolder holder, int position) {
        Counsellor counsellor = counsellorList.get(position);
        holder.counsellorName.setText(counsellor.getName());
        holder.counsellorPosition.setText(counsellor.getPosition());
        Glide.with(context).asBitmap().load(counsellor.getImageUrl()).into(holder.counsellorImg);
    }

    @Override
    public int getItemCount() {
        return counsellorList.size();
    }

    public class CounsellorViewHolder extends RecyclerView.ViewHolder {
        ImageView counsellorImg;
        TextView counsellorName , counsellorPosition;
        ImageButton counsellorFav;

        public CounsellorViewHolder(@NonNull View itemView , RecViewClickListener recViewClickListener) {
            super(itemView);
            counsellorImg = itemView.findViewById(R.id.counsellorImg);
            counsellorName = itemView.findViewById(R.id.counsellorName);
            counsellorPosition = itemView.findViewById(R.id.counsellorPosition);
            counsellorFav = itemView.findViewById(R.id.counsellorFav);

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
