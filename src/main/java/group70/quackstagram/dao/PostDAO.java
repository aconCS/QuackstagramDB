package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.Notification;
import group70.quackstagram.model.Post;
import group70.quackstagram.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    UserDAO userDAO;
    
    public PostDAO(){ userDAO = new UserDAO(); }

    public void insertPost(Post post) {
        String sql = "INSERT INTO posts " +
                "(user_id, picture_url, description, post_date) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, post.userID());
            statement.setString(2, post.pictureURL());
            statement.setString(3, post.description());
            statement.setDate(4, post.postDate());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Post> getPostsByUser(String username) throws SQLException {
        int userID = getUserIDByUsername(username);
        String sql = "SELECT * FROM posts WHERE user_id = ? ORDER BY post_date DESC";
        List<Post> posts = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(extractPostFromResultSet(resultSet));
                }
            }
        }
        return posts;
    }

    public int getPostCount(String username) throws SQLException {
        int userID = getUserIDByUsername(username);
        String sql = "SELECT COUNT(*) FROM posts WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return resultSet.getInt(1);
            }
        }
        return 0;
    }

    private int getUserIDByUsername(String username) throws SQLException {
        User user = userDAO.findUserByUsername(username);
        if (user == null) throw new SQLException("User not found: " + username);
        return user.getUserID();
    }

    private Post extractPostFromResultSet(ResultSet resultSet) throws SQLException {
        int postID = resultSet.getInt("post_id");
        int userID = resultSet.getInt("user_id");
        String pictureUrl = resultSet.getString("picture_url");
        String description = resultSet.getString("description");
        Date postDate = resultSet.getDate("post_date");
        return new Post(postID, userID, pictureUrl, description, postDate);
    }

}
