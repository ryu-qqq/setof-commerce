-- Cart V2 테이블 생성
-- 장바구니 및 장바구니 아이템 테이블

-- carts 테이블
CREATE TABLE IF NOT EXISTS carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    UNIQUE KEY uk_carts_member_id (member_id),
    INDEX idx_carts_member_id (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- cart_items 테이블
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_stock_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_group_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    selected BOOLEAN NOT NULL DEFAULT TRUE,
    added_at DATETIME(6) NOT NULL,
    INDEX idx_cart_items_cart_id (cart_id),
    INDEX idx_cart_items_product_stock_id (product_stock_id),
    INDEX idx_cart_items_seller_id (seller_id),
    CONSTRAINT fk_cart_items_cart_id FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
