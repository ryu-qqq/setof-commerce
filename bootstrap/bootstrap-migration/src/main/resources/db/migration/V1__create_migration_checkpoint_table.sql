-- ============================================
-- Migration Checkpoint 테이블
-- 스키마: setof_migration
-- 용도: 도메인별 마이그레이션 진행 상태 추적
-- ============================================

CREATE TABLE IF NOT EXISTS migration_checkpoint (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- 도메인 식별
    domain_name VARCHAR(50) NOT NULL COMMENT '도메인명 (member, product, order 등)',

    -- PK 기반 체크포인트
    last_migrated_id BIGINT NOT NULL DEFAULT 0 COMMENT '마지막으로 마이그레이션한 레거시 PK',

    -- 검증 정보
    migrated_count BIGINT NOT NULL DEFAULT 0 COMMENT '누적 마이그레이션 건수',
    legacy_total_count BIGINT DEFAULT NULL COMMENT '레거시 전체 건수 (스냅샷)',

    -- 상태 관리
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING, RUNNING, COMPLETED, FAILED, PAUSED',
    error_message TEXT COMMENT '에러 메시지',

    -- 실행 이력
    last_executed_at TIMESTAMP NULL COMMENT '마지막 실행 시작 시간',
    last_completed_at TIMESTAMP NULL COMMENT '마지막 실행 완료 시간',
    execution_time_ms BIGINT DEFAULT NULL COMMENT '마지막 실행 소요시간(ms)',

    -- 감사
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- 제약조건
    CONSTRAINT uk_migration_checkpoint_domain UNIQUE (domain_name),

    -- 인덱스
    INDEX idx_migration_checkpoint_status (status),
    INDEX idx_migration_checkpoint_domain_status (domain_name, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='마이그레이션 체크포인트';

-- ============================================
-- 초기 도메인 데이터 삽입
-- 필요한 도메인을 미리 등록
-- ============================================

INSERT INTO migration_checkpoint (domain_name, status) VALUES
    ('member', 'PENDING'),
    ('shipping_address', 'PENDING'),
    ('refund_account', 'PENDING'),
    ('product', 'PENDING'),
    ('product_group', 'PENDING'),
    ('order', 'PENDING'),
    ('payment', 'PENDING'),
    ('review', 'PENDING'),
    ('qna', 'PENDING')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;
