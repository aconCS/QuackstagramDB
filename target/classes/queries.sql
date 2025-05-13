SELECT followee_id, COUNT(*) AS follower_count
FROM follows
GROUP BY followee_id
HAVING COUNT(*) > ?;

SELECT owner, COUNT(*) AS post_count
FROM posts
GROUP BY owner;

SELECT c.*
FROM comments c
         JOIN posts p ON c.post_id = p.post_id
WHERE p.owner = ?;

SELECT post_id, COUNT(*) AS like_count
FROM likes
GROUP BY post_id
ORDER BY like_count DESC
LIMIT ?;

SELECT owner, COUNT(*) AS liked_posts
FROM likes
GROUP BY owner;

SELECT u.owner
FROM users u
LEFT JOIN posts p ON u.owner = p.owner
WHERE p.post_id IS NULL;

SELECT a.follower_id, a.followee_id
FROM follows a
JOIN follows b ON a.follower_id = b.followee_id
AND a.followee_id = b.follower_id;

SELECT owner, COUNT(*) AS post_count
FROM posts
GROUP BY owner
ORDER BY post_count DESC
LIMIT 1;

SELECT followee_id, COUNT(*) AS follower_count
FROM follows
GROUP BY followee_id
ORDER BY follower_count DESC
LIMIT ?;

SELECT post_id
FROM likes
GROUP BY post_id
HAVING COUNT(DISTINCT owner) = (SELECT COUNT(*) FROM users);

SELECT u.owner,
       COUNT(p.post_id) + COUNT(c.comment_id) + COUNT(l.post_id) AS activity
FROM users u
LEFT JOIN posts p ON u.owner = p.owner
LEFT JOIN comments c ON u.owner = c.owner
LEFT JOIN likes l ON u.owner = l.owner
GROUP BY u.owner
ORDER BY activity DESC
LIMIT 1;

--
SELECT p.owner, AVG(l.like_count) AS avg_likes
FROM posts p
LEFT JOIN (
    SELECT post_id, COUNT(*) AS like_count
    FROM likes
    GROUP BY post_id
) l ON p.post_id = l.post_id
GROUP BY p.owner;

--
SELECT p.post_id
FROM posts p
LEFT JOIN likes l ON p.post_id = l.post_id
LEFT JOIN comments c ON p.post_id = c.post_id
GROUP BY p.post_id
HAVING COUNT(DISTINCT c.comment_id) > COUNT(DISTINCT l.owner);

--
SELECT l.owner
FROM likes l
         JOIN posts p ON l.post_id = p.post_id
WHERE p.owner = ?
GROUP BY l.owner
HAVING COUNT(DISTINCT l.post_id) = ( SELECT COUNT(*) FROM posts WHERE owner = ?);

SELECT p.owner, p.post_id, COUNT(l.owner) AS like_count
FROM posts p
         LEFT JOIN likes l ON p.post_id = l.post_id
GROUP BY p.owner, p.post_id
HAVING COUNT(l.owner) = (
    SELECT MAX(likes_per_post)
    FROM (
             SELECT post_id, COUNT(owner) AS likes_per_post
             FROM likes
             WHERE post_id IN (SELECT post_id FROM posts WHERE owner = p.owner)
             GROUP BY post_id
         ) AS user_posts
);

SELECT u.owner,
       COUNT(DISTINCT f1.follower_id) * 1.0 /
       NULLIF(COUNT(DISTINCT f2.followee_id), 0) AS ratio
FROM users u
         LEFT JOIN follows f1 ON u.owner = f1.followee_id
         LEFT JOIN follows f2 ON u.owner = f2.follower_id
GROUP BY u.owner
ORDER BY ratio DESC
LIMIT 1;

SELECT
    EXTRACT(MONTH FROM post_date) AS month,
    COUNT(*) AS post_count
FROM posts
GROUP BY month
ORDER BY post_count DESC
LIMIT 1;

SELECT owner
FROM users
WHERE owner NOT IN (
    SELECT owner
    FROM likes
    WHERE post_id IN (SELECT post_id FROM posts WHERE owner = ?)
    UNION
    SELECT owner
    FROM comments
    WHERE post_id IN (SELECT post_id FROM posts WHERE owner = ?)
);

SELECT followee_id, COUNT(*) AS new_followers
FROM follows
WHERE follow_date >= DATE_SUB(CURDATE(), INTERVAL 20 DAY)
GROUP BY followee_id
ORDER BY new_followers DESC
LIMIT 1;

SELECT followee_id, COUNT(*) AS follower_count
FROM follows
GROUP BY followee_id
HAVING COUNT(*) > (SELECT COUNT(*) FROM users) * (? / 100.0);
