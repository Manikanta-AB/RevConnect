package service;



import dao.FollowerDAO;
import dao.impl.FollowerDAOImpl;

public class FollowerService {

    private FollowerDAO followerDAO = new FollowerDAOImpl();
     NotificationService notificationService=new NotificationService();
     UserService userService=new UserService();
    public boolean followUser(int followerId, int followingId) {

        // Cannot follow self
        if (followerId == followingId) {
            return false;
        }

        // Check user type
        String userType = userService.getUserTypeById(followingId);

        if (!"CREATOR".equals(userType) && !"BUSINESS".equals(userType)) {
            return false; // ❌ personal account
        }

        boolean followed = followerDAO.follow(followerId, followingId);

        if (followed) {
            notificationService.notify(
                    followingId,
                    "NEW_FOLLOWER",
                    "You have a new follower"
            );
        }

        return followed;
    }

    public int getFollowerCount(int userId) {
        return followerDAO.getFollowerCount(userId);
    }

    public int getFollowingCount(int userId) {
        return followerDAO.getFollowingCount(userId);
    }

    public boolean unfollowUser(int followerId, int followingId) {

        // Cannot unfollow self
        if (followerId == followingId) {
            return false;
        }

        // Check if follow exists
        if (!followerDAO.isFollowing(followerId, followingId)) {
            return false; // ❌ not following
        }

        boolean unfollowed = followerDAO.unfollow(followerId, followingId);

        return unfollowed;
    }

}

