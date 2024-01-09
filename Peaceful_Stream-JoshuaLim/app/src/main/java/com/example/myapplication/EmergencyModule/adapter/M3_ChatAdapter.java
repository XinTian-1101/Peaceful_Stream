package com.example.myapplication.EmergencyModule.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.M3ItemContainerReceiveMessageBinding;
import com.example.myapplication.databinding.M3ItemContainerSentMessageBinding;
import com.example.myapplication.EmergencyModule.model.M3_ChatMessage;

import java.util.List;

public class M3_ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<M3_ChatMessage> m3ChatMessages;
    private Bitmap receiverProfileImage;
    private final String senderId;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public void setReceiverProfileImage(Bitmap bitmap){
        receiverProfileImage = bitmap;
    }


    public M3_ChatAdapter(List<M3_ChatMessage> m3ChatMessages, Bitmap receiverProfileImage, String senderId) {
        this.m3ChatMessages = m3ChatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT)
        {
            return new SentMessageViewHolder(
                    M3ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
        else{
            return new ReceivedMessageViewHolder(
                    M3ItemContainerReceiveMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(m3ChatMessages.get(position));
        }else{
            ((ReceivedMessageViewHolder) holder).setData(m3ChatMessages.get(position),receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return m3ChatMessages.size();
    }

    public int getItemViewType(int position){
        if(m3ChatMessages.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }else{
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{

        private final M3ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(M3ItemContainerSentMessageBinding itemContainerSentMessageBinding){
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(M3_ChatMessage m3ChatMessage){
            binding.textMessage.setText(m3ChatMessage.message);
            binding.textDateTime.setText(m3ChatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

        private final M3ItemContainerReceiveMessageBinding binding;

        ReceivedMessageViewHolder(M3ItemContainerReceiveMessageBinding itemContainerReceiveMessageBinding){
            super(itemContainerReceiveMessageBinding.getRoot());
            binding = itemContainerReceiveMessageBinding;
        }

        void setData(M3_ChatMessage m3ChatMessage, Bitmap receiverProfileImage){
            binding.textMessage.setText(m3ChatMessage.message);
            binding.textDateTime.setText(m3ChatMessage.dateTime);
            if(receiverProfileImage != null){
                binding.imageProfile.setImageBitmap(receiverProfileImage);
            }

        }
    }
}
