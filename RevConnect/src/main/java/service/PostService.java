package service;



import dao.PostDAO;
import dao.impl.PostDAOImpl;
import model.Post;

import java.util.List;

public class    PostService {

    private PostDAO postDAO = new PostDAOImpl();
    public int getPostOwnerId(int postId) {
        return postDAO.getPostOwnerId(postId);
    }


    public boolean createPost(Post post) {
        return postDAO.createPost(post);
    }

    public List<Post> viewFeed() {
        return postDAO.getAllPosts();
    }

    public List<Post> viewMyPosts(int userId) {
        return postDAO.getPostsByUser(userId);
    }

    public boolean updatePost(Post post) {
        return postDAO.updatePost(post);
    }

    public boolean deletePost(int postId, int userId) {
        return postDAO.deletePost(postId, userId);
    }

    public boolean pinPost(int postId, int userId) {
        return postDAO.pinPost(postId, userId);
    }
}

