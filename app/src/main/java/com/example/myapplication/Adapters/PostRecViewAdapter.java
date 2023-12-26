package com.example.myapplication.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Comment;
import com.example.myapplication.Models.Post;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.EventListeners;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostRecViewAdapter extends RecyclerView.Adapter<PostRecViewAdapter.PostViewHolder> {
    private Context context;
    private List<Post> postList;

    public PostRecViewAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item , parent , false);
        PostViewHolder viewHolder = new PostViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.postUserName.setText(post.getUserId());
        holder.postTime.setText(post.getTimestamp().toDate().toString());
        holder.postContent.setText(post.getContent());
        FirebaseUtil.setImage(holder.postUserImage , post.getUserId());
        holder.postImage.setVisibility(View.VISIBLE);

        holder.commentTyping.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String userId = FirebaseUtil.currentUserUsername();
                String content = holder.commentTyping.getText().toString();
                Timestamp timestamp = Timestamp.now();
                String commentId = userId + "_" + timestamp;
                // Perform action on key press
                FirebaseUtil.getCommentReference(post.getPostId()).document(post.getPostId()).set(new Comment(commentId ,
                        userId , content , timestamp)).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                AndroidUtil.showToast(context , "Commented");
                                holder.commentTyping.setText("");
                            }
                        });
                return true;
            }
            return false;
        });


        List<Comment> commentList = new ArrayList<>();
        FirebaseUtil.setImage(holder.postImage , post.getPostId());
        CommentRecViewAdapter adapter = new CommentRecViewAdapter(context , commentList);
        holder.commentRecView.setAdapter(adapter);
        holder.commentRecView.setLayoutManager(new LinearLayoutManager(context));

        EventListeners.CommentEventChangeListener(post.getPostId() , commentList , adapter);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView postUserImage , postImage;
        private EditText commentTyping;
        private ImageButton commentAttachment;
        private TextView postUserName , postTime , postContent;
        private RecyclerView commentRecView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postUserImage = itemView.findViewById(R.id.postUserImage);
            postImage = itemView.findViewById(R.id.postImage);
            postUserName = itemView.findViewById(R.id.postUserName);
            postTime = itemView.findViewById(R.id.postTime);
            postContent = itemView.findViewById(R.id.postContent);
            commentTyping = itemView.findViewById(R.id.commentTyping);
            commentRecView = itemView.findViewById(R.id.commentRecView);
            commentAttachment = itemView.findViewById(R.id.commentAttachment);
        }
    }
}
