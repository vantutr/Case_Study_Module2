package com.social_network.view;

import com.social_network.model.Notification;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class NotificationView extends BaseView {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    public NotificationView(Scanner scanner) {
        super(scanner);
    }

    public void showNotifications(List<Notification> notifications) {
        if (notifications.isEmpty()) {
            System.out.println("Bạn không có thông báo nào.");
            return;
        }
        System.out.println("\n--- Thông báo ---");
        for (Notification notification : notifications) {
            System.out.printf("ID: %d | %s | %s\n",
                    notification.getId(),
                    notification.getTimestamp().format(dateTimeFormatter),
                    notification.getContent());
        }
    }

    public int showNotificationActionMenuAndGetChoice(boolean hasNotifications) {
        if (!hasNotifications) {
            return 0;
        }
        System.out.println("\n--- Tùy chọn Thông báo ---");
        System.out.println("1. Xóa một thông báo cụ thể");
        System.out.println("2. Xóa tất cả thông báo");
        System.out.println("0. Quay lại menu chính");
        return getIntInputWithRange("Chọn", 0, 2, null);
    }

    public int getNotificationIdToDelete() {
        return getIntInput("Nhập ID của thông báo bạn muốn xóa: ");
    }

    public void showNotificationDeleted() {
        System.out.println("Thông báo đã được xóa.");
    }

    public void showNotificationNotFound() {
        System.out.println("Không tìm thấy thông báo với ID này hoặc bạn không có quyền xóa.");
    }

    public void showAllNotificationsDeleted() {
        System.out.println("Tất cả thông báo đã được xóa.");
    }
}