package group70.quackstagram.dao;

import group70.quackstagram.database.Database;
import group70.quackstagram.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public List<Notification> getNotificationsForUser(String username) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE notified_username = ? AND is_read IS false ORDER BY date DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getInt("notification_id"),
                        rs.getString("notified_username"),
                        rs.getString("type"),
                        rs.getString("message"),
                        rs.getString("notifier_username"),
                        false,
                        rs.getTimestamp("created_at")
                );
                notifications.add(notification);
            }
        }

        return notifications;
    }

    public void markAsRead(String username) throws SQLException {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE notified_username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }
}
