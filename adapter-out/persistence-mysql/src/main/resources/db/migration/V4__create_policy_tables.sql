-- =====================================================
-- V4: 환불 정책 및 배송 정책 테이블 생성
-- =====================================================
-- JPA 엔티티: RefundPolicyJpaEntity, ShippingPolicyJpaEntity
-- =====================================================

-- =====================================================
-- 1. 환불 정책 테이블 (refund_policies)
-- =====================================================
CREATE TABLE refund_policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    policy_name VARCHAR(100) NOT NULL,
    is_default_policy BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    return_period_days INT NOT NULL DEFAULT 7 COMMENT '반품 가능 기간 (일)',
    exchange_period_days INT NOT NULL DEFAULT 7 COMMENT '교환 가능 기간 (일)',
    non_returnable_conditions VARCHAR(500) COMMENT '반품 불가 조건',
    is_partial_refund_enabled BOOLEAN NOT NULL DEFAULT FALSE COMMENT '부분 환불 허용 여부',
    is_inspection_required BOOLEAN NOT NULL DEFAULT FALSE COMMENT '검수 필요 여부',
    inspection_period_days INT DEFAULT 0 COMMENT '검수 기간 (일)',
    additional_info VARCHAR(2000) COMMENT '추가 정보',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    INDEX idx_refund_policies_seller_id (seller_id),
    INDEX idx_refund_policies_seller_default (seller_id, is_default_policy),
    INDEX idx_refund_policies_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. 배송 정책 테이블 (shipping_policies)
-- =====================================================
CREATE TABLE shipping_policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    policy_name VARCHAR(100) NOT NULL,
    is_default_policy BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    shipping_fee_type VARCHAR(30) NOT NULL COMMENT 'FREE, FIXED, CONDITIONAL',
    base_fee INT COMMENT '기본 배송비',
    free_threshold INT COMMENT '무료 배송 기준 금액',
    jeju_extra_fee INT COMMENT '제주 추가 배송비',
    island_extra_fee INT COMMENT '도서산간 추가 배송비',
    return_fee INT COMMENT '반품 배송비',
    exchange_fee INT COMMENT '교환 배송비',
    lead_time_min_days INT COMMENT '최소 배송 소요일',
    lead_time_max_days INT COMMENT '최대 배송 소요일',
    lead_time_cutoff_time TIME COMMENT '당일 출고 마감 시간',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    INDEX idx_shipping_policies_seller_id (seller_id),
    INDEX idx_shipping_policies_seller_default (seller_id, is_default_policy),
    INDEX idx_shipping_policies_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
