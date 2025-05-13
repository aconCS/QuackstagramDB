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

    public FollowDAO(){
        userDAO = new UserDAO();
    }

    public List<User> getFollowers(String username) throws SQLException {
        String sql = "SELECT u.* FROM follows f JOIN users u ON f.follower_name = u.username WHERE f.followee_name = ?";
        List<User> followers = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                followers.add(new User(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("bio"),
                        rs.getString("profile_picture_url")
                ));
            }
        }
        return followers;
    }

    public List<User> getFollowing(String username) throws SQLException {
        String sql = "SELECT u.* FROM follows f JOIN users u ON f.followee_name = u.username WHERE f.follower_name= ?";
        List<User> following = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                following.add(new User(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("bio"),
                        rs.getString("profile_picture_url")
                ));
            }
        }
        return following;
    }

    public boolean followUser(String followerId, String followeeId) throws SQLException {
        String sql = "INSERT IGNORE INTO follows (follower_name, followee_name) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, followerId);
            stmt.setString(2, followeeId);
            return stmt.executeUpdate() == 1;
        }
    }

    public boolean unfollowUser(String followerId, String followeeId) throws SQLException {
        String sql = "DELETE FROM follows WHERE follower_name = ? AND followee_name = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, followerId);
            stmt.setString(2, followeeId);
            return stmt.executeUpdate() == 1;
        }
    }
}
