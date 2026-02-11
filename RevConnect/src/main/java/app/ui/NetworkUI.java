package app.ui;

import model.User;
import service.ConnectionService;
import service.FollowerService;

import java.util.List;
import java.util.Scanner;

public class NetworkUI {

    public static void networkMenu(
            Scanner sc,
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
                                " Already following this account or invalid User ID");
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
}
