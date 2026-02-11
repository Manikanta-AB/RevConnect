-- Users Table
CREATE TABLE users (
    user_id NUMBER PRIMARY KEY,
    username VARCHAR2(255) NOT NULL,
    email VARCHAR2(255) NOT NULL UNIQUE,
    password VARCHAR2(255) NOT NULL,
    user_type VARCHAR2(50), 
    security_question VARCHAR2(255),
    security_answer VARCHAR2(255),
    full_name VARCHAR2(255),
    bio CLOB,
    location VARCHAR2(255),
    website_link VARCHAR2(255),
    profile_picture_path VARCHAR2(255)
);

CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER users_bir 
BEFORE INSERT ON users 
FOR EACH ROW 
BEGIN
    IF :new.user_id IS NULL THEN
        SELECT users_seq.nextval INTO :new.user_id FROM dual;
    END IF;
END;
/

-- Posts Table
CREATE TABLE posts (
    post_id NUMBER PRIMARY KEY,
    user_id NUMBER,
    post_content CLOB,
    hashtags VARCHAR2(255),
    is_pinned NUMBER(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE SEQUENCE posts_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER posts_bir 
BEFORE INSERT ON posts 
FOR EACH ROW 
BEGIN
    IF :new.post_id IS NULL THEN
        SELECT posts_seq.nextval INTO :new.post_id FROM dual;
    END IF;
END;
/

-- Likes Table
CREATE TABLE likes (
    post_id NUMBER,
    user_id NUMBER,
    PRIMARY KEY (post_id, user_id),
    CONSTRAINT fk_likes_post FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Comments Table
CREATE TABLE comments (
    comment_id NUMBER PRIMARY KEY,
    post_id NUMBER,
    user_id NUMBER,
    comment_text CLOB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE SEQUENCE comments_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER comments_bir 
BEFORE INSERT ON comments 
FOR EACH ROW 
BEGIN
    IF :new.comment_id IS NULL THEN
        SELECT comments_seq.nextval INTO :new.comment_id FROM dual;
    END IF;
END;
/

-- Connections Table
CREATE TABLE connections (
    connection_id NUMBER PRIMARY KEY,
    sender_id NUMBER,
    receiver_id NUMBER,
    status VARCHAR2(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_connections_sender FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_connections_receiver FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE SEQUENCE connections_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER connections_bir 
BEFORE INSERT ON connections 
FOR EACH ROW 
BEGIN
    IF :new.connection_id IS NULL THEN
        SELECT connections_seq.nextval INTO :new.connection_id FROM dual;
    END IF;
END;
/

-- Messages Table
CREATE TABLE messages (
    message_id NUMBER PRIMARY KEY,
    sender_id NUMBER,
    receiver_id NUMBER,
    message_text CLOB,
    is_read NUMBER(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_messages_sender FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_receiver FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE SEQUENCE messages_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER messages_bir 
BEFORE INSERT ON messages 
FOR EACH ROW 
BEGIN
    IF :new.message_id IS NULL THEN
        SELECT messages_seq.nextval INTO :new.message_id FROM dual;
    END IF;
END;
/

-- Notifications Table
CREATE TABLE notifications (
    notification_id NUMBER PRIMARY KEY,
    user_id NUMBER,
    notification_type VARCHAR2(50),
    message CLOB,
    is_read NUMBER(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE SEQUENCE notifications_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER notifications_bir 
BEFORE INSERT ON notifications 
FOR EACH ROW 
BEGIN
    IF :new.notification_id IS NULL THEN
        SELECT notifications_seq.nextval INTO :new.notification_id FROM dual;
    END IF;
END;
/

-- Blocked Users Table
CREATE TABLE blocked_users (
    blocker_id NUMBER,
    blocked_id NUMBER,
    PRIMARY KEY (blocker_id, blocked_id),
    CONSTRAINT fk_blocked_blocker FOREIGN KEY (blocker_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_blocked_blocked FOREIGN KEY (blocked_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Followers Table
CREATE TABLE followers (
    follower_id NUMBER,
    following_id NUMBER,
    PRIMARY KEY (follower_id, following_id),
    CONSTRAINT fk_followers_follower FOREIGN KEY (follower_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_followers_following FOREIGN KEY (following_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Business/Creator Profiles Table
CREATE TABLE business_creator_profile (
    profile_id NUMBER PRIMARY KEY,
    user_id NUMBER UNIQUE,
    business_or_creator_name VARCHAR2(255),
    category VARCHAR2(255),
    industry VARCHAR2(255),
    detailed_bio CLOB,
    business_address VARCHAR2(255),
    contact_information VARCHAR2(255),
    website_links VARCHAR2(255),
    social_media_links VARCHAR2(255),
    business_hours VARCHAR2(255),
    CONSTRAINT fk_profile_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE SEQUENCE profile_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER profile_bir 
BEFORE INSERT ON business_creator_profile 
FOR EACH ROW 
BEGIN
    IF :new.profile_id IS NULL THEN
        SELECT profile_seq.nextval INTO :new.profile_id FROM dual;
    END IF;
END;
/

-- Standalone Utilities

-- Function to check if a user exists by email
CREATE OR REPLACE FUNCTION check_user_exists(
    p_email IN VARCHAR2
) RETURN NUMBER IS
    l_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO l_count 
    FROM users 
    WHERE email = p_email;
    
    IF l_count > 0 THEN
        RETURN 1;
    ELSE
        RETURN 0;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END check_user_exists;
/

-- Procedure to update user bio safely
CREATE OR REPLACE PROCEDURE update_user_bio(
    p_user_id IN NUMBER,
    p_new_bio IN CLOB
) IS
BEGIN
    UPDATE users 
    SET bio = p_new_bio 
    WHERE user_id = p_user_id;
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
END update_user_bio;
/
