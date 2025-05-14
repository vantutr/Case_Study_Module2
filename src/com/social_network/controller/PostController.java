package com.social_network.controller;

import com.social_network.model.Post;
import com.social_network.model.User;
import com.social_network.service.PostService;
import com.social_network.view.PostView;

import java.util.List;

public class PostController {
    private final PostService postService;
    private final PostView postView;

    public PostController(PostService postService, PostView postView) {
        this.postService = postService;
        this.postView = postView;
    }

    public void processCreatePost(User currentUser) {
        String content = postView.getPostContentFromUser();
        if (content.isEmpty()) {
            postView.showEmptyInputError("Nội dung bài đăng");
            return;
        }
        postService.createPost(currentUser, content);
        postView.showPostCreated();
    }

    public void processTimelineMenu(User currentUser) {
        List<Post> timeline = postService.getTimeline(currentUser, currentUser.getFriends());
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

                targetPost = postService.getPostById(postId);
                if (targetPost == null) {
                    postView.showPostNotFound();
                    break;
                }
                if (postService.likePost(currentUser, postId)) {
                    postView.showPostLiked(targetPost.getAuthorUsername());
                } else {
                    postView.showPostLikeFailed();
                }
                break;
            case 2:
                postId = postView.getPostIdToInteract();
                if (postId < 0 && timeline.isEmpty()) { postView.showInvalidChoice(); break; }
                else if (postId < 0) { postView.showInvalidChoice(); break;}

                targetPost = postService.getPostById(postId);
                if (targetPost == null) {
                    postView.showPostNotFound();
                    break;
                }
                String commentContent = postView.getCommentContentFromUser();
                if (commentContent.isEmpty()) {
                    postView.showEmptyInputError("Nội dung bình luận");
                    break;
                }
                if (postService.commentPost(currentUser, postId, commentContent)) {
                    postView.showCommentAdded(targetPost.getAuthorUsername());
                } else {
                    postView.showCommentFailed();
                }
                break;
            case 3:
                postId = postView.getPostIdToInteract();
                if (postId < 0 && timeline.isEmpty()) { postView.showInvalidChoice(); break; }
                else if (postId < 0) { postView.showInvalidChoice(); break;}

                targetPost = postService.getPostById(postId);
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
                if (postService.editPost(currentUser, postId, newPostContent)) {
                    postView.showPostEdited();
                } else {
                    postView.showActionNotAllowed();
                }
                break;
            case 4:
                postId = postView.getPostIdToInteract();
                if (postId < 0 && timeline.isEmpty()) { postView.showInvalidChoice(); break; }
                else if (postId < 0) { postView.showInvalidChoice(); break;}

                targetPost = postService.getPostById(postId);
                if (targetPost == null) {
                    postView.showPostNotFound();
                    break;
                }
                if (!targetPost.getAuthorUsername().equals(currentUser.getUsername())) {
                    postView.showActionNotAllowed();
                    break;
                }
                if (postService.deletePost(currentUser, postId)) {
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