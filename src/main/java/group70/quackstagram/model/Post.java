package group70.quackstagram.model;

import java.sql.Date;

public record Post(int postID, int userID,
                   String pictureURL, String description,
                   Date postDate) {}
