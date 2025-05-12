package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.Comment;
import group70.quackstagram.model.Follow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommentDAO {

    public void insertComment(Comment comment) {
        String sql = "INSERT INTO comments " +
                "(comment_id, post_id, user_id, content, comment_date) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, comment.commentID());
            statement.setInt(2, comment.postID());
            statement.setInt(3, comment.userID());
            statement.setString(4, comment.content());
            statement.setDate(5, comment.comment_date());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
