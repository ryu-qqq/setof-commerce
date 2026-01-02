-- V21: Q&A 도메인 테이블 생성
-- 문의, 문의 답변, 문의 이미지 테이블

-- 1. qnas 테이블 (문의)
CREATE TABLE qnas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    qna_type VARCHAR(20) NOT NULL COMMENT '문의 유형 (PRODUCT, ORDER, SELLER, GENERAL)',
    detail_type VARCHAR(30) NOT NULL COMMENT '상세 유형',
    target_id BIGINT NULL COMMENT '대상 ID (상품ID, 주문ID 등)',
    writer_id VARCHAR(36) NOT NULL COMMENT '작성자 ID',
    writer_type VARCHAR(20) NOT NULL COMMENT '작성자 유형 (MEMBER, SELLER, ADMIN)',
    writer_name VARCHAR(50) NOT NULL COMMENT '작성자 이름',
    title VARCHAR(200) NOT NULL COMMENT '제목',
    content TEXT NOT NULL COMMENT '내용',
    is_secret BOOLEAN NOT NULL DEFAULT FALSE COMMENT '비밀글 여부',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '상태 (PENDING, ANSWERED, CLOSED)',
    reply_count INT NOT NULL DEFAULT 0 COMMENT '답변 수',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL COMMENT 'Soft Delete 일시',

    INDEX idx_qnas_qna_type (qna_type),
    INDEX idx_qnas_detail_type (detail_type),
    INDEX idx_qnas_target_id (target_id),
    INDEX idx_qnas_writer_id (writer_id),
    INDEX idx_qnas_writer_type (writer_type),
    INDEX idx_qnas_status (status),
    INDEX idx_qnas_deleted_at (deleted_at),
    INDEX idx_qnas_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='문의';

-- 2. qna_replies 테이블 (문의 답변)
CREATE TABLE qna_replies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    qna_id BIGINT NOT NULL COMMENT '문의 ID',
    parent_reply_id BIGINT NULL COMMENT '부모 답변 ID (대댓글)',
    writer_id VARCHAR(36) NOT NULL COMMENT '작성자 ID',
    writer_type VARCHAR(20) NOT NULL COMMENT '작성자 유형 (MEMBER, SELLER, ADMIN)',
    writer_name VARCHAR(50) NOT NULL COMMENT '작성자 이름',
    content TEXT NOT NULL COMMENT '답변 내용',
    path VARCHAR(500) NULL COMMENT 'Materialized Path (계층 구조)',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL COMMENT 'Soft Delete 일시',

    INDEX idx_qna_replies_qna_id (qna_id),
    INDEX idx_qna_replies_parent_reply_id (parent_reply_id),
    INDEX idx_qna_replies_writer_id (writer_id),
    INDEX idx_qna_replies_writer_type (writer_type),
    INDEX idx_qna_replies_path (path(255)),
    INDEX idx_qna_replies_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='문의 답변';

-- 3. qna_images 테이블 (문의 이미지)
CREATE TABLE qna_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    qna_id BIGINT NOT NULL COMMENT '문의 ID',
    image_url VARCHAR(500) NOT NULL COMMENT '이미지 URL',
    display_order INT NOT NULL DEFAULT 0 COMMENT '표시 순서',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    INDEX idx_qna_images_qna_id (qna_id),
    INDEX idx_qna_images_display_order (display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='문의 이미지';
