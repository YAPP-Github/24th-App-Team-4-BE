-- NOTIFICATION table
CREATE TABLE IF NOT EXISTS notification
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id           BIGINT       NOT NULL,
    notification_type VARCHAR(50)  NOT NULL,
    title             VARCHAR(255) NOT NULL,
    body              TEXT         NOT NULL,
    thumbnail_url     VARCHAR(2048),
    is_read           BOOLEAN      NOT NULL DEFAULT FALSE,
    navigation_type   VARCHAR(50),
    deep_link         VARCHAR(2048),
    is_deleted        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at        TIMESTAMP    NOT NULL,
    updated_at        TIMESTAMP    NOT NULL,
    INDEX idx_user_deleted_created (user_id, is_deleted, created_at DESC),
    INDEX idx_user_read_deleted (user_id, is_read, is_deleted)
);

