package dao.impl;



import config.DBConnection;
import dao.ConnectionDAO;
import model.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConnectionDAOImpl implements ConnectionDAO {

    @Override
    public boolean sendRequest(int senderId, int receiverId) {

        String sql = "INSERT INTO connections (sender_id, receiver_id, status) VALUES (?, ?, 'PENDING')";

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("⚠ Request already exists or error");
        }
        return false;
    }

    @Override
    public List<Connection> getPendingRequests(int userId) {

        List<Connection> list = new ArrayList<>();
        String sql = "SELECT * FROM connections WHERE receiver_id = ? AND status = 'PENDING'";

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Connection c = new Connection();
                c.setConnectionId(rs.getInt("connection_id"));
                c.setSenderId(rs.getInt("sender_id"));
                c.setReceiverId(rs.getInt("receiver_id"));
                c.setStatus(rs.getString("status"));
                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public Connection getConnectionById(int connectionId) {

        String sql = "SELECT * FROM connections WHERE connection_id = ?";

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, connectionId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Connection c = new Connection();
                c.setConnectionId(rs.getInt("connection_id"));
                c.setSenderId(rs.getInt("sender_id"));
                c.setReceiverId(rs.getInt("receiver_id"));
                c.setStatus(rs.getString("status"));
                return c;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean updateRequestStatus(int connectionId, String status) {

        String sql = "UPDATE connections SET status = ? WHERE connection_id = ?";

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, connectionId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Connection> getConnections(int userId) {

        List<Connection> list = new ArrayList<>();
        String sql = """
            SELECT * FROM connections
            WHERE status = 'ACCEPTED'
            AND (sender_id = ? OR receiver_id = ?)
        """;

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Connection c = new Connection();
                c.setConnectionId(rs.getInt("connection_id"));
                c.setSenderId(rs.getInt("sender_id"));
                c.setReceiverId(rs.getInt("receiver_id"));
                c.setStatus(rs.getString("status"));
                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public boolean areConnected(int userId1, int userId2) {

        String sql = """
        SELECT 1 FROM connections
        WHERE (
              sender_id = ? AND receiver_id = ?
           OR sender_id = ? AND receiver_id = ?
        )
        AND status = 'ACCEPTED'
    """;

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Integer> getConnectedUserIds(int userId) {

        List<Integer> ids = new ArrayList<>();

        String sql = """
        SELECT DISTINCT
            CASE
                WHEN sender_id = ? THEN receiver_id
                ELSE sender_id
            END AS connected_user_id
        FROM connections
        WHERE status = 'ACCEPTED'
        AND (sender_id = ? OR receiver_id = ?)
    """;

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ids.add(rs.getInt("connected_user_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ids;
    }

}

