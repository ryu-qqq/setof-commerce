-- ============================================================
-- discount_outbox 테이블 DDL
-- ============================================================
-- 할인 정책 변경 시 가격 재계산을 위한 Outbox 테이블
-- 스케줄러가 1초 폴링으로 PENDING → SQS 발행 → Consumer 재계산
-- ============================================================

CREATE TABLE IF NOT EXISTS discount_outbox (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    target_type       VARCHAR(30)  NOT NULL COMMENT '할인 대상 유형 (PRODUCT, CATEGORY, BRAND, SELLER)',
    target_id         BIGINT       NOT NULL COMMENT '대상 ID',
    status            VARCHAR(20)  NOT NULL DEFAULT 'PENDING' COMMENT '상태 (PENDING, PUBLISHED, COMPLETED, FAILED)',
    retry_count       INT          NOT NULL DEFAULT 0 COMMENT '재시도 횟수',
    payload           TEXT         NULL COMMENT 'SQS 메시지 페이로드 (JSON)',
    fail_reason       VARCHAR(500) NULL COMMENT '실패 사유',
    created_at        DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at        DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    INDEX idx_discount_outbox_status_created (status, created_at),
    INDEX idx_discount_outbox_status_updated (status, updated_at),
    INDEX idx_discount_outbox_target (target_type, target_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='할인 가격 재계산 아웃박스';
