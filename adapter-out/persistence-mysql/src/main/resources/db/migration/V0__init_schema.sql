-- ============================================================
-- V0: setof 스키마 초기 DDL
-- ============================================================
-- 실제 Stage DB (setof 스키마) 기준으로 작성
-- 생성일: 2026-03-11
-- ============================================================

-- ------------------------------------------------------------
-- 1. brand (브랜드)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `brand` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `brand_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `brand_icon_image_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `display_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `display_order` int NOT NULL DEFAULT '0',
    `displayed` tinyint(1) NOT NULL DEFAULT '1',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_brand_brand_name` (`brand_name`),
    KEY `idx_brand_displayed` (`displayed`),
    KEY `idx_brand_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 2. category (카테고리)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `category` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `category_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `category_depth` int NOT NULL,
    `parent_category_id` bigint NOT NULL DEFAULT '0',
    `display_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `displayed` tinyint(1) NOT NULL DEFAULT '1',
    `target_group` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `category_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `path` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_category_depth` (`category_depth`),
    KEY `idx_category_parent_id` (`parent_category_id`),
    KEY `idx_category_displayed` (`displayed`),
    KEY `idx_category_target_group` (`target_group`),
    KEY `idx_category_path` (`path`),
    KEY `idx_category_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 3. sellers (셀러)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `sellers` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `seller_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `display_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `logo_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `description` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_active` tinyint(1) NOT NULL DEFAULT '1',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sellers_seller_name` (`seller_name`),
    KEY `idx_sellers_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 4. seller_addresses (셀러 주소)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `seller_addresses` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `seller_id` bigint NOT NULL,
    `address_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `address_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `zipcode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `address` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `address_detail` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `contact_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `contact_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_default` tinyint(1) NOT NULL DEFAULT '0',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_seller_addresses_seller_id` (`seller_id`),
    KEY `idx_seller_addresses_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 5. seller_business_infos (셀러 사업자 정보)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `seller_business_infos` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `seller_id` bigint NOT NULL,
    `registration_number` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `company_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `representative` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `sale_report_number` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `business_zipcode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `business_address` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `business_address_detail` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_seller_business_infos_reg_num` (`registration_number`),
    KEY `idx_seller_business_infos_seller_id` (`seller_id`),
    KEY `idx_seller_business_infos_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 6. seller_cs (셀러 CS 정보)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `seller_cs` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `seller_id` bigint NOT NULL,
    `cs_phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `cs_mobile` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `cs_email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `operating_start_time` datetime(6) DEFAULT NULL,
    `operating_end_time` datetime(6) DEFAULT NULL,
    `operating_days` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `kakao_channel_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_seller_cs_seller_id` (`seller_id`),
    KEY `idx_seller_cs_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 7. shipping_policies (배송 정책)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `shipping_policies` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `seller_id` bigint NOT NULL,
    `policy_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `is_default_policy` tinyint(1) NOT NULL DEFAULT '0',
    `is_active` tinyint(1) NOT NULL DEFAULT '1',
    `shipping_fee_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
    `base_fee` int DEFAULT NULL,
    `free_threshold` int DEFAULT NULL,
    `jeju_extra_fee` int DEFAULT NULL,
    `island_extra_fee` int DEFAULT NULL,
    `return_fee` int DEFAULT NULL,
    `exchange_fee` int DEFAULT NULL,
    `lead_time_min_days` int DEFAULT NULL,
    `lead_time_max_days` int DEFAULT NULL,
    `lead_time_cutoff_time` time DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_shipping_policies_seller_id` (`seller_id`),
    KEY `idx_shipping_policies_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 8. refund_policies (환불 정책)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `refund_policies` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `seller_id` bigint NOT NULL,
    `policy_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `is_default_policy` tinyint(1) NOT NULL DEFAULT '0',
    `is_active` tinyint(1) NOT NULL DEFAULT '1',
    `return_period_days` int NOT NULL DEFAULT '0',
    `exchange_period_days` int NOT NULL DEFAULT '0',
    `non_returnable_conditions` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_partial_refund_enabled` tinyint(1) NOT NULL DEFAULT '0',
    `is_inspection_required` tinyint(1) NOT NULL DEFAULT '0',
    `inspection_period_days` int NOT NULL DEFAULT '0',
    `additional_info` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_refund_policies_seller_id` (`seller_id`),
    KEY `idx_refund_policies_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 9. common_code_types (공통 코드 타입)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `common_code_types` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `display_order` int NOT NULL DEFAULT '0',
    `is_active` tinyint(1) NOT NULL DEFAULT '1',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_common_code_types_code` (`code`),
    KEY `idx_common_code_types_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 10. common_codes (공통 코드)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `common_codes` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `common_code_type_id` bigint NOT NULL,
    `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `display_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `display_order` int NOT NULL DEFAULT '0',
    `is_active` tinyint(1) NOT NULL DEFAULT '1',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_common_codes_type_id` (`common_code_type_id`),
    KEY `idx_common_codes_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 11. product_groups (상품 그룹)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_groups` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `seller_id` bigint NOT NULL,
    `brand_id` bigint NOT NULL,
    `category_id` bigint NOT NULL,
    `shipping_policy_id` bigint NOT NULL,
    `refund_policy_id` bigint NOT NULL,
    `product_group_name` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `option_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `regular_price` int NOT NULL DEFAULT '0',
    `current_price` int NOT NULL DEFAULT '0',
    `sale_price` int NOT NULL DEFAULT '0',
    `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_product_groups_seller_id` (`seller_id`),
    KEY `idx_product_groups_brand_id` (`brand_id`),
    KEY `idx_product_groups_category_id` (`category_id`),
    KEY `idx_product_groups_status` (`status`),
    KEY `idx_product_groups_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 12. product_group_images (상품 그룹 이미지)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_group_images` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `product_group_id` bigint NOT NULL,
    `image_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `image_url` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
    `sort_order` int NOT NULL DEFAULT '0',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_product_group_images_group_id` (`product_group_id`),
    KEY `idx_product_group_images_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 13. product_group_descriptions (상품 그룹 상세설명)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_group_descriptions` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `product_group_id` bigint NOT NULL,
    `content` text COLLATE utf8mb4_unicode_ci,
    `cdn_path` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_product_group_descriptions_group_id` (`product_group_id`),
    KEY `idx_product_group_descriptions_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 14. products (상품/SKU)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `products` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `product_group_id` bigint NOT NULL,
    `sku_code` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `regular_price` int NOT NULL DEFAULT '0',
    `current_price` int NOT NULL DEFAULT '0',
    `sale_price` int DEFAULT NULL,
    `discount_rate` int NOT NULL DEFAULT '0',
    `stock_quantity` int NOT NULL DEFAULT '0',
    `status` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `sort_order` int NOT NULL DEFAULT '0',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_products_group_id` (`product_group_id`),
    KEY `idx_products_status` (`status`),
    KEY `idx_products_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 15. seller_option_groups (셀러 옵션 그룹)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `seller_option_groups` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `product_group_id` bigint NOT NULL,
    `option_group_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `sort_order` int NOT NULL DEFAULT '0',
    `deleted` tinyint(1) NOT NULL DEFAULT '0',
    `deleted_at` datetime(6) DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_seller_option_groups_group_id` (`product_group_id`),
    KEY `idx_seller_option_groups_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 16. seller_option_values (셀러 옵션 값)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `seller_option_values` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `seller_option_group_id` bigint NOT NULL,
    `option_value_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `sort_order` int NOT NULL DEFAULT '0',
    `deleted` tinyint(1) NOT NULL DEFAULT '0',
    `deleted_at` datetime(6) DEFAULT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_seller_option_values_group_id` (`seller_option_group_id`),
    KEY `idx_seller_option_values_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 17. product_option_mappings (상품-옵션 매핑)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_option_mappings` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `product_id` bigint NOT NULL,
    `seller_option_value_id` bigint NOT NULL,
    `deleted` tinyint(1) NOT NULL DEFAULT '0',
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_product_option_mappings_product_id` (`product_id`),
    KEY `idx_product_option_mappings_option_value_id` (`seller_option_value_id`),
    KEY `idx_product_option_mappings_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 18. product_notices (상품 고시)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_notices` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `product_group_id` bigint NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_product_notices_group_id` (`product_group_id`),
    KEY `idx_product_notices_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 19. product_notice_entries (상품 고시 항목)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `product_notice_entries` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `product_notice_id` bigint NOT NULL,
    `field_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `field_value` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `sort_order` int NOT NULL DEFAULT '0',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_product_notice_entries_notice_id` (`product_notice_id`),
    KEY `idx_product_notice_entries_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 20. discount_policy (할인 정책)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `discount_policy` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '정책명',
    `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '정책 설명',
    `discount_method` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '할인 방식 (RATE, FIXED_AMOUNT)',
    `discount_rate` double DEFAULT NULL COMMENT '할인율 (0~100, RATE 타입 시 필수)',
    `discount_amount` int DEFAULT NULL COMMENT '정액 할인금 (FIXED_AMOUNT 타입 시 필수)',
    `max_discount_amount` int DEFAULT NULL COMMENT '최대 할인 한도액',
    `discount_capped` tinyint(1) NOT NULL DEFAULT '0' COMMENT '한도 적용 여부',
    `minimum_order_amount` int DEFAULT NULL COMMENT '최소 주문금액',
    `application_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '적용 방식 (IMMEDIATE, COUPON)',
    `publisher_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '생성 주체 (ADMIN, BRAND, SELLER)',
    `seller_id` bigint DEFAULT NULL COMMENT '셀러 ID (셀러 할인 시)',
    `stacking_group` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '스태킹 그룹 (SELLER_INSTANT, PLATFORM_INSTANT, COUPON)',
    `priority` int NOT NULL DEFAULT '0' COMMENT '우선순위',
    `start_at` datetime(6) NOT NULL COMMENT '유효 시작 시점',
    `end_at` datetime(6) NOT NULL COMMENT '유효 종료 시점',
    `total_budget` int NOT NULL DEFAULT '0' COMMENT '총 예산',
    `used_budget` int NOT NULL DEFAULT '0' COMMENT '사용된 예산',
    `active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '활성 여부',
    `deleted_at` datetime(6) DEFAULT NULL COMMENT 'Soft Delete 시각',
    `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY `idx_discount_policy_active_period` (`active`, `start_at`, `end_at`),
    KEY `idx_discount_policy_application_type` (`application_type`, `active`),
    KEY `idx_discount_policy_seller` (`seller_id`, `active`),
    KEY `idx_discount_policy_stacking_group` (`stacking_group`, `active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='할인 정책';

