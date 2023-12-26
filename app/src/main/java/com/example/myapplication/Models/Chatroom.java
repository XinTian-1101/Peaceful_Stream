package com.example.myapplication.Models;

import com.google.firebase.Timestamp;

import java.util.List;

public class Chatroom {
    String chatroomId;
    List<String> userIds;
    Timestamp lastMessageTimestamp;
    String lastSenderId;

    public Chatroom() {
    }

    public Chatroom(String chatroomId, List<String> userIds, Timestamp lastMessageTimestamp, String lastSenderId) {
        this.chatroomId = chatroomId;
        this.userIds = userIds;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastSenderId = lastSenderId;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastSenderId() {
        return lastSenderId;
    }

    public void setLastSenderId(String lastSenderId) {
        this.lastSenderId = lastSenderId;
    }
}
