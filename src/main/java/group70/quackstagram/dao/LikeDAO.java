package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LikeDAO {

    public void insertLike(Like like) {
        String sql = "INSERT INTO likes " +
                "(post_id, user_id, like_date) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, like.postID());
            statement.setInt(2, like.userID());
            statement.setDate(3, like.likeDate());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
