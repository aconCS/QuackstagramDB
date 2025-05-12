package group70.quackstagram.dto;

public class UserDTO {
    private String username;
    private String bio;
    private String profilePictureUrl;

    public UserDTO(String username, String bio, String profilePictureUrl) {
        this.username = username;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUsername() { return username; }
    public String getBio() { return bio; }
    public String getProfilePictureUrl() { return profilePictureUrl; }

    public void setUsername(String username) { this.username = username; }
    public void setBio(String bio) { this.bio = bio; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
}
