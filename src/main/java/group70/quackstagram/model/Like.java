package group70.quackstagram.model;

import java.sql.Date;

public class Like {
    private int postID;
    private String username;
    private Date likeDate;

    // Constructors
    public Like(int postID, String username) {
        this.postID = postID;
        this.username = username;
    }

    public Like(int postID, String username, Date likeDate) {
        this.postID = postID;
        this.username = username;
        this.likeDate = likeDate;
    }

    // Getters
    public int getPostID() { return postID; }

    public String getUsername() { return username; }

    public Date getLikeDate() { return likeDate; }

    // Setters
    public void setPostID(int postID) { this.postID = postID; }

    public void setUsername(String username) { this.username = username; }

    public void setLikeDate(Date likeDate) { this.likeDate = likeDate; }
}

