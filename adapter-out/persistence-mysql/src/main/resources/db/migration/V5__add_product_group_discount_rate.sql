-- 상품그룹 할인율 컬럼 추가 (정렬/필터 인덱스용)
ALTER TABLE product_groups
    ADD COLUMN discount_rate INT NOT NULL DEFAULT 0 AFTER sale_price;

-- 할인율 정렬용 인덱스
CREATE INDEX idx_product_groups_discount_rate ON product_groups (discount_rate, id);
