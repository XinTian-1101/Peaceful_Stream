package com.example.myapplication.CommunicationModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.Adapters.MessagesRecViewAdapter;
import com.example.myapplication.Models.Chatroom;
import com.example.myapplication.Models.Message;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AndroidUtil;
import com.example.myapplication.Utils.EventListeners;
import com.example.myapplication.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatPage extends AppCompatActivity {
    private ImageButton chatPageBack , chatPageSend;
    private TextView chatPageOtherUserName;
    private EditText chatPageMessage;
    private RecyclerView chatPageRecView;
    private String otherUserName , otherUserId , currentUserId , chatroomId;
    private Chatroom chatroom;
    private List<Message> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        chatPageBack = findViewById(R.id.chatPageBack);
        chatPageSend = findViewById(R.id.chatPageSend);
        chatPageOtherUserName = findViewById(R.id.chatPageOtherUserName);
        chatPageMessage = findViewById(R.id.chatPageMessage);
        chatPageRecView = findViewById(R.id.chatPageRecView);
        otherUserName = getIntent().getExtras().getString("name");
        otherUserId = getIntent().getExtras().getString("id");
        currentUserId = FirebaseUtil.currentUserUsername();
        chatroomId = FirebaseUtil.getChatroomId(currentUserId , otherUserId);
        messageList = new ArrayList<>();

        chatPageOtherUserName.setText(otherUserName);
        chatPageBack.setOnClickListener(v -> onBackPressed());

        getOrCreateChatroom ();

        MessagesRecViewAdapter adapter = new MessagesRecViewAdapter(messageList , this);
        chatPageRecView.setAdapter(adapter);
        chatPageRecView.setLayoutManager(new LinearLayoutManager(this));
        EventListeners.MessagesEventChangeListener(chatroomId , messageList , adapter);

        chatPageSend.setOnClickListener(v -> {
            String content = chatPageMessage.getText().toString().trim();
            if (content.isEmpty()) return;
            AndroidUtil.logMsg("current id" , currentUserId);
            String userId = currentUserId;
            Message message = new Message(userId , content , Timestamp.now());

            chatroom.setLastMessageTimestamp(Timestamp.now());
            chatroom.setLastSenderId(currentUserId);
            FirebaseUtil.getChatroomReference(chatroomId).set(chatroom);

            FirebaseUtil.getChatroomMessageReference(chatroomId).add(message)
                    .addOnCompleteListener(task -> chatPageMessage.setText(""));
        });
    }

    void getOrCreateChatroom () {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatroom = task.getResult().toObject(Chatroom.class);
                if (chatroom == null) {
                    chatroom = new Chatroom(chatroomId , Arrays.asList(currentUserId , otherUserId) ,
                            Timestamp.now() , "");
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroom);
                }
            }
        });
    }
}