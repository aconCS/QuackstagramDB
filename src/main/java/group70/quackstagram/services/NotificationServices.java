package group70.quackstagram.services;

import group70.quackstagram.dao.NotificationDAO;
import group70.quackstagram.model.Notification;

import java.sql.SQLException;
import java.util.List;

public class NotificationServices {
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public List<Notification> getUserNotifications(String username) {
        try {
            return notificationDAO.getNotificationsForUser(username);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch notifications", e);
        }
    }

    public void markAllAsRead(String username) {
        try {
            notificationDAO.markAsRead(username);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to mark notifications as read", e);
        }
    }
}
