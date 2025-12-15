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

-- -----------------------------------------------------------------------------
-- ProductGroup 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_groups (
    id BIGINT NOT NULL AUTO_INCREMENT,
    seller_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    option_type VARCHAR(20) NOT NULL,
    regular_price DECIMAL(15, 2) NOT NULL,
    current_price DECIMAL(15, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    shipping_policy_id BIGINT NULL,
    refund_policy_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    PRIMARY KEY (id),
    INDEX idx_product_group_seller (seller_id),
    INDEX idx_product_group_category (category_id),
    INDEX idx_product_group_brand (brand_id),
    INDEX idx_product_group_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- Product 테이블 (SKU)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_group_id BIGINT NOT NULL,
    option_type VARCHAR(20) NOT NULL,
    option1_name VARCHAR(50) NULL,
    option1_value VARCHAR(100) NULL,
    option2_name VARCHAR(50) NULL,
    option2_value VARCHAR(100) NULL,
    additional_price DECIMAL(15, 2) NULL DEFAULT 0.00,
    sold_out TINYINT(1) NOT NULL DEFAULT 0,
    display_yn TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    PRIMARY KEY (id),
    INDEX idx_product_group (product_group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- ProductImage 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_group_id BIGINT NOT NULL,
    image_type VARCHAR(20) NOT NULL,
    origin_url VARCHAR(500) NOT NULL,
    cdn_url VARCHAR(500) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_product_image_group (product_group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- ProductDescription 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_descriptions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_group_id BIGINT NOT NULL,
    html_content LONGTEXT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_product_description_group UNIQUE (product_group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- ProductDescriptionImage 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_description_images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_description_id BIGINT NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    origin_url VARCHAR(1000) NOT NULL,
    cdn_url VARCHAR(1000) NOT NULL,
    uploaded_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_description_image_desc (product_description_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- ProductNotice 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_notices (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_group_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_product_notice_group UNIQUE (product_group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- ProductNoticeItem 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_notice_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_notice_id BIGINT NOT NULL,
    field_key VARCHAR(50) NOT NULL,
    field_value TEXT NULL,
    display_order INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    INDEX idx_notice_item_notice (product_notice_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- ProductStock 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_stocks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    version BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_product_stock_product UNIQUE (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
