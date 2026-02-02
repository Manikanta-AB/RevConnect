package util;



import model.User;

public class ProfilePrinter {

    public static void printUserProfile(User user,int followers, int following) {

        System.out.println("\n========== USER PROFILE ==========");
        System.out.println("Username      : " + user.getUsername());
        System.out.println("Name          : " + valueOrNA(user.getFullName()));
        System.out.println("Bio           : " + valueOrNA(user.getBio()));
        System.out.println("Location      : " + valueOrNA(user.getLocation()));
        System.out.println("Website       : " + valueOrNA(user.getWebsiteLink()));
        System.out.println("----------------------------------");
        System.out.println("Followers     : " + followers);
        System.out.println("Following     : " + following);
        System.out.println("==================================\n");
    }

    private static String valueOrNA(String value) {
        return (value == null || value.isBlank()) ? "Not set" : value;
    }
}
