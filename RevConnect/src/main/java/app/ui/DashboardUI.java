package app.ui;

import model.User;
import service.*;
import java.util.Scanner;

public class DashboardUI {

    public static void showDashboard(Scanner sc, User loggedInUser, UserService userService) {
        MessageService messageService = new MessageService();
        BlockService blockService = new BlockService();
        ProfileService profileService = new ProfileService();
        PostService postService = new PostService();
        LikeService likeService = new LikeService();
        CommentService commentService = new CommentService();
        ConnectionService connectionService = new ConnectionService();
        NotificationService notificationService = new NotificationService();
        FollowerService followerService = new FollowerService();

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
                case 1 -> AccountUI.myAccountMenu(sc, loggedInUser, profileService, postService, likeService,
                        commentService, followerService, userService);
                case 2 -> PostUI.postMenu(sc, loggedInUser, postService, likeService, commentService, profileService);
                case 3 -> NetworkUI.networkMenu(sc, loggedInUser, connectionService, followerService);
                case 4 -> AccountUI.notificationMenu(sc, loggedInUser, notificationService);
                case 5 -> MessagingUI.messagingMenu(sc, loggedInUser, messageService);
                case 6 -> AccountUI.blockUserMenu(sc, loggedInUser, blockService);
                case 7 -> {
                    System.out.println("Logged out successfully");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
