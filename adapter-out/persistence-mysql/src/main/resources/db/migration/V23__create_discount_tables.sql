-- V23: Discount 도메인 테이블 생성
-- 할인 정책, 할인 사용 이력 테이블

-- 1. discount_policies 테이블 (할인 정책)
CREATE TABLE discount_policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NULL COMMENT '판매자 ID (NULL이면 플랫폼 할인)',
    policy_name VARCHAR(100) NOT NULL COMMENT '정책명',
    discount_group VARCHAR(30) NOT NULL COMMENT '할인 그룹 (COUPON, PROMOTION, EVENT)',
    discount_type VARCHAR(30) NOT NULL COMMENT '할인 유형 (PERCENTAGE, FIXED_AMOUNT)',
    target_type VARCHAR(30) NOT NULL COMMENT '적용 대상 (ALL, CATEGORY, PRODUCT, SELLER)',
    target_ids VARCHAR(1000) NULL COMMENT '적용 대상 ID 목록 (쉼표 구분)',
    discount_rate DECIMAL(5, 2) NULL COMMENT '할인율 (PERCENTAGE 타입)',
    discount_amount BIGINT NULL COMMENT '할인 금액 (FIXED_AMOUNT 타입)',
    maximum_discount_amount BIGINT NULL COMMENT '최대 할인 금액',
    minimum_order_amount BIGINT NULL COMMENT '최소 주문 금액',
    valid_start_at DATETIME(6) NOT NULL COMMENT '유효 시작일',
    valid_end_at DATETIME(6) NOT NULL COMMENT '유효 종료일',
    max_usage_per_customer INT NULL COMMENT '고객당 최대 사용 횟수',
    max_total_usage INT NULL COMMENT '전체 최대 사용 횟수',
    platform_cost_share_ratio DECIMAL(5, 2) NOT NULL DEFAULT 0.00 COMMENT '플랫폼 비용 분담률',
    seller_cost_share_ratio DECIMAL(5, 2) NOT NULL DEFAULT 100.00 COMMENT '판매자 비용 분담률',
    priority INT NOT NULL DEFAULT 0 COMMENT '적용 우선순위',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '활성화 여부',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL COMMENT 'Soft Delete 일시',

    INDEX idx_discount_policies_seller_id (seller_id),
    INDEX idx_discount_policies_discount_group (discount_group),
    INDEX idx_discount_policies_discount_type (discount_type),
    INDEX idx_discount_policies_target_type (target_type),
    INDEX idx_discount_policies_valid_start_at (valid_start_at),
    INDEX idx_discount_policies_valid_end_at (valid_end_at),
    INDEX idx_discount_policies_is_active (is_active),
    INDEX idx_discount_policies_priority (priority),
    INDEX idx_discount_policies_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='할인 정책';

-- 2. discount_usage_histories 테이블 (할인 사용 이력)
CREATE TABLE discount_usage_histories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    discount_policy_id BIGINT NOT NULL COMMENT '할인 정책 ID',
    member_id VARCHAR(36) NOT NULL COMMENT '회원 ID',
    checkout_id VARCHAR(36) NOT NULL COMMENT '체크아웃 ID',
    order_id VARCHAR(36) NULL COMMENT '주문 ID',
    applied_amount BIGINT NOT NULL COMMENT '적용된 할인 금액',
    original_amount BIGINT NOT NULL COMMENT '원래 주문 금액',
    platform_ratio DECIMAL(5, 2) NOT NULL COMMENT '플랫폼 분담 비율',
    seller_ratio DECIMAL(5, 2) NOT NULL COMMENT '판매자 분담 비율',
    platform_cost BIGINT NOT NULL COMMENT '플랫폼 부담 금액',
    seller_cost BIGINT NOT NULL COMMENT '판매자 부담 금액',
    used_at DATETIME(6) NOT NULL COMMENT '사용 일시',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    INDEX idx_discount_usage_histories_discount_policy_id (discount_policy_id),
    INDEX idx_discount_usage_histories_member_id (member_id),
    INDEX idx_discount_usage_histories_checkout_id (checkout_id),
    INDEX idx_discount_usage_histories_order_id (order_id),
    INDEX idx_discount_usage_histories_used_at (used_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='할인 사용 이력';
