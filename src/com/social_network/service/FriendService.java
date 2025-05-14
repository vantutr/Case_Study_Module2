package com.social_network.service;

import com.social_network.model.User;
import com.social_network.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FriendService {
    private final String USER_FILE_PATH = "data/users.dat";
    private List<User> users;
    private final NotificationService notificationService;

    public FriendService(NotificationService notificationService) {
        this.notificationService = notificationService;
        loadData();
    }

    private void loadData() {
        try {
            users = FileUtil.readData(USER_FILE_PATH);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc dữ liệu người dùng từ " + USER_FILE_PATH + ": " + e.getMessage());
            users = new ArrayList<>();
        }
    }

    private void saveData() {
        try {
            FileUtil.writeData(USER_FILE_PATH, users);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu dữ liệu người dùng vào " + USER_FILE_PATH + ": " + e.getMessage());
        }
    }

    public List<User> getFriends(User user) {
        List<User> result = new ArrayList<>();
        List<String> friendUsernames = user.getFriends();
        for (User u : users) {
            if (friendUsernames.contains(u.getUsername())) {
                result.add(u);
            }
        }
        return result;
    }

    public List<User> getFriendRequests(User user) {
        List<User> result = new ArrayList<>();
        List<String> requestUsernames = user.getFriendRequests();
        for (User u : users) {
            if (requestUsernames.contains(u.getUsername())) {
                result.add(u);
            }
        }
        return result;
    }

    public List<User> getUsersByNameDisplay(String nameDisplay) {
        List<User> result = new ArrayList<>();
        for (User u : users) {
            if (u.getNameDisplay().equalsIgnoreCase(nameDisplay)) {
                result.add(u);
            }
        }
        return result;
    }

    public String sendFriendRequest(User sender, String receiverUsername) {
        User receiver = getByUsername(receiverUsername);
        if (receiver == null) {
            return "Người dùng không tồn tại.";
        }
        if (sender.getUsername().equals(receiver.getUsername())) {
            return "Bạn không thể gửi lời mời cho chính mình.";
        }
        if (sender.getFriends().contains(receiver.getUsername())) {
            return receiver.getNameDisplay() + " đã là bạn của bạn.";
        }
        if (receiver.getFriendRequests().contains(sender.getUsername())) {
            return "Bạn đã gửi lời mời cho " + receiver.getNameDisplay() + " trước đó.";
        }
        if (sender.getFriendRequests().contains(receiver.getUsername())) {
            return receiver.getNameDisplay() + " đã gửi lời mời cho bạn. Hãy kiểm tra danh sách lời mời.";
        }
        receiver.getFriendRequests().add(sender.getUsername());
        notificationService.addNotification(receiver.getUsername(),
                sender.getNameDisplay() + " đã gửi lời mời kết bạn.");
        saveData();
        return null;
    }

    public boolean acceptFriendRequest(User receiver, String senderUsername) {
        User sender = getByUsername(senderUsername);
        if (sender == null || !receiver.getFriendRequests().contains(senderUsername)) {
            return false;
        }
        receiver.getFriendRequests().remove(senderUsername);
        receiver.getFriends().add(senderUsername);
        sender.getFriends().add(receiver.getUsername());
        notificationService.addNotification(sender.getUsername(),
                receiver.getNameDisplay() + " đã chấp nhận lời mời kết bạn của bạn.");
        saveData();
        return true;
    }

    public boolean rejectFriendRequest(User receiver, String senderUsername) {
        boolean removed = receiver.getFriendRequests().remove(senderUsername);
        if (removed) {
            saveData();
        }
        return removed;
    }

    public User getByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }
}