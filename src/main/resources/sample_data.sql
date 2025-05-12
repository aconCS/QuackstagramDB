-- USERS
INSERT INTO users (username, password_hash, bio, profile_picture_url) VALUES
('alice', 'hash_alice', 'Nature lover and photographer.', 'alice_pic.jpg'),
('bob', 'hash_bob', 'Coffee addict and coder.', 'bob_pic.jpg'),
('carol', 'hash_carol', 'World traveler.', 'carol_pic.jpg'),
('dave', 'hash_dave', 'Tech nerd.', 'dave_pic.jpg'),
('eve', 'hash_eve', 'Reads everything.', 'eve_pic.jpg'),
('frank', 'hash_frank', 'Biker and gamer.', 'frank_pic.jpg'),
('grace', 'hash_grace', 'Painter and art blogger.', 'grace_pic.jpg');

-- POSTS
INSERT INTO posts (user_id, picture_url, description) VALUES
(1, 'forest.jpg', 'Morning walk in the forest.'),
(1, 'lake.jpg', 'Still water, calm mind.'),
(2, 'coffee.jpg', 'Nothing beats a good brew.'),
(2, 'laptop.jpg', 'Code and coffee = life.'),
(3, 'sunset.jpg', 'Somewhere in Bali.'),
(3, 'mountain.jpg', 'Climbed all the way up.'),
(4, 'keyboard.jpg', 'New mechanical keyboard!'),
(5, 'book.jpg', 'Finished “1984” again.'),
(6, 'bike.jpg', 'Weekend ride.'),
(7, 'painting.jpg', 'My latest acrylic work.');

-- COMMENTS
INSERT INTO comments (post_id, user_id, content) VALUES
(1, 2, 'Wow! Peaceful.'),
(1, 3, 'Where is this?'),
(2, 4, 'Serene vibes.'),
(3, 1, 'Can relate!'),
(3, 5, 'Perfect cup!'),
(4, 3, 'Nice desk.'),
(5, 6, 'Beautiful capture.'),
(6, 1, 'Inspirational!'),
(9, 7, 'I want this bike!');

-- LIKES
INSERT INTO likes (post_id, user_id) VALUES
(1, 2),
(1, 3),
(2, 4),
(3, 1),
(3, 5),
(4, 6),
(5, 2),
(6, 4),
(6, 1),
(7, 3),
(8, 6),
(9, 1),
(10, 2),
(10, 3),
(10, 5);

-- FOLLOWS
INSERT INTO follows (follower_id, followee_id) VALUES
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(1, 2),
(1, 3),
(1, 4),
(6, 2),
(7, 3),
(5, 6),
(6, 5),
(2, 7);

-- NOTIFICATIONS
INSERT INTO notifications (user_id, type, message, related_user_id, is_read, date) VALUES
(1, 'like', 'Bob liked your post.', 2, false, NOW()),
(1, 'comment', 'Carol commented on your post.', 3, false, NOW()),
(2, 'follow', 'Alice followed you.', 1, false, NOW()),
(3, 'like', 'Dave liked your post.', 4, true, NOW()),
(5, 'comment', 'Eve commented on your book post.', 5, false, NOW()),
(6, 'like', 'Grace liked your painting.', 7, false, NOW()),
(7, 'follow', 'Frank followed you.', 6, false, NOW());

