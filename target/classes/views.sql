-- View 1: User behavior - posts, followers, following, likes, comments per user
CREATE OR REPLACE VIEW user_behavior_view AS
SELECT
    u.owner,
    COUNT(DISTINCT p.post_id) AS post_count,
    COUNT(DISTINCT f1.follower_name) AS follower_count,
    COUNT(DISTINCT f2.followee_name) AS following_count,
    COUNT(DISTINCT l.post_id) AS like_count,
    COUNT(DISTINCT c.comment_id) AS comment_count
FROM users u
         LEFT JOIN posts p ON u.owner = p.owner
         LEFT JOIN follows f1 ON u.owner = f1.followee_name
         LEFT JOIN follows f2 ON u.owner = f2.follower_name
         LEFT JOIN likes l ON u.owner = l.owner
         LEFT JOIN comments c ON u.owner = c.owner
GROUP BY u.owner
HAVING post_count > 0 OR comment_count > 0 OR like_count > 0;

-- View 2: Content popularity - most liked and commented posts
CREATE OR REPLACE VIEW content_popularity_view AS
SELECT
    p.post_id,
    p.description,
    u.owner,
    COUNT(DISTINCT l.owner) AS like_count,
    COUNT(DISTINCT c.comment_id) AS comment_count
FROM posts p
         JOIN users u ON p.owner = u.owner
         LEFT JOIN likes l ON p.post_id = l.post_id
         LEFT JOIN comments c ON p.post_id = c.post_id
GROUP BY p.post_id
HAVING like_count + comment_count > 0
ORDER BY like_count + comment_count DESC;

-- View 3: System analytics - total likes, new posts, new comments per month
CREATE OR REPLACE VIEW system_analytics_view AS
SELECT
    activity.month,
    COALESCE(l.total_likes, 0) AS total_likes,
    COALESCE(p.new_posts, 0) AS new_posts,
    COALESCE(c.new_comments, 0) AS new_comments
FROM (  SELECT DATE_FORMAT(register_date, '%Y-%m') AS month
        FROM users
        GROUP BY month
     ) AS activity
    LEFT JOIN ( SELECT DATE_FORMAT(like_date, '%Y-%m') AS month, COUNT(*) AS total_likes
                FROM likes
                GROUP BY month) l ON activity.month = l.month
    LEFT JOIN ( SELECT DATE_FORMAT(post_date, '%Y-%m') AS month, COUNT(*) AS new_posts
                FROM posts
                GROUP BY month) p ON activity.month = p.month
    LEFT JOIN ( SELECT DATE_FORMAT(comment_date, '%Y-%m') AS month, COUNT(*) AS new_comments
                FROM comments
                GROUP BY month) c ON activity.month = c.month;


DROP INDEX follows_follower_followee_ix ON follows;
DROP INDEX likes_post_user_date_ix ON likes;

SELECT
    ub.owner,
    ub.post_count,
    ub.follower_count,
    ub.following_count,
    ub.like_count,
    ub.comment_count,
    cp.post_id,
    cp.description,
    cp.like_count AS post_likes,
    cp.comment_count AS post_comments,
    sa.month,
    sa.total_likes,
    sa.new_posts,
    sa.new_comments
FROM user_behavior_view ub
         JOIN content_popularity_view cp ON ub.owner = cp.owner
         JOIN system_analytics_view sa ON sa.month = DATE_FORMAT(
        (SELECT u.register_date FROM users u WHERE u.owner = ub.owner),
        '%Y-%m'
                                                     )
WHERE ub.post_count > 0
  AND cp.like_count > 0
  AND cp.comment_count > 0
  AND EXISTS (
    SELECT 1 FROM follows f
    WHERE f.follower_name = ub.owner
      AND f.follow_date >= DATE_SUB(NOW(), INTERVAL 1 YEAR)
)
  AND EXISTS (
    SELECT 1 FROM likes l
    WHERE l.owner = ub.owner
      AND l.like_date >= DATE_SUB(NOW(), INTERVAL 6 MONTH)
)
ORDER BY sa.month DESC, cp.like_count + cp.comment_count DESC
LIMIT 200;


CREATE INDEX follows_follower_followee_ix ON follows(follower_name, followee_name);
CREATE INDEX likes_post_user_date_ix ON likes(post_id, owner, like_date);

SELECT
    ub.owner,
    ub.post_count,
    ub.follower_count,
    ub.following_count,
    ub.like_count,
    ub.comment_count,
    cp.post_id,
    cp.description,
    cp.like_count AS post_likes,
    cp.comment_count AS post_comments,
    sa.month,
    sa.total_likes,
    sa.new_posts,
    sa.new_comments
FROM user_behavior_view ub
         JOIN content_popularity_view cp ON ub.owner = cp.owner
         JOIN system_analytics_view sa ON sa.month = DATE_FORMAT(
        (SELECT u.register_date FROM users u WHERE u.owner = ub.owner),
        '%Y-%m'
                                                     )
WHERE ub.post_count > 0
  AND cp.like_count > 0
  AND cp.comment_count > 0
  AND EXISTS (
    SELECT 1 FROM follows f
    WHERE f.follower_name = ub.owner
      AND f.follow_date >= DATE_SUB(NOW(), INTERVAL 1 YEAR)
)
  AND EXISTS (
    SELECT 1 FROM likes l
    WHERE l.owner = ub.owner
      AND l.like_date >= DATE_SUB(NOW(), INTERVAL 6 MONTH)
)
ORDER BY sa.month DESC, cp.like_count + cp.comment_count DESC
LIMIT 200;


