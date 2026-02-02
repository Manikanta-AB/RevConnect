package dao;



import model.Comment;
import model.CommentView;

import java.util.List;

public interface CommentDAO {

    boolean addComment(Comment comment);

    List<Comment> getCommentsByPost(int postId);

    boolean deleteComment(int commentId, int userId);
    List<CommentView> getCommentsWithUserByPost(int postId);

}
