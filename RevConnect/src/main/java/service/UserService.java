package service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;

public class UserService {
    private static final Logger logger =
            LogManager.getLogger(UserService.class);


    private UserDAO userDAO = new UserDAOImpl();

    public boolean register(User user) {

        if (user.getEmail() == null || user.getPassword() == null) {
            System.out.println(" Email and Password are required");
            return false;
        }
        logger.info("Registering user: {}", user.getEmail());
        return userDAO.registerUser(user);
    }

    public User getUserForRecovery(String email) {
        return userDAO.getUserForRecovery(email);
    }

    public boolean recoverPassword(String email, String answer, String newPassword) {

        User user = userDAO.getUserForRecovery(email);
        if (user == null) return false;

        if (!user.getSecurityAnswer().equalsIgnoreCase(answer)) {
            return false;
        }
        return userDAO.updatePassword(user.getUserId(), newPassword);
    }

    public boolean changePassword(int userId, String newPassword) {
        return userDAO.updatePassword(userId, newPassword);
    }

    public User login(String email, String password) {

        if (email == null || password == null) {
            System.out.println("Invalid login details");
            return null;
        }
        logger.info("Login attempt for email: {}", email);

        return userDAO.loginUser(email, password);
    }

    public String getUsernameById(int id) {
        return userDAO.getUsernameById(id);
    }

    public String getUserTypeById(int followingId) {
        return userDAO.getUserTypeById(followingId);
    }


    public boolean resetPassword(int userId, String newPassword) {
        return userDAO.updatePassword(userId, newPassword);
    }

}

