-- Function: Get total likes on a post
DELIMITER $$
CREATE FUNCTION get_total_likes(post_id_input INT)
    RETURNS INT
    DETERMINISTIC
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total
    FROM likes
    WHERE post_id = post_id_input;
    RETURN total;
END$$
DELIMITER ;

-- Procedure: Insert a notification
DELIMITER $$
CREATE PROCEDURE send_notification(
    IN to_user_id INT,
    IN from_user_id INT,
    IN notif_type VARCHAR(50),
    IN msg TEXT
)
BEGIN
    INSERT INTO notifications (notified_username, type, message, notifier_username, is_read, date)
    VALUES (to_user_id, notif_type, msg, from_user_id, FALSE, NOW());
END$$
DELIMITER ;

-- Trigger: When a new like is inserted, send a notification
DELIMITER $$
CREATE TRIGGER after_like_insert
    AFTER INSERT ON likes
    FOR EACH ROW
BEGIN
    DECLARE post_owner INT;
    SELECT poster INTO post_owner FROM posts WHERE post_id = NEW.post_id;

    IF post_owner IS NOT NULL AND post_owner != NEW.liker THEN
        CALL send_notification(post_owner,
                               NEW.liker,
                               'like',
                               CONCAT('Your post got a like! Total likes: ',
                                      get_total_likes(NEW.post_id)));
    END IF;
END$$
DELIMITER ;

-- Trigger: When a new comment is inserted, send a notification
DELIMITER $$
CREATE TRIGGER after_comment_insert
    AFTER INSERT ON comments
    FOR EACH ROW
BEGIN
    DECLARE post_owner INT;
    SELECT poster INTO post_owner FROM posts WHERE post_id = NEW.post_id;

    IF post_owner IS NOT NULL AND post_owner != NEW.commenter THEN
        CALL send_notification(post_owner,
                               NEW.commenter,
                               'comment',
                               'Your post got a new comment!');
    END IF;
END $$
DELIMITER ;

