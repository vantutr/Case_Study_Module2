package com.social_network.view;

import com.social_network.model.User;
import com.social_network.service.UserService;
import com.social_network.service.NotificationService;

import java.util.Scanner;

public class MainMenuView extends BaseView {
    private final UserService userService;
    private final NotificationService notificationService;

    public MainMenuView(Scanner scanner, UserService userService, NotificationService notificationService) {
        super(scanner);
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public int showUserMenuAndGetChoice(User user) {
        long notificationCount = notificationService.getNotifications(user).size();
        System.out.println("\n===== CHÀO " + user.getNameDisplay().toUpperCase() + " =====");
        System.out.println("1. Hồ sơ cá nhân");
        System.out.println("2. Kết bạn và mạng lưới xã hội");
        System.out.println("3. Đăng bài");
        System.out.println("4. Xem timeline");
        System.out.println("5. Nhắn tin");
        System.out.println("6. Xem thông báo (" + notificationCount + (notificationCount > 0 ? " mới" : "") + ")");
        System.out.println("7. Đăng xuất");
        return getIntInputWithRange("Chọn", 1, 7, null);
    }
}