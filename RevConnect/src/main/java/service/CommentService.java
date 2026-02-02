package service;



import dao.CommentDAO;
import dao.impl.CommentDAOImpl;
import model.Comment;
import model.CommentView;
import model.User;

import java.util.List;

public class CommentService {

    private CommentDAO commentDAO = new CommentDAOImpl();
    private PostService postService=new PostService();
    NotificationService notificationService=new NotificationService();
    User user =new User();

    public void addComment(Comment comment) {

        // 1. Add comment
        boolean added = commentDAO.addComment(comment);

        if (!added) {
            System.out.println("❌ Failed to add comment");
            return;
        }

        // 2. Find post owner
        int postOwnerId = postService.getPostOwnerId(comment.getPostId());

        if (postOwnerId == -1) {
            System.out.println("⚠ Invalid post for notification");
            return;
        }

        // 3. Do NOT notify self
        if (postOwnerId == comment.getUserId()) {
            return;
        }

        // 4. Send notification to post owner
        notificationService.notify(
                postOwnerId,                    // ✅ CORRECT USER ID
                "POST_COMMENT",
                "Someone commented on your post"
        );
    }

    public List<CommentView> getCommentsWithUser(int postId) {
        return commentDAO.getCommentsWithUserByPost(postId);
    }


    public List<Comment> viewComments(int postId) {
        return commentDAO.getCommentsByPost(postId);
    }

    public boolean deleteComment(int commentId, int userId) {
        return commentDAO.deleteComment(commentId, userId);
    }


}

