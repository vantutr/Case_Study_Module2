package view;

import model.Comment;
import model.User;
import model.Post;
import model.Message;

import java.util.List;
import java.util.Scanner;

public class UserView {
    private final Scanner scanner;

    public UserView() {
        this.scanner = new Scanner(System.in);
    }

    public int showMainMenu() {
        System.out.println("\n==== MẠNG XÃ HỘI ====");
        System.out.println("1. Đăng ký");
        System.out.println("2. Đăng nhập");
        System.out.println("3. Quên mật khẩu");
        System.out.println("0. Thoát");
        System.out.print("Nhập lựa chọn: ");
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void showInvalidChoice() {
        System.out.println("Lựa chọn không hợp lệ!");
    }

    public void showUserMenu(User user) {
        System.out.println("\n===== CHÀO " + user.getNameDisplay() + " =====");
        System.out.println("1. Hồ sơ cá nhân");
        System.out.println("2. Kết bạn và mạng lưới xã hội");
        System.out.println("3. Đăng bài");
        System.out.println("4. Xem timeline");
        System.out.println("5. Nhắn tin");
        System.out.println("6. Đăng xuất");
        System.out.print("Chọn: ");
    }

    public int getUserMenuChoice() {
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void showProfileMenu() {
        System.out.println("\n--- Hồ sơ cá nhân ---");
        System.out.println("1. Xem thông tin cá nhân");
        System.out.println("2. Chỉnh sửa thông tin cá nhân");
        System.out.println("0. Thoát");
        System.out.print("Chọn: ");
    }

    public String getProfileMenuChoice() {
        return scanner.nextLine().trim();
    }

    public void showProfile(User user) {
        System.out.println("Tên hiển thị: " + user.getNameDisplay());
        System.out.println("Mô tả: " + (user.getDescription() != null ? user.getDescription() : ""));
        System.out.println("Sở thích: " + (user.getHobbies() != null ? user.getHobbies() : ""));
    }

    public void showEditProfileMenu() {
        System.out.println("\n--- Chỉnh sửa thông tin ---");
        System.out.println("1. Chỉnh sửa tên hiển thị");
        System.out.println("2. Chỉnh sửa mô tả");
        System.out.println("3. Chỉnh sửa sở thích");
        System.out.println("0. Thoát");
        System.out.print("Chọn: ");
    }

    public String getEditProfileMenuChoice() {
        return scanner.nextLine().trim();
    }

    public String getNewNameDisplay() {
        System.out.print("Nhập tên hiển thị mới: ");
        return scanner.nextLine().trim();
    }

    public String getNewDescription() {
        System.out.print("Nhập mô tả mới: ");
        return scanner.nextLine().trim();
    }

    public String getNewHobbies() {
        System.out.print("Nhập sở thích mới: ");
        return scanner.nextLine().trim();
    }

    public void showUpdateSuccess() {
        System.out.println("Cập nhật thành công!");
    }

    public void showInvalidNameFormat() {
        System.out.println("Tên không hợp lệ!");
    }

    public void showSocialMenu() {
        System.out.println("\n--- Kết bạn & Mạng lưới ---");
        System.out.println("1. Gửi lời mời kết bạn");
        System.out.println("2. Xem lời mời kết bạn đến");
        System.out.println("3. Xem danh sách bạn bè");
        System.out.println("0. Thoát");
        System.out.print("Chọn: ");
    }

    public String getSocialMenuChoice() {
        return scanner.nextLine().trim();
    }

    public String getFriendRequestReceiver() {
        System.out.print("Nhập tên hiển thị người muốn kết bạn: ");
        return scanner.nextLine().trim();
    }

    public void showFriendRequestSent(String receiverNameDisplay) {
        System.out.println("Đã gửi lời mời kết bạn tới " + receiverNameDisplay);
    }

    public void showFriendRequestFailed() {
        System.out.println("Gửi lời mời thất bại. Có thể người dùng không tồn tại, đã là bạn, hoặc đã gửi rồi.");
    }

    public void showFriendRequests(List<User> requests) {
        if (requests.isEmpty()) {
            System.out.println("Không có lời mời kết bạn nào.");
            return;
        }
        System.out.println("Danh sách lời mời:");
        for (int i = 0; i < requests.size(); i++) {
            System.out.printf("%d. %s (%s)\n", i + 1, requests.get(i).getNameDisplay(), requests.get(i).getUsername());
        }
    }

    public int getFriendRequestChoice(int max) {
        System.out.print("Chọn số thứ tự để chấp nhận (hoặc 0 để bỏ qua): ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean confirmAcceptFriendRequest(User sender) {
        System.out.print("Chấp nhận kết bạn với " + sender.getNameDisplay() + "? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        return confirm.equals("y");
    }

    public void showFriendRequestAccepted() {
        System.out.println("Đã chấp nhận lời mời kết bạn.");
    }

    public void showFriendRequestRejected() {
        System.out.println("Đã từ chối lời mời kết bạn.");
    }

    public void showInvalidRequestChoice() {
        System.out.println("Lựa chọn không hợp lệ.");
    }

    public void showFriends(List<User> friends) {
        if (friends.isEmpty()) {
            System.out.println("Bạn chưa có người bạn nào.");
        } else {
            System.out.println("Danh sách bạn bè:");
            for (User friend : friends) {
                System.out.printf("- %s (%s)\n", friend.getNameDisplay(), friend.getUsername());
            }
        }
    }

    public String getPostContent() {
        System.out.print("Nhập nội dung bài đăng: ");
        return scanner.nextLine().trim();
    }

    public void showPostCreated() {
        System.out.println("Đã đăng bài thành công!");
    }

    public void showTimeline(List<Post> posts) {
        if (posts.isEmpty()) {
            System.out.println("Không có bài đăng nào trong timeline.");
            return;
        }
        System.out.println("\n--- Timeline ---");
        for (Post post : posts) {
            System.out.println(post);
            System.out.println("  Likes: " + post.getLikes().size());
            System.out.println("  Comments:");
            for (Comment comment : post.getComments()) {
                System.out.println("    " + comment);
            }
        }
    }

    public int getPostId() {
        System.out.print("Nhập ID bài đăng: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void showPostNotFound() {
        System.out.println("Không tìm thấy bài đăng.");
    }

    public void showPostLiked() {
        System.out.println("Đã thích bài đăng.");
    }

    public String getCommentContent() {
        System.out.print("Nhập nội dung bình luận: ");
        return scanner.nextLine().trim();
    }

    public void showCommentAdded() {
        System.out.println("Đã thêm bình luận.");
    }

    public String getMessageReceiver() {
        System.out.print("Nhập tên hiển thị người nhận: ");
        return scanner.nextLine().trim();
    }

    public String getMessageContent() {
        System.out.print("Nhập nội dung tin nhắn: ");
        return scanner.nextLine().trim();
    }

    public void showMessageSent() {
        System.out.println("Đã gửi tin nhắn.");
    }

    public void showUserNotFound() {
        System.out.println("Không tìm thấy người dùng.");
    }

    public void showMessages(List<Message> messages) {
        if (messages.isEmpty()) {
            System.out.println("Không có tin nhắn nào.");
            return;
        }
        System.out.println("\n--- Tin nhắn ---");
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    public String getUsername() {
        System.out.print("Tên đăng nhập: ");
        return scanner.nextLine().trim();
    }

    public String getPassword() {
        System.out.print("Mật khẩu: ");
        return scanner.nextLine().trim();
    }

    public String getNameDisplay() {
        System.out.print("Nhập tên hiển thị (First name + Last name): ");
        return scanner.nextLine().trim();
    }

    public String getDescription() {
        System.out.print("Nhập mô tả bản thân: ");
        return scanner.nextLine().trim();
    }

    public String getHobbies() {
        System.out.print("Nhập sở thích: ");
        return scanner.nextLine().trim();
    }

    public String getSecurityQuestion() {
        System.out.print("Nhập câu hỏi bảo mật (Ví dụ: Tên ở nhà của bạn?): ");
        return scanner.nextLine().trim();
    }

    public String getSecurityAnswer() {
        System.out.print("Nhập câu trả lời: ");
        return scanner.nextLine().trim();
    }

    public void showEmptyUsernameError() {
        System.out.println("Tên đăng nhập không được để trống!");
    }

    public void showInvalidUsernameError() {
        System.out.println("Tên đăng nhập không hợp lệ! " +
                "\n- Chỉ cho phép chữ và số" +
                "\n- Tối đa 20 ký tự" +
                "\n- Phải chứa ít nhất một chữ cái.");
    }

    public void showUsernameExistsError() {
        System.out.println("Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
    }

    public void showEmptyNameDisplayError() {
        System.out.println("Tên hiển thị không được để trống!");
    }

    public void showInvalidNameDisplayError() {
        System.out.println("Tên hiển thị không hợp lệ! " +
                "\n- Tên hiển thị chỉ chứa chữ cái" +
                "\n- Gồm họ và tên cách nhau bởi 1 dấu cách.");
    }

    public void showEmptySecurityQuestionError() {
        System.out.println("Câu hỏi bảo mật không được để trống!");
    }

    public void showEmptySecurityAnswerError() {
        System.out.println("Câu trả lời không được để trống!");
    }

    public void showPasswordError(String message) {
        System.out.println(message);
        System.out.println("Vui lòng nhập lại.\n");
    }

    public void showRegisterSuccess() {
        System.out.println("Đăng ký thành công!");
    }

    public void showLoginSuccess(String nameDisplay) {
        System.out.println("Đăng nhập thành công. Xin chào, " + nameDisplay + "!");
    }

    public void showLoginFailure() {
        System.out.println("Sai tên đăng nhập hoặc mật khẩu.");
    }

    public void showSecurityQuestion(String question) {
        System.out.println("Câu hỏi bảo mật: " + question);
    }

    public void showWrongAnswerError() {
        System.out.println("Câu trả lời sai!");
    }

    public void showResetPasswordSuccess() {
        System.out.println("Đổi mật khẩu thành công!");
    }

    public void showLogout() {
        System.out.println("Đã đăng xuất!");
    }
}