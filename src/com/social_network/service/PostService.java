package com.social_network.service;

import com.social_network.model.Comment;
import com.social_network.model.Post;
import com.social_network.model.User;
import com.social_network.util.FileUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PostService {
    private final String POST_FILE_PATH = "data/posts.dat";
    private List<Post> posts;
    private static int POST_AUTO_ID = 1;
    private static int COMMENT_AUTO_ID = 1;
    private final NotificationService notificationService;

    public PostService(NotificationService notificationService) {
        this.notificationService = notificationService;
        loadData();
    }

    private void loadData() {
        try {
            posts = FileUtil.readData(POST_FILE_PATH);
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
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc dữ liệu bài đăng từ " + POST_FILE_PATH + ": " + e.getMessage());
            posts = new ArrayList<>();
            POST_AUTO_ID = 1;
            COMMENT_AUTO_ID = 1;
        }
    }

    private void saveData() {
        try {
            FileUtil.writeData(POST_FILE_PATH, posts);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu dữ liệu bài đăng vào " + POST_FILE_PATH + ": " + e.getMessage());
        }
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
                    notificationService.addNotification(post.getAuthorUsername(),
                            user.getNameDisplay() + " đã thích bài đăng của bạn (ID: " + postId + ").");
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
                notificationService.addNotification(post.getAuthorUsername(),
                        user.getNameDisplay() + " đã bình luận bài đăng của bạn (ID: " + postId + ").");
            }
            saveData();
            return true;
        }
        return false;
    }

    public List<Post> getTimeline(User user, List<String> friendUsernames) {
        List<String> peopleToFollow = new ArrayList<>(friendUsernames);
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
}