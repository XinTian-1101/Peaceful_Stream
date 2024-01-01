package com.example.completecnc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.completecnc.Item.M1_PostItem;
import com.example.completecnc.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class M1_PostAdapter extends RecyclerView.Adapter<M1_PostAdapter.ImageViewHolder> {
    Context context;
    List<M1_PostItem> list;

    public M1_PostAdapter(Context context, List<M1_PostItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.m1_post_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        M1_PostItem postItem = list.get(position);
        holder.postDesc.setText(postItem.getName());
//        Picasso.get()
//                .load(postItem.getImageUrl())
//                .fit()
//                .centerCrop()
//                .into(holder.postImage);
        String imageUri = null;
        imageUri = postItem.getImageUrl();
        Picasso.get().load(imageUri).into(holder.postImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView postDesc;
        public ImageView postImage;

        public ImageViewHolder(View itemView) {
            super(itemView);

            postDesc = itemView.findViewById(R.id.TVDesc);
            postImage = itemView.findViewById(R.id.IVPost);
        }
    }
}