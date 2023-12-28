package com.example.completecnc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.completecnc.Item.PostItem;
import com.example.completecnc.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ImageViewHolder> {
    Context context;
    List<PostItem> list;

    public PostAdapter(Context context, List<PostItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        PostItem postItem = list.get(position);
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