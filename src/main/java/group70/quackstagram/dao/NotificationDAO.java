package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationDAO {

    public void insertNotification(Notification notification) {
        String sql = "INSERT INTO notifications " +
                "(user_id, type, message_type, related_user_id, is_read, date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, notification.userID());
            statement.setString(2, notification.messageType());
            statement.setString(3, notification.message());
            statement.setInt(4, notification.relatedUserID());
            statement.setBoolean(5, notification.isRead());
            statement.setDate(6, notification.date());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
