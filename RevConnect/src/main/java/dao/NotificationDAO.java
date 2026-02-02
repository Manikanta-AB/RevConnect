package dao;


import model.Notification;
import java.util.List;

public interface NotificationDAO {

    boolean createNotification(Notification notification);

    List<Notification> getNotificationsByUser(int userId);

    int getUnreadCount(int userId);

    boolean markAsRead(int notificationId);
}
