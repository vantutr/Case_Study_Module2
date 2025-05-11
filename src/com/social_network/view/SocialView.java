package com.social_network.view;

import com.social_network.model.User;
import java.util.List;
import java.util.Scanner;

public class SocialView extends BaseView {

    public SocialView(Scanner scanner) {
        super(scanner);
    }

    public int showSocialMenuAndGetChoice() {
        System.out.println("\n--- Kết bạn & Mạng lưới ---");
        System.out.println("1. Gửi lời mời kết bạn");
        System.out.println("2. Xem lời mời kết bạn đã nhận");
        System.out.println("3. Xem danh sách bạn bè");
        System.out.println("0. Quay lại menu chính");
        return getIntInputWithRange("Chọn", 0, 3, null);
    }

    public String getFriendRequestReceiverNameDisplay() {
        System.out.print("Nhập tên hiển thị của người bạn muốn gửi lời mời: ");
        return scanner.nextLine().trim();
    }

    public void showMatchingUsers(List<User> users, User currentUser) {
        if (users.isEmpty()) {
            System.out.println("Không tìm thấy người dùng nào.");
            return;
        }
        System.out.println("\n--- Người dùng khớp ---");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String display = String.format("%d. %s (@%s)", i + 1, user.getNameDisplay(), user.getUsername());
            if (user.getUsername().equals(currentUser.getUsername())) {
                display += " - tài khoản của bạn -";
            }
            System.out.println(display);
        }
    }

    public int getMatchingUserIndex(int maxUsers) {
        if (maxUsers == 0) return 0;
        return getIntInputWithRange("Chọn số thứ tự của người dùng", 0, maxUsers, "nhập 0 để quay lại");
    }

    public void showFriendRequestSent(User receiver) {
        System.out.printf("Đã gửi lời mời kết bạn tới %s (@%s).\n", receiver.getNameDisplay(), receiver.getUsername());
    }

    public void showFriendRequestFailed(String reason) {
        System.out.println("Gửi lời mời thất bại: " + reason);
    }

    public void showFriendRequests(List<User> requests) {
        if (requests.isEmpty()) {
            System.out.println("Không có lời mời kết bạn nào.");
            return;
        }
        System.out.println("\n--- Danh sách lời mời kết bạn ---");
        for (int i = 0; i < requests.size(); i++) {
            System.out.printf("%d. Từ: %s (@%s)\n", i + 1, requests.get(i).getNameDisplay(), requests.get(i).getUsername());
        }
    }

    public int getFriendRequestIndexToProcess(int maxRequests) {
        if (maxRequests == 0) return 0;
        return getIntInputWithRange("Chọn số thứ tự của lời mời để xử lý", 1, maxRequests, "nhập 0 để quay lại");
    }

    public int getAcceptOrRejectChoice(User sender) {
        System.out.println("Xử lý lời mời từ: " + sender.getNameDisplay() + " (@" + sender.getUsername() + ")");
        System.out.println("1. Chấp nhận");
        System.out.println("2. Từ chối");
        System.out.println("0. Bỏ qua / Quay lại");
        return getIntInputWithRange("Lựa chọn của bạn", 0, 2, null);
    }

    public void showFriendRequestAccepted(String friendName) {
        System.out.println("Đã chấp nhận lời mời kết bạn từ " + friendName + ". Hai bạn giờ là bạn bè!");
    }

    public void showFriendRequestRejected(String requesterName) {
        System.out.println("Đã từ chối lời mời kết bạn từ " + requesterName + ".");
    }

    public void showNoActionTaken() {
        System.out.println("Không thực hiện hành động nào.");
    }

    public void showInvalidRequestChoice() {
        System.out.println("Lựa chọn lời mời không hợp lệ.");
    }

    public void showFriends(List<User> friends) {
        if (friends.isEmpty()) {
            System.out.println("Bạn chưa có người bạn nào.");
        } else {
            System.out.println("\n--- Danh sách bạn bè ---");
            for (User friend : friends) {
                System.out.printf("- %s (Username: %s)\n", friend.getNameDisplay(), friend.getUsername());
            }
        }
    }

    public void showUserNotFound() {
        System.out.println("Không tìm thấy người dùng.");
    }
}