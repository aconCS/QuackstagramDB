package group70.quackstagram.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Comment {
    private int commentId; // can be 0 or unused on insert
    private int postId;
    private String owner;
    private String content;
    private Timestamp commentDate; // optional on insert

    public Comment(int commentId, int postId, String username, String content, Timestamp commentDate) {
        this.commentId = commentId;
        this.postId = postId;
        this.owner = username;
        this.content = content;
        this.commentDate = commentDate;
    }

    // Constructor for insertion (no commentId or date)
    public Comment(int postId, String username, String content) {
        this.postId = postId;
        this.owner = username;
        this.content = content;
    }

    // Getters
    public int getPostId() { return postId; }
    public String getOwner() { return owner; }
    public String getContent() { return content; }
    public Timestamp getCommentDate() { return commentDate; }
    public int getCommentId() { return commentId; }

    // Setters
    public void setCommentId(int commentId) { this.commentId = commentId; }
    public void setPostId(int postId) { this.postId = postId; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setContent(String content) { this.content = content; }
    public void setCommentDate(Timestamp commentDate) { this.commentDate = commentDate; }
}
