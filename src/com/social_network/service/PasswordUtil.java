package com.social_network.service;

import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;

public class PasswordUtil {

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi khi kiểm tra mật khẩu: " + e.getMessage());
            return false;
        }
    }

    public static void validatePassword(String password) throws IllegalArgumentException {
        List<String> errors = new ArrayList<>();

        if (password == null || password.length() < 8) {
            errors.add(" - Mật khẩu phải có ít nhất 8 ký tự");
        }
        if (password != null && !password.matches(".*[A-Z].*")) {
            errors.add(" - Mật khẩu phải chứa ít nhất một chữ cái viết hoa (A-Z)");
        }
        if (password != null && !password.matches(".*[a-z].*")) {
            errors.add(" - Mật khẩu phải chứa ít nhất một chữ cái viết thường (a-z)");
        }
        if (password != null && !password.matches(".*\\d.*")) {
            errors.add(" - Mật khẩu phải chứa ít nhất một chữ số (0-9)");
        }
        if (password != null && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            errors.add(" - Mật khẩu phải chứa ít nhất một ký tự đặc biệt (ví dụ: !@#$%^&*)");
        }

        if (!errors.isEmpty()) {
            String fullMessage = "Mật khẩu không hợp lệ. Vui lòng đảm bảo:\n" + String.join("\n", errors);
            throw new IllegalArgumentException(fullMessage);
        }
    }
}