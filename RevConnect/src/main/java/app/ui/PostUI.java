package app.ui;

import model.Comment;
import model.CommentView;
import model.Post;
import model.User;
import service.CommentService;
import service.LikeService;
import service.PostService;
import service.ProfileService;

import java.util.List;
import java.util.Scanner;

public class PostUI {

    public static void postMenu(
            Scanner sc,
            User loggedInUser,
            PostService postService,
            LikeService likeService,
            CommentService commentService,
            ProfileService profileService) {
        while (true) {
            System.out.println("-----------------------");
            System.out.println("     POSTS ");
            System.out.println("-----------------------");
            System.out.println("1. Create Post");
            System.out.println("2. View Feed");
            System.out.println("3. View My Posts");
            System.out.println("4. Edit My Post");
            System.out.println("5. Delete My Post");
            System.out.println("6. Pin My Post");
            System.out.println("7. Like Post");
            System.out.println("8. Unlike Post");
            System.out.println("9. View Comments");
            System.out.println("10. Add Comment");
            System.out.println("11. Delete Comment");
            System.out.println("12. Back");
            System.out.println("-----------------------");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    Post post = new Post();
                    post.setUserId(loggedInUser.getUserId());

                    System.out.print("Enter post content: ");
                    post.setPostContent(sc.nextLine());

                    System.out.print("Enter hashtags (comma separated): ");
                    post.setHashtags(sc.nextLine());

                    post.setPinned(false);
                    postService.createPost(post);
                    System.out.println("Post created!");
                }
                case 2 -> {
                    List<Post> posts = postService.viewFeed();

                    for (Post p : posts) {

                        System.out.println("\n----------------------------------------");
                        System.out.println("Post ID   : " + p.getPostId());

                        // Get post author name
                        User author = profileService.viewOwnProfile(p.getUserId());
                        System.out.println("Author    : " + (author != null ? author.getUsername() : "Unknown"));

                        System.out.println("Content   : " + p.getPostContent());
                        System.out.println("Hashtags  : " + p.getHashtags());

                        // Likes
                        int likes = likeService.getLikeCount(p.getPostId());
                        System.out.println("\n Likes   : " + likes);

                        // Comments
                        System.out.println("\n Comments:");
                        List<CommentView> comments = commentService.getCommentsWithUser(p.getPostId());

                        if (comments.isEmpty()) {
                            System.out.println("(No comments yet)");
                        } else {
                            for (CommentView c : comments) {
                                System.out.println(
                                        "[Comment ID: " + c.getCommentId() + "] "
                                                + c.getUsername() + " : "
                                                + c.getCommentText());

                            }
                        }

                        System.out.println("----------------------------------------");
                    }
                }
                case 3 -> {
                    List<Post> posts = postService.viewMyPosts(loggedInUser.getUserId());

                    if (posts.isEmpty()) {
                        System.out.println(" You have not created any posts yet");
                        break;
                    }

                    for (Post p : posts) {

                        System.out.println("\n----------------------------------------");
                        System.out.println("Post ID   : " + p.getPostId());
                        System.out.println("Author    : " + loggedInUser.getUsername());
                        System.out.println("Content   : " + p.getPostContent());
                        System.out.println("Hashtags  : " + p.getHashtags());
                        System.out.println("Pinned    : " + (p.isPinned() ? "Yes " : "No"));

                        // Likes
                        int likes = likeService.getLikeCount(p.getPostId());
                        System.out.println("\n Likes   : " + likes);

                        // Comments
                        System.out.println("\n Comments:");
                        List<CommentView> comments = commentService.getCommentsWithUser(p.getPostId());

                        if (comments.isEmpty()) {
                            System.out.println("(No comments yet)");
                        } else {
                            for (CommentView c : comments) {
                                System.out.println(
                                        "[Comment ID: " + c.getCommentId() + "] "
                                                + c.getUsername() + " : "
                                                + c.getCommentText());
                            }
                        }

                        System.out.println("----------------------------------------");
                    }
                }

                case 4 -> {
                    System.out.print("Post ID: ");
                    int postId = sc.nextInt();
                    sc.nextLine();

                    Post post = new Post();
                    post.setPostId(postId);
                    post.setUserId(loggedInUser.getUserId());

                    System.out.print("New content: ");
                    post.setPostContent(sc.nextLine());

                    System.out.print("New hashtags: ");
                    post.setHashtags(sc.nextLine());

                    boolean updated = postService.updatePost(post);

                    if (updated) {
                        System.out.println(" Post updated successfully");
                    } else {
                        System.out.println("You are not allowed to update this post");
                    }

                }
                case 5 -> {
                    System.out.print("Post ID: ");
                    int postId = sc.nextInt();
                    sc.nextLine();

                    boolean deleted = postService.deletePost(postId, loggedInUser.getUserId());

                    if (deleted) {
                        System.out.println("Post deleted successfully");
                    } else {
                        System.out.println(" You are not allowed to delete this post or post does not exist");
                    }
                }

                case 6 -> {
                    if (!loggedInUser.getUserType().equals("PERSONAL")) {
                        System.out.print("Post ID to pin: ");
                        int postId = sc.nextInt();
                        sc.nextLine();

                        postService.pinPost(postId, loggedInUser.getUserId());
                        System.out.println(" Post pinned");
                    } else {
                        System.out.println("Only Creator/Business can pin posts");
                    }
                }
                case 7 -> {
                    System.out.print("Post ID: ");
                    int postId = sc.nextInt();
                    sc.nextLine();

                    likeService.likePost(postId, loggedInUser.getUserId());
                    System.out.println(" Post liked");
                }
                case 8 -> {
                    System.out.print("Post ID: ");
                    int postId = sc.nextInt();
                    sc.nextLine();

                    likeService.unlikePost(postId, loggedInUser.getUserId());
                    System.out.println(" Like removed");
                }
                case 9 -> {
                    System.out.print("Post ID: ");
                    int postId = sc.nextInt();
                    sc.nextLine();

                    List<Comment> comments = commentService.viewComments(postId);
                    for (Comment c : comments) {
                        System.out.println("Comment ID: " + c.getCommentId());
                        System.out.println("User ID: " + c.getUserId());
                        System.out.println("Text: " + c.getCommentText());
                        System.out.println("-------------------");
                    }
                }
                case 10 -> {
                    Comment comment = new Comment();

                    System.out.print("Post ID: ");
                    comment.setPostId(sc.nextInt());
                    sc.nextLine();

                    comment.setUserId(loggedInUser.getUserId());
                    System.out.print("Comment: ");
                    comment.setCommentText(sc.nextLine());

                    commentService.addComment(comment);
                    System.out.println("Comment added");
                }
                case 11 -> {
                    System.out.print("Comment ID: ");
                    int commentId = sc.nextInt();
                    sc.nextLine();

                    boolean deleted = commentService.deleteComment(
                            commentId,
                            loggedInUser.getUserId());

                    if (deleted) {
                        System.out.println(" Comment deleted successfully");
                    } else {
                        System.out.println("You can delete only your own comments or comment does not exist");
                    }
                }
                case 12 -> {
                    return;
                }
                default -> System.out.println(" Invalid choice");
            }
        }
    }
}
