package group70.quackstagram.model;

public record UserProfileData (
    String username,
    String bio,
    String profile_pic,
    int postCount,
    int followerCount,
    int followingCount
){}

