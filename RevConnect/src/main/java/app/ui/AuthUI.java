package app.ui;

import model.User;
import service.UserService;
import java.util.Scanner;

public class AuthUI {

    public static void register(Scanner sc, UserService userService) {
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

    public static User login(Scanner sc, UserService userService) {
        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        User user = userService.login(email, password);

        if (user != null) {
            System.out.println("Login successful");
            System.out.println("Welcome!! " + user.getUsername());
            return user;
        } else {
            System.out.println(" Invalid email or password");
            return null;
        }
    }

    public static void forgotPassword(Scanner sc, UserService userService) {
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
}
