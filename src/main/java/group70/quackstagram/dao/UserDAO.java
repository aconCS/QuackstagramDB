package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public UserDAO() {}

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET bio = ?, profile_picture_url = ? WHERE username = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getBio());
            stmt.setString(2, user.getProfilePictureURL());
            stmt.setString(3, user.getUsername());
            stmt.executeUpdate();
        }
    }

    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, bio, profile_picture_url) VALUES (?, SHA2(?, 256), ?, ?)";
        try (Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getBio());
            stmt.setString(4, user.getProfilePictureURL());
            return stmt.executeUpdate() == 1;
        }catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public User validateLogin(String username, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = SHA2(?, 256)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, plainPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("bio"),
                        rs.getString("profile_picture_url")
                );
            }
        }
        return null;
    }



    public User findByUsername(String username){
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("bio"),
                        rs.getString("profile_picture_url")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<User> getFilteredUsers(String filter){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE username LIKE ?";
        try (Connection conn = Database.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, filter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User newUser = new User(
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("bio"),
                        rs.getString("profile_picture_url")
                );
                users.add(newUser);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
