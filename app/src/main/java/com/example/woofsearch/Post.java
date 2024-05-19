package com.example.woofsearch;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.sql.Timestamp;

public class Post {
    private final String postId;
    private final String phone;
    private final String content;
    private final Uri imageUrl;
    private final Uri videoUrl;
    private final Timestamp createdAt;

    public Post(String postId, String phone, String content, Uri imageUrl, Uri videoUrl, Timestamp createdAt) {
        this.postId = postId;
        this.phone = phone;
        this.content = content;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.createdAt = createdAt;
    }

    public String getPostId() {
        return postId;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public String getContent() {
        return content;
    }

    @Nullable
    public Uri getImageUrl() {
        return imageUrl;
    }

    @Nullable
    public Uri getVideoUrl() {
        return videoUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}