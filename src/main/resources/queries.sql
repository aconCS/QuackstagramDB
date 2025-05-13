SELECT followee_name, COUNT(*) AS follower_count
FROM follows
GROUP BY followee_name
HAVING COUNT(*) > ?;

SELECT poster, COUNT(*) AS post_count
FROM posts
GROUP BY poster;

SELECT c.*
FROM comments c
         JOIN posts p ON c.post_id = p.post_id
WHERE p.poster = ?;

SELECT post_id, COUNT(*) AS like_count
FROM likes
GROUP BY post_id
ORDER BY like_count DESC
LIMIT ?;

SELECT liker, COUNT(*) AS liked_posts
FROM likes
GROUP BY liker;

SELECT u.username
FROM users u
LEFT JOIN posts p ON u.username = p.poster
WHERE p.post_id IS NULL;

SELECT a.follower_name, a.followee_name
FROM follows a
JOIN follows b ON a.follower_name = b.followee_name
AND a.followee_name = b.follower_name;

SELECT poster, COUNT(*) AS post_count
FROM posts
GROUP BY poster
ORDER BY post_count DESC
LIMIT 1;

SELECT followee_name, COUNT(*) AS follower_count
FROM follows
GROUP BY followee_name
ORDER BY follower_count DESC
LIMIT ?;

SELECT post_id
FROM likes
GROUP BY post_id
HAVING COUNT(DISTINCT liker) = (SELECT COUNT(*) FROM users);

SELECT u.username,
       COUNT(p.post_id) + COUNT(c.comment_id) + COUNT(l.post_id) AS activity
FROM users u
LEFT JOIN posts p ON u.username = p.poster
LEFT JOIN comments c ON u.username = c.commenter
LEFT JOIN likes l ON u.username = l.liker
GROUP BY u.username
ORDER BY activity DESC
LIMIT 1;

--
SELECT p.poster, AVG(l.like_count) AS avg_likes
FROM posts p
LEFT JOIN (
    SELECT post_id, COUNT(*) AS like_count
    FROM likes
    GROUP BY post_id
) l ON p.post_id = l.post_id
GROUP BY p.poster;

--
SELECT p.post_id
FROM posts p
LEFT JOIN likes l ON p.post_id = l.post_id
LEFT JOIN comments c ON p.post_id = c.post_id
GROUP BY p.post_id
HAVING COUNT(DISTINCT c.comment_id) > COUNT(DISTINCT l.liker);

--
SELECT l.liker
FROM likes l
         JOIN posts p ON l.post_id = p.post_id
WHERE p.poster = ?
GROUP BY l.liker
HAVING COUNT(DISTINCT l.post_id) = ( SELECT COUNT(*) FROM posts WHERE poster = ?);

SELECT p.poster, p.post_id, COUNT(l.liker) AS like_count
FROM posts p
         LEFT JOIN likes l ON p.post_id = l.post_id
GROUP BY p.poster, p.post_id
HAVING COUNT(l.liker) = (
    SELECT MAX(likes_per_post)
    FROM (
             SELECT post_id, COUNT(liker) AS likes_per_post
             FROM likes
             WHERE post_id IN (SELECT post_id FROM posts WHERE poster = p.poster)
             GROUP BY post_id
         ) AS user_posts
);

SELECT u.username,
       COUNT(DISTINCT f1.follower_name) * 1.0 /
       NULLIF(COUNT(DISTINCT f2.followee_name), 0) AS ratio
FROM users u
         LEFT JOIN follows f1 ON u.username = f1.followee_name
         LEFT JOIN follows f2 ON u.username = f2.follower_name
GROUP BY u.username
ORDER BY ratio DESC
LIMIT 1;

SELECT
    EXTRACT(MONTH FROM post_date) AS month,
    COUNT(*) AS post_count
FROM posts
GROUP BY month
ORDER BY post_count DESC
LIMIT 1;

SELECT username
FROM users
WHERE username NOT IN (
    SELECT liker
    FROM likes
    WHERE post_id IN (SELECT post_id FROM posts WHERE poster = ?)
    UNION
    SELECT commenter
    FROM comments
    WHERE post_id IN (SELECT post_id FROM posts WHERE poster = ?)
);

SELECT followee_name, COUNT(*) AS new_followers
FROM follows
WHERE follow_date >= DATE_SUB(CURDATE(), INTERVAL 20 DAY)
GROUP BY followee_name
ORDER BY new_followers DESC
LIMIT 1;

SELECT followee_name, COUNT(*) AS follower_count
FROM follows
GROUP BY followee_name
HAVING COUNT(*) > (SELECT COUNT(*) FROM users) * (? / 100.0);
