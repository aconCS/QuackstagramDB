-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate all tables
TRUNCATE TABLE notifications;
TRUNCATE TABLE likes;
TRUNCATE TABLE comments;
TRUNCATE TABLE posts;
TRUNCATE TABLE follows;
TRUNCATE TABLE users;

-- Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Insert 100 users
INSERT INTO users (username, password_hash, bio, profile_picture_url, register_date)
SELECT
    CONCAT('user', LPAD(n, 3, '0')) AS owner,
    CONCAT('$2a$10$', SUBSTRING(MD5(RAND()) FROM 1 FOR 22), '.', SUBSTRING(MD5(RAND()) FROM 1 FOR 31)) AS password_hash,
    CONCAT('Bio for user', n),
    'src/main/resources/img/dummy/logo.png',
    DATE_ADD('2022-01-01', INTERVAL FLOOR(RAND() * 730) DAY)
FROM (
         SELECT @row := @row + 1 AS n FROM
                                          (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                                           UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
                                          (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                                           UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
                                          (SELECT @row := 0) t0
         LIMIT 100
     ) AS numbers;

-- Insert 100 posts (1 per user)
INSERT INTO posts (owner, picture_url, description, post_date)
SELECT
    username,
    'src/main/resources/img/dummy/logo.png',
    CONCAT('This is a post by ', username),
    DATE_ADD(register_date, INTERVAL FLOOR(RAND() * 365) DAY)
FROM users;

-- Insert 300 random comments
INSERT INTO comments (post_id, owner, content, comment_date)
SELECT
    p.post_id,
    u.username,
    CONCAT('Comment by ', u.username),
    DATE_ADD(p.post_date, INTERVAL FLOOR(RAND() * 30) DAY)
FROM posts p
         JOIN users u ON RAND() < 0.3
LIMIT 300;

-- Insert 500 random likes
INSERT INTO likes (post_id, liker, like_date)
SELECT
    p.post_id,
    u.username,
    DATE_ADD(p.post_date, INTERVAL FLOOR(RAND() * 10) DAY)
FROM posts p
         JOIN users u ON RAND() < 0.5
LIMIT 500;

-- Insert 300 follows (no self-follow)
INSERT INTO follows (follower_name, followee_name, follow_date)
SELECT
    u1.username,
    u2.username,
    DATE_ADD(LEAST(u1.register_date, u2.register_date), INTERVAL FLOOR(RAND() * 300) DAY)
FROM users u1
         JOIN users u2 ON u1.username <> u2.username AND RAND() < 0.1
LIMIT 300;

-- Insert 200 notifications
INSERT INTO notifications (owner, type, message, related_username, is_read, date)
SELECT
    u.username,
    CASE n % 4
        WHEN 0 THEN 'like'
        WHEN 1 THEN 'comment'
        WHEN 2 THEN 'follow'
        ELSE 'system'
        END,
    CONCAT('Notification #', n, ' for ', u.username),
    (
        SELECT username FROM users ORDER BY RAND() LIMIT 1
    ),
    IF(n % 2 = 0, TRUE, FALSE),
    DATE_ADD(u.register_date, INTERVAL FLOOR(RAND() * 600) DAY)
FROM (
         SELECT @n := @n + 1 AS n FROM
                                      (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                                       UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
                                      (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                                       UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b,
                                      (SELECT @n := 0) t
         LIMIT 200
     ) n_seq
         JOIN users u ON RAND() < 0.5
LIMIT 200;

-- Verify insert counts
SELECT 'users' AS table_name, COUNT(*) FROM users
UNION ALL
SELECT 'posts', COUNT(*) FROM posts
UNION ALL
SELECT 'comments', COUNT(*) FROM comments
UNION ALL
SELECT 'likes', COUNT(*) FROM likes
UNION ALL
SELECT 'follows', COUNT(*) FROM follows
UNION ALL
SELECT 'notifications', COUNT(*) FROM notifications;
