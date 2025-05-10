package com.social_network.service;

import com.social_network.model.*;
import com.social_network.util.FileUtil;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private final String USER_FILE_PATH = "data/users.dat";
    private final String POST_FILE_PATH = "data/posts.dat";
    private final String MESSAGE_FILE_PATH = "data/messages.dat";
    private final String NOTIFICATION_FILE_PATH = "data/notifications.dat";
    private List<User> users;
    private List<Post> posts;
    private List<Message> messages;
    private List<Notification> notifications;
    private static int POST_AUTO_ID = 1;
    private static int COMMENT_AUTO_ID = 1;
    private static int NOTIFICATION_AUTO_ID = 1;

    public UserService() {
        loadData();
    }

    public void ensureDataFilesExist() {
        ensureParentDirectoryExists(USER_FILE_PATH);
        ensureParentDirectoryExists(POST_FILE_PATH);
        ensureParentDirectoryExists(MESSAGE_FILE_PATH);
        ensureParentDirectoryExists(NOTIFICATION_FILE_PATH);
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
        users = FileUtil.readData(USER_FILE_PATH);
        posts = FileUtil.readData(POST_FILE_PATH);
        messages = FileUtil.readData(MESSAGE_FILE_PATH);
        notifications = FileUtil.readData(NOTIFICATION_FILE_PATH);

        if (!posts.isEmpty()) {
            POST_AUTO_ID = posts.stream().mapToInt(Post::getId).max().orElse(0) + 1;
            COMMENT_AUTO_ID = posts.stream()
                    .flatMap(post -> post.getComments().stream())
                    .mapToInt(Comment::getId)
                    .max().orElse(0) + 1;
        } else {
            POST_AUTO_ID = 1;
            COMMENT_AUTO_ID = 1;
        }

        if (!notifications.isEmpty()) {
            NOTIFICATION_AUTO_ID = notifications.stream().mapToInt(Notification::getId).max().orElse(0) + 1;
        } else {
            NOTIFICATION_AUTO_ID = 1;
        }

        if(!messages.isEmpty()){
            Message.setCurrentAutoId(messages.stream().mapToInt(Message::getId).max().orElse(0) + 1);
        } else {
            Message.setCurrentAutoId(1);
        }
    }

    private void saveData() {
        FileUtil.writeData(USER_FILE_PATH, users);
        FileUtil.writeData(POST_FILE_PATH, posts);
        FileUtil.writeData(MESSAGE_FILE_PATH, messages);
        FileUtil.writeData(NOTIFICATION_FILE_PATH, notifications);
    }

    public boolean register(String username, String hashedPassword, String question, String answer, String nameDisplay, String description, String hobbies) {
        if (users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username) || u.getNameDisplay().equalsIgnoreCase(nameDisplay))) {
            return false;
        }
        User newUser = new User(username, hashedPassword, question, answer, nameDisplay, description, hobbies);
        users.add(newUser);
        saveData();
        return true;
    }

    public User getByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public void resetPassword(User user, String newHashedPassword) {
        user.setPassword(newHashedPassword);
        saveData();
    }

    public User getByNameDisplay(String nameDisplay) {
        return users.stream()
                .filter(u -> u.getNameDisplay().equalsIgnoreCase(nameDisplay))
                .findFirst()
                .orElse(null);
    }

    public boolean updateProfile(User currentUser, String newNameDisplay, String newDescription, String newHobbies) {
        if (users.stream().anyMatch(u -> !u.getUsername().equals(currentUser.getUsername()) && u.getNameDisplay().equalsIgnoreCase(newNameDisplay))) {
            return false;
        }
        currentUser.setNameDisplay(newNameDisplay);
        currentUser.setDescription(newDescription);
        currentUser.setHobbies(newHobbies);
        saveData();
        return true;
    }

    public List<User> getFriends(User user) {
        return users.stream()
                .filter(u -> user.getFriends().contains(u.getUsername()))
                .collect(Collectors.toList());
    }

    public List<User> getFriendRequests(User user) {
        return users.stream()
                .filter(u -> user.getFriendRequests().contains(u.getUsername()))
                .collect(Collectors.toList());
    }

    public boolean sendFriendRequest(User sender, String receiverNameDisplay) {
        User receiver = getByNameDisplay(receiverNameDisplay);
        if (receiver == null || sender.getUsername().equals(receiver.getUsername())) return false;

        if (sender.getFriends().contains(receiver.getUsername()) ||
                receiver.getFriendRequests().contains(sender.getUsername()) ||
                sender.getFriendRequests().contains(receiver.getUsername())) {
            return false;
        }

        receiver.getFriendRequests().add(sender.getUsername());
        addNotification(receiver.getUsername(), sender.getNameDisplay() + " đã gửi lời mời kết bạn.");
        saveData();
        return true;
    }

    public boolean acceptFriendRequest(User receiver, String senderUsername) {
        User sender = getByUsername(senderUsername);
        if (sender == null || !receiver.getFriendRequests().contains(senderUsername)) {
            return false;
        }

        receiver.getFriendRequests().remove(senderUsername);
        receiver.getFriends().add(senderUsername);
        sender.getFriends().add(receiver.getUsername());

        addNotification(sender.getUsername(), receiver.getNameDisplay() + " đã chấp nhận lời mời kết bạn của bạn.");
        saveData();
        return true;
    }

    public boolean rejectFriendRequest(User receiver, String senderUsername) {
        boolean removed = receiver.getFriendRequests().remove(senderUsername);
        if (removed) {
            saveData();
        }
        return removed;
    }

    public void createPost(User user, String content) {
        Post newPost = new Post(POST_AUTO_ID++, content, user.getUsername(), LocalDateTime.now());
        posts.add(newPost);
        saveData();
    }

    public boolean deletePost(User user, int postId) {
        Post post = getPostById(postId);
        if (post != null && post.getAuthorUsername().equals(user.getUsername())) {
            posts.remove(post);
            saveData();
            return true;
        }
        return false;
    }

    public boolean editPost(User user, int postId, String newContent) {
        Post post = getPostById(postId);
        if (post != null && post.getAuthorUsername().equals(user.getUsername())) {
            post.setContent(newContent);
            saveData();
            return true;
        }
        return false;
    }

    public boolean likePost(User user, int postId) {
        Post post = getPostById(postId);
        if (post != null) {
            if (!post.getLikes().contains(user.getUsername())) {
                post.getLikes().add(user.getUsername());
                if (!post.getAuthorUsername().equals(user.getUsername())) {
                    addNotification(post.getAuthorUsername(), user.getNameDisplay() + " đã thích bài đăng của bạn (ID: " + postId + ").");
                }
                saveData();
                return true;
            }
        }
        return false;
    }

    public boolean commentPost(User user, int postId, String content) {
        Post post = getPostById(postId);
        if (post != null) {
            Comment newComment = new Comment(COMMENT_AUTO_ID++, content, user.getUsername(), LocalDateTime.now());
            post.getComments().add(newComment);
            if (!post.getAuthorUsername().equals(user.getUsername())) {
                addNotification(post.getAuthorUsername(), user.getNameDisplay() + " đã bình luận bài đăng của bạn (ID: " + postId + ").");
            }
            saveData();
            return true;
        }
        return false;
    }

    public boolean sendMessage(User sender, String receiverNameDisplay, String content) {
        User receiver = getByNameDisplay(receiverNameDisplay);
        if (receiver != null && !sender.getUsername().equals(receiver.getUsername())) {
            Message newMessage = new Message(sender.getNameDisplay(), receiverNameDisplay, content);
            messages.add(newMessage);
            addNotification(receiver.getUsername(), sender.getNameDisplay() + " đã gửi bạn một tin nhắn.");
            saveData();
            return true;
        }
        return false;
    }

    public List<Message> getMessages(User user, String otherNameDisplay) {
        String userNameDisplay = user.getNameDisplay();
        return messages.stream()
                .filter(msg -> (msg.getSenderNameDisplay().equalsIgnoreCase(userNameDisplay) && msg.getReceiverNameDisplay().equalsIgnoreCase(otherNameDisplay)) ||
                        (msg.getSenderNameDisplay().equalsIgnoreCase(otherNameDisplay) && msg.getReceiverNameDisplay().equalsIgnoreCase(userNameDisplay)))
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }

    public List<Post> getTimeline(User user) {
        List<String> peopleToFollow = new ArrayList<>(user.getFriends());
        peopleToFollow.add(user.getUsername());

        return posts.stream()
                .filter(post -> peopleToFollow.contains(post.getAuthorUsername()))
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public Post getPostById(int postId) {
        return posts.stream()
                .filter(post -> post.getId() == postId)
                .findFirst()
                .orElse(null);
    }

    public void addNotification(String recipientUsername, String content) {
        User recipient = getByUsername(recipientUsername);
        if (recipient != null) {
            Notification newNotification = new Notification(NOTIFICATION_AUTO_ID++, recipientUsername, content, LocalDateTime.now());
            notifications.add(newNotification);
            saveData();
        }
    }

    public List<Notification> getNotifications(User user) {
        return notifications.stream()
                .filter(n -> n.getRecipientUsername().equals(user.getUsername()))
                .sorted(Comparator.comparing(Notification::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public boolean deleteNotification(User user, int notificationId) {
        boolean removed = notifications.removeIf(n -> n.getId() == notificationId && n.getRecipientUsername().equals(user.getUsername()));
        if (removed) {
            saveData();
        }
        return removed;
    }

    public void deleteAllNotifications(User user) {
        boolean removed = notifications.removeIf(n -> n.getRecipientUsername().equals(user.getUsername()));
        if (removed) {
            saveData();
        }
    }
}