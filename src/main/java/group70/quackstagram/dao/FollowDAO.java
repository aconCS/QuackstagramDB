package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.Follow;
import group70.quackstagram.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FollowDAO {

    UserDAO userDAO;

    public FollowDAO(){ userDAO = new UserDAO(); }

    public void insertFollow(Follow follow) {
        String sql = "INSERT INTO follows " +
                "(follower_id, followee_id, follow_date) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, follow.follower_id());
            statement.setInt(2, follow.followee_id());
            statement.setDate(3, follow.follow_date());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createFollow(String followerUsername, String followeeUsername) throws SQLException {
        int followerId = getUserIdByUsername(followerUsername);
        int followeeId = getUserIdByUsername(followeeUsername);
        String sql = "INSERT INTO follows (follower_id, followee_id, follow_date) VALUES (?, ?, CURRENT_DATE)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, followerId);
            statement.setInt(2, followeeId);
            statement.executeUpdate();
        }
    }

    public void deleteFollow(String followerUsername, String followeeUsername) throws SQLException {
        int followerId = getUserIdByUsername(followerUsername);
        int followeeId = getUserIdByUsername(followeeUsername);
        String sql = "DELETE FROM follows WHERE follower_id = ? AND followee_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, followerId);
            statement.setInt(2, followeeId);
            statement.executeUpdate();
        }
    }

    public boolean isFollowing(String followerUsername, String followeeUsername) throws SQLException {
        int followerId = getUserIdByUsername(followerUsername);
        int followeeId = getUserIdByUsername(followeeUsername);
        String sql = "SELECT 1 FROM follows WHERE follower_id = ? AND followee_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, followerId);
            statement.setInt(2, followeeId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public List<User> getFollowers(String username) throws SQLException {
        int userId = getUserIdByUsername(username);
        String sql = "SELECT u.* FROM users u JOIN follows f ON u.user_id = f.follower_id WHERE f.followee_id = ?";
        List<User> followers = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    followers.add(extractUserFromResultSet(resultSet));
                }
            }
        }
        return followers;
    }

    public List<User> getFollowing(String username) throws SQLException {
        int userId = getUserIdByUsername(username);
        String sql = "SELECT u.* FROM users u JOIN follows f ON u.user_id = f.followee_id WHERE f.follower_id = ?";
        List<User> following = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    following.add(extractUserFromResultSet(resultSet));
                }
            }
        }
        return following;
    }

    private int getUserIdByUsername(String username) throws SQLException {
        User user = userDAO.findUserByUsername(username);
        if (user == null) throw new SQLException("User not found: " + username);
        return user.userID();
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("user_id");
        String username = resultSet.getString("username");
        String passwordHash = resultSet.getString("password_hash");
        String bio = resultSet.getString("bio");
        String profilePictureUrl = resultSet.getString("profile_picture_url");
        return new User(userId, username, passwordHash, bio, profilePictureUrl);
    }
}
