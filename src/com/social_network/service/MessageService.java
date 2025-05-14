package com.social_network.service;

import com.social_network.model.Message;
import com.social_network.model.User;
import com.social_network.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MessageService {
    private final String MESSAGE_FILE_PATH = "data/messages.dat";
    private List<Message> messages;
    private final NotificationService notificationService;

    public MessageService(NotificationService notificationService) {
        this.notificationService = notificationService;
        loadData();
    }

    private void loadData() {
        try {
            messages = FileUtil.readData(MESSAGE_FILE_PATH);
            if (!messages.isEmpty()) {
                int maxMessageId = 0;
                for (Message message : messages) {
                    if (message.getId() > maxMessageId) {
                        maxMessageId = message.getId();
                    }
                }
                Message.setCurrentAutoId(maxMessageId + 1);
            } else {
                Message.setCurrentAutoId(1);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc dữ liệu tin nhắn từ " + MESSAGE_FILE_PATH + ": " + e.getMessage());
            messages = new ArrayList<>();
            Message.setCurrentAutoId(1);
        }
    }

    private void saveData() {
        try {
            FileUtil.writeData(MESSAGE_FILE_PATH, messages);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu dữ liệu tin nhắn vào " + MESSAGE_FILE_PATH + ": " + e.getMessage());
        }
    }

    public boolean sendMessage(User sender, User receiver, String content) {
        if (receiver == null) {
            throw new IllegalArgumentException("Người nhận không tồn tại.");
        }
        if (sender.getUsername().equals(receiver.getUsername())) {
            throw new IllegalArgumentException("Không thể gửi tin nhắn cho chính mình.");
        }
        Message newMessage = new Message(sender.getNameDisplay(), receiver.getNameDisplay(),
                sender.getUsername(), receiver.getUsername(), content);
        messages.add(newMessage);
        notificationService.addNotification(receiver.getUsername(),
                sender.getNameDisplay() + " đã gửi bạn một tin nhắn.");
        saveData();
        return true;
    }

    public List<Message> getMessages(User user, User otherUser) {
        if (otherUser == null) {
            throw new IllegalArgumentException("Người dùng không tồn tại.");
        }
        String userUsername = user.getUsername();
        String otherUserUsername = otherUser.getUsername();
        List<Message> result = new ArrayList<>();
        for (Message msg : messages) {
            boolean sentByUser = msg.getSenderUsername().equalsIgnoreCase(userUsername)
                    && msg.getReceiverUsername().equalsIgnoreCase(otherUserUsername);
            boolean receivedByUser = msg.getSenderUsername().equalsIgnoreCase(otherUserUsername)
                    && msg.getReceiverUsername().equalsIgnoreCase(userUsername);
            if (sentByUser || receivedByUser) {
                result.add(msg);
            }
        }
        result.sort(Comparator.comparing(Message::getTimestamp));
        return result;
    }
}