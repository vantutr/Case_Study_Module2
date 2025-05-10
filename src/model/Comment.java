package model;

import java.io.Serializable;

public class Comment implements Serializable {
    private static int AUTO_ID = 1;
    private int id;
    private String content;
    private String authorUsername;

    public Comment() {}

    public Comment(String content, String authorUsername) {
        this.id = AUTO_ID++;
        this.content = content;
        this.authorUsername = authorUsername;
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

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("Comment ID: %d | Author: %s | Content: %s",
                id, authorUsername, content);
    }
}