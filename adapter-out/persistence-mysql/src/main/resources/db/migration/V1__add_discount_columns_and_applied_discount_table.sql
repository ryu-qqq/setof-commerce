-- V1: 상품그룹 할인 컬럼 추가 + 적용 할인 내역 테이블 생성
-- Hybrid Discount Architecture:
--   product_groups에 할인 필드 Materialized (정렬/필터 성능)
--   product_group_applied_discounts에 개별 할인 내역 (정산/감사)

-- ------------------------------------------------------------
-- 1. product_groups 테이블에 할인 컬럼 추가
-- ------------------------------------------------------------
ALTER TABLE `product_groups`
    ADD COLUMN `discount_rate` int NOT NULL DEFAULT 0 COMMENT '전체 할인율 (정가 대비, 0~100)' AFTER `sale_price`,
    ADD COLUMN `direct_discount_rate` int NOT NULL DEFAULT 0 COMMENT '즉시할인율 (현재가 대비, 0~100)' AFTER `discount_rate`,
    ADD COLUMN `direct_discount_price` int NOT NULL DEFAULT 0 COMMENT '즉시할인가 (현재가 - 판매가)' AFTER `direct_discount_rate`;

-- 정렬/필터 인덱스
CREATE INDEX `idx_product_groups_discount_rate` ON `product_groups` (`discount_rate`);
CREATE INDEX `idx_product_groups_sale_price` ON `product_groups` (`sale_price`);

-- ------------------------------------------------------------
-- 2. product_group_applied_discounts (적용 할인 내역 - 정산/감사용)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_group_applied_discounts` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `product_group_id` bigint NOT NULL COMMENT '상품그룹 ID',
    `discount_policy_id` bigint NOT NULL COMMENT '할인 정책 ID',
    `stacking_group` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '스태킹 그룹 (SELLER_INSTANT, PLATFORM_INSTANT, COUPON)',
    `discount_rate` int NOT NULL DEFAULT 0 COMMENT '해당 정책의 할인율',
    `discount_amount` int NOT NULL DEFAULT 0 COMMENT '해당 정책의 할인 금액',
    `share_ratio` decimal(5,4) NOT NULL DEFAULT 0.0000 COMMENT '전체 할인 대비 비율 (0.0~1.0)',
    `applied_at` datetime(6) NOT NULL COMMENT '할인 적용 시각',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_applied_discounts_product_group_id` (`product_group_id`),
    KEY `idx_applied_discounts_policy_id` (`discount_policy_id`),
    KEY `idx_applied_discounts_stacking_group` (`stacking_group`),
    KEY `idx_applied_discounts_applied_at` (`applied_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
