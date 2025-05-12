-- View 1: User's  (posts, following, followers) - User Behavior
CREATE OR REPLACE VIEW user_profile_info AS
SELECT
    u.user_id,
    u.username,
    COUNT(DISTINCT p.post_id) AS posts_count,
    (SELECT COUNT(*) FROM follows WHERE followee_id = u.user_id) AS follower_count,
    (SELECT COUNT(*) FROM follows WHERE follower_id = u.user_id) AS following_count
FROM users u
         LEFT JOIN posts p ON u.user_id = p.user_id
GROUP BY u.user_id;


-- View 2: Post's popularity (most liked and most commented posts)
CREATE OR REPLACE VIEW content_popularity AS
SELECT
    p.post_id,
    p.description,
    u.username AS author,
    COUNT(DISTINCT l.user_id) AS like_count,
    COUNT(DISTINCT c.comment_id) AS comment_count
FROM posts p
         LEFT JOIN likes l ON p.post_id = l.post_id
         LEFT JOIN comments c ON p.post_id = c.post_id
         JOIN users u ON p.user_id = u.user_id
GROUP BY p.post_id
ORDER BY like_count + comment_count;


-- View 3: Active user's analytics ranked on post, comment and like counts
CREATE OR REPLACE VIEW top_active_users AS
SELECT
    u.user_id,
    u.username,
    COUNT(p.post_id) AS post_count,
    COUNT(c.comment_id) AS comment_count,
    COUNT(l.post_id) AS like_count
FROM users u
         LEFT JOIN posts p ON u.user_id = p.user_id
         LEFT JOIN comments c ON u.user_id = c.user_id
         LEFT JOIN likes l ON u.user_id = l.user_id
GROUP BY u.user_id
HAVING post_count > 0 OR comment_count > 0 OR like_count > 0 -- Ensure that they are active
ORDER BY post_count + comment_count + like_count;

SELECT * FROM top_active_users;
SELECT * FROM content_popularity;
SELECT * FROM user_profile_info;

CREATE INDEX posts_user_id_ix ON posts(user_id);
CREATE INDEX likes_post_id_ix ON likes(post_id);
CREATE INDEX follows_followee_id_ix ON follows(followee_id);
CREATE INDEX follows_follower_id_ix ON follows(follower_id);
