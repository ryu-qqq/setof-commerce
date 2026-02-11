-- =====================================================
-- V8: SellerAdmin 테이블 생성
-- =====================================================

CREATE TABLE seller_admins (
    id VARCHAR(36) PRIMARY KEY COMMENT '셀러 관리자 ID (UUIDv7)',
    seller_id BIGINT NOT NULL COMMENT '셀러 ID',
    auth_user_id VARCHAR(100) NULL COMMENT 'AuthHub 사용자 ID',
    login_id VARCHAR(100) NOT NULL COMMENT '로그인 ID (이메일)',
    name VARCHAR(50) NOT NULL COMMENT '관리자 이름',
    phone_number VARCHAR(20) NULL COMMENT '전화번호',
    status VARCHAR(30) NOT NULL COMMENT '상태 (PENDING_APPROVAL, ACTIVE, SUSPENDED, WITHDRAWN)',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    UNIQUE KEY uk_seller_admins_login_id (login_id),
    INDEX idx_seller_admins_seller_id (seller_id),
    INDEX idx_seller_admins_status (status),
    INDEX idx_seller_admins_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
