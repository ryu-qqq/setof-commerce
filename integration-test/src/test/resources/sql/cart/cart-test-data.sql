-- ============================================================
-- Cart Integration Test Data
-- ============================================================
-- 장바구니 통합 테스트용 상품/판매자 참조 데이터
-- H2 MySQL Compatibility Mode 사용
--
-- 회원 및 장바구니 데이터는 API를 통해 동적으로 생성됩니다.
-- ============================================================

-- H2에서 FK 체크 비활성화
SET REFERENTIAL_INTEGRITY FALSE;

-- 기존 데이터 삭제 (테스트 격리)
DELETE FROM cart_items WHERE cart_id IN (SELECT id FROM carts);
DELETE FROM carts;
DELETE FROM product_stocks WHERE id IN (101, 102, 103);
DELETE FROM products WHERE id IN (10, 11, 12);
DELETE FROM product_groups WHERE id = 10;
DELETE FROM sellers WHERE id = 10;
DELETE FROM category WHERE id = 10;
DELETE FROM brand WHERE id = 10;

-- H2에서 FK 체크 활성화
SET REFERENTIAL_INTEGRITY TRUE;

-- ============================================================
-- 1. Reference Data (Brand, Category, Seller, Products)
-- ============================================================

-- Brand
INSERT INTO brand (id, code, name_ko, name_en, logo_url, status, created_at, updated_at)
VALUES (10, 'CART_TEST_BRAND', '장바구니테스트브랜드', 'Cart Test Brand', 'https://example.com/cart-brand-logo.jpg', 'ACTIVE', NOW(), NOW());

-- Category
INSERT INTO category (id, code, name_ko, parent_id, depth, path, sort_order, is_leaf, status, created_at, updated_at)
VALUES (10, 'CAT_CART', '장바구니테스트카테고리', NULL, 0, '/10/', 1, TRUE, 'ACTIVE', NOW(), NOW());

-- Seller
INSERT INTO sellers (
    id, tenant_id, organization_id, seller_name, logo_url, description, approval_status,
    registration_number, sale_report_number, representative,
    business_address_line1, business_address_line2, business_zip_code,
    created_at, updated_at, deleted_at
) VALUES (
    10, 'default', 'default', '장바구니테스트스토어', 'https://example.com/cart-seller-logo.jpg', '장바구니 테스트 판매자입니다', 'APPROVED',
    '111-22-33333', '2024-서울강남-99999', '김테스트',
    '서울시 강남구 테스트로 123', '테스트빌딩 5층', '06234',
    NOW(), NOW(), NULL
);

-- Product Group
INSERT INTO product_groups (
    id, seller_id, category_id, brand_id, name, option_type, regular_price, current_price,
    status, shipping_policy_id, refund_policy_id, created_at, updated_at, deleted_at
) VALUES (
    10, 10, 10, 10, '장바구니 테스트 상품 그룹', 'SINGLE', 29900.00, 29900.00,
    'ON_SALE', NULL, NULL, NOW(), NOW(), NULL
);

-- Products
INSERT INTO products (id, product_group_id, option_type, option1_name, option1_value, option2_name, option2_value, additional_price, sold_out, display_yn, created_at, updated_at, deleted_at) VALUES
(10, 10, 'SINGLE', '색상', '블랙', '사이즈', 'M', 0.00, FALSE, TRUE, NOW(), NOW(), NULL),
(11, 10, 'SINGLE', '색상', '화이트', '사이즈', 'L', 0.00, FALSE, TRUE, NOW(), NOW(), NULL),
(12, 10, 'SINGLE', '색상', '블루', '사이즈', 'XL', 0.00, FALSE, TRUE, NOW(), NOW(), NULL);

-- Product Stocks
INSERT INTO product_stocks (id, product_id, quantity, version, created_at, updated_at) VALUES
(101, 10, 100, 0, NOW(), NOW()),
(102, 11, 100, 0, NOW(), NOW()),
(103, 12, 100, 0, NOW(), NOW());
