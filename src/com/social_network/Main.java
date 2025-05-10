package com.social_network;

import com.social_network.controller.MainAppController;
import com.social_network.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        MainAppController appController = new MainAppController(userService);
        appController.start();
    }
}