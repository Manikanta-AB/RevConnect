package dao;



import model.Connection;
import java.util.List;

public interface ConnectionDAO {

    boolean sendRequest(int senderId, int receiverId);

    List<Connection> getPendingRequests(int userId);

    boolean updateRequestStatus(int connectionId, String status);

    List<Connection> getConnections(int userId);
    Connection getConnectionById(int connectionId);
    List<Integer> getConnectedUserIds(int userId);
    boolean areConnected(int userId1, int userId2);



}

