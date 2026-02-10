package service;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FollowerServiceTest {

    private FollowerService followerService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        followerService = new FollowerService();
        userService = new UserService();
    }

    @Test
    void testFollowPersonalAccount() {
        // Create two personal users
        User follower = new User();
        follower.setUsername("follower_" + System.currentTimeMillis());
        follower.setEmail("follower_" + System.currentTimeMillis() + "@test.com");
        follower.setPassword("pass123");
        follower.setUserType("PERSONAL");
        follower.setSecurityQuestion("Q");
        follower.setSecurityAnswer("A");

        User following = new User();
        following.setUsername("following_" + System.currentTimeMillis());
        following.setEmail("following_" + System.currentTimeMillis() + "@test.com");
        following.setPassword("pass123");
        following.setUserType("PERSONAL");
        following.setSecurityQuestion("Q");
        following.setSecurityAnswer("A");

        userService.register(follower);
        userService.register(following);

        // Get their IDs (registration sets them if successful, but we need to retrieve
        // them)
        // Actually UserService.login or a direct DAO call would be better,
        // but let's assume registration succeeds and it's a test environment.
        // We'll use a mocked or separate test DB if possible, but for now we follow
        // existing patterns.

        User registeredFollower = userService.login(follower.getEmail(), follower.getPassword());
        User registeredFollowing = userService.login(following.getEmail(), following.getPassword());

        assertNotNull(registeredFollower);
        assertNotNull(registeredFollowing);

        boolean result = followerService.followUser(registeredFollower.getUserId(), registeredFollowing.getUserId());
        assertTrue(result, "Should be able to follow a personal account");
    }
}
