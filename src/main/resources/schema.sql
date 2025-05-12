-- User Table
CREATE TABLE users
(
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    bio TEXT,
    profile_picture_url VARCHAR(255),
    register_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Post Table
CREATE TABLE posts
(
    post_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    picture_url VARCHAR(255),
    description TEXT,
    post_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Comment Table
CREATE TABLE comments
(
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT,
    user_id INT,
    content TEXT,
    comment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Likes Table
CREATE TABLE likes
(
    post_id   INT,
    user_id   INT,
    like_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


-- Follow Table
CREATE TABLE follows (
    follower_id INT,
    followee_id INT,
    follow_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followee_id),
    FOREIGN KEY (follower_id) REFERENCES users(user_id),
    FOREIGN KEY (followee_id) REFERENCES users(user_id)
);



-- Notification Table
CREATE TABLE notifications
(
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    message_type VARCHAR(50),
    message TEXT,
    related_user_id INT,
    is_read BOOLEAN DEFAULT false,
    date DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (related_user_id) REFERENCES users(user_id)
);