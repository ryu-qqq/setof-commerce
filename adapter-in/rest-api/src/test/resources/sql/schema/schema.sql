-- =============================================================================
-- Schema for REST API Integration Tests
-- TestContainers MySQL에서 사용되는 테이블 스키마
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Brand 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS brand (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL,
    name_ko VARCHAR(255) NOT NULL,
    name_en VARCHAR(255) NULL,
    logo_url VARCHAR(500) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_brand_code UNIQUE (code),
    INDEX idx_brand_status (status),
    INDEX idx_brand_name_ko (name_ko)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Category 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL,
    name_ko VARCHAR(255) NOT NULL,
    parent_id BIGINT NULL,
    depth TINYINT UNSIGNED NOT NULL DEFAULT 0,
    path VARCHAR(500) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    is_leaf TINYINT(1) NOT NULL DEFAULT 1,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_category_code UNIQUE (code),
    INDEX idx_category_parent (parent_id),
    INDEX idx_category_path (path),
    INDEX idx_category_status (status),
    INDEX idx_category_depth (depth),
    INDEX idx_category_parent_sort (parent_id, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Bank 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS banks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    bank_code VARCHAR(10) NOT NULL,
    bank_name VARCHAR(50) NOT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    display_order INT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_bank_code UNIQUE (bank_code),
    INDEX idx_bank_active_order (is_active, display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- ShippingAddress 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS shipping_addresses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id VARCHAR(36) NOT NULL,
    address_name VARCHAR(50) NOT NULL,
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(11) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    road_address VARCHAR(200) NULL,
    jibun_address VARCHAR(200) NULL,
    detail_address VARCHAR(100) NULL,
    delivery_request VARCHAR(200) NULL,
    is_default TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    PRIMARY KEY (id),
    INDEX idx_shipping_address_member (member_id),
    INDEX idx_shipping_address_member_default (member_id, is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- RefundAccount 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS refund_accounts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id VARCHAR(36) NOT NULL,
    bank_id BIGINT NOT NULL,
    account_number VARCHAR(20) NOT NULL,
    account_holder_name VARCHAR(50) NOT NULL,
    is_verified TINYINT(1) NOT NULL DEFAULT 0,
    verified_at DATETIME(6) NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_refund_account_member UNIQUE (member_id),
    INDEX idx_refund_account_bank (bank_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
