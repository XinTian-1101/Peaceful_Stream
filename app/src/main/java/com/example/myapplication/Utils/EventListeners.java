package com.example.myapplication.Utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.SongsRecViewAdapter;
import com.example.myapplication.Models.Comment;
import com.example.myapplication.Models.Counsellor;
import com.example.myapplication.Models.Message;
import com.example.myapplication.Models.Post;
import com.example.myapplication.Models.Song;
import com.example.myapplication.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class EventListeners {

    public static void SearchEventChangeListener (List<Song> songList , SongsRecViewAdapter adapter , String searchText) {
        FirebaseUtil.getSongReference().get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                if (doc.getString("title").contains(searchText)
                        || doc.getString("artist").contains(searchText)) {
                    songList.add(doc.toObject(Song.class));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    public static void CounsellorChatEventChangeListener (List <Counsellor> counsellorList , RecyclerView.Adapter adapter) {
//        CollectionReference chatroom = FirebaseFirestore.getInstance().collection("Chatrooms");
//        chatroom.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                if (!task.getResult().isEmpty()) {
//                    chatroom.orderBy("lastMessageTimestamp" , Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
//                        List<DocumentSnapshot> documentSnapshotList = value.getDocuments();
//                        for (DocumentSnapshot snapshot : documentSnapshotList) {
//                            if (snapshot.getString("chatroomId").contains(FirebaseUtil.currentUserUsername())) {
//                                counsellorList.add(snapshot.toObject(Counsellor.class));
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    });
//                }
//            }
//        });
        FirebaseFirestore.getInstance().collection("Users").addSnapshotListener((value, error) -> {
            counsellorList.clear();
            List<DocumentSnapshot> documentSnapshotList = value.getDocuments();
            for (DocumentSnapshot snapshot : documentSnapshotList) {
                if (snapshot.getBoolean("counsellor") && !counsellorList.contains(snapshot)) {
                    counsellorList.add(snapshot.toObject(Counsellor.class));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static void UserChatEventChangeListener (List <User> userList , RecyclerView.Adapter adapter) {
        CollectionReference chatroom = FirebaseFirestore.getInstance().collection("Chatrooms");
        chatroom.orderBy("lastMessageTimestamp" , Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            userList.clear();
            List<DocumentSnapshot> documentSnapshotList = value.getDocuments();
            for (DocumentSnapshot snapshot : documentSnapshotList) {
                if (snapshot.getString("chatroomId").contains(FirebaseUtil.currentUserUsername())) {
                    List<String> userIds = (List<String>) snapshot.get("userIds");
                    String otherUserId;
                    if (userIds.get(0).equals(FirebaseUtil.currentUserUsername())) otherUserId = userIds.get(1);
                    else otherUserId = userIds.get(0);
                    FirebaseUtil.getUserReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<DocumentSnapshot> allUsers = task.getResult().getDocuments();
                            for (DocumentSnapshot user : allUsers) {
                                if (!user.getBoolean("counsellor")
                                        && !userList.contains(user)
                                        && user.getString("username").equals(otherUserId)) {
                                    userList.add(user.toObject(User.class));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    public static void ChatroomEventChangeListener (boolean isCounsellor , List<User> userList , RecyclerView.Adapter adapter) {
        CollectionReference chatroom = FirebaseFirestore.getInstance().collection("Chatrooms");
        if (isCounsellor) {
            chatroom.orderBy("lastMessageTimestamp").addSnapshotListener((value, error) -> {
                List<DocumentSnapshot> documentSnapshotList = value.getDocuments();
                for (DocumentSnapshot snapshot : documentSnapshotList) {
                    if (snapshot.getString("chatroomId").contains(FirebaseUtil.currentUserUsername())) {
                        userList.add(snapshot.toObject(User.class));
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        else {
            chatroom.orderBy("lastMessageTimestamp").addSnapshotListener((value, error) -> {
                List<DocumentSnapshot> documentSnapshotList = value.getDocuments();
                for (DocumentSnapshot snapshot : documentSnapshotList) {
                    if (snapshot.getString("chatroomId").contains(FirebaseUtil.currentUserUsername())) {
                        userList.add(snapshot.toObject(Counsellor.class));
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            FirebaseFirestore.getInstance().collection("Users").addSnapshotListener((value, error) -> {
                List<DocumentSnapshot> documentSnapshotList = value.getDocuments();
                for (DocumentSnapshot snapshot : documentSnapshotList) {
                    if (snapshot.getBoolean("isCounsellor") && !userList.contains(snapshot)) {
                        userList.add(snapshot.toObject(Counsellor.class));
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public static void MessagesEventChangeListener (String chatroomId , List <Message> messageList , RecyclerView.Adapter adapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Chatrooms").document(chatroomId).collection("Chats")
                .orderBy("timestamp" , Query.Direction.ASCENDING).addSnapshotListener((value, error) -> {
            List<DocumentSnapshot> documentSnapshotList = value.getDocuments();
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    messageList.add(dc.getDocument().toObject(Message.class));
                    adapter.notifyItemInserted(messageList.size() - 1);
                }
            }
        });
    }

    public static void PostEventChangeListener (List<Post> postList , RecyclerView.Adapter adapter) {
        FirebaseUtil.getPostReference().addSnapshotListener((value, error) -> {
            List<DocumentSnapshot> documentSnapshotList = value.getDocuments();
            for (DocumentSnapshot doc : documentSnapshotList) {
                postList.add(doc.toObject(Post.class));
                adapter.notifyDataSetChanged();
            }
        });
    }
    public static void CommentEventChangeListener (String postId , List<Comment> commentList , RecyclerView.Adapter adapter) {
        FirebaseUtil.getCommentReference(postId).addSnapshotListener((value, error) -> {
            List<DocumentSnapshot> documentSnapshotList = value.getDocuments();
            for (DocumentSnapshot doc : documentSnapshotList) {
                commentList.add(doc.toObject(Comment.class));
                adapter.notifyDataSetChanged();
            }
        });
    }
}
