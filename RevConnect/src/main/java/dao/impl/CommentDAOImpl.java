package dao.impl;



import config.DBConnection;
import dao.CommentDAO;
import model.Comment;
import model.CommentView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommentDAOImpl implements CommentDAO {

    @Override
    public boolean addComment(Comment comment) {

        String sql = "INSERT INTO comments (post_id, user_id, comment_text) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, comment.getPostId());
            ps.setInt(2, comment.getUserId());
            ps.setString(3, comment.getCommentText());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Comment> getCommentsByPost(int postId) {

        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Comment c = new Comment();
                c.setCommentId(rs.getInt("comment_id"));
                c.setPostId(rs.getInt("post_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setCommentText(rs.getString("comment_text"));
                comments.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }
    @Override
    public List<CommentView> getCommentsWithUserByPost(int postId) {

        List<CommentView> list = new ArrayList<>();

        String sql = """
        SELECT c.comment_id, u.username, c.comment_text
        FROM comments c
        JOIN users u ON c.user_id = u.user_id
        WHERE c.post_id = ?
        ORDER BY c.created_at
    """;

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new CommentView(
                        rs.getInt("comment_id"),
                        rs.getString("username"),
                        rs.getString("comment_text")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    @Override
    public boolean deleteComment(int commentId, int userId) {

        String sql = """
        DELETE FROM comments
        WHERE comment_id = ? AND user_id = ?
    """;

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, commentId);
            ps.setInt(2, userId);   // 🔥 ownership check

            return ps.executeUpdate() > 0; // 0 → not owner or invalid ID

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
