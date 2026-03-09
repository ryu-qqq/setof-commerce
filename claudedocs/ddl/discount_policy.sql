-- ============================================================
-- discount_policy 테이블 DDL
-- ============================================================
-- 즉시할인/쿠폰의 기반이 되는 핵심 할인 규칙 테이블
-- ============================================================

CREATE TABLE IF NOT EXISTS discount_policy (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                    VARCHAR(50)  NOT NULL COMMENT '정책명',
    description             VARCHAR(500) NULL COMMENT '정책 설명',
    discount_method         VARCHAR(20)  NOT NULL COMMENT '할인 방식 (RATE, FIXED_AMOUNT)',
    discount_rate           DOUBLE       NULL COMMENT '할인율 (0~100, RATE 타입 시 필수)',
    discount_amount         INT          NULL COMMENT '정액 할인금 (FIXED_AMOUNT 타입 시 필수)',
    max_discount_amount     INT          NULL COMMENT '최대 할인 한도액',
    discount_capped         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '한도 적용 여부',
    minimum_order_amount    INT          NULL COMMENT '최소 주문금액',
    application_type        VARCHAR(20)  NOT NULL COMMENT '적용 방식 (IMMEDIATE, COUPON)',
    publisher_type          VARCHAR(20)  NOT NULL COMMENT '생성 주체 (ADMIN, BRAND, SELLER)',
    seller_id               BIGINT       NULL COMMENT '셀러 ID (셀러 할인 시)',
    stacking_group          VARCHAR(30)  NOT NULL COMMENT '스태킹 그룹 (SELLER_INSTANT, PLATFORM_INSTANT, COUPON)',
    priority                INT          NOT NULL DEFAULT 0 COMMENT '우선순위',
    start_at                DATETIME(6)  NOT NULL COMMENT '유효 시작 시점',
    end_at                  DATETIME(6)  NOT NULL COMMENT '유효 종료 시점',
    total_budget            INT          NOT NULL DEFAULT 0 COMMENT '총 예산',
    used_budget             INT          NOT NULL DEFAULT 0 COMMENT '사용된 예산',
    active                  TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '활성 여부',
    deleted_at              DATETIME(6)  NULL COMMENT 'Soft Delete 시각',
    created_at              DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at              DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    INDEX idx_discount_policy_active_period (active, start_at, end_at),
    INDEX idx_discount_policy_application_type (application_type, active),
    INDEX idx_discount_policy_seller (seller_id, active),
    INDEX idx_discount_policy_stacking_group (stacking_group, active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='할인 정책';

-- ============================================================
-- discount_target 테이블 DDL
-- ============================================================
-- 할인 정책이 적용되는 대상 (브랜드, 셀러, 카테고리, 상품)
-- ============================================================

CREATE TABLE IF NOT EXISTS discount_target (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    discount_policy_id BIGINT       NOT NULL COMMENT '할인 정책 ID',
    target_type       VARCHAR(20)  NOT NULL COMMENT '대상 유형 (BRAND, SELLER, CATEGORY, PRODUCT)',
    target_id         BIGINT       NOT NULL COMMENT '대상 ID',
    active            TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '활성 여부',
    created_at        DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at        DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    INDEX idx_discount_target_policy (discount_policy_id, active),
    INDEX idx_discount_target_type_id (target_type, target_id, active),
    INDEX idx_discount_target_lookup (target_type, target_id, discount_policy_id, active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='할인 적용 대상';
