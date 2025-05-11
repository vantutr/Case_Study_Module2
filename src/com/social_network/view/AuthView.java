package com.social_network.view;

import java.util.Scanner;

public class AuthView extends BaseView {

    public AuthView(Scanner scanner) {
        super(scanner);
    }

    public int showMainMenuAndGetChoice() {
        System.out.println("\n==== MẠNG XÃ HỘI ====");
        System.out.println("1. Đăng ký");
        System.out.println("2. Đăng nhập");
        System.out.println("3. Quên mật khẩu");
        System.out.println("0. Thoát");
        return getIntInputWithRange("Nhập lựa chọn", 0, 3, null);
    }

    public String getUsernameForInput() {
        System.out.print("Tên đăng nhập: ");
        return scanner.nextLine().trim();
    }

    public String getPasswordForInput() {
        System.out.print("Mật khẩu: ");
        return scanner.nextLine().trim();
    }

    public String getNameDisplayForInput() {
        System.out.print("Tên hiển thị (ví dụ: Van A): ");
        return scanner.nextLine().trim();
    }

    public String getDescriptionForInput() {
        System.out.print("Mô tả bản thân (bỏ trống nếu không có): ");
        return scanner.nextLine().trim();
    }

    public String getHobbiesForInput() {
        System.out.print("Sở thích (cách nhau bằng dấu phẩy, bỏ trống nếu không có): ");
        return scanner.nextLine().trim();
    }

    public String getSecurityQuestionForInput() {
        System.out.print("Câu hỏi bảo mật (ví dụ: Tên con vật cưng đầu tiên của bạn?): ");
        return scanner.nextLine().trim();
    }

    public String getSecurityAnswerForInput() {
        System.out.print("Câu trả lời cho câu hỏi bảo mật: ");
        return scanner.nextLine().trim();
    }

    public void showInvalidUsernameError() {
        System.out.println("Tên đăng nhập không hợp lệ! Phải từ 1-20 ký tự, chứa ít nhất một chữ cái, chỉ gồm chữ và số.");
    }

    public void showUsernameExistsError() {
        System.out.println("Lỗi: Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
    }

    public void showNameDisplayExistsError() {
        System.out.println("Lỗi: Tên hiển thị đã tồn tại! Vui lòng chọn tên khác.");
    }

    public void showNameDisplayOrUsernameExistsError() {
        System.out.println("Lỗi: Tên đăng nhập hoặc Tên hiển thị đã tồn tại! Vui lòng chọn thông tin khác.");
    }

    public void showInvalidNameDisplayError() {
        System.out.println("Tên hiển thị không hợp lệ! Phải có dạng 'FirstName LastName...' và chỉ chứa chữ cái, số, và khoảng trắng.");
    }

    public void showPasswordValidationError(String message) {
        System.out.println("Lỗi mật khẩu: \n" + message);
        System.out.println("Vui lòng thử lại.");
    }

    public void showRegisterSuccess() {
        System.out.println("Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
    }

    public void showRegisterFailure() {
        System.out.println("Đăng ký thất bại. Vui lòng thử lại.");
    }

    public void showLoginSuccess(String nameDisplay) {
        System.out.println("Đăng nhập thành công. Welcome, " + nameDisplay.toUpperCase() + "!");
    }

    public void showLoginFailure() {
        System.out.println("Đăng nhập thất bại. Sai tên đăng nhập hoặc mật khẩu.");
    }

    public void showUserNotFound() {
        System.out.println("Không tìm thấy người dùng với thông tin này.");
    }

    public void showSecurityQuestion(String question) {
        System.out.println("Câu hỏi bảo mật của bạn: " + question);
    }

    public void showWrongSecurityAnswerError() {
        System.out.println("Câu trả lời không đúng. Không thể đặt lại mật khẩu.");
    }

    public void showResetPasswordSuccess() {
        System.out.println("Mật khẩu đã được đặt lại thành công! Vui lòng đăng nhập bằng mật khẩu mới.");
    }

    public void showResetPasswordFailed() {
        System.out.println("Đặt lại mật khẩu thất bại.");
    }

    public void showLogoutMessage() {
        System.out.println("Bạn đã đăng xuất. Hẹn gặp lại!");
    }

    public void showExitMessage() {
        System.out.println("Cảm ơn bạn đã sử dụng ứng dụng. Tạm biệt!");
    }

    public String getDateOfBirthInput(){
        System.out.println("Ngày sinh (dd/MM/yyyy): ");
        return scanner.nextLine().trim();
    }

    public void showInvalidDateOfBirthError(){
        System.out.println("Ngày sinh lỗi, vui lòng thử lại theo định dạng: dd/MM/yyyy");
    }

    public void showUnderageError() {
        System.out.println("Lỗi: Từ 18 tuổi mới được đăng ký.");
    }
}