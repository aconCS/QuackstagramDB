package group70.quackstagram.controller;

import group70.quackstagram.model.Notification;
import group70.quackstagram.services.NotificationServices;

import java.util.List;

public class NotificationController {

    private final NotificationServices notificationServices;

    public NotificationController() {
        this.notificationServices = new NotificationServices();
    }

    public List<Notification> getNotifications(String username) {
        return notificationServices.getUserNotifications(username);
    }

    public void markAsRead(String username) {
        notificationServices.markAllAsRead(username);
    }
}
