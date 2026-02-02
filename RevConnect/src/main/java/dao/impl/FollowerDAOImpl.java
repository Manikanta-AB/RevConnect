package dao.impl;



import config.DBConnection;
import dao.FollowerDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FollowerDAOImpl implements FollowerDAO {

    @Override
    public boolean follow(int followerId, int followingId) {

        String sql = "INSERT INTO followers (follower_id, following_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, followerId);
            ps.setInt(2, followingId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("⚠ Already following or error");
        }
        return false;
    }
    @Override
    public int getFollowerCount(int userId) {
        String sql = "SELECT COUNT(*) FROM followers WHERE following_id = ?";
        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public boolean isFollowing(int followerId, int followingId) {

        String sql = """
        SELECT 1 FROM followers
        WHERE follower_id = ? AND following_id = ?
    """;

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, followerId);
            ps.setInt(2, followingId);
            ResultSet rs = ps.executeQuery();

            return rs.next(); // true if exists

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public int getFollowingCount(int userId) {
        String sql = "SELECT COUNT(*) FROM followers WHERE follower_id = ?";
        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public boolean unfollow(int followerId, int followingId) {

        String sql = "DELETE FROM followers WHERE follower_id = ? AND following_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, followerId);
            ps.setInt(2, followingId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
