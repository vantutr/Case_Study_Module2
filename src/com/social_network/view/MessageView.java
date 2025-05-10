package com.social_network.view;

import com.social_network.model.Message;
import com.social_network.model.User;
import com.social_network.service.UserService;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MessageView extends BaseView {
    private final UserService userService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    public MessageView(Scanner scanner, UserService userService) {
        super(scanner);
        this.userService = userService;
    }

    public String getMessageReceiverNameDisplay() {
        System.out.print("Nhập tên hiển thị của người bạn muốn nhắn tin: ");
        return scanner.nextLine().trim();
    }

    public String getMessageContentFromUser() {
        System.out.print("Nhập nội dung tin nhắn (bỏ trống để không gửi): ");
        return scanner.nextLine().trim();
    }

    public void showMessageSent(String receiverName) {
        System.out.println("Tin nhắn đã được gửi tới " + receiverName + ".");
    }

    public void showMessageSendFailed(String reason) {
        System.out.println("Gửi tin nhắn thất bại: " + reason);
    }

    public void showUserNotFound() {
        System.out.println("Không tìm thấy người dùng với tên hiển thị này.");
    }

    public void showChatHistory(List<Message> messages, User currentUser, User otherUser) {
        if (messages.isEmpty()) {
            System.out.println("Chưa có tin nhắn nào với " + otherUser.getNameDisplay() + ".");
            return;
        }
        System.out.println("\n--- Cuộc trò chuyện với " + otherUser.getNameDisplay() + " ---");
        for (Message message : messages) {
            String prefix;
            if (message.getSenderNameDisplay().equalsIgnoreCase(currentUser.getNameDisplay())) {
                prefix = "Bạn";
            } else {
                prefix = message.getSenderNameDisplay();
            }
            System.out.printf("[%s - %s]: %s\n",
                    prefix,
                    message.getTimestamp().format(dateTimeFormatter),
                    message.getContent());
        }
        System.out.println("--- Kết thúc cuộc trò chuyện ---");
    }

    public void showCannotMessageSelf() {
        System.out.println("Bạn không thể tự nhắn tin cho chính mình.");
    }
}