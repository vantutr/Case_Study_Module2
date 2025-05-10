package com.social_network.controller;

import com.social_network.model.Post;
import com.social_network.model.User;
import com.social_network.service.UserService;
import com.social_network.view.PostView;
import java.util.List;

public class PostController {
    private final UserService userService;
    private final PostView postView;

    public PostController(UserService userService, PostView postView) {
        this.userService = userService;
        this.postView = postView;
    }

    public void processCreatePost(User currentUser) {
        String content = postView.getPostContentFromUser();
        if (content.isEmpty()) {
            postView.showEmptyInputError("Nội dung bài đăng");
            return;
        }
        userService.createPost(currentUser, content);
        postView.showPostCreated();
    }

    public void processTimelineMenu(User currentUser) {
        List<Post> timeline = userService.getTimeline(currentUser);
        postView.showTimeline(timeline, currentUser);

        if (timeline.isEmpty()) {
            return;
        }

        int choice = postView.showTimelineActionMenuAndGetChoice();
        int postId;
        Post targetPost;

        switch (choice) {
            case 1:
                postId = postView.getPostIdToInteract();
                if (postId < 0 && timeline.isEmpty()) { postView.showInvalidChoice(); break; }
                else if (postId < 0) { postView.showInvalidChoice(); break;}

                targetPost = userService.getPostById(postId);
                if (targetPost == null) {
                    postView.showPostNotFound();
                    break;
                }
                if (userService.likePost(currentUser, postId)) {
                    User postAuthor = userService.getByUsername(targetPost.getAuthorUsername());
                    postView.showPostLiked(postAuthor != null ? postAuthor.getNameDisplay() : targetPost.getAuthorUsername());
                } else {
                    postView.showPostLikeFailed();
                }
                break;
            case 2:
                postId = postView.getPostIdToInteract();
                if (postId < 0 && timeline.isEmpty()) { postView.showInvalidChoice(); break; }
                else if (postId < 0) { postView.showInvalidChoice(); break;}

                targetPost = userService.getPostById(postId);
                if (targetPost == null) {
                    postView.showPostNotFound();
                    break;
                }
                String commentContent = postView.getCommentContentFromUser();
                if (commentContent.isEmpty()) {
                    postView.showEmptyInputError("Nội dung bình luận");
                    break;
                }
                if (userService.commentPost(currentUser, postId, commentContent)) {
                    User postAuthor = userService.getByUsername(targetPost.getAuthorUsername());
                    postView.showCommentAdded(postAuthor != null ? postAuthor.getNameDisplay() : targetPost.getAuthorUsername());
                } else {
                    postView.showCommentFailed();
                }
                break;
            case 3:
                postId = postView.getPostIdToInteract();
                if (postId < 0 && timeline.isEmpty()) { postView.showInvalidChoice(); break; }
                else if (postId < 0) { postView.showInvalidChoice(); break;}

                targetPost = userService.getPostById(postId);
                if (targetPost == null) {
                    postView.showPostNotFound();
                    break;
                }
                if (!targetPost.getAuthorUsername().equals(currentUser.getUsername())) {
                    postView.showActionNotAllowed();
                    break;
                }
                String newPostContent = postView.getNewPostContentFromUser();
                if (newPostContent.isEmpty()) {
                    postView.showEmptyInputError("Nội dung bài đăng mới");
                    break;
                }
                if (userService.editPost(currentUser, postId, newPostContent)) {
                    postView.showPostEdited();
                } else {
                    postView.showActionNotAllowed();
                }
                break;
            case 4:
                postId = postView.getPostIdToInteract();
                if (postId < 0 && timeline.isEmpty()) { postView.showInvalidChoice(); break; }
                else if (postId < 0) { postView.showInvalidChoice(); break;}

                targetPost = userService.getPostById(postId);
                if (targetPost == null) {
                    postView.showPostNotFound();
                    break;
                }
                if (!targetPost.getAuthorUsername().equals(currentUser.getUsername())) {
                    postView.showActionNotAllowed();
                    break;
                }
                if (userService.deletePost(currentUser, postId)) {
                    postView.showPostDeleted();
                } else {
                    postView.showActionNotAllowed();
                }
                break;
            case 0:
                return;
            default:
                postView.showInvalidChoice();
        }
    }
}