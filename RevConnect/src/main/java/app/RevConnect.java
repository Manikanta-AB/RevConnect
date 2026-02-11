package app;

import app.ui.*;
import service.UserService;
import java.util.Scanner;

public class RevConnect {

    private static UserService userService = new UserService();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        MainMenuUI.showWelcomeMenu(sc, userService);
    }
}
