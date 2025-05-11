package com.social_network.controller;

import com.social_network.model.User;
import com.social_network.service.UserService;
import com.social_network.view.SocialView;

import java.util.List;

public class SocialController {
    private final UserService userService;
    private final SocialView socialView;

    public SocialController(UserService userService, SocialView socialView) {
        this.userService = userService;
        this.socialView = socialView;
    }

    public void processSocialMenu(User currentUser) {
        while (true) {
            int choice = socialView.showSocialMenuAndGetChoice();
            switch (choice) {
                case 1:
                    sendFriendRequest(currentUser);
                    break;
                case 2:
                    handleFriendRequests(currentUser);
                    break;
                case 3:
                    List<User> friends = userService.getFriends(currentUser);
                    socialView.showFriends(friends);
                    break;
                case 0:
                    return;
                default:
                    socialView.showInvalidChoice();
            }
        }
    }

    private void sendFriendRequest(User currentUser) {
        String receiverNameDisplay = socialView.getFriendRequestReceiverNameDisplay();
        if (receiverNameDisplay.isEmpty()) {
            socialView.showEmptyInputError("Tên người nhận");
            return;
        }

        List<User> matchingUsers = userService.getUsersByNameDisplay(receiverNameDisplay);
        if (matchingUsers.isEmpty()) {
            socialView.showUserNotFound();
            return;
        }

        socialView.showMatchingUsers(matchingUsers, currentUser);
        int selectedIndex = socialView.getMatchingUserIndex(matchingUsers.size());
        if (selectedIndex == 0) {
            socialView.showNoActionTaken();
            return;
        }
        if (selectedIndex < 1 || selectedIndex > matchingUsers.size()) {
            socialView.showInvalidChoice();
            return;
        }

        User receiver = matchingUsers.get(selectedIndex - 1);
        String error = userService.sendFriendRequest(currentUser, receiver.getUsername());
        if (error == null) {
            socialView.showFriendRequestSent(receiver);
        } else {
            socialView.showFriendRequestFailed(error);
        }
    }

    private void handleFriendRequests(User currentUser) {
        List<User> requests = userService.getFriendRequests(currentUser);
        socialView.showFriendRequests(requests);
        if (requests.isEmpty()) {
            return;
        }

        int requestIndex = socialView.getFriendRequestIndexToProcess(requests.size());
        if (requestIndex == 0) {
            return;
        }
        User sender = requests.get(requestIndex - 1);

        int actionChoice = socialView.getAcceptOrRejectChoice(sender);
        switch (actionChoice) {
            case 1:
                if (userService.acceptFriendRequest(currentUser, sender.getUsername())) {
                    socialView.showFriendRequestAccepted(sender.getNameDisplay());
                } else {
                    System.out.println("Lỗi: Không thể chấp nhận lời mời kết bạn.");
                }
                break;
            case 2:
                if (userService.rejectFriendRequest(currentUser, sender.getUsername())) {
                    socialView.showFriendRequestRejected(sender.getNameDisplay());
                } else {
                    System.out.println("Lỗi: Không thể từ chối lời mời kết bạn.");
                }
                break;
            case 0:
                socialView.showNoActionTaken();
                break;
            default:
                socialView.showInvalidChoice();
        }
    }
}