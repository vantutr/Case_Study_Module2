package com.social_network.view;

import com.social_network.model.User;
import java.util.Scanner;

public class ProfileView extends BaseView {

    public ProfileView(Scanner scanner) {
        super(scanner);
    }

    public int showProfileMenuAndGetChoice() {
        System.out.println("\n--- Hồ sơ cá nhân ---");
        System.out.println("1. Xem thông tin cá nhân");
        System.out.println("2. Chỉnh sửa thông tin cá nhân");
        System.out.println("0. Quay lại menu chính");
        return getIntInputWithRange("Chọn", 0, 2, null);
    }

    public void showUserProfile(User user) {
        System.out.println("\n--- Thông tin cá nhân của " + user.getNameDisplay() + " ---");
        System.out.println("Tên đăng nhập: " + user.getUsername());
        System.out.println("Tên hiển thị: " + user.getNameDisplay());
        System.out.println("Mô tả: " + (user.getDescription() != null && !user.getDescription().isEmpty() ? user.getDescription() : "Chưa có thông tin"));
        System.out.println("Sở thích: " + (user.getHobbies() != null && !user.getHobbies().isEmpty() ? user.getHobbies() : "Chưa có thông tin"));
    }

    public int showEditProfileMenuAndGetChoice() {
        System.out.println("\n--- Chỉnh sửa thông tin ---");
        System.out.println("1. Chỉnh sửa tên hiển thị");
        System.out.println("2. Chỉnh sửa mô tả");
        System.out.println("3. Chỉnh sửa sở thích");
        System.out.println("0. Quay lại");
        return getIntInputWithRange("Chọn", 0, 3, null);
    }

    public String getNewNameDisplay() {
        System.out.print("Nhập tên hiển thị mới (First Last...): ");
        return scanner.nextLine().trim();
    }

    public String getNewDescription() {
        System.out.print("Nhập mô tả mới: ");
        return scanner.nextLine().trim();
    }

    public String getNewHobbies() {
        System.out.print("Nhập sở thích mới (cách nhau bởi dấu phẩy nếu nhiều): ");
        return scanner.nextLine().trim();
    }

    public void showUpdateSuccess() {
        System.out.println("Cập nhật thông tin thành công!");
    }

    public void showUpdateFailed(String reason) {
        System.out.println("Cập nhật thông tin thất bại: " + reason);
    }

    public void showInvalidNameFormat() {
        System.out.println("Lỗi: Tên hiển thị không hợp lệ! Phải có dạng 'FirstName LastName...' và chỉ chứa chữ cái, số, và khoảng trắng.");
    }

    public void showNameDisplayExistsError() {
        System.out.println("Lỗi: Tên hiển thị đã tồn tại! Vui lòng chọn tên khác.");
    }
}