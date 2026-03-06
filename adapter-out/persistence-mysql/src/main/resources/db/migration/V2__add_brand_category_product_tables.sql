-- ============================================================
-- V2: Add brand, category, product, and related tables
-- ============================================================

-- ------------------------------------------------------------
-- 3. brand
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS brand (
    id                   BIGINT          NOT NULL AUTO_INCREMENT,
    brand_name           VARCHAR(100)    NOT NULL,
    brand_icon_image_url VARCHAR(500)    NOT NULL,
    display_name         VARCHAR(100)    NOT NULL,
    display_order        INT             NOT NULL DEFAULT 0,
    displayed            TINYINT(1)      NOT NULL DEFAULT 1,
    created_at           DATETIME(6)     NOT NULL,
    updated_at           DATETIME(6)     NOT NULL,
    deleted_at           DATETIME(6)     NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_brand_brand_name (brand_name),
    KEY idx_brand_displayed (displayed),
    KEY idx_brand_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 4. category
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS category (
    id                   BIGINT          NOT NULL AUTO_INCREMENT,
    category_name        VARCHAR(100)    NOT NULL,
    category_depth       INT             NOT NULL,
    parent_category_id   BIGINT          NOT NULL DEFAULT 0,
    display_name         VARCHAR(100)    NOT NULL,
    displayed            TINYINT(1)      NOT NULL DEFAULT 1,
    target_group         VARCHAR(20)     NOT NULL,
    category_type        VARCHAR(20)     NOT NULL,
    path                 VARCHAR(500)    NOT NULL,
    created_at           DATETIME(6)     NOT NULL,
    updated_at           DATETIME(6)     NOT NULL,
    deleted_at           DATETIME(6)     NULL,
    PRIMARY KEY (id),
    KEY idx_category_depth (category_depth),
    KEY idx_category_parent_id (parent_category_id),
    KEY idx_category_displayed (displayed),
    KEY idx_category_target_group (target_group),
    KEY idx_category_path (path),
    KEY idx_category_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 9. shipping_policies
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS shipping_policies (
    id                    BIGINT          NOT NULL AUTO_INCREMENT,
    seller_id             BIGINT          NOT NULL,
    policy_name           VARCHAR(100)    NOT NULL,
    is_default_policy     TINYINT(1)      NOT NULL DEFAULT 0,
    is_active             TINYINT(1)      NOT NULL DEFAULT 1,
    shipping_fee_type     VARCHAR(30)     NOT NULL,
    base_fee              INT             NULL,
    free_threshold        INT             NULL,
    jeju_extra_fee        INT             NULL,
    island_extra_fee      INT             NULL,
    return_fee            INT             NULL,
    exchange_fee          INT             NULL,
    lead_time_min_days    INT             NULL,
    lead_time_max_days    INT             NULL,
    lead_time_cutoff_time TIME            NULL,
    created_at            DATETIME(6)     NOT NULL,
    updated_at            DATETIME(6)     NOT NULL,
    deleted_at            DATETIME(6)     NULL,
    PRIMARY KEY (id),
    KEY idx_shipping_policies_seller_id (seller_id),
    KEY idx_shipping_policies_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 10. refund_policies
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS refund_policies (
    id                        BIGINT          NOT NULL AUTO_INCREMENT,
    seller_id                 BIGINT          NOT NULL,
    policy_name               VARCHAR(100)    NOT NULL,
    is_default_policy         TINYINT(1)      NOT NULL DEFAULT 0,
    is_active                 TINYINT(1)      NOT NULL DEFAULT 1,
    return_period_days        INT             NOT NULL DEFAULT 0,
    exchange_period_days      INT             NOT NULL DEFAULT 0,
    non_returnable_conditions VARCHAR(500)    NULL,
    is_partial_refund_enabled TINYINT(1)      NOT NULL DEFAULT 0,
    is_inspection_required    TINYINT(1)      NOT NULL DEFAULT 0,
    inspection_period_days    INT             NOT NULL DEFAULT 0,
    additional_info           VARCHAR(2000)   NULL,
    created_at                DATETIME(6)     NOT NULL,
    updated_at                DATETIME(6)     NOT NULL,
    deleted_at                DATETIME(6)     NULL,
    PRIMARY KEY (id),
    KEY idx_refund_policies_seller_id (seller_id),
    KEY idx_refund_policies_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 11. product_groups
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_groups (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    seller_id           BIGINT          NOT NULL,
    brand_id            BIGINT          NOT NULL,
    category_id         BIGINT          NOT NULL,
    shipping_policy_id  BIGINT          NOT NULL,
    refund_policy_id    BIGINT          NOT NULL,
    product_group_name  VARCHAR(500)    NOT NULL,
    option_type         VARCHAR(50)     NOT NULL,
    regular_price       INT             NOT NULL DEFAULT 0,
    current_price       INT             NOT NULL DEFAULT 0,
    sale_price          INT             NOT NULL DEFAULT 0,
    status              VARCHAR(50)     NOT NULL,
    created_at          DATETIME(6)     NOT NULL,
    updated_at          DATETIME(6)     NOT NULL,
    deleted_at          DATETIME(6)     NULL,
    PRIMARY KEY (id),
    KEY idx_product_groups_seller_id (seller_id),
    KEY idx_product_groups_brand_id (brand_id),
    KEY idx_product_groups_category_id (category_id),
    KEY idx_product_groups_status (status),
    KEY idx_product_groups_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 12. product_group_images
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_group_images (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    product_group_id BIGINT          NOT NULL,
    image_type       VARCHAR(50)     NOT NULL,
    image_url        VARCHAR(1000)   NOT NULL,
    sort_order       INT             NOT NULL DEFAULT 0,
    created_at       DATETIME(6)     NOT NULL,
    updated_at       DATETIME(6)     NOT NULL,
    deleted_at       DATETIME(6)     NULL,
    PRIMARY KEY (id),
    KEY idx_product_group_images_group_id (product_group_id),
    KEY idx_product_group_images_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 13. product_group_descriptions
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_group_descriptions (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    product_group_id BIGINT          NOT NULL,
    content          TEXT            NULL,
    cdn_path         VARCHAR(1000)   NULL,
    created_at       DATETIME(6)     NOT NULL,
    updated_at       DATETIME(6)     NOT NULL,
    deleted_at       DATETIME(6)     NULL,
    PRIMARY KEY (id),
    KEY idx_product_group_descriptions_group_id (product_group_id),
    KEY idx_product_group_descriptions_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 14. product_notices
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_notices (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    product_group_id BIGINT          NOT NULL,
    created_at       DATETIME(6)     NOT NULL,
    updated_at       DATETIME(6)     NOT NULL,
    deleted_at       DATETIME(6)     NULL,
    PRIMARY KEY (id),
    KEY idx_product_notices_group_id (product_group_id),
    KEY idx_product_notices_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 15. product_notice_entries
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_notice_entries (
    id                BIGINT          NOT NULL AUTO_INCREMENT,
    product_notice_id BIGINT          NOT NULL,
    field_name        VARCHAR(100)    NOT NULL,
    field_value       VARCHAR(500)    NOT NULL,
    sort_order        INT             NOT NULL DEFAULT 0,
    created_at        DATETIME(6)     NOT NULL,
    updated_at        DATETIME(6)     NOT NULL,
    deleted_at        DATETIME(6)     NULL,
    PRIMARY KEY (id),
    KEY idx_product_notice_entries_notice_id (product_notice_id),
    KEY idx_product_notice_entries_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 16. seller_option_groups
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS seller_option_groups (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    product_group_id BIGINT          NOT NULL,
    option_group_name VARCHAR(100)   NOT NULL,
    sort_order       INT             NOT NULL DEFAULT 0,
    deleted          TINYINT(1)      NOT NULL DEFAULT 0,
    deleted_at       DATETIME(6)     NULL,
    created_at       DATETIME(6)     NOT NULL,
    updated_at       DATETIME(6)     NOT NULL,
    PRIMARY KEY (id),
    KEY idx_seller_option_groups_group_id (product_group_id),
    KEY idx_seller_option_groups_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 17. seller_option_values
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS seller_option_values (
    id                     BIGINT          NOT NULL AUTO_INCREMENT,
    seller_option_group_id BIGINT          NOT NULL,
    option_value_name      VARCHAR(100)    NOT NULL,
    sort_order             INT             NOT NULL DEFAULT 0,
    deleted                TINYINT(1)      NOT NULL DEFAULT 0,
    deleted_at             DATETIME(6)     NULL,
    created_at             DATETIME(6)     NOT NULL,
    updated_at             DATETIME(6)     NOT NULL,
    PRIMARY KEY (id),
    KEY idx_seller_option_values_group_id (seller_option_group_id),
    KEY idx_seller_option_values_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 18. products
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS products (
    id               BIGINT          NOT NULL AUTO_INCREMENT,
    product_group_id BIGINT          NOT NULL,
    sku_code         VARCHAR(100)    NULL,
    regular_price    INT             NOT NULL DEFAULT 0,
    current_price    INT             NOT NULL DEFAULT 0,
    sale_price       INT             NULL,
    discount_rate    INT             NOT NULL DEFAULT 0,
    stock_quantity   INT             NOT NULL DEFAULT 0,
    status           VARCHAR(50)     NOT NULL,
    sort_order       INT             NOT NULL DEFAULT 0,
    created_at       DATETIME(6)     NOT NULL,
    updated_at       DATETIME(6)     NOT NULL,
    deleted_at       DATETIME(6)     NULL,
    PRIMARY KEY (id),
    KEY idx_products_group_id (product_group_id),
    KEY idx_products_status (status),
    KEY idx_products_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- 19. product_option_mappings
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS product_option_mappings (
    id                     BIGINT          NOT NULL AUTO_INCREMENT,
    product_id             BIGINT          NOT NULL,
    seller_option_value_id BIGINT          NOT NULL,
    deleted                TINYINT(1)      NOT NULL DEFAULT 0,
    deleted_at             DATETIME(6)     NULL,
    created_at             DATETIME(6)     NOT NULL,
    updated_at             DATETIME(6)     NOT NULL,
    PRIMARY KEY (id),
    KEY idx_product_option_mappings_product_id (product_id),
    KEY idx_product_option_mappings_option_value_id (seller_option_value_id),
    KEY idx_product_option_mappings_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
