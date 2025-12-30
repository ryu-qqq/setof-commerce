-- =============================================================================
-- V9__create_checkout_order_payment_tables.sql
-- Checkout, Order, Payment 관련 테이블 생성
-- =============================================================================
-- 커머스 핵심 테이블: 체크아웃, 주문, 결제
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. checkouts 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS checkouts (
    id BINARY(16) NOT NULL,
    member_id VARCHAR(36) NOT NULL,
    status VARCHAR(30) NOT NULL,
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    address_detail VARCHAR(100),
    zip_code VARCHAR(10) NOT NULL,
    memo VARCHAR(500),
    total_amount DECIMAL(15, 2) NOT NULL,
    discount_amount DECIMAL(15, 2) NOT NULL,
    final_amount DECIMAL(15, 2) NOT NULL,
    expired_at TIMESTAMP(6),
    completed_at TIMESTAMP(6),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_checkouts_member_id (member_id),
    INDEX idx_checkouts_status (status),
    INDEX idx_checkouts_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 2. checkout_items 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS checkout_items (
    id BINARY(16) NOT NULL,
    checkout_id BINARY(16) NOT NULL,
    product_stock_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    total_price DECIMAL(15, 2) NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    product_image VARCHAR(500),
    option_name VARCHAR(200),
    brand_name VARCHAR(100),
    seller_name VARCHAR(100),
    PRIMARY KEY (id),
    INDEX idx_checkout_items_checkout_id (checkout_id),
    INDEX idx_checkout_items_product_id (product_id),
    INDEX idx_checkout_items_seller_id (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 3. payments 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS payments (
    id BINARY(16) NOT NULL,
    checkout_id BINARY(16) NOT NULL,
    pg_provider VARCHAR(30) NOT NULL,
    pg_transaction_id VARCHAR(100),
    method VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    requested_amount DECIMAL(15, 2) NOT NULL,
    approved_amount DECIMAL(15, 2) NOT NULL,
    refunded_amount DECIMAL(15, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    approved_at TIMESTAMP(6),
    cancelled_at TIMESTAMP(6),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_payments_checkout_id (checkout_id),
    INDEX idx_payments_status (status),
    INDEX idx_payments_pg_transaction_id (pg_transaction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 4. orders 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
    id BINARY(16) NOT NULL,
    order_number VARCHAR(30) NOT NULL,
    checkout_id BINARY(16) NOT NULL,
    payment_id BINARY(16) NOT NULL,
    seller_id BIGINT NOT NULL,
    member_id VARCHAR(36) NOT NULL,
    status VARCHAR(30) NOT NULL,
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    address_detail VARCHAR(100),
    zip_code VARCHAR(10) NOT NULL,
    memo VARCHAR(500),
    total_item_amount DECIMAL(15, 2) NOT NULL,
    shipping_fee DECIMAL(15, 2) NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL,
    ordered_at TIMESTAMP(6) NOT NULL,
    confirmed_at TIMESTAMP(6),
    shipped_at TIMESTAMP(6),
    delivered_at TIMESTAMP(6),
    completed_at TIMESTAMP(6),
    cancelled_at TIMESTAMP(6),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_orders_order_number (order_number),
    INDEX idx_orders_checkout_id (checkout_id),
    INDEX idx_orders_payment_id (payment_id),
    INDEX idx_orders_seller_id (seller_id),
    INDEX idx_orders_member_id (member_id),
    INDEX idx_orders_status (status),
    INDEX idx_orders_ordered_at (ordered_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 5. order_items 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS order_items (
    id BINARY(16) NOT NULL,
    order_id BINARY(16) NOT NULL,
    product_id BIGINT NOT NULL,
    product_stock_id BIGINT NOT NULL,
    ordered_quantity INT NOT NULL,
    cancelled_quantity INT NOT NULL DEFAULT 0,
    refunded_quantity INT NOT NULL DEFAULT 0,
    unit_price DECIMAL(15, 2) NOT NULL,
    total_price DECIMAL(15, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    product_image VARCHAR(500),
    option_name VARCHAR(200),
    brand_name VARCHAR(100),
    seller_name VARCHAR(100),
    original_price DECIMAL(15, 2) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_order_items_order_id (order_id),
    INDEX idx_order_items_product_id (product_id),
    INDEX idx_order_items_product_stock_id (product_stock_id),
    INDEX idx_order_items_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
