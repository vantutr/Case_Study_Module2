package com.social_network.controller;

import com.social_network.model.Notification;
import com.social_network.model.User;
import com.social_network.service.UserService;
import com.social_network.view.NotificationView;
import java.util.List;

public class NotificationController {
    private final UserService userService;
    private final NotificationView notificationView;

    public NotificationController(UserService userService, NotificationView notificationView) {
        this.userService = userService;
        this.notificationView = notificationView;
    }

    public void processNotificationsMenu(User currentUser) {
        List<Notification> notifications = userService.getNotifications(currentUser);
        notificationView.showNotifications(notifications);

        int choice = notificationView.showNotificationActionMenuAndGetChoice(!notifications.isEmpty());

        switch (choice) {
            case 1:
                if (notifications.isEmpty()) {
                    notificationView.showInvalidChoice();
                    break;
                }
                int notificationId = notificationView.getNotificationIdToDelete();
                if (notificationId < 0 && notifications.isEmpty()) {
                    notificationView.showInvalidChoice(); break;
                } else if (notificationId < 0) {
                    notificationView.showInvalidChoice(); break;
                }

                if (userService.deleteNotification(currentUser, notificationId)) {
                    notificationView.showNotificationDeleted();
                } else {
                    notificationView.showNotificationNotFound();
                }
                break;
            case 2:
                if (notifications.isEmpty()) {
                    notificationView.showInvalidChoice();
                    break;
                }
                userService.deleteAllNotifications(currentUser);
                notificationView.showAllNotificationsDeleted();
                break;
            case 0:
                return;
            default:
                notificationView.showInvalidChoice();
        }
    }
}