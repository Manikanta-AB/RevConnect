package dao;



public interface LikeDAO {

    boolean likePost(int postId, int userId);

    boolean unlikePost(int postId, int userId);

    int getLikeCount(int postId);
}

