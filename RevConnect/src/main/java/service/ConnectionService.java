package service;

import dao.ConnectionDAO;
import dao.impl.ConnectionDAOImpl;
import model.Connection;
import model.User;
import org.w3c.dom.Notation;

import java.util.ArrayList;
import java.util.List;

public class ConnectionService {

    private ConnectionDAO connectionDAO = new ConnectionDAOImpl();
    NotificationService notificationService=new NotificationService();
    UserService userService=new UserService();
    User user=new User();
    public boolean areConnected(int userId1, int userId2) {
        return connectionDAO.areConnected(userId1, userId2);
    }

    public boolean sendRequest(int senderId, int receiverId) {

        // Prevent self-request
        if (senderId == receiverId) {
            return false;
        }

        boolean inserted = connectionDAO.sendRequest(senderId, receiverId);

        // Send notification ONLY if DB insert succeeded
        if (inserted) {
            notificationService.notify(
                    receiverId,
                    "CONNECTION_REQUEST",
                    "You received a new connection request"
            );
        }

        return inserted;
    }


    public List<Connection> viewPending(int userId) {
        return connectionDAO.getPendingRequests(userId);
    }


    public boolean acceptRequest(int connectionId) {

        Connection c = connectionDAO.getConnectionById(connectionId);

        if (c == null) {
            return false;
        }

        boolean updated = connectionDAO.updateRequestStatus(
                connectionId,
                "ACCEPTED"
        );

        if (updated) {
            notificationService.notify(
                    c.getSenderId(),
                    "CONNECTION_ACCEPTED",
                    "Your connection request was accepted"
            );
        }

        return updated;
    }



    public boolean rejectRequest(int connectionId) {

        Connection c = connectionDAO.getConnectionById(connectionId);

        if (c == null) {
            return false;
        }

        boolean updated = connectionDAO.updateRequestStatus(
                connectionId,
                "REJECTED"
        );

        // (Optional) notify sender
        if (updated) {
            notificationService.notify(
                    c.getSenderId(),
                    "CONNECTION_REJECTED",
                    "Your connection request was rejected"
            );
        }

        return updated;
    }


    public List<String> viewMyConnections(int userId) {

        List<Integer> ids = connectionDAO.getConnectedUserIds(userId);
        List<String> usernames = new ArrayList<>();

        for (int id : ids) {
            usernames.add(userService.getUsernameById(id));
        }

        return usernames;
    }

}

