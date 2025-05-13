package com.social_network.service;

import com.social_network.model.*;
import com.social_network.util.FileUtil;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

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
            int maxPostId = 0;
            int maxCommentId = 0;
            for (Post post : posts) {
                if (post.getId() > maxPostId) {
                    maxPostId = post.getId();
                }
                for (Comment comment : post.getComments()) {
                    if (comment.getId() > maxCommentId) {
                        maxCommentId = comment.getId();
                    }
                }
            }
            POST_AUTO_ID = maxPostId + 1;
            COMMENT_AUTO_ID = maxCommentId + 1;
        } else {
            POST_AUTO_ID = 1;
            COMMENT_AUTO_ID = 1;
        }

        if (!notifications.isEmpty()) {
            int maxNotificationId = 0;
            for (Notification notification : notifications) {
                if (notification.getId() > maxNotificationId) {
                    maxNotificationId = notification.getId();
                }
            }
            NOTIFICATION_AUTO_ID = maxNotificationId + 1;
        } else {
            NOTIFICATION_AUTO_ID = 1;
        }

        if (!messages.isEmpty()) {
            int maxMessageId = 0;
            for (Message message : messages) {
                if (message.getId() > maxMessageId) {
                    maxMessageId = message.getId();
                }
            }
            Message.setCurrentAutoId(maxMessageId + 1);
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

    public boolean register(String username, String hashedPassword, String question, String answer, String nameDisplay, String description, String hobbies, LocalDate dateOfBirth) {
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

    public boolean updateProfile(User currentUser, String newNameDisplay, String newDescription, String newHobbies, LocalDate newDateOfBirth) {
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

    public List<User> getFriends(User user) {
        List<User> result = new ArrayList<>();
        List<String> friendUsernames = user.getFriends();
        for (User u : users) {
            if (friendUsernames.contains(u.getUsername())) {
                result.add(u);
            }
        }
        return result;
    }

    public List<User> getFriendRequests(User user) {
        List<User> result = new ArrayList<>();
        List<String> requestUsernames = user.getFriendRequests();
        for (User u : users) {
            if (requestUsernames.contains(u.getUsername())) {
                result.add(u);
            }
        }
        return result;
    }

    public String sendFriendRequest(User sender, String receiverUsername) {
        User receiver = getByUsername(receiverUsername);
        if (receiver == null) {
            return "Người dùng không tồn tại.";
        }
        if (sender.getUsername().equals(receiver.getUsername())) {
            return "Bạn không thể gửi lời mời cho chính mình.";
        }
        if (sender.getFriends().contains(receiver.getUsername())) {
            return receiver.getNameDisplay() + " đã là bạn của bạn.";
        }
        if (receiver.getFriendRequests().contains(sender.getUsername())) {
            return "Bạn đã gửi lời mời cho " + receiver.getNameDisplay() + " trước đó.";
        }
        if (sender.getFriendRequests().contains(receiver.getUsername())) {
            return receiver.getNameDisplay() + " đã gửi lời mời cho bạn. Hãy kiểm tra danh sách lời mời.";
        }
        receiver.getFriendRequests().add(sender.getUsername());
        addNotification(receiver.getUsername(), sender.getNameDisplay() + " đã gửi lời mời kết bạn.");
        saveData();
        return null;
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

    public boolean sendMessage(User sender, String receiverUsername, String content) {
        User receiver = getByUsername(receiverUsername);
        if (receiver == null) {
            System.out.println("Debug: Receiver not found for username: " + receiverUsername);
            return false;
        }
        if (sender.getUsername().equals(receiver.getUsername())) {
            System.out.println("Debug: Cannot send message to self: " + receiverUsername);
            return false;
        }
        Message newMessage = new Message(sender.getNameDisplay(), receiver.getNameDisplay(), sender.getUsername(), receiver.getUsername(), content);
        messages.add(newMessage);
        addNotification(receiver.getUsername(), sender.getNameDisplay() + " đã gửi bạn một tin nhắn.");
        saveData();
        return true;
    }

    public List<Message> getMessages(User user, String otherUsername) {
        User otherUser = getByUsername(otherUsername);
        if (otherUser == null) {
            System.out.println("Debug: Other user not found for username: " + otherUsername);
            return new ArrayList<>();
        }
        String userUsername = user.getUsername();
        String otherUserUsername = otherUser.getUsername();
        List<Message> result = new ArrayList<>();
        for (Message msg : messages) {
            boolean sentByUser = msg.getSenderUsername().equalsIgnoreCase(userUsername)
                    && msg.getReceiverUsername().equalsIgnoreCase(otherUserUsername);
            boolean receivedByUser = msg.getSenderUsername().equalsIgnoreCase(otherUserUsername)
                    && msg.getReceiverUsername().equalsIgnoreCase(userUsername);
            if (sentByUser || receivedByUser) {
                result.add(msg);
            }
        }
        result.sort(Comparator.comparing(Message::getTimestamp));
        return result;
    }

    public List<Post> getTimeline(User user) {
        List<String> peopleToFollow = new ArrayList<>(user.getFriends());
        peopleToFollow.add(user.getUsername());
        List<Post> result = new ArrayList<>();
        for (Post post : posts) {
            if (peopleToFollow.contains(post.getAuthorUsername())) {
                result.add(post);
            }
        }
        result.sort(Comparator.comparing(Post::getTimestamp).reversed());
        return result;
    }

    public Post getPostById(int postId) {
        for (Post post : posts) {
            if (post.getId() == postId) {
                return post;
            }
        }
        return null;
    }

    public void addNotification(String recipientUsername, String content) {
        User recipient = getByUsername(recipientUsername);
        if (recipient != null) {
            Notification newNotification = new Notification(NOTIFICATION_AUTO_ID++, recipientUsername, content, LocalDateTime.now());
            notifications.add(newNotification);
            saveData();
        } else {
            System.out.println("Debug: Recipient not found for notification: " + recipientUsername);
        }
    }

    public List<Notification> getNotifications(User user) {
        List<Notification> result = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.getRecipientUsername().equals(user.getUsername())) {
                result.add(notification);
            }
        }
        result.sort(Comparator.comparing(Notification::getTimestamp).reversed());
        return result;
    }

    public boolean deleteNotification(User user, int notificationId) {
        Iterator<Notification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            Notification n = iterator.next();
            if (n.getId() == notificationId && n.getRecipientUsername().equals(user.getUsername())) {
                iterator.remove();
                saveData();
                return true;
            }
        }
        return false;
    }

    public void deleteAllNotifications(User user) {
        Iterator<Notification> iterator = notifications.iterator();
        boolean removed = false;
        while (iterator.hasNext()) {
            Notification n = iterator.next();
            if (n.getRecipientUsername().equals(user.getUsername())) {
                iterator.remove();
                removed = true;
            }
        }
        if (removed) {
            saveData();
        }
    }
}