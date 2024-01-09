package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Comment;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FirebaseUtil;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentRecViewAdapter extends RecyclerView.Adapter<CommentRecViewAdapter.CommentViewHolder> {
    private Context context;
    private List<Comment> commentList;

    public CommentRecViewAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item , null , false);
        CommentViewHolder viewHolder = new CommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        FirebaseUtil.setImage(holder.commentUserImage , comment.getUserId());
        holder.commentUserName.setText(comment.getUserId());
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm a");
        String dateTime = format.format(comment.getTimestamp().toDate());
        holder.commentTime.setText(dateTime);
        holder.commentContent.setText(comment.getContent().toString());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView commentUserImage;
        private TextView commentTime , commentContent , commentUserName;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentUserName = itemView.findViewById(R.id.commentUserName);
            commentUserImage = itemView.findViewById(R.id.commentUserImage);
            commentTime = itemView.findViewById(R.id.commentTime);
            commentContent = itemView.findViewById(R.id.commentContent);
        }
    }
}
