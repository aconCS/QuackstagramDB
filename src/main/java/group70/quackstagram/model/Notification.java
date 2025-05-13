package group70.quackstagram.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Notification {
    private int id;
    private String username;
    private String type;
    private String relatedUserID;
    private boolean isRead;
    private Timestamp date;
    private String message;

    public Notification() {}

    public Notification(int id, String username, String type, String message, String relatedUserID, boolean isRead, Timestamp date) {
        this.id = id;
        this.username = username;
        this.type = type;
        this.relatedUserID = relatedUserID;
        this.isRead = isRead;
        this.date = date;
        this.message = message;
    }


    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getType() { return type; }
    public String getRelatedUserID() { return relatedUserID; }
    public boolean isRead() { return isRead; }
    public Timestamp getDate() { return date; }
    public String getMessage() { return message; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setType(String type) { this.type = type; }
    public void setRelatedUserID(String relatedUserID) {
        this.relatedUserID = relatedUserID;
    }
    public void setRead(boolean isRead) { this.isRead = isRead; }
    public void setDate(Timestamp date) { this.date = date; }
    public void setMessage(String message) { this.message = message; }
}
