package model;

public class CommentView {

    private int commentId;
    private String username;
    private String commentText;

    public CommentView(int commentId, String username, String commentText) {
        this.commentId = commentId;
        this.username = username;
        this.commentText = commentText;
    }

    public int getCommentId() {
        return commentId;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }
}

