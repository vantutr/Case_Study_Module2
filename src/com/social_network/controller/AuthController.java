package com.social_network.controller;

import com.social_network.model.Notification;
import com.social_network.model.User;
import com.social_network.service.PasswordUtil;
import com.social_network.service.UserService;
import com.social_network.view.AuthView;
import com.social_network.view.NotificationView;

import java.util.List;

public class AuthController {
    private final UserService userService;
    private final AuthView authView;
    private final NotificationView notificationView;

    public AuthController(UserService userService, AuthView authView, NotificationView notificationView) {
        this.userService = userService;
        this.authView = authView;
        this.notificationView = notificationView;
    }

    public User loginUser() {
        String username = authView.getUsernameForInput();
        if (username.isEmpty()) {
            authView.showEmptyInputError("Tên đăng nhập");
            return null;
        }
        String password = authView.getPasswordForInput();
        if (password.isEmpty()) {
            authView.showEmptyInputError("Mật khẩu");
            return null;
        }

        User user = userService.getByUsername(username);
        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
            authView.showLoginSuccess(user.getNameDisplay());
            List<Notification> notifications = userService.getNotifications(user);
            if (!notifications.isEmpty()) {
                notificationView.showNotifications(notifications);
            } else {
                System.out.println("(Không có thông báo mới)");
            }
            return user;
        } else {
            authView.showLoginFailure();
            return null;
        }
    }

    public void registerUser() {
        String username;
        while (true) {
            username = authView.getUsernameForInput();
            if (username.isEmpty()) {
                authView.showEmptyInputError("Tên đăng nhập");
                continue;
            }
            if (!username.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9]{1,20}$")) {
                authView.showInvalidUsernameError();
                continue;
            }
            if (userService.getByUsername(username) != null) {
                authView.showUsernameExistsError();
                continue;
            }
            break;
        }

        String nameDisplay;
        while (true) {
            nameDisplay = authView.getNameDisplayForInput();
            if (nameDisplay.isEmpty()) {
                authView.showEmptyInputError("Tên hiển thị");
                continue;
            }
            if (!nameDisplay.matches("^[A-Za-z0-9À-ỹà-ỹĐđ ]+( [A-Za-z0-9À-ỹà-ỹĐđ ]+)*$")) {
                authView.showInvalidNameDisplayError();
                continue;
            }
            if (userService.getByNameDisplay(nameDisplay) != null) {
                authView.showNameDisplayExistsError();
                continue;
            }
            break;
        }

        String description = authView.getDescriptionForInput();
        String hobbies = authView.getHobbiesForInput();
        String password;
        while (true) {
            password = authView.getPasswordForInput();
            try {
                PasswordUtil.validatePassword(password);
                break;
            } catch (IllegalArgumentException e) {
                authView.showPasswordValidationError(e.getMessage());
            }
        }
        String hashedPassword = PasswordUtil.hashPassword(password);

        String question;
        while (true) {
            question = authView.getSecurityQuestionForInput();
            if (question.isEmpty()) {
                authView.showEmptyInputError("Câu hỏi bảo mật");
                continue;
            }
            break;
        }

        String answer;
        while (true) {
            answer = authView.getSecurityAnswerForInput();
            if (answer.isEmpty()) {
                authView.showEmptyInputError("Câu trả lời bảo mật");
                continue;
            }
            break;
        }

        boolean registered = userService.register(username, hashedPassword, question, answer, nameDisplay, description, hobbies);
        if (registered) {
            authView.showRegisterSuccess();
        } else {
            authView.showNameDisplayOrUsernameExistsError();
        }
    }

    public void forgotPassword() {
        String username = authView.getUsernameForInput();
        User user = userService.getByUsername(username);
        if (user == null) {
            authView.showUserNotFound();
            return;
        }

        authView.showSecurityQuestion(user.getSecurityQuestion());
        String answer = authView.getSecurityAnswerForInput();
        if (!user.getSecurityAnswer().equalsIgnoreCase(answer)) {
            authView.showWrongSecurityAnswerError();
            return;
        }

        String newPassword;
        while (true) {
            newPassword = authView.getPasswordForInput();
            try {
                PasswordUtil.validatePassword(newPassword);
                break;
            } catch (IllegalArgumentException e) {
                authView.showPasswordValidationError(e.getMessage());
            }
        }
        String newHashedPassword = PasswordUtil.hashPassword(newPassword);
        userService.resetPassword(user, newHashedPassword);
        authView.showResetPasswordSuccess();
    }

    public void logoutUser() {
        authView.showLogoutMessage();
    }
}