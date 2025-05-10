package com.social_network.controller;

import com.social_network.model.Message;
import com.social_network.model.User;
import com.social_network.service.UserService;
import com.social_network.view.MessageView;

import java.util.List;

public class MessageController {
    private final UserService userService;
    private final MessageView messageView;

    public MessageController(UserService userService, MessageView messageView) {
        this.userService = userService;
        this.messageView = messageView;
    }

    public void processMessagingMenu(User currentUser) {
        String receiverNameDisplay = messageView.getMessageReceiverNameDisplay();
        if (receiverNameDisplay.isEmpty()) {
            messageView.showEmptyInputError("Tên người nhận");
            return;
        }

        if (receiverNameDisplay.equalsIgnoreCase(currentUser.getNameDisplay())) {
            messageView.showCannotMessageSelf();
            return;
        }

        User receiver = userService.getByNameDisplay(receiverNameDisplay);
        if (receiver == null) {
            messageView.showUserNotFound();
            return;
        }

        List<Message> chatHistory = userService.getMessages(currentUser, receiverNameDisplay);
        messageView.showChatHistory(chatHistory, currentUser, receiver);

        String content = messageView.getMessageContentFromUser();
        if (content.isEmpty()) {
            return;
        }

        if (userService.sendMessage(currentUser, receiverNameDisplay, content)) {
            messageView.showMessageSent(receiverNameDisplay);
            chatHistory = userService.getMessages(currentUser, receiverNameDisplay);
            messageView.showChatHistory(chatHistory, currentUser, receiver);
        } else {
            messageView.showMessageSendFailed("Không thể gửi tin nhắn. Người dùng có thể không tồn tại.");
        }
    }
}