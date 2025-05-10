package service;

import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;

public class PasswordUtil {


    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }


    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    public static void validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        if (password.length() < 8) {
            errors.add(" - Mật khẩu phải có ít nhất 8 ký tự");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add(" - Mật khẩu phải chứa ít nhất một chữ cái viết hoa (A-Z)");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.add(" - Mật khẩu phải chứa ít nhất một chữ cái viết thường (a-z)");
        }
        if (!password.matches(".*\\d.*")) {
            errors.add(" - Mật khẩu phải chứa ít nhất một chữ số (0-9)");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            errors.add(" - Mật khẩu phải chứa ít nhất một ký tự đặc biệt (ví dụ: !@#$%^&*)");
        }

        if (!errors.isEmpty()) {
            String fullMessage = " Mật khẩu không hợp lệ. Vui lòng đảm bảo:\n" + String.join("\n", errors);
            throw new IllegalArgumentException(fullMessage);
        }
    }

}






