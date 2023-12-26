package com.example.myapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Message;
import com.example.myapplication.R;
import com.example.myapplication.RecViewClickListener;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.FirebaseUtil;

import java.util.List;

public class MessagesRecViewAdapter extends RecyclerView.Adapter<MessagesRecViewAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private Context context;

    public MessagesRecViewAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item , parent , false);
        MessageViewHolder viewHolder = new MessageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(holder.chatItemLayout);
        AndroidUtil.logMsg("sender ID is ", message.getSenderId() + "");
        if (message.getSenderId().equals(FirebaseUtil.currentUserUsername()))
            constraintSet.clear(R.id.chatBubble , ConstraintSet.START);
        else constraintSet.clear(R.id.chatBubble , ConstraintSet.END);
        constraintSet.applyTo(holder.chatItemLayout);
        holder.chatItemMessage.setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout chatItemLayout;
        RelativeLayout chatBubble;
        TextView chatItemMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            chatItemLayout = itemView.findViewById(R.id.chatItemLayout);
            chatBubble = itemView.findViewById(R.id.chatBubble);
            chatItemMessage = itemView.findViewById(R.id.chatItemMessage);
        }
    }
}
