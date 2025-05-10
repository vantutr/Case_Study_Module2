package com.social_network.view;

import com.social_network.model.Comment;
import com.social_network.model.Post;
import com.social_network.model.User;
import com.social_network.service.UserService;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class PostView extends BaseView {
    private final UserService userService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    public PostView(Scanner scanner, UserService userService) {
        super(scanner);
        this.userService = userService;
    }

    public String getPostContentFromUser() {
        System.out.print("Nhập nội dung bài đăng của bạn: ");
        return scanner.nextLine().trim();
    }

    public void showPostCreated() {
        System.out.println("Bài đăng đã được tạo thành công!");
    }

    public void showTimeline(List<Post> posts, User currentUser) {
        if (posts.isEmpty()) {
            System.out.println("Timeline của bạn hiện đang trống.");
            return;
        }
        System.out.println("\n--- Timeline ---");
        for (Post post : posts) {
            User author = userService.getByUsername(post.getAuthorUsername());
            String authorNameDisplay = (author != null) ? author.getNameDisplay() : post.getAuthorUsername();

            System.out.println("--------------------------------------------------");
            System.out.printf("ID: %d | Tác giả: %s (@%s) | Thời gian: %s\n",
                    post.getId(), authorNameDisplay, post.getAuthorUsername(), post.getTimestamp().format(dateTimeFormatter));
            System.out.println("Nội dung: " + post.getContent());
            System.out.printf("Lượt thích: %d | Bình luận: %d\n", post.getLikes().size(), post.getComments().size());
            if (currentUser != null && post.getLikes().contains(currentUser.getUsername())) {
                System.out.println("(Bạn đã thích bài này)");
            }

            if (!post.getComments().isEmpty()) {
                System.out.println("  Bình luận:");
                for (Comment comment : post.getComments()) {
                    User commentAuthor = userService.getByUsername(comment.getAuthorUsername());
                    String commentAuthorName = (commentAuthor != null) ? commentAuthor.getNameDisplay() : comment.getAuthorUsername();
                    System.out.printf("    [%s (@%s) - %s]: %s\n",
                            commentAuthorName, comment.getAuthorUsername(),
                            comment.getTimestamp().format(dateTimeFormatter),
                            comment.getContent());
                }
            }
            System.out.println("--------------------------------------------------");
        }
    }

    public int showTimelineActionMenuAndGetChoice() {
        System.out.println("\n--- Tùy chọn Timeline ---");
        System.out.println("1. Thích bài đăng");
        System.out.println("2. Bình luận bài đăng");
        System.out.println("3. Chỉnh sửa bài đăng của bạn");
        System.out.println("4. Xóa bài đăng của bạn");
        System.out.println("0. Quay lại menu chính");
        return getIntInputWithRange("Chọn", 0, 4, null);
    }

    public int getPostIdToInteract() {
        return getIntInput("Nhập ID của bài đăng bạn muốn tương tác: ");
    }

    public void showPostNotFound() {
        System.out.println("Không tìm thấy bài đăng với ID này.");
    }

    public void showPostLiked(String postAuthorName) {
        System.out.println("Bạn đã thích bài đăng của " + postAuthorName + ".");
    }

    public void showPostLikeFailed() {
        System.out.println("Thích bài đăng thất bại (có thể bạn đã thích rồi hoặc bài đăng không tồn tại).");
    }

    public String getCommentContentFromUser() {
        System.out.print("Nhập nội dung bình luận của bạn: ");
        return scanner.nextLine().trim();
    }

    public void showCommentAdded(String postAuthorName) {
        System.out.println("Bạn đã bình luận bài đăng của " + postAuthorName + ".");
    }

    public void showCommentFailed() {
        System.out.println("Bình luận thất bại (bài đăng có thể không tồn tại).");
    }

    public String getNewPostContentFromUser() {
        System.out.print("Nhập nội dung mới cho bài đăng: ");
        return scanner.nextLine().trim();
    }

    public void showPostEdited() {
        System.out.println("Bài đăng đã được chỉnh sửa thành công.");
    }

    public void showPostDeleted() {
        System.out.println("Bài đăng đã được xóa thành công.");
    }

    public void showActionNotAllowed() {
        System.out.println("Lỗi: Bạn không có quyền thực hiện hành động này.");
    }
}