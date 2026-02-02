package dao.impl;



import config.DBConnection;
import dao.UserDAO;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            LogManager.getLogger(UserDAOImpl.class);

    @Override
    public String getUsernameById(int userId) {

        String sql = "SELECT username FROM users WHERE user_id = ?";

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }

        } catch (Exception e) {
            logger.error("Error during getting username by id", e);
        }
        return "Unknown User";
    }

    @Override
    public User getUserForRecovery(String email) {

        String sql = """
        SELECT user_id, security_question, security_answer
        FROM users WHERE email = ?
    """;

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setSecurityQuestion(rs.getString("security_question"));
                u.setSecurityAnswer(rs.getString("security_answer"));
                return u;
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean registerUser(User user) {
        String sql = """
INSERT INTO users
(username, email, password, user_type, security_question, security_answer)
VALUES (?, ?, ?, ?, ?, ?)
""";


        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            ps.setString(4, user.getUserType());
            ps.setString(5, user.getSecurityQuestion());
            ps.setString(6, user.getSecurityAnswer());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error during registration", e);

        }
        return false;
    }

    @Override
    public User loginUser(String email, String password) {

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setUserType(rs.getString("user_type"));
                user.setFullName(rs.getString("full_name"));
                user.setBio(rs.getString("bio"));
                user.setLocation(rs.getString("location"));
                user.setWebsiteLink(rs.getString("website_link"));
                return user;
            }

        } catch (Exception e) {

            logger.error("Error during login", e);

        }
        return null;
    }
    @Override
    public String getUserTypeById(int userId) {

        String sql = "SELECT user_type FROM users WHERE user_id = ?";

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("user_type");
            }

        } catch (Exception e) {
            logger.error("Error while getting usertype by id", e);

        }
        return null;
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {

        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Error during updating password", e);

        }
        return false;
    }

}
