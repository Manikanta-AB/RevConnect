package dao;





import model.Message;
import java.util.List;

public interface MessageDAO {

    boolean sendMessage(int senderId, int receiverId, String message);

    List<Message> getConversation(int userId, int otherUserId);

    boolean markAsRead(int messageId);

    boolean markAsUnread(int messageId);

    boolean deleteConversation(int userId, int otherUserId);
}
