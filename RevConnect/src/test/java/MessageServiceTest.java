

import org.junit.jupiter.api.Test;
import service.MessageService;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest {

    MessageService messageService = new MessageService();

    @Test
    void testSendMessageFailsIfNotConnected() {
        boolean result = messageService.sendMessage(1, 99, "Hello");
        assertFalse(result);
    }
}
