package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private static int AUTO_ID = 1;
    private int id;
    private String senderNameDisplay;
    private String receiverNameDisplay;
    private String content;
    private LocalDateTime timestamp;

    public Message() {}

    public Message(String senderNameDisplay, String receiverNameDisplay, String content) {
        this.id = AUTO_ID++;
        this.senderNameDisplay = senderNameDisplay;
        this.receiverNameDisplay = receiverNameDisplay;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getSenderNameDisplay() {
        return senderNameDisplay;
    }

    public String getReceiverNameDisplay() {
        return receiverNameDisplay;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Message ID: %d | From: %s | To: %s | Time: %s | Content: %s",
                id, senderNameDisplay, receiverNameDisplay, timestamp.format(formatter), content);
    }
}