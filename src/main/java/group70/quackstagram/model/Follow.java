package group70.quackstagram.model;

import java.sql.Date;

public record Follow(int follower_id, int followee_id, Date follow_date) {
}
