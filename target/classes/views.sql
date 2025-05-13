-- View 1: User behavior - posts, followers, following, likes, comments per user
CREATE OR REPLACE VIEW user_behavior_view AS
SELECT
    u.username,
    COUNT(DISTINCT p.post_id) AS post_count,
    COUNT(DISTINCT f1.follower_name) AS follower_count,
    COUNT(DISTINCT f2.followee_name) AS following_count,
    COUNT(DISTINCT l.post_id) AS like_count,
    COUNT(DISTINCT c.comment_id) AS comment_count
FROM users u
         LEFT JOIN posts p ON u.username = p.poster
         LEFT JOIN follows f1 ON u.username = f1.followee_name
         LEFT JOIN follows f2 ON u.username = f2.follower_name
         LEFT JOIN likes l ON u.username = l.liker
         LEFT JOIN comments c ON u.username = c.commenter
GROUP BY u.username
HAVING post_count > 0 OR comment_count > 0 OR like_count > 0;

-- View 2: Content popularity - most liked and commented posts
CREATE OR REPLACE VIEW content_popularity_view AS
SELECT
    p.post_id,
    p.description,
    u.username,
    COUNT(DISTINCT l.liker) AS like_count,
    COUNT(DISTINCT c.comment_id) AS comment_count
FROM posts p
         JOIN users u ON p.poster = u.username
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


