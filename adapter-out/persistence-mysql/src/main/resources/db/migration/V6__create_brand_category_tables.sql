-- =====================================================
-- V6: Brand 및 Category 테이블 생성
-- =====================================================

-- =====================================================
-- 1. Brand 테이블
-- =====================================================

CREATE TABLE brands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(100) NOT NULL COMMENT '브랜드 영문명',
    brand_icon_image_url VARCHAR(500) NOT NULL COMMENT '브랜드 아이콘 이미지 URL',
    display_name VARCHAR(100) NOT NULL COMMENT '화면 표시명',
    display_order INT NOT NULL DEFAULT 0 COMMENT '정렬 순서',
    displayed BOOLEAN NOT NULL DEFAULT TRUE COMMENT '노출 여부',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    UNIQUE KEY uk_brands_brand_name (brand_name),
    INDEX idx_brands_displayed_order (displayed, display_order),
    INDEX idx_brands_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. Category 테이블
-- =====================================================

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL COMMENT '카테고리명',
    category_depth INT NOT NULL COMMENT '카테고리 깊이 (0: 최상위)',
    parent_category_id BIGINT NOT NULL DEFAULT 0 COMMENT '부모 카테고리 ID (0: 최상위)',
    display_name VARCHAR(100) NOT NULL COMMENT '화면 표시명',
    displayed BOOLEAN NOT NULL DEFAULT TRUE COMMENT '노출 여부',
    target_group VARCHAR(20) NOT NULL COMMENT '대상 그룹 (MEN, WOMEN, UNISEX, KIDS)',
    category_type VARCHAR(20) NOT NULL COMMENT '카테고리 유형 (NORMAL, BRAND, SPECIAL)',
    path VARCHAR(500) NOT NULL COMMENT '카테고리 경로 (예: /1/2/3/)',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    INDEX idx_categories_parent_id (parent_category_id),
    INDEX idx_categories_depth (category_depth),
    INDEX idx_categories_target_type (target_group, category_type),
    INDEX idx_categories_displayed (displayed),
    INDEX idx_categories_path (path(255)),
    INDEX idx_categories_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
