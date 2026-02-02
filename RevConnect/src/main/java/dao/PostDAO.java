package dao;



import model.Post;
import java.util.List;

public interface PostDAO {

    boolean createPost(Post post);
    int getPostOwnerId(int postId);


    List<Post> getAllPosts();

    List<Post> getPostsByUser(int userId);

    boolean updatePost(Post post);

    boolean deletePost(int postId, int userId);

    boolean pinPost(int postId, int userId);
}

