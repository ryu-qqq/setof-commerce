-- =============================================================================
-- Schema for REST API Admin Integration Tests
-- TestContainers MySQL에서 사용되는 테이블 스키마
-- =============================================================================

-- -----------------------------------------------------------------------------
-- Seller 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS sellers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    seller_name VARCHAR(100) NOT NULL,
    logo_url VARCHAR(500) NULL,
    description TEXT NULL,
    approval_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_seller_approval_status (approval_status),
    INDEX idx_seller_name (seller_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Seller Business Info 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS seller_business_infos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    seller_id BIGINT NOT NULL,
    registration_number VARCHAR(10) NOT NULL,
    sale_report_number VARCHAR(100) NULL,
    representative VARCHAR(50) NOT NULL,
    address_line1 VARCHAR(200) NOT NULL,
    address_line2 VARCHAR(100) NULL,
    zip_code VARCHAR(10) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_seller_business_seller UNIQUE (seller_id),
    INDEX idx_seller_business_registration (registration_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Seller CS Info 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS seller_cs_infos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    seller_id BIGINT NOT NULL,
    email VARCHAR(100) NULL,
    mobile_phone VARCHAR(11) NULL,
    landline_phone VARCHAR(11) NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_seller_cs_seller UNIQUE (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Shipping Policy 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS shipping_policies (
    id BIGINT NOT NULL AUTO_INCREMENT,
    seller_id BIGINT NOT NULL,
    policy_name VARCHAR(100) NOT NULL,
    shipping_fee INT NOT NULL DEFAULT 0,
    free_shipping_threshold INT NULL,
    is_default TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    PRIMARY KEY (id),
    INDEX idx_shipping_policy_seller (seller_id),
    INDEX idx_shipping_policy_default (seller_id, is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Refund Policy 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS refund_policies (
    id BIGINT NOT NULL AUTO_INCREMENT,
    seller_id BIGINT NOT NULL,
    policy_name VARCHAR(100) NOT NULL,
    refund_shipping_fee INT NOT NULL DEFAULT 0,
    exchange_shipping_fee INT NOT NULL DEFAULT 0,
    is_default TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    PRIMARY KEY (id),
    INDEX idx_refund_policy_seller (seller_id),
    INDEX idx_refund_policy_default (seller_id, is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
