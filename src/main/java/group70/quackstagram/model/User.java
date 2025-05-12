package group70.quackstagram.model;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String bio;
    private String profilePictureUrl;

    public User(int userId, String username, String passwordHash, String bio, String profilePictureUrl) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    public User(String username, String passwordHash, String bio, String profilePictureUrl) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getBio() { return bio; }
    public String getProfilePictureUrl() { return profilePictureUrl; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setBio(String bio) { this.bio = bio; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
}
