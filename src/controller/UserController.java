package controller;

import model.Message;
import model.User;
import model.Post;
import service.PasswordUtil;
import service.UserService;
import view.UserView;

import java.util.List;
import java.util.Scanner;

public class UserController {
    private final UserService userService;
    private final UserView userView;
    private User currentUser;
    private final Scanner scanner;

    public UserController(UserService userService, UserView userView) {
        this.userService = userService;
        this.userView = userView;
        this.currentUser = null;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            if (currentUser == null) {
                int choice = userView.showMainMenu();
                switch (choice) {
                    case 1:
                        register();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        forgotPassword();
                        break;
                    case 0:
                        System.exit(0);
                        break;
                    default:
                        userView.showInvalidChoice();
                }
            } else {
                userView.showUserMenu(currentUser);
                int choice = userView.getUserMenuChoice();
                switch (choice) {
                    case 1:
                        handleProfileMenu();
                        break;
                    case 2:
                        handleSocialMenu();
                        break;
                    case 3:
                        createPost();
                        break;
                    case 4:
                        handleTimeline();
                        break;
                    case 5:
                        handleMessaging();
                        break;
                    case 6:
                        currentUser = null;
                        userView.showLogout();
                        break;
                    default:
                        userView.showInvalidChoice();
                }
            }
        }
    }

    private void handleProfileMenu() {
        while (true) {
            userView.showProfileMenu();
            String choice = userView.getProfileMenuChoice();
            switch (choice) {
                case "1":
                    userView.showProfile(currentUser);
                    break;
                case "2":
                    handleEditProfileMenu();
                    break;
                case "0":
                    return;
                default:
                    userView.showInvalidChoice();
            }
        }
    }

    private void handleEditProfileMenu() {
        while (true) {
            userView.showEditProfileMenu();
            String choice = userView.getEditProfileMenuChoice();
            switch (choice) {
                case "1":
                    String newName = userView.getNewNameDisplay();
                    if (!newName.matches("^[A-Za-z]+ [A-Za-z]+$")) {
                        userView.showInvalidNameFormat();
                        break;
                    }
                    userService.updateProfile(currentUser, newName, currentUser.getDescription(), currentUser.getHobbies());
                    userView.showUpdateSuccess();
                    break;
                case "2":
                    String newDesc = userView.getNewDescription();
                    userService.updateProfile(currentUser, currentUser.getNameDisplay(), newDesc, currentUser.getHobbies());
                    userView.showUpdateSuccess();
                    break;
                case "3":
                    String newHobby = userView.getNewHobbies();
                    userService.updateProfile(currentUser, currentUser.getNameDisplay(), currentUser.getDescription(), newHobby);
                    userView.showUpdateSuccess();
                    break;
                case "0":
                    return;
                default:
                    userView.showInvalidChoice();
            }
        }
    }

    private void handleSocialMenu() {
        while (true) {
            userView.showSocialMenu();
            String choice = userView.getSocialMenuChoice();
            switch (choice) {
                case "1":
                    String receiverNameDisplay = userView.getFriendRequestReceiver();
                    if (userService.sendFriendRequest(currentUser, receiverNameDisplay)) {
                        userView.showFriendRequestSent(receiverNameDisplay);
                    } else {
                        userView.showFriendRequestFailed();
                    }
                    break;
                case "2":
                    handleFriendRequests();
                    break;
                case "3":
                    List<User> friends = userService.getFriends(currentUser);
                    userView.showFriends(friends);
                    break;
                case "0":
                    return;
                default:
                    userView.showInvalidChoice();
            }
        }
    }

    private void handleFriendRequests() {
        List<User> requests = userService.getFriendRequests(currentUser);
        userView.showFriendRequests(requests);
        if (requests.isEmpty()) return;

        int choice = userView.getFriendRequestChoice(requests.size());
        if (choice < 1 || choice > requests.size()) {
            userView.showInvalidRequestChoice();
            return;
        }

        User sender = requests.get(choice - 1);
        if (userView.confirmAcceptFriendRequest(sender)) {
            userService.acceptFriendRequest(currentUser, sender.getUsername());
            userView.showFriendRequestAccepted();
        } else {
            userService.rejectFriendRequest(currentUser, sender.getUsername());
            userView.showFriendRequestRejected();
        }
    }

    private void createPost() {
        String content = userView.getPostContent();
        if (!content.isEmpty()) {
            userService.createPost(currentUser, content);
            userView.showPostCreated();
        } else {
            userView.showInvalidChoice();
        }
    }

    private void handleTimeline() {
        List<Post> timeline = userService.getTimeline(currentUser);
        userView.showTimeline(timeline);
        if (timeline.isEmpty()) return;

        System.out.println("\n1. Thích bài đăng");
        System.out.println("2. Bình luận");
        System.out.println("0. Quay lại");
        System.out.print("Chọn: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                int postId = userView.getPostId();
                Post post = userService.getPostById(postId);
                if (post != null) {
                    userService.likePost(currentUser, postId);
                    userView.showPostLiked();
                } else {
                    userView.showPostNotFound();
                }
                break;
            case "2":
                postId = userView.getPostId();
                post = userService.getPostById(postId);
                if (post != null) {
                    String commentContent = userView.getCommentContent();
                    if (!commentContent.isEmpty()) {
                        userService.commentPost(currentUser, postId, commentContent);
                        userView.showCommentAdded();
                    } else {
                        userView.showInvalidChoice();
                    }
                } else {
                    userView.showPostNotFound();
                }
                break;
            case "0":
                return;
            default:
                userView.showInvalidChoice();
        }
    }

    private void handleMessaging() {
        String receiverNameDisplay = userView.getMessageReceiver();
        User receiver = userService.getByNameDisplay(receiverNameDisplay);
        if (receiver == null) {
            userView.showUserNotFound();
            return;
        }

        List<Message> messages = userService.getMessages(currentUser, receiverNameDisplay);
        userView.showMessages(messages);

        String content = userView.getMessageContent();
        if (!content.isEmpty()) {
            userService.sendMessage(currentUser, receiverNameDisplay, content);
            userView.showMessageSent();
        } else {
            userView.showInvalidChoice();
        }
    }

    private void register() {
        String username;
        while (true) {
            username = userView.getUsername();
            if (username.isEmpty()) {
                userView.showEmptyUsernameError();
                continue;
            }
            if (!username.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9]{1,20}$")) {
                userView.showInvalidUsernameError();
                continue;
            }
            if (userService.getByUsername(username) != null) {
                userView.showUsernameExistsError();
                continue;
            }
            break;
        }

        String nameDisplay;
        while (true) {
            nameDisplay = userView.getNameDisplay();
            if (nameDisplay.isEmpty()) {
                userView.showEmptyNameDisplayError();
                continue;
            }
            if (!nameDisplay.matches("^[A-Za-z]+ [A-Za-z]+$")) {
                userView.showInvalidNameDisplayError();
                continue;
            }
            break;
        }

        String description = userView.getDescription();
        String hobbies = userView.getHobbies();

        String password;
        while (true) {
            password = userView.getPassword();
            try {
                PasswordUtil.validatePassword(password);
                break;
            } catch (IllegalArgumentException e) {
                userView.showPasswordError(e.getMessage());
            }
        }
        String hashed = PasswordUtil.hashPassword(password);

        String question;
        while (true) {
            question = userView.getSecurityQuestion();
            if (question.isEmpty()) {
                userView.showEmptySecurityQuestionError();
                continue;
            }
            break;
        }

        String answer;
        while (true) {
            answer = userView.getSecurityAnswer();
            if (answer.isEmpty()) {
                userView.showEmptySecurityAnswerError();
                continue;
            }
            break;
        }

        userService.register(username, hashed, question, answer, nameDisplay, description, hobbies);
        userView.showRegisterSuccess();
    }

    private void login() {
        String username;
        while (true) {
            username = userView.getUsername();
            if (!username.isEmpty()) break;
            userView.showEmptyUsernameError();
        }

        String password = userView.getPassword();
        User user = userService.getByUsername(username);
        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
            currentUser = user;
            userView.showLoginSuccess(user.getNameDisplay());
        } else {
            userView.showLoginFailure();
        }
    }

    private void forgotPassword() {
        String username = userView.getUsername();
        User user = userService.getByUsername(username);
        if (user == null) {
            userView.showUserNotFound();
            return;
        }

        userView.showSecurityQuestion(user.getSecurityQuestion());
        String answer = userView.getSecurityAnswer();
        if (!user.getSecurityAnswer().equalsIgnoreCase(answer)) {
            userView.showWrongAnswerError();
            return;
        }

        String newPassword;
        while (true) {
            newPassword = userView.getPassword();
            try {
                PasswordUtil.validatePassword(newPassword);
                break;
            } catch (IllegalArgumentException e) {
                userView.showPasswordError(e.getMessage());
            }
        }

        userService.resetPassword(user, PasswordUtil.hashPassword(newPassword));
        userView.showResetPasswordSuccess();
    }
}