package dao;



import model.User;

public interface ProfileDAO {

    User getUserProfile(int userId);

    boolean updateUserProfile(User user);

    User getUserByUsername(String username);
}
