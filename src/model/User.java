package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static int AUTO_ID = 1;
    private int id;
    private String username;
    private String password;
    private String nameDisplay;
    private String securityQuestion;
    private String securityAnswer;

    private String description;
    private String hobbies;
    private List<String> friends;
    private List<String> friendRequests;

    public User() {}

    public User(String username, String password, String securityQuestion, String securityAnswer, String nameDisplay, String description, String hobbies) {
        this.id = AUTO_ID++;
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.nameDisplay = nameDisplay;
        this.description = description;
        this.hobbies = hobbies;
        this.friends = new ArrayList<>();
        this.friendRequests = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public String getNameDisplay() {
        return nameDisplay;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getSecurityQuestion() {
        return securityQuestion;
    }
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNameDisplay(String nameDisplay) {
        this.nameDisplay = nameDisplay;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Tài khoản: %s | Tên hiển thị: %s", id, username, nameDisplay);
    }
}