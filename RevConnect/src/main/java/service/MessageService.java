package service;



import dao.MessageDAO;
import dao.impl.MessageDAOImpl;
import model.Message;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO = new MessageDAOImpl();
    private ConnectionService connectionService = new ConnectionService();
    private BlockService blockService = new BlockService();

    public boolean sendMessage(int senderId, int receiverId, String text) {

        if (!connectionService.areConnected(senderId, receiverId)) {
            return false;
        }

        if (blockService.isBlocked(receiverId, senderId)) {
            return false;
        }

        return messageDAO.sendMessage(senderId, receiverId, text);
    }

    public List<Message> getConversation(int userId, int otherUserId) {
        return messageDAO.getConversation(userId, otherUserId);
    }

    public boolean markAsRead(int messageId) {
        return messageDAO.markAsRead(messageId);
    }

    public boolean markAsUnread(int messageId) {
        return messageDAO.markAsUnread(messageId);
    }

    public boolean deleteConversation(int userId, int otherUserId) {
        return messageDAO.deleteConversation(userId, otherUserId);
    }
}

