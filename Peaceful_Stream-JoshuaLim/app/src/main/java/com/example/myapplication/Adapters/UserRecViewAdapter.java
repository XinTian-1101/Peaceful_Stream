package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;

import java.util.List;

public class UserRecViewAdapter extends RecyclerView.Adapter<UserRecViewAdapter.UserViewHolder> {
    private Context context;
    private List<User> userList;
    private RecViewClickListener listener;

    public UserRecViewAdapter(Context context, List<User> userList, RecViewClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_chat_item , parent , false);
        UserViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        AndroidUtil.logMsg("user image username" , user.getUsername());
        FirebaseUtil.setImage(holder.userChatImage , user.getUsername());
        holder.userChatUsername.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView userChatImage;
        TextView userChatUsername;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.userChatLayout);
            userChatImage = itemView.findViewById(R.id.userChatImage);
            userChatUsername = itemView.findViewById(R.id.userChatUsername);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
