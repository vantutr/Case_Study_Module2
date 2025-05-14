package com.social_network.service;

import com.social_network.model.User;
import com.social_network.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final String USER_FILE_PATH = "data/users.dat";
    private List<User> users;

    public UserService() {
        loadData();
    }

    public void ensureDataFilesExist() {
        ensureParentDirectoryExists(USER_FILE_PATH);
    }

    private void ensureParentDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.err.println("Không thể tạo thư mục dữ liệu: " + parentDir.getAbsolutePath());
            }
        }
    }

    private void loadData() {
        try {
            users = FileUtil.readData(USER_FILE_PATH);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc dữ liệu người dùng từ " + USER_FILE_PATH + ": " + e.getMessage());
            users = new ArrayList<>();
        }
    }

    public void saveData() {
        try {
            FileUtil.writeData(USER_FILE_PATH, users);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu dữ liệu người dùng vào " + USER_FILE_PATH + ": " + e.getMessage());
        }
    }

    public boolean register(String username, String hashedPassword, String question, String answer,
                            String nameDisplay, String description, String hobbies, LocalDate dateOfBirth) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        User newUser = new User(username, hashedPassword, question, answer, nameDisplay, description, hobbies, dateOfBirth);
        users.add(newUser);
        saveData();
        return true;
    }

    public User getByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    public void resetPassword(User user, String newHashedPassword) {
        user.setPassword(newHashedPassword);
        saveData();
    }

    public User getByNameDisplay(String nameDisplay) {
        for (User u : users) {
            if (u.getNameDisplay().equalsIgnoreCase(nameDisplay)) {
                return u;
            }
        }
        return null;
    }

    public List<User> getUsersByNameDisplay(String nameDisplay) {
        List<User> result = new ArrayList<>();
        for (User u : users) {
            if (u.getNameDisplay().equalsIgnoreCase(nameDisplay)) {
                result.add(u);
            }
        }
        return result;
    }

    public boolean updateProfile(User currentUser, String newNameDisplay, String newDescription,
                                 String newHobbies, LocalDate newDateOfBirth) {
        if (newNameDisplay != null && !newNameDisplay.equalsIgnoreCase(currentUser.getNameDisplay())) {
            currentUser.setNameDisplay(newNameDisplay);
        }
        if (newHobbies != null) {
            currentUser.setHobbies(newHobbies);
        }
        if (newDescription != null) {
            currentUser.setDescription(newDescription);
        }
        if (newDateOfBirth != null) {
            currentUser.setDateOfBirth(newDateOfBirth);
        }
        saveData();
        return true;
    }
}