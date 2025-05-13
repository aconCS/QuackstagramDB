package group70.quackstagram.model;

import java.io.Serializable;
import java.util.Comparator;

public class User {
    private String username;
    private String passwordHash;
    private String bio;
    private String profilePictureURL;

    public User(String username, String password, String bio, String profilePictureURL) {
        this.username = username;
        this.passwordHash = password;
        this.bio = bio;
        this.profilePictureURL = profilePictureURL;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getBio() { return bio; }
    public String getProfilePictureURL() { return profilePictureURL; }

    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setBio(String bio) { this.bio = bio; }
    public void setProfilePictureURL(String profilePictureURL) { this.profilePictureURL = profilePictureURL; }
}