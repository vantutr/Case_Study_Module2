package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private static int AUTO_ID = 1;
    private int id;
    private String content;
    private String authorUsername;
    private List<String> likes;
    private List<Comment> comments;
    private LocalDateTime timestamp;

    public Post() {}

    public Post(String content, String authorUsername) {
        this.id = AUTO_ID++;
        this.content = content;
        this.authorUsername = authorUsername;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public List<String> getLikes() {
        return likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Post ID: %d | Author: %s | Time: %s | Content: %s | Likes: %d | Comments: %d",
                id, authorUsername, timestamp.format(formatter), content, likes.size(), comments.size());
    }
}