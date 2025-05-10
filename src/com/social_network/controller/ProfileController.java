package com.social_network.controller;

import com.social_network.model.User;
import com.social_network.service.UserService;
import com.social_network.view.ProfileView;

public class ProfileController {
    private final UserService userService;
    private final ProfileView profileView;

    public ProfileController(UserService userService, ProfileView profileView) {
        this.userService = userService;
        this.profileView = profileView;
    }

    public void processProfileMenu(User currentUser) {
        while (true) {
            int choice = profileView.showProfileMenuAndGetChoice();
            switch (choice) {
                case 1:
                    profileView.showUserProfile(currentUser);
                    break;
                case 2:
                    processEditProfile(currentUser);
                    break;
                case 0:
                    return;
                default:
                    profileView.showInvalidChoice();
            }
        }
    }

    private void processEditProfile(User currentUser) {
        int choice = profileView.showEditProfileMenuAndGetChoice();
        switch (choice) {
            case 1:
                String newNameDisplay;
                while (true) {
                    newNameDisplay = profileView.getNewNameDisplay();
                    if (newNameDisplay.isEmpty()) {
                        profileView.showEmptyInputError("Tên hiển thị");
                        continue;
                    }
                    if (!newNameDisplay.matches("^[A-Za-z0-9À-ỹà-ỹĐđ ]+( [A-Za-z0-9À-ỹà-ỹĐđ ]+)*$")) {
                        profileView.showInvalidNameFormat();
                        continue;
                    }
                    if (newNameDisplay.equalsIgnoreCase(currentUser.getNameDisplay())) {
                        System.out.println("Tên hiển thị mới giống tên cũ. Không có gì thay đổi.");
                        return;
                    }
                    if (userService.getByNameDisplay(newNameDisplay) != null) {
                        profileView.showNameDisplayExistsError();
                        continue;
                    }
                    break;
                }
                if (userService.updateProfile(currentUser, newNameDisplay, currentUser.getDescription(), currentUser.getHobbies())) {
                    profileView.showUpdateSuccess();
                } else {
                    profileView.showUpdateFailed("Tên hiển thị có thể đã bị người khác sử dụng.");
                }
                break;
            case 2:
                String newDesc = profileView.getNewDescription();
                userService.updateProfile(currentUser, currentUser.getNameDisplay(), newDesc, currentUser.getHobbies());
                profileView.showUpdateSuccess();
                break;
            case 3:
                String newHobbies = profileView.getNewHobbies();
                userService.updateProfile(currentUser, currentUser.getNameDisplay(), currentUser.getDescription(), newHobbies);
                profileView.showUpdateSuccess();
                break;
            case 0:
                return;
            default:
                profileView.showInvalidChoice();
        }
    }
}