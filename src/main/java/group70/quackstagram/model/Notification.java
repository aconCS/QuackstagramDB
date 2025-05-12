package group70.quackstagram.model;

import java.sql.Date;

public record Notification(int userID, String messageType,
                        String message, int relatedUserID,
                        boolean isRead, Date date) {}