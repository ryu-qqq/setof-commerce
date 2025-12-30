-- =============================================================================
-- V14: Add legacy_id columns for product migration
-- Purpose: Support Strangler Fig migration from legacy DB (Long PK → Long PK)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. product_groups 테이블에 legacy_product_group_id 컬럼 추가
-- -----------------------------------------------------------------------------
ALTER TABLE product_groups
    ADD COLUMN legacy_product_group_id BIGINT NULL COMMENT '레거시 PRODUCT_GROUP.PRODUCT_GROUP_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_product_groups_legacy_id
    ON product_groups (legacy_product_group_id);

-- -----------------------------------------------------------------------------
-- 2. products 테이블에 legacy_product_id 컬럼 추가
-- -----------------------------------------------------------------------------
ALTER TABLE products
    ADD COLUMN legacy_product_id BIGINT NULL COMMENT '레거시 PRODUCT.PRODUCT_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_products_legacy_id
    ON products (legacy_product_id);
