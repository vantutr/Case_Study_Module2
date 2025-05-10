package com.social_network.controller;

import com.social_network.model.User;
import com.social_network.service.UserService;
import com.social_network.view.*;
import java.util.Scanner;

public class MainAppController {
    private final UserService userService;
    private User currentUser;

    private final AuthView authView;
    private final MainMenuView mainMenuView;
    private final ProfileView profileView;
    private final SocialView socialView;
    private final PostView postView;
    private final MessageView messageView;
    private final NotificationView notificationView;
    private final AuthController authController;
    private final ProfileController profileController;
    private final SocialController socialController;
    private final PostController postController;
    private final MessageController messageController;
    private final NotificationController notificationController;

    public MainAppController(UserService userService) {
        this.userService = userService;
        this.currentUser = null;

        Scanner sharedScanner = new Scanner(System.in);

        this.authView = new AuthView(sharedScanner);
        this.mainMenuView = new MainMenuView(sharedScanner, userService);
        this.profileView = new ProfileView(sharedScanner);
        this.socialView = new SocialView(sharedScanner);
        this.postView = new PostView(sharedScanner, userService);
        this.messageView = new MessageView(sharedScanner, userService);
        this.notificationView = new NotificationView(sharedScanner);
        this.authController = new AuthController(userService, authView, notificationView);
        this.profileController = new ProfileController(userService, profileView);
        this.socialController = new SocialController(userService, socialView);
        this.postController = new PostController(userService, postView);
        this.messageController = new MessageController(userService, messageView);
        this.notificationController = new NotificationController(userService, notificationView);
    }

    public void start() {
        userService.ensureDataFilesExist();

        while (true) {
            if (currentUser == null) {
                int choice = authView.showMainMenuAndGetChoice();
                switch (choice) {
                    case 1:
                        authController.registerUser();
                        break;
                    case 2:
                        User loggedInUser = authController.loginUser();
                        if (loggedInUser != null) {
                            this.currentUser = loggedInUser;
                        }
                        break;
                    case 3:
                        authController.forgotPassword();
                        break;
                    case 0:
                        authView.showExitMessage();
                        System.exit(0);
                        return;
                    default:
                        authView.showInvalidChoice();
                }
            } else {
                int choice = mainMenuView.showUserMenuAndGetChoice(currentUser);
                switch (choice) {
                    case 1:
                        profileController.processProfileMenu(currentUser);
                        break;
                    case 2:
                        socialController.processSocialMenu(currentUser);
                        break;
                    case 3:
                        postController.processCreatePost(currentUser);
                        break;
                    case 4:
                        postController.processTimelineMenu(currentUser);
                        break;
                    case 5:
                        messageController.processMessagingMenu(currentUser);
                        break;
                    case 6:
                        notificationController.processNotificationsMenu(currentUser);
                        break;
                    case 7:
                        this.currentUser = null;
                        authController.logoutUser();
                        break;
                }
            }
        }
    }
}