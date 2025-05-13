package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    public PostDAO() {}

    public Post createPost(Post post) throws SQLException {
        String sql = "INSERT INTO posts (owner, picture_url, description) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, post.getOwner());
            stmt.setString(2, post.getPictureUrl());
            stmt.setString(3, post.getDescription());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating post failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setPostId(generatedKeys.getInt(1)); // set the auto-generated post ID
                } else {
                    throw new SQLException("Creating post failed, no ID obtained.");
                }
            }
        }

        return post;
    }


    public List<Post> getFilteredPosts(String filter, boolean exactMatch) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = exactMatch
                ? "SELECT * FROM posts WHERE owner = ? ORDER BY post_date DESC"
                : "SELECT * FROM posts WHERE owner LIKE ? ORDER BY post_date DESC";
        String likeSql = exactMatch
                ? "SELECT COUNT(*) AS like_count FROM likes WHERE post_id = ?"
                : "SELECT COUNT(*) AS like_count FROM likes WHERE post_id LIKE ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement likeStmt = conn.prepareStatement(likeSql);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, exactMatch ? filter : "%" + filter + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                likeStmt.setInt(1, rs.getInt("post_id"));
                ResultSet likeRs = likeStmt.executeQuery();

                int likeCount = 0;
                if (likeRs.next()) {
                    likeCount = likeRs.getInt("like_count");
                }

                Post post = new Post(
                        rs.getInt("post_id"),
                        rs.getString("owner"),
                        rs.getString("picture_url"),
                        rs.getString("description"),
                        rs.getTimestamp("post_date"),
                        likeCount
                );
                posts.add(post);
            }
        }
        return posts;
    }

    public int countPostsByUser(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM posts WHERE owner = ?";
        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public int getNextPostId(String username) throws SQLException {
        String sql = "SELECT MAX(post_id) FROM posts WHERE owner = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int maxId = rs.getInt(1);
                    return maxId + 1;
                }
            }
        }
        return 1;
    }

    public Post getPostById(int postId) {
        String sql = "SELECT * FROM posts WHERE post_id = ?";
        String likeSql = "SELECT COUNT(*) AS like_count FROM likes WHERE post_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             PreparedStatement likeStmt = conn.prepareStatement(likeSql)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            likeStmt.setInt(1, postId);
            ResultSet likeRs = likeStmt.executeQuery();

            if (rs.next()) {

                int likeCount = 0;
                if (likeRs.next()) {
                    likeCount = likeRs.getInt("like_count");
                }

                return new Post(
                        rs.getInt("post_id"),
                        rs.getString("owner"),
                        rs.getString("picture_url"),
                        rs.getString("description"),
                        rs.getTimestamp("post_date"),
                        likeCount
                );
            } else {
                throw new RuntimeException("Post not found with id: " + postId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get post", e);
        }
    }

}
