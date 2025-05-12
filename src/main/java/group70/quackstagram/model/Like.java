package group70.quackstagram.model;

import java.sql.Date;

public record Like(int postID, int userID, Date likeDate) {
}
