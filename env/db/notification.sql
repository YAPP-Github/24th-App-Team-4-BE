-- NOTIFICATION table
CREATE TABLE IF NOT EXISTS notification
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id           BIGINT       NOT NULL,
    notification_type VARCHAR(50)  NOT NULL,
    title             VARCHAR(255) NOT NULL,
    body              TEXT         NOT NULL,
    category_image_url VARCHAR(2048),
    is_read           BOOLEAN      NOT NULL DEFAULT FALSE,
    navigation_type   VARCHAR(50),
    deep_link         VARCHAR(2048),
    is_deleted        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at        TIMESTAMP    NOT NULL,
    updated_at        TIMESTAMP    NOT NULL,
    INDEX idx_user_deleted_created (user_id, is_deleted, created_at DESC),
    INDEX idx_user_read_deleted (user_id, is_read, is_deleted)
);

-- PUSH_MESSAGE_TEMPLATE table
CREATE TABLE IF NOT EXISTS push_message_template
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    notification_type VARCHAR(50)  NOT NULL,
    title             VARCHAR(255) NOT NULL,
    body              TEXT         NOT NULL,
    navigation_type   VARCHAR(50)  NOT NULL,
    created_at        TIMESTAMP    NOT NULL,
    updated_at        TIMESTAMP    NOT NULL
);

INSERT INTO push_message_template (notification_type, title, body, navigation_type, created_at, updated_at)
VALUES ('LINK_ADDED', '''{categoryName}'' 포킷에 링크가 추가되었어요', '{nickname}님이 추가한 링크를 지금 확인해보세요', 'CONTENT_DETAIL', NOW(), NOW()),
       ('POKIT_USE_RESTRICTION', '''{categoryName}'' 포킷 사용이 제한되었어요', '포킷 관리자가 회원님을 내보냈어요', 'NONE', NOW(), NOW()),
       ('NEW_MEMBER_JOINED', '새로운 멤버가 참여했어요!', '''{categoryName}'' 포킷에 {nickname}님이 참여했어요', 'CATEGORY_DETAIL', NOW(), NOW());

-- SHARED_CATEGORY 테이블에 알림 설정 컬럼 추가
ALTER TABLE SHARED_CATEGORY ADD COLUMN alert_enabled BOOLEAN NOT NULL DEFAULT FALSE;

