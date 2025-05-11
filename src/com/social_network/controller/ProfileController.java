package com.social_network.controller;

import com.social_network.model.User;
import com.social_network.service.UserService;
import com.social_network.view.ProfileView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.Period;

public class ProfileController {
    private final UserService userService;
    private final ProfileView profileView;
    private static final int MINIMUM_AGE = 18;

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
                    break;
                }
                if (userService.updateProfile(currentUser, newNameDisplay, null, null, null)) {
                    profileView.showUpdateSuccess();
                } else {
                    profileView.showUpdateFailed("Cập nhật thất bại.");
                }
                break;
            case 2:
                String newDesc = profileView.getNewDescription();
                userService.updateProfile(currentUser, null, newDesc, null, null);
                profileView.showUpdateSuccess();
                break;
            case 3:
                String newHobbies = profileView.getNewHobbies();
                userService.updateProfile(currentUser, null, null, newHobbies, null);
                profileView.showUpdateSuccess();
                break;
            case 4:
                LocalDate newDateOfBirth = null;
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                boolean validInput = false;
                while (!validInput) {
                    String dobInput = profileView.getDateOfBirthInput();
                    if (dobInput.isEmpty()){
                        validInput = true;
                        break;
                    }
                    try{
                        newDateOfBirth = LocalDate.parse(dobInput,dateFormatter);
                        if (Period.between(newDateOfBirth, LocalDate.now()).getYears() < MINIMUM_AGE) {
                            profileView.showUnderageError();
                            newDateOfBirth = null;
                            System.out.println("Vui lòng nhập lại ngày sinh hoặc bỏ trống để giữ lại ngày sinh cũ.");
                            continue;
                        }
                        validInput = true;
                    } catch (DateTimeParseException e){
                        profileView.showInvalidDateOfBirthError();
                    }
                }
                if (validInput){
                    userService.updateProfile(currentUser, null, null, null, newDateOfBirth);
                    profileView.showUpdateSuccess();
                }
                break;
            case 0:
                return;
            default:
                profileView.showInvalidChoice();
        }
    }
}