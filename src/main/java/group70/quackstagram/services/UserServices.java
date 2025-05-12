package group70.quackstagram.services;

import group70.quackstagram.dao.FollowDAO;
import group70.quackstagram.dao.PostDAO;
import group70.quackstagram.dao.UserDAO;
import group70.quackstagram.model.Post;
import group70.quackstagram.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserServices {
    private final UserDAO userDAO;
    private final FollowDAO followDAO;
    private final PostDAO postDAO;
    private User user;

    public UserServices(UserDAO userDAO, FollowDAO followDAO, PostDAO postDAO, User user) {
        this.userDAO = userDAO;
        this.followDAO = followDAO;
        this.postDAO = postDAO;
        this.user = refreshUserData(user.username());
    }

    private User refreshUserData(String username) {
        User dbUser = userDAO.findUserByUsername(username);
        if (dbUser == null) throw new RuntimeException("User not found: " + username);
        return dbUser;
    }

    public User getCurrentUser() {
        return user;
    }

    public void updateUserBio(String newBio) {
        userDAO.updateUserBio(user.username(), newBio);
        this.user = new User(this.user.userID(), this.user.username(),
                this.user.passwordHash(), newBio, this.user.profilePictureURL());
    }

    public void updateProfilePicture(String newImageUrl) {
        User updatedUser = new User(
                user.userID(),
                user.username(),
                user.passwordHash(),
                user.bio(),
                newImageUrl
        );
        userDAO.updateUserDetails(updatedUser);
        this.user = updatedUser;
    }

    public boolean isFollowing(String targetUsername) {
        try {
            return followDAO.isFollowing(user.username(), targetUsername);
        } catch (SQLException e) {
            throw new RuntimeException("Follow check failed", e);
        }
    }

    public void followUser(String targetUsername) {
        try {
            if (!isFollowing(targetUsername)) {
                followDAO.createFollow(user.username(), targetUsername);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Follow operation failed", e);
        }
    }

    public void unfollowUser(String targetUsername) {
        try {
            if (isFollowing(targetUsername)) {
                followDAO.deleteFollow(user.username(), targetUsername);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unfollow operation failed", e);
        }
    }

    public List<User> getFollowers() {
        try {
            return followDAO.getFollowers(user.username());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get followers", e);
        }
    }

    public List<User> getFollowing() {
        try {
            return followDAO.getFollowing(user.username());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get following", e);
        }
    }

    public int getFollowersCount() {
        return getFollowers().size();
    }

    public int getFollowingCount() {
        return getFollowing().size();
    }

    public List<Post> getUserPosts() {
        try {
            return postDAO.getPostsByUser(user.username());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user posts", e);
        }
    }

    public int getUserPostCount() {
        try {
            return postDAO.getPostCount(user.username());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get user post count", e);
        }
    }

    public List<User> searchUsers(String query) {
        return userDAO.findUsersByUsernameContaining(query);
    }
}
