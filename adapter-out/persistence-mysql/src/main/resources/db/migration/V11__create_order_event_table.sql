-- =============================================================================
-- V11__create_order_event_table.sql
-- Order Event 테이블 생성
-- =============================================================================
-- 주문 이벤트 이력 추적용 테이블
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. order_event 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS order_event (
    id BIGINT NOT NULL AUTO_INCREMENT,
    order_id VARCHAR(36) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_source VARCHAR(20) NOT NULL,
    source_id VARCHAR(36),
    previous_status VARCHAR(30),
    current_status VARCHAR(30),
    actor_type VARCHAR(20) NOT NULL,
    actor_id VARCHAR(36),
    description VARCHAR(500),
    metadata JSON,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_order_event_order_id (order_id),
    INDEX idx_order_event_source (event_source, source_id),
    INDEX idx_order_event_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
