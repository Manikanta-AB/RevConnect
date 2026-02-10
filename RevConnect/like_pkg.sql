CREATE OR REPLACE PACKAGE like_pkg AS
    /**
     * Toggles a like on a post for a specific user.
     * Returns 'LIKED' if added, 'UNLIKED' if removed.
     */
    PROCEDURE toggle_like(
        p_post_id IN NUMBER,
        p_user_id IN NUMBER,
        p_status OUT VARCHAR2
    );

    /**
     * Returns the total number of likes for a given post.
     */
    FUNCTION get_like_count(
        p_post_id IN NUMBER
    ) RETURN NUMBER;

END like_pkg;
/

CREATE OR REPLACE PACKAGE BODY like_pkg AS

    PROCEDURE toggle_like(
        p_post_id IN NUMBER,
        p_user_id IN NUMBER,
        p_status OUT VARCHAR2
    ) IS
        l_count NUMBER;
    BEGIN
        -- Check if user already liked the post
        SELECT COUNT(*) INTO l_count 
        FROM likes 
        WHERE post_id = p_post_id AND user_id = p_user_id;

        IF l_count > 0 THEN
            -- Already liked, so remove it
            DELETE FROM likes 
            WHERE post_id = p_post_id AND user_id = p_user_id;
            p_status := 'UNLIKED';
        ELSE
            -- Not liked, so add it
            INSERT INTO likes (post_id, user_id) 
            VALUES (p_post_id, p_user_id);
            p_status := 'LIKED';
        END IF;
        
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            p_status := 'ERROR';
            ROLLBACK;
    END toggle_like;

    FUNCTION get_like_count(
        p_post_id IN NUMBER
    ) RETURN NUMBER IS
        l_count NUMBER;
    BEGIN
        SELECT COUNT(*) INTO l_count 
        FROM likes 
        WHERE post_id = p_post_id;
        
        RETURN l_count;
    EXCEPTION
        WHEN OTHERS THEN
            RETURN 0;
    END get_like_count;

END like_pkg;
/
