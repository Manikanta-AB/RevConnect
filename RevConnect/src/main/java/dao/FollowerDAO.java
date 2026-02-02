package dao;



public interface FollowerDAO {

    boolean follow(int followerId, int followingId);

    boolean unfollow(int followerId, int followingId);
    int getFollowerCount(int userId);
    int getFollowingCount(int userId);
    boolean isFollowing(int followerId, int followingId);

}
