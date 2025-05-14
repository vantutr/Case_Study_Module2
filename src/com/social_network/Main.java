package com.social_network;

import com.social_network.controller.MainAppController;
import com.social_network.service.*;

public class Main {
    public static void main(String[] args) {
        NotificationService notificationService = new NotificationService();
        UserService userService = new UserService();
        PostService postService = new PostService(notificationService);
        MessageService messageService = new MessageService(notificationService);
        FriendService friendService = new FriendService(notificationService);
        MainAppController appController = new MainAppController();
        appController.start();
    }
}