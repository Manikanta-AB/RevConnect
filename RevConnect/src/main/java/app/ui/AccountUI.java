package app.ui;

import model.CommentView;
import model.Post;
import model.User;
import service.*;
import util.ProfilePrinter;

import java.util.List;
import java.util.Scanner;

public class AccountUI {

    public static void myAccountMenu(
            Scanner sc,
            User loggedInUser,
            ProfileService profileService,
            PostService postService,
            LikeService likeService,
            CommentService commentService,
            FollowerService followerService,
            UserService userService) {
        while (true) {
            System.out.println("-----------------------");
            System.out.println("     MY ACCOUNT ");
            System.out.println("-----------------------");
            System.out.println("1. View My Profile");
            System.out.println("2. Update My Profile");
            System.out.println("3. Search User Profile");
            System.out.println("4. View My Posts");
            System.out.println("5. Change Password");
            System.out.println("6. Back");
            System.out.println("-----------------------");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    User u = profileService.viewOwnProfile(loggedInUser.getUserId());
                    int followers = followerService.getFollowerCount(u.getUserId());
                    int following = followerService.getFollowingCount(u.getUserId());
                    ProfilePrinter.printUserProfile(u, followers, following);

                    List<Post> posts = postService.viewMyPosts(u.getUserId());
                    if (posts.isEmpty()) {
                        System.out.println(" No posts yet");
                        break;
                    }

                    System.out.println("\n========== MY POSTS ==========");
                    for (Post p : posts) {
                        System.out.println("\n----------------------------------------");
                        System.out.println("Post ID   : " + p.getPostId());
                        System.out.println("Content   : " + p.getPostContent());
                        System.out.println("Hashtags  : " + p.getHashtags());
                        System.out.println("Pinned    : " + (p.isPinned() ? "Yes " : "No"));

                        int likes = likeService.getLikeCount(p.getPostId());
                        System.out.println("\n Likes   : " + likes);

                        System.out.println("\n Comments:");
                        List<CommentView> comments = commentService.getCommentsWithUser(p.getPostId());
                        if (comments.isEmpty()) {
                            System.out.println("(No comments yet)");
                        } else {
                            for (CommentView c : comments) {
                                System.out.println("[Comment ID: " + c.getCommentId() + "] " + c.getUsername() + " : "
                                        + c.getCommentText());
                            }
                        }
                        System.out.println("----------------------------------------");
                    }
                }
                case 2 -> {
                    System.out.print("Full Name: ");
                    loggedInUser.setFullName(sc.nextLine());
                    System.out.print("Bio: ");
                    loggedInUser.setBio(sc.nextLine());
                    System.out.print("Location: ");
                    loggedInUser.setLocation(sc.nextLine());
                    profileService.updateProfile(loggedInUser);
                    System.out.println("Profile Updated!");
                }
                case 3 -> {
                    System.out.print("Enter username: ");
                    String uname = sc.nextLine();
                    User u = profileService.searchUser(uname);
                    if (u == null) {
                        System.out.println("User not found");
                        break;
                    }
                    int followers = followerService.getFollowerCount(u.getUserId());
                    int following = followerService.getFollowingCount(u.getUserId());
                    ProfilePrinter.printUserProfile(u, followers, following);
                }
                case 4 -> {
                    List<Post> posts = postService.viewMyPosts(loggedInUser.getUserId());
                    if (posts.isEmpty()) {
                        System.out.println("You have not created any posts yet");
                        break;
                    }
                    for (Post p : posts) {
                        System.out.println("\n----------------------------------------");
                        System.out.println("Post ID   : " + p.getPostId());
                        System.out.println("Author    : " + loggedInUser.getUsername());
                        System.out.println("Content   : " + p.getPostContent());
                        System.out.println("Hashtags  : " + p.getHashtags());
                        System.out.println("Pinned    : " + (p.isPinned() ? "Yes " : "No"));

                        int likes = likeService.getLikeCount(p.getPostId());
                        System.out.println("\n Likes   : " + likes);

                        System.out.println("\n Comments:");
                        List<CommentView> comments = commentService.getCommentsWithUser(p.getPostId());
                        if (comments.isEmpty()) {
                            System.out.println("(No comments yet)");
                        } else {
                            for (CommentView c : comments) {
                                System.out.println("[Comment ID: " + c.getCommentId() + "] " + c.getUsername() + " : "
                                        + c.getCommentText());
                            }
                        }
                        System.out.println("----------------------------------------");
                    }
                }
                case 5 -> changePassword(sc, loggedInUser, userService);
                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    public static void changePassword(Scanner sc, User user, UserService userService) {
        System.out.print("Current Password: ");
        String current = sc.nextLine();
        if (!current.equals(user.getPassword())) {
            System.out.println(" Incorrect current password");
            return;
        }
        System.out.print("New Password: ");
        String newPass = sc.nextLine();
        boolean updated = userService.changePassword(user.getUserId(), newPass);
        if (updated) {
            user.setPassword(newPass);
            System.out.println(" Password changed successfully");
        } else {
            System.out.println(" Failed to change password");
        }
    }

    public static void blockUserMenu(Scanner sc, User user, BlockService blockService) {
        while (true) {
            System.out.println("-----------------------");
            System.out.println("     BLOCK USERS ");
            System.out.println("-----------------------");
            System.out.println("1. Block User");
            System.out.println("2. Unblock User");
            System.out.println("3. View Blocked Users");
            System.out.println("4. Back");
            System.out.println("-----------------------");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    System.out.print("User ID to block: ");
                    int bid = sc.nextInt();
                    sc.nextLine();
                    blockService.blockUser(user.getUserId(), bid);
                    System.out.println("User blocked successfully");
                }
                case 2 -> {
                    System.out.print("User ID to unblock: ");
                    int bid = sc.nextInt();
                    sc.nextLine();
                    blockService.unblockUser(user.getUserId(), bid);
                    System.out.println("User unblocked successfully");
                }
                case 3 -> {
                    var list = blockService.getBlockedUsers(user.getUserId());
                    if (list.isEmpty()) {
                        System.out.println("No blocked users");
                        break;
                    }
                    System.out.println("Blocked Users:");
                    list.forEach(id -> System.out.println("- User ID: " + id));
                }
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    public static void notificationMenu(Scanner sc, User loggedInUser, NotificationService notificationService) {
        while (true) {
            System.out.println("-----------------------");
            System.out.println("     NOTIFICATIONS ");
            System.out.println("-----------------------");
            System.out.println("1. View Notifications");
            System.out.println("2. Mark Notification as Read");
            System.out.println("3. Back");
            System.out.println("-----------------------");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    int unread = notificationService.getUnreadCount(loggedInUser.getUserId());
                    System.out.println(" Unread Notifications: " + unread);
                    var list = notificationService.viewNotifications(loggedInUser.getUserId());
                    for (var n : list) {
                        System.out.println(
                                "ID: " + n.getNotificationId() + " | " + n.getMessage() + " | Read: " + n.isRead());
                    }
                }
                case 2 -> {
                    System.out.print("Notification ID: ");
                    int nid = sc.nextInt();
                    sc.nextLine();
                    notificationService.markAsRead(nid);
                    System.out.println(" Notification marked as read");
                }
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
