package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public void insertComment(Comment comment) throws SQLException {
        String sql = "INSERT INTO comments " +
                "(post_id, commenter, content) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, comment.getPostId());
            statement.setString(2, comment.getOwner());
            statement.setString(3, comment.getContent());

            statement.executeUpdate();
        }
    }

    public List<Comment> getCommentsForPost(int postId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY comment_date ASC";

        try (Connection conn = Database.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment(
                        rs.getInt("comment_id"),
                        rs.getInt("post_id"),
                        rs.getString("commenter"),
                        rs.getString("content"),
                        rs.getTimestamp("comment_date")
                );
                comments.add(comment);
            }
        }

        return comments;
    }
}
