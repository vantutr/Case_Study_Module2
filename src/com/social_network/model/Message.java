package com.social_network.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int currentAutoId = 1;

    private int id;
    private String senderNameDisplay;
    private String receiverNameDisplay;
    private String senderUsername;
    private String receiverUsername;
    private String content;
    private LocalDateTime timestamp;

    public Message() {}

    public Message(String senderNameDisplay, String receiverNameDisplay, String senderUsername, String receiverUsername, String content) {
        this.id = currentAutoId++;
        this.senderNameDisplay = senderNameDisplay;
        this.receiverNameDisplay = receiverNameDisplay;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public static int getCurrentAutoId() {
        return currentAutoId;
    }

    public static void setCurrentAutoId(int id) {
        currentAutoId = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderNameDisplay() {
        return senderNameDisplay;
    }

    public void setSenderNameDisplay(String senderNameDisplay) {
        this.senderNameDisplay = senderNameDisplay;
    }

    public String getReceiverNameDisplay() {
        return receiverNameDisplay;
    }

    public void setReceiverNameDisplay(String receiverNameDisplay) {
        this.receiverNameDisplay = receiverNameDisplay;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Message ID: %d | From: %s (@%s) | To: %s (@%s) | Time: %s | Content: %s",
                id, senderNameDisplay, senderUsername, receiverNameDisplay, receiverUsername, timestamp.format(formatter), content);
    }
}