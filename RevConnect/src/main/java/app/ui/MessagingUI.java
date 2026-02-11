package app.ui;

import model.User;
import service.MessageService;
import java.util.Scanner;

public class MessagingUI {

    public static void messagingMenu(Scanner sc, User user, MessageService messageService) {

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
}
