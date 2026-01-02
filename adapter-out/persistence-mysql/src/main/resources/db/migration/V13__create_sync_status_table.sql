-- ========================================
-- 동기화 상태 관리 테이블
-- ========================================
-- 레거시 → 신규 DB 증분 동기화 상태 추적
-- Strangler Fig 패턴 지원
-- ========================================

CREATE TABLE sync_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    domain_name VARCHAR(50) NOT NULL UNIQUE COMMENT '도메인명 (member, product 등)',
    last_sync_at DATETIME(6) NOT NULL COMMENT '마지막 동기화 시간',
    last_synced_count BIGINT DEFAULT 0 COMMENT '마지막 동기화 건수',
    total_synced_count BIGINT DEFAULT 0 COMMENT '누적 동기화 건수',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE, PAUSED, COMPLETED',
    sync_interval_minutes INT DEFAULT 10 COMMENT '동기화 주기 (분)',
    error_message TEXT NULL COMMENT '마지막 에러 메시지',
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    INDEX idx_sync_status_domain (domain_name),
    INDEX idx_sync_status_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='동기화 상태 관리 테이블';

-- 초기 도메인 등록
INSERT INTO sync_status (domain_name, last_sync_at, sync_interval_minutes, status) VALUES
('member', '1970-01-01 00:00:00.000000', 10, 'ACTIVE'),
('shipping_address', '1970-01-01 00:00:00.000000', 30, 'ACTIVE'),
('refund_account', '1970-01-01 00:00:00.000000', 30, 'ACTIVE'),
('product', '1970-01-01 00:00:00.000000', 1, 'ACTIVE'),
('product_group', '1970-01-01 00:00:00.000000', 1, 'ACTIVE');
