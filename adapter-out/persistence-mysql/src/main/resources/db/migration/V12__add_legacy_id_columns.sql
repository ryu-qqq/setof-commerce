-- =============================================================================
-- V12: Add legacy_id columns for data migration
-- Purpose: Support Strangler Fig migration from legacy DB (Long PK → UUID PK)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. members 테이블에 legacy_user_id 컬럼 추가
-- -----------------------------------------------------------------------------
ALTER TABLE members
    ADD COLUMN legacy_user_id BIGINT NULL COMMENT '레거시 Users.USER_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_members_legacy_user_id
    ON members (legacy_user_id)
    WHERE legacy_user_id IS NOT NULL;

-- -----------------------------------------------------------------------------
-- 2. orders 테이블에 legacy_order_id 컬럼 추가 (향후 Order 마이그레이션용)
-- -----------------------------------------------------------------------------
ALTER TABLE orders
    ADD COLUMN legacy_order_id BIGINT NULL COMMENT '레거시 ORDER.ORDER_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_orders_legacy_order_id
    ON orders (legacy_order_id)
    WHERE legacy_order_id IS NOT NULL;

-- -----------------------------------------------------------------------------
-- 3. payments 테이블에 legacy_payment_id 컬럼 추가 (향후 Payment 마이그레이션용)
-- -----------------------------------------------------------------------------
ALTER TABLE payments
    ADD COLUMN legacy_payment_id BIGINT NULL COMMENT '레거시 PAYMENT.PAYMENT_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_payments_legacy_payment_id
    ON payments (legacy_payment_id)
    WHERE legacy_payment_id IS NOT NULL;
