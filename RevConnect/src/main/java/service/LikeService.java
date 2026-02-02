package service;


import dao.LikeDAO;
import dao.impl.LikeDAOImpl;

public class LikeService {

    private LikeDAO likeDAO = new LikeDAOImpl();
    NotificationService notificationService=new NotificationService();

    public void likePost(int postId, int userId) {
        likeDAO.likePost(postId, userId);
        notificationService.notify(
                userId,
                "POST_LIKE",
                "Someone liked your post"
        );

    }

    public void unlikePost(int postId, int userId) {
        likeDAO.unlikePost(postId, userId);
    }

    public int getLikeCount(int postId) {
        return likeDAO.getLikeCount(postId);
    }
}
