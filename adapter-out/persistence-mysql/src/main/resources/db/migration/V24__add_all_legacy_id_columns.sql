-- =============================================================================
-- V24: Add legacy_id columns for all migration domains
-- Purpose: Support Strangler Fig migration from legacy DB
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. members 테이블에 legacy_user_id 컬럼 추가
-- Note: members.id는 VARCHAR(36) UUID, legacy user_id는 BIGINT
-- -----------------------------------------------------------------------------
ALTER TABLE members
    ADD COLUMN legacy_user_id BIGINT NULL COMMENT '레거시 USERS.USER_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_members_legacy_user_id
    ON members (legacy_user_id);

-- -----------------------------------------------------------------------------
-- 2. shipping_addresses 테이블에 legacy_shipping_address_id 컬럼 추가
-- -----------------------------------------------------------------------------
ALTER TABLE shipping_addresses
    ADD COLUMN legacy_shipping_address_id BIGINT NULL COMMENT '레거시 SHIPPING_ADDRESS.SHIPPING_ADDRESS_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_shipping_addresses_legacy_id
    ON shipping_addresses (legacy_shipping_address_id);

-- -----------------------------------------------------------------------------
-- 3. refund_accounts 테이블에 legacy_refund_account_id 컬럼 추가
-- -----------------------------------------------------------------------------
ALTER TABLE refund_accounts
    ADD COLUMN legacy_refund_account_id BIGINT NULL COMMENT '레거시 REFUND_ACCOUNT.REFUND_ACCOUNT_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_refund_accounts_legacy_id
    ON refund_accounts (legacy_refund_account_id);

-- -----------------------------------------------------------------------------
-- 4. product_groups: V14__add_product_legacy_id_columns.sql에서 처리됨
-- 5. products: V14__add_product_legacy_id_columns.sql에서 처리됨
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- 6. orders 테이블에 legacy_order_id 컬럼 추가
-- Note: orders.id는 BINARY(16) UUID, legacy order_id는 BIGINT
-- -----------------------------------------------------------------------------
ALTER TABLE orders
    ADD COLUMN legacy_order_id BIGINT NULL COMMENT '레거시 ORDERS.ORDER_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_orders_legacy_id
    ON orders (legacy_order_id);

-- -----------------------------------------------------------------------------
-- 7. payments 테이블에 legacy_payment_id 컬럼 추가
-- Note: payments.id는 BINARY(16) UUID, legacy payment_id는 BIGINT
-- -----------------------------------------------------------------------------
ALTER TABLE payments
    ADD COLUMN legacy_payment_id BIGINT NULL COMMENT '레거시 PAYMENT.PAYMENT_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_payments_legacy_id
    ON payments (legacy_payment_id);

-- -----------------------------------------------------------------------------
-- 8. reviews 테이블에 legacy_review_id 컬럼 추가
-- Prerequisite: V20__create_review_tables.sql 실행 후
-- -----------------------------------------------------------------------------
ALTER TABLE reviews
    ADD COLUMN legacy_review_id BIGINT NULL COMMENT '레거시 REVIEW.REVIEW_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_reviews_legacy_id
    ON reviews (legacy_review_id);

-- -----------------------------------------------------------------------------
-- 9. qnas 테이블에 legacy_qna_id 컬럼 추가
-- Prerequisite: V21__create_qna_tables.sql 실행 후
-- -----------------------------------------------------------------------------
ALTER TABLE qnas
    ADD COLUMN legacy_qna_id BIGINT NULL COMMENT '레거시 QNA.QNA_ID (마이그레이션용)';

CREATE UNIQUE INDEX idx_qnas_legacy_id
    ON qnas (legacy_qna_id);
