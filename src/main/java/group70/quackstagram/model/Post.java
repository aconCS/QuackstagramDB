package group70.quackstagram.model;

import java.sql.Timestamp;
import java.util.List;

public class Post {
    private int postId;
    private String owner;
    private String pictureUrl;
    private String description;
    private Timestamp postDate;
    private int likes;

    // Constructors
    public Post(String owner, String pictureUrl, String description) {
        this.owner = owner;
        this.pictureUrl = pictureUrl;
        this.description = description;
    }

    public Post(int postId, String username, String pictureUrl, String description, Timestamp postDate, int likes) {
        this.postId = postId;
        this.owner = username;
        this.pictureUrl = pictureUrl;
        this.description = description;
        this.postDate = postDate;
        this.likes = likes;
    }

    // Getters
    public int getPostId() { return postId; }
    public String getOwner() { return owner; }
    public String getPictureUrl() { return pictureUrl; }
    public String getDescription() { return description; }
    public java.util.Date getPostDate() { return postDate; }
    public int getLikes() { return likes; }

    // Setters
    public void setPostId(int postId) { this.postId = postId; }
    public void setOwner(String username) { this.owner = username; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setPostDate(Timestamp postDate) { this.postDate = postDate; }
    public void setLikes(int likes) { this.likes = likes; }
}

