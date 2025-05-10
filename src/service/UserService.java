package service;

import model.User;
import model.Post;
import model.Comment;
import model.Message;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final String USER_FILE_PATH = "data/users.dat";
    private final String POST_FILE_PATH = "data/posts.dat";
    private final String MESSAGE_FILE_PATH = "data/messages.dat";
    private List<User> users;
    private List<Post> posts;
    private List<Message> messages;

    public UserService() {
        users = FileUtil.readData(USER_FILE_PATH);
        if (users == null) {
            users = new ArrayList<>();
        }
        posts = FileUtil.readData(POST_FILE_PATH);
        if (posts == null) {
            posts = new ArrayList<>();
        }
        messages = FileUtil.readData(MESSAGE_FILE_PATH);
        if (messages == null) {
            messages = new ArrayList<>();
        }
    }

    public void register(String username, String password, String question, String answer, String nameDisplay, String description, String hobbies) {
        users.add(new User(username, password, question, answer, nameDisplay, description, hobbies));
        FileUtil.writeData(USER_FILE_PATH, users);
    }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public void resetPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        FileUtil.writeData(USER_FILE_PATH, users);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return u;
        }
        return null;
    }

    public User getByNameDisplay(String nameDisplay) {
        for (User u : users) {
            if (u.getNameDisplay().equalsIgnoreCase(nameDisplay)) return u;
        }
        return null;
    }

    public void updateProfile(User user, String nameDisplay, String description, String hobbies) {
        user.setNameDisplay(nameDisplay);
        user.setDescription(description);
        user.setHobbies(hobbies);
        FileUtil.writeData(USER_FILE_PATH, users);
    }

    public List<User> getFriends(User user) {
        List<User> friends = new ArrayList<>();
        for (User u : users) {
            if (user.getFriends().contains(u.getUsername())) {
                friends.add(u);
            }
        }
        return friends;
    }

    public List<User> getFriendRequests(User user) {
        List<User> pendingRequests = new ArrayList<>();
        for (User u : users) {
            if (user.getFriendRequests().contains(u.getUsername())) {
                pendingRequests.add(u);
            }
        }
        return pendingRequests;
    }

    public boolean sendFriendRequest(User sender, String receiverNameDisplay) {
        if (sender.getNameDisplay().equalsIgnoreCase(receiverNameDisplay)) return false;

        User receiver = null;
        for (User u : users) {
            if (u.getNameDisplay().equalsIgnoreCase(receiverNameDisplay)) {
                receiver = u;
                break;
            }
        }

        if (receiver == null) return false;

        if (sender.getFriends().contains(receiver.getUsername())) return false;

        if (!receiver.getFriendRequests().contains(sender.getUsername())) {
            receiver.getFriendRequests().add(sender.getUsername());
            FileUtil.writeData(USER_FILE_PATH, users);
            return true;
        }

        return false;
    }

    public boolean acceptFriendRequest(User receiver, String senderUsername) {
        if (!receiver.getFriendRequests().contains(senderUsername)) return false;

        receiver.getFriendRequests().remove(senderUsername);
        receiver.getFriends().add(senderUsername);

        User sender = getByUsername(senderUsername);
        if (sender != null && !sender.getFriends().contains(receiver.getUsername())) {
            sender.getFriends().add(receiver.getUsername());
        }

        FileUtil.writeData(USER_FILE_PATH, users);
        return true;
    }

    public boolean rejectFriendRequest(User receiver, String senderUsername) {
        boolean removed = receiver.getFriendRequests().remove(senderUsername);
        if (removed) {
            FileUtil.writeData(USER_FILE_PATH, users);
        }
        return removed;
    }

    public void createPost(User user, String content) {
        posts.add(new Post(content, user.getUsername()));
        FileUtil.writeData(POST_FILE_PATH, posts);
    }

    public void likePost(User user, int postId) {
        for (Post post : posts) {
            if (post.getId() == postId) {
                if (!post.getLikes().contains(user.getUsername())) {
                    post.getLikes().add(user.getUsername());
                    FileUtil.writeData(POST_FILE_PATH, posts);
                }
                break;
            }
        }
    }

    public void commentPost(User user, int postId, String content) {
        for (Post post : posts) {
            if (post.getId() == postId) {
                post.getComments().add(new Comment(content, user.getUsername()));
                FileUtil.writeData(POST_FILE_PATH, posts);
                break;
            }
        }
    }

    public void sendMessage(User sender, String receiverNameDisplay, String content) {
        User receiver = getByNameDisplay(receiverNameDisplay);
        if (receiver != null) {
            messages.add(new Message(sender.getNameDisplay(), receiverNameDisplay, content));
            FileUtil.writeData(MESSAGE_FILE_PATH, messages);
        }
    }

    public List<Message> getMessages(User user, String otherNameDisplay) {
        List<Message> userMessages = new ArrayList<>();
        for (Message message : messages) {
            if ((message.getSenderNameDisplay().equals(user.getNameDisplay()) && message.getReceiverNameDisplay().equalsIgnoreCase(otherNameDisplay)) ||
                    (message.getSenderNameDisplay().equalsIgnoreCase(otherNameDisplay) && message.getReceiverNameDisplay().equals(user.getNameDisplay()))) {
                userMessages.add(message);
            }
        }
        return userMessages;
    }

    public List<Post> getTimeline(User user) {
        List<Post> timeline = new ArrayList<>();
        List<String> friends = user.getFriends();
        friends.add(user.getUsername()); // Include user's own posts
        for (Post post : posts) {
            if (friends.contains(post.getAuthorUsername())) {
                timeline.add(post);
            }
        }
        return timeline;
    }

    public Post getPostById(int postId) {
        for (Post post : posts) {
            if (post.getId() == postId) {
                return post;
            }
        }
        return null;
    }

    public void saveUsers() {
        FileUtil.writeData(USER_FILE_PATH, users);
    }
}