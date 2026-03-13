-- ============================================================
-- V2: Banner, Navigation, Content 도메인 DDL
-- ============================================================
-- 생성일: 2026-03-13
-- ============================================================

-- ------------------------------------------------------------
-- 1. banner_group (배너 그룹)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `banner_group` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `banner_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `display_start_at` datetime(6) NOT NULL,
    `display_end_at` datetime(6) NOT NULL,
    `active` tinyint(1) NOT NULL DEFAULT '1',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_banner_group_banner_type` (`banner_type`),
    KEY `idx_banner_group_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 2. banner_slide (배너 슬라이드)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `banner_slide` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `banner_group_id` bigint NOT NULL,
    `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `image_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `link_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `display_order` int NOT NULL DEFAULT '0',
    `display_start_at` datetime(6) NOT NULL,
    `display_end_at` datetime(6) NOT NULL,
    `active` tinyint(1) NOT NULL DEFAULT '1',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_banner_slide_banner_group_id` (`banner_group_id`),
    KEY `idx_banner_slide_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 3. navigation_menu (네비게이션 메뉴)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `navigation_menu` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `link_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `display_order` int NOT NULL DEFAULT '0',
    `display_start_at` datetime(6) NOT NULL,
    `display_end_at` datetime(6) NOT NULL,
    `active` tinyint(1) NOT NULL DEFAULT '1',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_navigation_menu_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 4. content_page (콘텐츠 페이지)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `content_page` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `memo` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `image_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `display_start_at` datetime(6) NOT NULL,
    `display_end_at` datetime(6) NOT NULL,
    `active` tinyint(1) NOT NULL DEFAULT '1',
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_content_page_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 5. display_component (전시 컴포넌트)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `display_component` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `content_page_id` bigint NOT NULL,
    `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
    `component_type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
    `display_order` int NOT NULL DEFAULT '0',
    `list_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
    `order_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
    `badge_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
    `filter_enabled` tinyint(1) NOT NULL DEFAULT '0',
    `exposed_products` int NOT NULL DEFAULT '0',
    `display_start_at` datetime(6) NOT NULL,
    `display_end_at` datetime(6) NOT NULL,
    `active` tinyint(1) NOT NULL DEFAULT '1',
    `view_extension_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `view_extension_link_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `view_extension_button_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `view_extension_product_count_per_click` int DEFAULT NULL,
    `view_extension_max_click_count` int DEFAULT NULL,
    `view_extension_after_max_action_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `view_extension_after_max_action_link_url` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `spec_data` json NOT NULL,
    `created_at` datetime(6) NOT NULL,
    `updated_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_display_component_content_page_id` (`content_page_id`),
    KEY `idx_display_component_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 6. display_tab (전시 탭)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `display_tab` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `component_id` bigint NOT NULL,
    `tab_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    `display_order` int NOT NULL DEFAULT '0',
    `created_at` datetime(6) NOT NULL,
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_display_tab_component_id` (`component_id`),
    KEY `idx_display_tab_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 7. component_fixed_product (컴포넌트 고정 상품)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `component_fixed_product` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `component_id` bigint NOT NULL,
    `tab_id` bigint DEFAULT NULL,
    `product_group_id` bigint NOT NULL,
    `display_order` int NOT NULL DEFAULT '0',
    `deleted_at` datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_component_fixed_product_component_id` (`component_id`),
    KEY `idx_component_fixed_product_tab_id` (`tab_id`),
    KEY `idx_component_fixed_product_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
