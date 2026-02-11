package app.ui;

import model.User;
import service.UserService;
import java.util.Scanner;

public class MainMenuUI {

    public static void showWelcomeMenu(Scanner sc, UserService userService) {
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
                case 1 -> AuthUI.register(sc, userService);
                case 2 -> {
                    User user = AuthUI.login(sc, userService);
                    if (user != null) {
                        DashboardUI.showDashboard(sc, user, userService);
                    }
                }
                case 3 -> AuthUI.forgotPassword(sc, userService);
                case 4 -> {
                    System.out.println("Thank you for using RevConnect");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
