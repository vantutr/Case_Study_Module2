package com.social_network.controller;

import com.social_network.model.User;
import com.social_network.service.NotificationService;
import com.social_network.service.PasswordUtil;
import com.social_network.service.UserService;
import com.social_network.view.AuthView;
import com.social_network.view.NotificationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AuthController {
    private final UserService userService;
    private final NotificationService notificationService;
    private final AuthView authView;
    private final NotificationView notificationView;

    public AuthController(UserService userService, NotificationService notificationService, AuthView authView, NotificationView notificationView) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.authView = authView;
        this.notificationView = notificationView;
    }

    public void registerUser() {
        String username = authView.getUsernameForInput();
        if (username.isEmpty()) {
            authView.showEmptyInputError("Tên đăng nhập");
            return;
        }
        if (!isValidUsername(username)) {
            authView.showInvalidUsernameError();
            return;
        }

        User existingUser = userService.getByUsername(username);
        if (existingUser != null) {
            authView.showUsernameExistsError();
            return;
        }

        String nameDisplay = authView.getNameDisplayForInput();
        if (nameDisplay.isEmpty()) {
            authView.showEmptyInputError("Tên hiển thị");
            return;
        }
        if (!isValidNameDisplay(nameDisplay)) {
            authView.showInvalidNameDisplayError();
            return;
        }

        String dateOfBirthInput = authView.getDateOfBirthInput();
        LocalDate dateOfBirth = null;
        if (!dateOfBirthInput.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dateOfBirth = LocalDate.parse(dateOfBirthInput, formatter);
                if (LocalDate.now().minusYears(18).isBefore(dateOfBirth)) {
                    authView.showUnderageError();
                    return;
                }
            } catch (DateTimeParseException e) {
                authView.showInvalidDateOfBirthError();
                return;
            }
        }

        String password = authView.getPasswordForInput();
        if (password.isEmpty()) {
            authView.showEmptyInputError("Mật khẩu");
            return;
        }
        try {
            PasswordUtil.validatePassword(password);
        } catch (IllegalArgumentException e) {
            authView.showPasswordValidationError(e.getMessage());
            return;
        }
        String hashedPassword = PasswordUtil.hashPassword(password);

        String securityQuestion = authView.getSecurityQuestionForInput();
        if (securityQuestion.isEmpty()) {
            authView.showEmptyInputError("Câu hỏi bảo mật");
            return;
        }

        String securityAnswer = authView.getSecurityAnswerForInput();
        if (securityAnswer.isEmpty()) {
            authView.showEmptyInputError("Câu trả lời bảo mật");
            return;
        }

        String description = authView.getDescriptionForInput();
        String hobbies = authView.getHobbiesForInput();

        boolean registered = userService.register(
                username,
                hashedPassword,
                securityQuestion,
                securityAnswer,
                nameDisplay,
                description,
                hobbies,
                dateOfBirth
        );

        if (registered) {
            authView.showRegisterSuccess();
        } else {
            authView.showRegisterFailure();
        }
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
        if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
            authView.showLoginFailure();
            return null;
        }

        authView.showLoginSuccess(user.getNameDisplay());
        notificationView.showNotifications(notificationService.getNotifications(user));
        return user;
    }

    public void forgotPassword() {
        String username = authView.getUsernameForInput();
        if (username.isEmpty()) {
            authView.showEmptyInputError("Tên đăng nhập");
            return;
        }

        User user = userService.getByUsername(username);
        if (user == null) {
            authView.showUserNotFound();
            return;
        }

        authView.showSecurityQuestion(user.getSecurityQuestion());
        String answer = authView.getSecurityAnswerForInput();
        if (!answer.equalsIgnoreCase(user.getSecurityAnswer())) {
            authView.showWrongSecurityAnswerError();
            return;
        }

        String newPassword = authView.getPasswordForInput();
        if (newPassword.isEmpty()) {
            authView.showEmptyInputError("Mật khẩu mới");
            return;
        }
        try {
            PasswordUtil.validatePassword(newPassword);
        } catch (IllegalArgumentException e) {
            authView.showPasswordValidationError(e.getMessage());
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        userService.resetPassword(user, hashedPassword);
        authView.showResetPasswordSuccess();
    }

    public void logoutUser() {
        authView.showLogoutMessage();
    }

    private boolean isValidUsername(String username) {
        return username.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9]{1,20}$");
    }

    private boolean isValidNameDisplay(String nameDisplay) {
        return nameDisplay.matches("^[a-zA-Z0-9\\s]+$") && nameDisplay.trim().contains(" ");
    }
}