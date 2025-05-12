package group70.quackstagram.controller;

import group70.quackstagram.model.Post;
import group70.quackstagram.model.User;
import group70.quackstagram.services.UserServices;

import java.util.List;

public class UserController {
    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    public User getCurrentUser() {
        return userServices.getCurrentUser();
    }

    public String getBio() {
        return userServices.getCurrentUser().bio();
    }

    public void editBio(String newBio) {
        userServices.updateUserBio(newBio);
    }

    public String getProfilePictureURL() {
        return userServices.getCurrentUser().profilePictureURL();
    }

    public void updateProfilePicture(String newURL) {
        userServices.updateProfilePicture(newURL);
    }

    public int getPostCount() {
        return userServices.getPostCount();
    }

    public List<Post> getUserPosts() {
        return userServices.getUserPosts();
    }

    public int getFollowersCount() {
        return userServices.getFollowers().size();
    }

    public int getFollowingCount() {
        return userServices.getFollowing().size();
    }

    // Get the list of followers
    public List<User> getFollowers() {
        return userServices.getFollowers();
    }

    // Get the list of users this user is following
    public List<User> getFollowing() {
        return userServices.getFollowing();
    }

    public boolean isFollowing(String targetUsername) {
        return userServices.isFollowing(targetUsername);
    }

    public void followUser(String targetUsername) {
        userServices.followUser(targetUsername);
    }

    public void unfollowUser(String targetUsername) {
        userServices.unfollowUser(targetUsername);
    }

    // Search for users by username (partial match)
    public List<User> searchUsers(String query) {
        return userServices.searchUsers(query);
    }
}