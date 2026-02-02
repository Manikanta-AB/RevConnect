package dao.impl;

import config.DBConnection;
import dao.BlockDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BlockDAOImpl implements BlockDAO {

    @Override
    public boolean blockUser(int blockerId, int blockedId) {

        String sql = """
            INSERT IGNORE INTO blocked_users (blocker_id, blocked_id)
            VALUES (?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blockerId);
            ps.setInt(2, blockedId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unblockUser(int blockerId, int blockedId) {

        String sql = """
            DELETE FROM blocked_users
            WHERE blocker_id = ? AND blocked_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blockerId);
            ps.setInt(2, blockedId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isBlocked(int blockerId, int blockedId) {

        String sql = """
            SELECT 1 FROM blocked_users
            WHERE blocker_id = ? AND blocked_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blockerId);
            ps.setInt(2, blockedId);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Integer> getBlockedUsers(int blockerId) {

        List<Integer> list = new ArrayList<>();

        String sql = "SELECT blocked_id FROM blocked_users WHERE blocker_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, blockerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("blocked_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