-- ------------------------------------------------------------
-- 21. discount_target (할인 적용 대상)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `discount_target` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `discount_policy_id` bigint NOT NULL COMMENT '할인 정책 ID',
    `target_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '대상 유형 (BRAND, SELLER, CATEGORY, PRODUCT)',
    `target_id` bigint NOT NULL COMMENT '대상 ID',
    `active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '활성 여부',
    `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY `idx_discount_target_policy` (`discount_policy_id`, `active`),
    KEY `idx_discount_target_type_id` (`target_type`, `target_id`, `active`),
    KEY `idx_discount_target_lookup` (`target_type`, `target_id`, `discount_policy_id`, `active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='할인 적용 대상';

-- ------------------------------------------------------------
-- 22. faq (FAQ)
-- ------------------------------------------------------------
CREATE TABLE `faq` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `faq_type` varchar(30) NOT NULL COMMENT 'FAQ 유형 (MEMBER_LOGIN, PRODUCT_SELLER, SHIPPING, ORDER_PAYMENT, CANCEL_REFUND, EXCHANGE_RETURN, TOP)',
    `title` varchar(100) NOT NULL COMMENT 'FAQ 제목',
    `contents` varchar(2000) NOT NULL COMMENT 'FAQ 내용',
    `display_order` int NOT NULL DEFAULT '0' COMMENT '표시 순서',
    `top_display_order` int DEFAULT NULL COMMENT '상단 고정 순서 (null이면 일반 FAQ)',
    `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_faq_type` (`faq_type`, `deleted_at`),
    KEY `idx_faq_deleted` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='FAQ';
