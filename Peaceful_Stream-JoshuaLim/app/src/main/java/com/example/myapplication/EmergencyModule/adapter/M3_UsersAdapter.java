package com.example.myapplication.EmergencyModule.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.M3ItemContainerUserBinding;
import com.example.myapplication.EmergencyModule.listeners.M3_UserListener;
import com.example.myapplication.EmergencyModule.model.M3_User;

import java.util.List;

public class M3_UsersAdapter extends RecyclerView.Adapter<M3_UsersAdapter.UserViewHolder>{

    private final List<M3_User> users;
    private final M3_UserListener m3UserListener;

    public M3_UsersAdapter(List<M3_User> users, M3_UserListener m3UserListener){
        this.m3UserListener = m3UserListener;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        M3ItemContainerUserBinding itemContainerUserBinding = M3ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));

    }

    @Override
    public int getItemCount() {

        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        M3ItemContainerUserBinding binding;

        UserViewHolder(M3ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;
        }

        void setUserData(M3_User user){
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> m3UserListener.onUserClicked(user));
        }
    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
