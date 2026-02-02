package service;



import dao.NotificationDAO;
import dao.impl.NotificationDAOImpl;
import model.Notification;

import java.util.List;

public class NotificationService {

    private NotificationDAO notificationDAO = new NotificationDAOImpl();

    public void notify(int userId, String type, String message) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setNotificationType(type);
        n.setMessage(message);
        notificationDAO.createNotification(n);
    }

    public List<Notification> viewNotifications(int userId) {
        return notificationDAO.getNotificationsByUser(userId);
    }

    public int getUnreadCount(int userId) {
        return notificationDAO.getUnreadCount(userId);
    }

    public void markAsRead(int notificationId) {
        notificationDAO.markAsRead(notificationId);
    }
}

