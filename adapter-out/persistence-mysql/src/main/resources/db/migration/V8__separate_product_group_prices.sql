-- product_group_prices 테이블 신설 (할인 재계산 전용)
-- 상품 CRUD와 할인 재계산의 row 경합을 제거하기 위해 가격 파생 필드를 분리

CREATE TABLE IF NOT EXISTS product_group_prices (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_group_id      BIGINT NOT NULL,
    sale_price            INT NOT NULL DEFAULT 0,
    discount_rate         INT NOT NULL DEFAULT 0,
    direct_discount_rate  INT NOT NULL DEFAULT 0,
    direct_discount_price INT NOT NULL DEFAULT 0,
    created_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at            DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    UNIQUE KEY uk_pgp_product_group_id (product_group_id),
    INDEX idx_pgp_discount_rate (discount_rate, product_group_id),
    INDEX idx_pgp_sale_price (sale_price, product_group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 기존 데이터 마이그레이션
INSERT INTO product_group_prices (product_group_id, sale_price, discount_rate, created_at, updated_at)
SELECT id, sale_price, discount_rate, NOW(6), NOW(6)
FROM product_groups
WHERE deleted_at IS NULL;

-- product_groups에서 가격 파생 컬럼 제거
ALTER TABLE product_groups DROP COLUMN sale_price;
ALTER TABLE product_groups DROP COLUMN discount_rate;

-- V5에서 추가한 인덱스 제거 (discount_rate 컬럼 삭제로 자동 제거될 수 있으나 명시적으로)
-- DROP INDEX idx_product_groups_discount_rate ON product_groups;
