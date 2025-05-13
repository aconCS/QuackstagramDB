-- User Table
CREATE TABLE users
(
    username VARCHAR(50) PRIMARY KEY NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    bio TEXT,
    profile_picture_url VARCHAR(255),
    register_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Post Table
CREATE TABLE posts
(
    post_id INT PRIMARY KEY AUTO_INCREMENT,
    owner VARCHAR(50),
    picture_url VARCHAR(255),
    description TEXT,
    post_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner) REFERENCES users(username)
);

-- Comment Table
CREATE TABLE comments
(
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT,
    owner VARCHAR(50),
    content TEXT,
    comment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (owner) REFERENCES users(username)
);

-- Likes Table
CREATE TABLE likes
(
    post_id   INT,
    liker   VARCHAR(50),
    like_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id, liker),
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (liker) REFERENCES users(username)
);


-- Follow Table
CREATE TABLE follows (
    follower_name VARCHAR(50),
    followee_name VARCHAR(50),
    follow_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_name, followee_name),
    FOREIGN KEY (follower_name) REFERENCES users(username),
    FOREIGN KEY (followee_name) REFERENCES users(username)
);

-- Notification Table
CREATE TABLE notifications
(
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    owner VARCHAR(50),
    type VARCHAR(50),
    message VARCHAR(256),
    related_username VARCHAR(50),
    is_read BOOLEAN DEFAULT false,
    date DATETIME,
    FOREIGN KEY (owner) REFERENCES users(username),
    FOREIGN KEY (related_username) REFERENCES users(username)
);

-- MOCK DATA INSERTION IN mock_data.sql (LLM GENERATED)