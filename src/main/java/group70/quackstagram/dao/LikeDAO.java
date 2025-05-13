package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeDAO {

    public void insertLike(Like like) {
        String sql = "INSERT INTO likes " +
                "(post_id, liker) " +
                "VALUES (?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, like.getPostID());
            statement.setString(2, like.getUsername());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't insert like: ", e);
        }
    }

    public void removeLike(Like like) {
        String sql = "DELETE FROM likes WHERE post_id = ? AND liker = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, like.getPostID());
            statement.setString(2, like.getUsername());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't remove like: ", e);
        }
    }


    public boolean checkLike(Like like) {
        String sql = "SELECT 1 FROM likes WHERE post_id = ? AND liker = ? LIMIT 1";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, like.getPostID());
            statement.setString(2, like.getUsername());

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException("Couldn't check like: ", e);
        }
    }

}
