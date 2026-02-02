package dao.impl;



import config.DBConnection;
import dao.PostDAO;
import model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PostDAOImpl implements PostDAO {

    @Override
    public boolean createPost(Post post) {

        String sql = "INSERT INTO posts (user_id, post_content, hashtags, is_pinned) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getPostContent());
            ps.setString(3, post.getHashtags());
            ps.setBoolean(4, post.isPinned());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Post> getAllPosts() {

        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts ORDER BY created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getInt("post_id"));
                post.setUserId(rs.getInt("user_id"));
                post.setPostContent(rs.getString("post_content"));
                post.setHashtags(rs.getString("hashtags"));
                post.setPinned(rs.getBoolean("is_pinned"));
                posts.add(post);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public List<Post> getPostsByUser(int userId) {

        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE user_id = ? ORDER BY created_at DESC";

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Post post = new Post();
                post.setPostId(rs.getInt("post_id"));
                post.setUserId(rs.getInt("user_id"));
                post.setPostContent(rs.getString("post_content"));
                post.setHashtags(rs.getString("hashtags"));
                post.setPinned(rs.getBoolean("is_pinned"));
                posts.add(post);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }


    @Override
    public boolean updatePost(Post post) {

        String sql = """
        UPDATE posts
        SET post_content = ?, hashtags = ?
        WHERE post_id = ? AND user_id = ?
    """;

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, post.getPostContent());
            ps.setString(2, post.getHashtags());
            ps.setInt(3, post.getPostId());
            ps.setInt(4, post.getUserId());   // 🔥 ownership check

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean deletePost(int postId, int userId) {

        String sql = "DELETE FROM posts WHERE post_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public int getPostOwnerId(int postId) {

        String sql = "SELECT user_id FROM posts WHERE post_id = ?";

        try (java.sql.Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // invalid
    }


    @Override
    public boolean pinPost(int postId, int userId) {

        String sql = "UPDATE posts SET is_pinned = TRUE WHERE post_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
