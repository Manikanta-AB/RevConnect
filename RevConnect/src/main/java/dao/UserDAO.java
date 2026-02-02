package dao;



import model.User;

public interface UserDAO {

    boolean registerUser(User user);
    User loginUser(String email, String password);
    String getUsernameById(int userId);
    String getUserTypeById(int userId);

    User getUserForRecovery(String email);
    boolean updatePassword(int userId, String newPassword);

}

