package com.social_network.view;

import com.social_network.model.User;
import com.social_network.service.UserService;
import com.social_network.view.BaseView;

import java.util.Scanner;

public class MainMenuView extends BaseView {
    private final UserService userService;

    public MainMenuView(Scanner scanner, UserService userService) {
        super(scanner);
        this.userService = userService;
    }

    public int showUserMenuAndGetChoice(User user) {
        long notificationCount = userService.getNotifications(user).size();
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