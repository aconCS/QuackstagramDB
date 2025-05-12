-- User Table
CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- model.group70.quackstagram.Post Table
CREATE TABLE model.group70.quackstagram.Post (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- Comment Table
CREATE TABLE Comment (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES model.group70.quackstagram.Post(post_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- Follow Table
CREATE TABLE Follow (
    follower_id INT NOT NULL,
    followee_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followee_id),
    FOREIGN KEY (follower_id) REFERENCES User(user_id),
    FOREIGN KEY (followee_id) REFERENCES User(user_id)
);

-- INSERTS for testing
INSERT INTO User (username, email, first_name, last_name, password_hash) VALUES
('alice', 'alice@example.com', 'Alice', 'Smith', 'hash1'),
('bob', 'bob@example.com', 'Bob', 'Johnson', 'hash2'),
('charlie', 'charlie@example.com', 'Charlie', 'Lee', 'hash3');

INSERT INTO model.group70.quackstagram.Post (user_id, content) VALUES
(1, 'Hello World!'),
(2, 'My first post'),
(1, 'Another update');

INSERT INTO Comment (post_id, user_id, content) VALUES
(1, 2, 'Nice post!'),
(1, 3, 'Welcome!'),
(2, 1, 'Good job!');

INSERT INTO Follow (follower_id, followee_id) VALUES
(2, 1),
(3, 1),
(1, 3);
