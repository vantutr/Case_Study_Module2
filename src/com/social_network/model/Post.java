package com.social_network.model;

import com.social_network.model.Comment;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String content;
    private String authorUsername;
    private List<String> likes;
    private List<Comment> comments;
    private LocalDateTime timestamp;

    public Post() {
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Post(int id, String content, String authorUsername, LocalDateTime timestamp) {
        this.id = id;
        this.content = content;
        this.authorUsername = authorUsername;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Post ID: %d | Author: %s | Time: %s | Content: %s | Likes: %d | Comments: %d",
                id, authorUsername, timestamp.format(formatter), content, likes.size(), comments.size());
    }
}