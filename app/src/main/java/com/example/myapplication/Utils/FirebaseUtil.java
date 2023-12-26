package com.example.myapplication.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.CounsellorLandingPage;
import com.example.myapplication.UserLandingPage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FirebaseUtil {
    public static FirebaseUser currentUser () {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean isLoggedIn () {
        return currentUser() != null;
    }

    public static boolean isCounsellor () {
        final boolean[] isCounsellor = {false};
        if (isLoggedIn()){
            currentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    isCounsellor[0] = task.getResult().getBoolean("counsellor");
                }
            });
        }
        return isCounsellor[0];
    }

    public static String currentUserUsername () {
        return currentUser().getDisplayName();
    }
    public static DocumentReference currentUserDetails () {
        return FirebaseFirestore.getInstance().collection("Users").document(currentUserUsername());
    }
    public static CollectionReference getUserReference () {
        return FirebaseFirestore.getInstance().collection("Users");
    }
    public static DocumentReference getOneUserReference (String userId) {
        return FirebaseFirestore.getInstance().collection("Users").document(userId);
    }
    public static void userAuth (Context context , AppCompatActivity activity) {
        if (isLoggedIn()){
            currentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean isCounsellor = task.getResult().getBoolean("counsellor");
                    AndroidUtil.logMsg("isCounsellor" , "User is " + isCounsellor);
                    if (isCounsellor) AndroidUtil.intentChg(context , CounsellorLandingPage.class);
                    else AndroidUtil.intentChg(context , UserLandingPage.class);
                    activity.finish();
                }
            });
        }
    }
    public static void setCurrentUserImage (ImageView image) {
        setImage(image , currentUserUsername());
    }

    public static void setImage (ImageView image , String fileName) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + fileName);

        try {
            File localFile = File.createTempFile("tempFile" , ".jpeg");
            storageReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                image.setImageBitmap(bitmap);
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CollectionReference getSongReference () {
        return FirebaseFirestore.getInstance().collection("Songs");
    }

    public static DocumentReference getOneSong (String songName) {
        return getSongReference().document(songName);
    }

    public static CollectionReference getPlaylistReference () {
        return currentUserDetails().collection("Playlists");
    }

    public static DocumentReference getOnePlaylistReference (String playlistName) {
        return getPlaylistReference().document(playlistName);
    }

    public static DocumentReference getFavSongReference () {
        return getPlaylistReference().document("Favourite Songs");
    }

    public static DocumentReference getChatroomReference (String chatroomId) {
        return FirebaseFirestore.getInstance().collection("Chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference (String chatroomId) {
        return getChatroomReference(chatroomId).collection("Chats");
    }

    public static CollectionReference getPostReference () {
        return FirebaseFirestore.getInstance().collection("Posts");
    }

    public static DocumentReference getOnePostReference (String postId) {
        return getPostReference().document(postId);
    }

    public static CollectionReference getCommentReference (String postId) {
        return  getOnePostReference(postId).collection("Comments");
    }

    public static String getChatroomId (String userId1 , String userId2) {
        if (userId1.hashCode() < userId2.hashCode()) return userId1 + "_" + userId2;
        else return userId2 + "_" + userId1;
    }
}
