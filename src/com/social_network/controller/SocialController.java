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
        if (receiverNameDisplay.equalsIgnoreCase(currentUser.getNameDisplay())) {
            socialView.showFriendRequestFailed("Bạn không thể gửi lời mời cho chính mình.");
            return;
        }
        User receiver = userService.getByNameDisplay(receiverNameDisplay);
        if (receiver == null) {
            socialView.showUserNotFound();
            return;
        }

        if (currentUser.getFriends().contains(receiver.getUsername())) {
            socialView.showFriendRequestFailed(receiverNameDisplay + " đã là bạn của bạn.");
            return;
        }
        if (receiver.getFriendRequests().contains(currentUser.getUsername())) {
            socialView.showFriendRequestFailed("Bạn đã gửi lời mời cho " + receiverNameDisplay + " trước đó.");
            return;
        }
        if (currentUser.getFriendRequests().contains(receiver.getUsername())) {
            socialView.showFriendRequestFailed(receiverNameDisplay + " đã gửi lời mời cho bạn. Hãy kiểm tra danh sách lời mời.");
            return;
        }

        if (userService.sendFriendRequest(currentUser, receiverNameDisplay)) {
            socialView.showFriendRequestSent(receiverNameDisplay);
        } else {
            socialView.showFriendRequestFailed("Không thể gửi lời mời. Vui lòng thử lại.");
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