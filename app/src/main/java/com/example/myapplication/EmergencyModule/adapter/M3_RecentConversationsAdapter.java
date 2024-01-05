package com.example.myapplication.EmergencyModule.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.M3ItemContainerRecentConversionBinding;
import com.example.myapplication.EmergencyModule.listeners.M3_ConversionListener;
import com.example.myapplication.EmergencyModule.model.M3_ChatMessage;
import com.example.myapplication.EmergencyModule.model.M3_User;

import java.util.List;

public class M3_RecentConversationsAdapter extends RecyclerView.Adapter<M3_RecentConversationsAdapter.ConversionViewHolder> {

    private final List<M3_ChatMessage> m3ChatMessage;
    private final M3_ConversionListener m3ConversionListener;

    public M3_RecentConversationsAdapter(List<M3_ChatMessage> m3ChatMessage, M3_ConversionListener m3ConversionListener) {
        this.m3ChatMessage = m3ChatMessage;
        this.m3ConversionListener = m3ConversionListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                M3ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(m3ChatMessage.get(position));
    }

    @Override
    public int getItemCount() {
        return m3ChatMessage.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder{
        M3ItemContainerRecentConversionBinding binding;

        ConversionViewHolder(M3ItemContainerRecentConversionBinding itemContainerRecentConversionBinding) {
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;
        }

        void setData(M3_ChatMessage m3ChatMessage){
            binding.imageProfile.setImageBitmap(getConversionImage(m3ChatMessage.conversionImage));
            binding.textName.setText(m3ChatMessage.conversionName);
            binding.textRecentMessage.setText(m3ChatMessage.message);
            binding.getRoot().setOnClickListener(v -> {
                M3_User user = new M3_User();
                user.id = m3ChatMessage.conversionId;
                user.name = m3ChatMessage.conversionName;
                user.image = m3ChatMessage.conversionImage;
                m3ConversionListener.onConversionClicked(user);
            });
        }
    }
    private Bitmap getConversionImage(String encodedImage){
        byte[]bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
