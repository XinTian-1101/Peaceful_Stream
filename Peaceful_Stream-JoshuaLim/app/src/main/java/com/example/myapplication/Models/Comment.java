package com.example.myapplication.Models;

import com.google.firebase.Timestamp;

public class Comment {
    private String commentId , userId , content;
    private Timestamp timestamp;

    public Comment() {
    }

    public Comment(String commentId, String userId, String content, Timestamp timestamp) {
        this.commentId = commentId;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
