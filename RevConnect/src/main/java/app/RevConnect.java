package app;

import model.Comment;
import model.CommentView;
import model.Post;
import model.User;
import service.*;
import util.ProfilePrinter;

import java.util.List;
import java.util.Scanner;

public class RevConnect {

    private static UserService userService = new UserService();
    private static Scanner sc = new Scanner(System.in);
    private static FollowerService followerService = new FollowerService();

    public static void main(String[] args) {

        while (true) {
            System.out.println(" ______________________________________");
            System.out.println("|             RevConnect               |");
            System.out.println("|______________________________________|");
            System.out.println("|  1. Register                         |");
            System.out.println("|  2. Login                            |");
            System.out.println("|  3. Forgot Password                  |");
            System.out.println("|  4. Exit                             |");
            System.out.println("|______________________________________|");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

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
                case 4: {
                    System.out.println("Thank you for using RevConnect");
                    System.exit(0);

                }

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void userDashboard(User loggedInUser) {
        MessageService messageService = new MessageService();
        BlockService blockService = new BlockService();

        ProfileService profileService = new ProfileService();
        PostService postService = new PostService();
        LikeService likeService = new LikeService();
        CommentService commentService = new CommentService();
        ConnectionService connectionService = new ConnectionService();
        FollowerService followerService = new FollowerService();
        NotificationService notificationService = new NotificationService();

        while (true) {
            System.out.println("-----------------------");
            System.out.println("     USER DASHBOARD ");
            System.out.println("-----------------------");
            System.out.println("1. My Account");
            System.out.println("2. Posts");
            System.out.println("3. My Network");
            System.out.println("4. Notifications");
            System.out.println("5. Messaging");
            System.out.println("6. Block Users");
            System.out.println("7. Logout");
            System.out.println("-----------------------");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> myAccountMenu(loggedInUser, profileService, postService, likeService, commentService);
                case 2 -> postMenu(loggedInUser, postService, likeService, commentService, profileService);
                case 3 -> networkMenu(loggedInUser, connectionService, followerService);
                case 4 -> notificationMenu(loggedInUser, notificationService);
                case 5 -> messagingMenu(loggedInUser, messageService);
                case 6 -> blockUserMenu(loggedInUser, blockService);
                case 7 -> {
                    System.out.println("Logged out successfully");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void myAccountMenu(
            User loggedInUser,
            ProfileService profileService,
            PostService postService,
            LikeService likeService,
            CommentService commentService) {
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
                    // 1. Profile info
                    User u = profileService.viewOwnProfile(loggedInUser.getUserId());

                    int followers = followerService.getFollowerCount(u.getUserId());
                    int following = followerService.getFollowingCount(u.getUserId());

                    ProfilePrinter.printUserProfile(u, followers, following);

                    // 2. My posts
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
                        return;
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
                case 5 -> changePassword(loggedInUser);

                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void postMenu(
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
                        System.out.println("Author    : " + author.getUsername());

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

    private static void forgotPassword() {

        System.out.print("Enter registered email: ");
        String email = sc.nextLine();

        User user = userService.getUserForRecovery(email);

        if (user == null) {
            System.out.println(" Email not found");
            return;
        }

        System.out.println(" Security Question:");
        System.out.println(user.getSecurityQuestion());

        System.out.print("Your Answer: ");
        String ans = sc.nextLine();

        System.out.print("New Password: ");
        String newPass = sc.nextLine();

        boolean success = userService.recoverPassword(email, ans, newPass);

        System.out.println(success
                ? "Password reset successful. Please login."
                : "Incorrect answer");

    }

    private static void blockUserMenu(User user, BlockService blockService) {

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

    private static void changePassword(User user) {

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

    private static void messagingMenu(User user, MessageService messageService) {

        while (true) {
            System.out.println("-----------------------");
            System.out.println("     MESSAGING ");
            System.out.println("-----------------------");
            System.out.println("1. Send Message");
            System.out.println("2. View Conversation");
            System.out.println("3. Mark Message as Read");
            System.out.println("4. Mark Message as Unread");
            System.out.println("5. Delete Conversation");
            System.out.println("6. Back");
            System.out.println("-----------------------");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1 -> {
                    System.out.print("Send to User ID: ");
                    int rid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Message: ");
                    String msg = sc.nextLine();

                    boolean sent = messageService.sendMessage(
                            user.getUserId(), rid, msg);

                    System.out.println(sent
                            ? " Message sent"
                            : " Cannot send message (not connected / blocked)");
                }

                case 2 -> {
                    System.out.print("Conversation with User ID: ");
                    int otherId = sc.nextInt();
                    sc.nextLine();

                    var msgs = messageService.getConversation(
                            user.getUserId(), otherId);

                    if (msgs.isEmpty()) {
                        System.out.println("No messages found");
                        break;
                    }

                    for (var m : msgs) {
                        System.out.println(
                                (m.isRead() ? "✔" : "✉") +
                                        " [ID: " + m.getMessageId() + "] " +
                                        m.getMessageText());
                    }
                }

                case 3 -> {
                    System.out.print("Message ID: ");
                    int mid = sc.nextInt();
                    sc.nextLine();
                    messageService.markAsRead(mid);
                    System.out.println("Message marked as read");
                }

                case 4 -> {
                    System.out.print("Message ID: ");
                    int mid = sc.nextInt();
                    sc.nextLine();
                    messageService.markAsUnread(mid);
                    System.out.println("Message marked as unread");
                }

                case 5 -> {
                    System.out.print("Delete conversation with User ID: ");
                    int oid = sc.nextInt();
                    sc.nextLine();
                    messageService.deleteConversation(user.getUserId(), oid);
                    System.out.println("Conversation deleted");
                }

                case 6 -> {
                    return;
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void networkMenu(
            User loggedInUser,
            ConnectionService connectionService,
            FollowerService followerService) {
        while (true) {
            System.out.println("-----------------------");
            System.out.println("     MY NETWORK ");
            System.out.println("-----------------------");
            System.out.println("1. Send Connection Request");
            System.out.println("2. View Pending Requests");
            System.out.println("3. Accept Request");
            System.out.println("4. Reject Request");
            System.out.println("5. View Connections");
            System.out.println("6. Follow User");
            System.out.println("7. Unfollow User");
            System.out.println("8. Back");
            System.out.println("-----------------------");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> {
                    System.out.print("Enter User ID to connect: ");
                    int receiverId = sc.nextInt();
                    sc.nextLine();

                    boolean sent = connectionService.sendRequest(
                            loggedInUser.getUserId(),
                            receiverId);

                    if (sent) {
                        System.out.println("Connection request sent successfully");
                    } else {
                        System.out.println(" Cannot send request (self / duplicate / invalid user)");
                    }
                }
                case 2 -> {
                    var list = connectionService.viewPending(loggedInUser.getUserId());

                    if (list.isEmpty()) {
                        System.out.println("ℹ No pending connection requests");
                        break;
                    }

                    System.out.println("Pending Connection Requests:");
                    for (var c : list) {
                        System.out.println(
                                "Connection ID: " + c.getConnectionId() +
                                        " | From User ID: " + c.getSenderId());
                    }
                }

                case 3 -> {
                    System.out.print("Connection ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    boolean accepted = connectionService.acceptRequest(id);

                    if (accepted) {
                        System.out.println("Connection request accepted successfully");
                    } else {
                        System.out.println("Invalid connection ID or already processed");
                    }
                }

                case 4 -> {
                    System.out.print("Connection ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    boolean rejected = connectionService.rejectRequest(id);

                    if (rejected) {
                        System.out.println(" Connection request rejected successfully");
                    } else {
                        System.out.println(" Invalid connection ID or already processed");
                    }
                }

                case 5 -> {
                    List<String> connections = connectionService.viewMyConnections(loggedInUser.getUserId());

                    if (connections.isEmpty()) {
                        System.out.println("ℹ You have no connections yet");
                        break;
                    }

                    System.out.println("\n=====  My Connections =====");
                    for (String username : connections) {
                        System.out.println("- " + username);
                    }
                }

                case 6 -> {
                    System.out.print("User ID to follow: ");
                    int followId = sc.nextInt();
                    sc.nextLine();

                    boolean success = followerService.followUser(
                            loggedInUser.getUserId(),
                            followId);

                    if (success) {
                        System.out.println("You are now following this account");
                    } else {
                        System.out.println(
                                " You can follow only Creator or Business accounts (or already following)");
                    }
                }

                case 7 -> {
                    System.out.print("User ID to unfollow: ");
                    int unfollowId = sc.nextInt();
                    sc.nextLine();

                    boolean success = followerService.unfollowUser(
                            loggedInUser.getUserId(),
                            unfollowId);

                    if (success) {
                        System.out.println(" You have unfollowed the user successfully");
                    } else {
                        System.out.println("You are not following this user");
                    }
                }
                case 8 -> {
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void notificationMenu(User loggedInUser, NotificationService notificationService) {
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
                                "ID: " + n.getNotificationId() +
                                        " | " + n.getMessage() +
                                        " | Read: " + n.isRead());
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

    private static void register() {
        User user = new User();

        System.out.print("Username: ");
        user.setUsername(sc.nextLine());

        System.out.print("Email: ");
        user.setEmail(sc.nextLine());

        System.out.print("Password: ");
        user.setPassword(sc.nextLine());

        System.out.print("Security Question (e.g. Your first school?): ");
        user.setSecurityQuestion(sc.nextLine());

        System.out.print("Security Answer: ");
        user.setSecurityAnswer(sc.nextLine());

        System.out.print("User Type (PERSONAL / CREATOR / BUSINESS): ");
        user.setUserType(sc.nextLine().toUpperCase());

        boolean success = userService.register(user);

        if (success) {
            System.out.println("Registration successful");
        } else {
            System.out.println(" Registration failed");
        }
    }

    private static void login() {

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        User user = userService.login(email, password);

        if (user != null) {
            System.out.println("Login successful");
            System.out.println("Welcome!! " + user.getUsername());
            // Next step: user dashboard
            userDashboard(user);
        } else {
            System.out.println(" Invalid email or password");
        }
    }
}
