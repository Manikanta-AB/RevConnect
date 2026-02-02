import model.User;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserService userService = new UserService();

    @Test
    void testLoginInvalidUser() {
        User user = userService.login("fake@mail.com", "wrong");
        assertNull(user);
    }

    @Test
    void testRegisterUser() {

        User user = new User();
        user.setUsername("junit_uer_" + System.currentTimeMillis());
        user.setEmail("junit_" + System.currentTimeMillis() + "@te.com");
        user.setPassword("test123");
        user.setUserType("PERSONAL");
        user.setSecurityQuestion("What is your favorite color?");
        user.setSecurityAnswer("blue");

        boolean result = userService.register(user);

        assertTrue(result);
    }
}
