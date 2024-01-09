package com.example.myapplication.Models;


import com.google.firebase.Timestamp;

public class Message {
    private String senderId , content;
    private Timestamp timestamp;

    public Message() {
    }

    public Message(String senderId, String content, com.google.firebase.Timestamp timestamp) {
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
