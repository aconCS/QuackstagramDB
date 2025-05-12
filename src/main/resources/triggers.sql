DELIMITER //
CREATE PROCEDURE send_notification(
    IN post_owner_id INT,
    IN notification_type VARCHAR(50),
    IN notification_message TEXT,
    IN interacting_user_id INT
)
BEGIN
    INSERT INTO notifications (user_id, type, message, related_user_id, date)
    VALUES (post_owner_id, notification_type, notification_message, interacting_user_id, CURDATE());
END; //

-- Stored Function: Create and return notification message
-- This function returns the number of likes for a specific post
DELIMITER //

CREATE FUNCTION get_like_count(post_id INT)
    RETURNS INT
    DETERMINISTIC
BEGIN
    DECLARE likeCount INT;

    SELECT COUNT(*) INTO likeCount
    FROM likes
    WHERE likes.post_id = post_id;

    RETURN likeCount;
END; //

-- Trigger 1: After a Like is Added
-- This trigger will call the send_notification procedure to notify the user who owns the post when a like is added.
DELIMITER //

CREATE TRIGGER after_like_insert
    AFTER INSERT ON likes
    FOR EACH ROW
BEGIN
    -- Call stored procedure to notify the post owner about the like
    CALL send_notification(
            (SELECT user_id FROM posts WHERE post_id = NEW.post_id),  -- post owner
            'like',  -- notification type
            CONCAT('User ', (SELECT username FROM users WHERE user_id = NEW.user_id), ' liked your post.'),  -- notification message
            NEW.user_id  -- related user (the one who liked the post)
         );
END; //

DELIMITER ;


-- Trigger 2: After a Comment is Added
-- This trigger will call the send_notification procedure to notify the user who owns the post when a comment is added
DELIMITER //

CREATE TRIGGER after_comment_insert
    AFTER INSERT ON comments
    FOR EACH ROW
BEGIN
    -- Call stored procedure to notify the post owner about the comment
    CALL send_notification(
            (SELECT user_id FROM posts WHERE post_id = NEW.post_id),  -- post owner
            'comment',  -- notification type
            CONCAT('User ', (SELECT username FROM users WHERE user_id = NEW.user_id), ' commented on your post.'),
            NEW.user_id  -- related user (the one who made the comment)
         );
END; //

DELIMITER ;
