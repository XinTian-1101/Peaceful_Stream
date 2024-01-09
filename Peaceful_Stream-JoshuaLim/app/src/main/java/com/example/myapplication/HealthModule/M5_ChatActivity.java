package com.example.myapplication.HealthModule;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class M5_ChatActivity  extends AppCompatActivity {
    private EditText messageInput;
    private TextView chatDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m5_activity_chat);

        messageInput = findViewById(R.id.messageInput);
        chatDisplay = findViewById(R.id.chatDisplay);

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (!message.isEmpty()) {
            appendMessageToChat(message);
            messageInput.setText(""); // Clear the input field after sending the message
        }
    }

    private void appendMessageToChat(String message) {
        String currentChat = chatDisplay.getText().toString();
        currentChat += "You: " + message + "\n";
        chatDisplay.setText(currentChat);
    }
}