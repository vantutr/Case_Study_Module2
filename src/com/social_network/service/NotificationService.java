package com.social_network.service;

import com.social_network.model.Notification;
import com.social_network.model.User;
import com.social_network.util.FileUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class NotificationService {
    private final String NOTIFICATION_FILE_PATH = "data/notifications.dat";
    private List<Notification> notifications;
    private static int NOTIFICATION_AUTO_ID = 1;

    public NotificationService() {
        loadData();
    }

    private void loadData() {
        try {
            notifications = FileUtil.readData(NOTIFICATION_FILE_PATH);
            if (!notifications.isEmpty()) {
                int maxNotificationId = 0;
                for (Notification notification : notifications) {
                    if (notification.getId() > maxNotificationId) {
                        maxNotificationId = notification.getId();
                    }
                }
                NOTIFICATION_AUTO_ID = maxNotificationId + 1;
            } else {
                NOTIFICATION_AUTO_ID = 1;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc dữ liệu thông báo từ " + NOTIFICATION_FILE_PATH + ": " + e.getMessage());
            notifications = new ArrayList<>();
            NOTIFICATION_AUTO_ID = 1;
        }
    }

    private void saveData() {
        try {
            FileUtil.writeData(NOTIFICATION_FILE_PATH, notifications);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu dữ liệu thông báo vào " + NOTIFICATION_FILE_PATH + ": " + e.getMessage());
        }
    }

    public void addNotification(String recipientUsername, String content) {
        Notification newNotification = new Notification(NOTIFICATION_AUTO_ID++,
                recipientUsername, content, LocalDateTime.now());
        notifications.add(newNotification);
        saveData();
    }

    public List<Notification> getNotifications(User user) {
        List<Notification> result = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getRecipientUsername().equals(user.getUsername())) {
                result.add(notification);
            }
        }
        result.sort(Comparator.comparing(Notification::getTimestamp).reversed());
        return result;
    }

    public boolean deleteNotification(User user, int notificationId) {
        Iterator<Notification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            Notification n = iterator.next();
            if (n.getId() == notificationId && n.getRecipientUsername().equals(user.getUsername())) {
                iterator.remove();
                saveData();
                return true;
            }
        }
        return false;
    }

    public void deleteAllNotifications(User user) {
        Iterator<Notification> iterator = notifications.iterator();
        boolean removed = false;
        while (iterator.hasNext()) {
            Notification n = iterator.next();
            if (n.getRecipientUsername().equals(user.getUsername())) {
                iterator.remove();
                removed = true;
            }
        }
        if (removed) {
            saveData();
        }
    }
}