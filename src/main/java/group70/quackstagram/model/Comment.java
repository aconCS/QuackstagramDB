package group70.quackstagram.model;

import java.sql.Date;

public record Comment(int commentID, int postID,
                      int userID, String content,
                      Date comment_date) {}
