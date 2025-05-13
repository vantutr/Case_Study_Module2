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

        List<User> matchingUsers = userService.getUsersByNameDisplay(receiverNameDisplay);
        if (matchingUsers.isEmpty()) {
            messageView.showUserNotFound();
            return;
        }

        messageView.showMatchingUsers(matchingUsers, currentUser);
        int selectedIndex = messageView.getMatchingUserIndex(matchingUsers.size());
        if (selectedIndex == 0) {
            messageView.showNoActionTaken();
            return;
        }
        if (selectedIndex < 1 || selectedIndex > matchingUsers.size()) {
            messageView.showInvalidChoice();
            return;
        }

        User receiver = matchingUsers.get(selectedIndex - 1);
        List<Message> chatHistory = userService.getMessages(currentUser, receiver.getUsername());
        messageView.showChatHistory(chatHistory, currentUser, receiver);

        String content = messageView.getMessageContentFromUser();
        if (content.isEmpty()) {
            return;
        }

        if (userService.sendMessage(currentUser, receiver.getUsername(), content)) {
            messageView.showMessageSent(receiver.getNameDisplay());
            chatHistory = userService.getMessages(currentUser, receiver.getUsername());
            messageView.showChatHistory(chatHistory, currentUser, receiver);
        } else {
            messageView.showMessageSendFailed("Không thể gửi tin nhắn. Vui lòng thử lại.");
        }
    }
}