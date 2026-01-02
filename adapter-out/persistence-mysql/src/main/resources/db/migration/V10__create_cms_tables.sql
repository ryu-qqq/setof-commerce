-- =============================================================================
-- V10__create_cms_tables.sql
-- CMS 관련 테이블 생성 (Banner, GNB, Content, Component)
-- =============================================================================
-- CMS: Content Management System 관련 테이블
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. cms_banners 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cms_banners (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    banner_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    display_start_date TIMESTAMP(6) NOT NULL,
    display_end_date TIMESTAMP(6) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_cms_banners_status (status),
    INDEX idx_cms_banners_banner_type (banner_type),
    INDEX idx_cms_banners_display_dates (display_start_date, display_end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 2. cms_gnbs 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cms_gnbs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    link_url VARCHAR(500),
    display_order INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    display_start_date TIMESTAMP(6),
    display_end_date TIMESTAMP(6),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_cms_gnbs_status (status),
    INDEX idx_cms_gnbs_display_order (display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 3. cms_contents 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cms_contents (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    memo VARCHAR(500),
    image_url VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    display_start_date TIMESTAMP(6) NOT NULL,
    display_end_date TIMESTAMP(6) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_cms_contents_status (status),
    INDEX idx_cms_contents_display_dates (display_start_date, display_end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 4. cms_components 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cms_components (
    id BIGINT NOT NULL AUTO_INCREMENT,
    content_id BIGINT NOT NULL,
    component_type VARCHAR(30) NOT NULL,
    component_name VARCHAR(100),
    display_order INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    exposed_products INT NOT NULL DEFAULT 0,
    display_start_date TIMESTAMP(6),
    display_end_date TIMESTAMP(6),
    component_detail TEXT,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_cms_components_content_id (content_id),
    INDEX idx_cms_components_status (status),
    INDEX idx_cms_components_display_order (display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
