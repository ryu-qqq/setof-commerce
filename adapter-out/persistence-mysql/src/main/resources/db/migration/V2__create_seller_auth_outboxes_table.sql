-- =====================================================
-- V2: 셀러 인증 Outbox 테이블 생성
-- =====================================================

-- 셀러 인증 Outbox 테이블
-- 외부 인증 서버(Tenant/Organization 생성)를 위한 Outbox 패턴 테이블
--
-- 동시성 제어:
--   version: JPA @Version 낙관적 락용
--   updated_at: PROCESSING 좀비 상태 감지용 (타임아웃 체크)
--   idempotency_key: 외부 API 호출 멱등성 보장 (Auth Hub 전달용)
CREATE TABLE seller_auth_outboxes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL COMMENT '셀러 ID',
    payload TEXT NOT NULL COMMENT 'JSON 페이로드 (셀러 정보)',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING, PROCESSING, COMPLETED, FAILED',
    retry_count INT NOT NULL DEFAULT 0 COMMENT '재시도 횟수',
    max_retry INT NOT NULL DEFAULT 3 COMMENT '최대 재시도 횟수',
    created_at DATETIME(6) NOT NULL COMMENT '생성 시각',
    updated_at DATETIME(6) NOT NULL COMMENT '갱신 시각 (좀비 상태 감지용)',
    processed_at DATETIME(6) NULL COMMENT '처리 완료 시각',
    error_message VARCHAR(1000) NULL COMMENT '에러 메시지',
    version BIGINT NOT NULL DEFAULT 0 COMMENT '낙관적 락 버전',
    idempotency_key VARCHAR(100) NOT NULL COMMENT '멱등키 (SAO:{sellerId}:{epochMilli})',
    UNIQUE KEY uk_seller_auth_outboxes_idempotency_key (idempotency_key),
    INDEX idx_seller_auth_outboxes_seller_id (seller_id),
    INDEX idx_seller_auth_outboxes_status (status),
    INDEX idx_seller_auth_outboxes_status_retry (status, retry_count, max_retry),
    INDEX idx_seller_auth_outboxes_created_at (created_at),
    INDEX idx_seller_auth_outboxes_status_updated (status, updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='셀러 인증 Outbox';
