-- =============================================================================
-- Product Test Data for REST API Integration Tests
-- =============================================================================

-- 1. 기존 데이터 정리 (의존성 역순으로)
DELETE FROM product_stocks;
DELETE FROM product_notice_items;
DELETE FROM product_notices;
DELETE FROM product_description_images;
DELETE FROM product_descriptions;
DELETE FROM product_images;
DELETE FROM products;
DELETE FROM product_groups;

-- 2. ProductGroup 테스트 데이터 (10개)
INSERT INTO product_groups (id, seller_id, category_id, brand_id, name, option_type, regular_price, current_price, status, shipping_policy_id, refund_policy_id, created_at, updated_at, deleted_at)
VALUES
    (1, 1, 100, 1, '기본 반팔 티셔츠', 'SINGLE', 29000.00, 19900.00, 'ACTIVE', 1, 1, NOW(), NOW(), NULL),
    (2, 1, 100, 1, '프리미엄 후드티', 'TWO_LEVEL', 89000.00, 69000.00, 'ACTIVE', 1, 1, NOW(), NOW(), NULL),
    (3, 1, 101, 2, '슬림핏 청바지', 'TWO_LEVEL', 79000.00, 59000.00, 'ACTIVE', 1, 1, NOW(), NOW(), NULL),
    (4, 2, 100, 1, '오버사이즈 맨투맨', 'SINGLE', 49000.00, 39000.00, 'ACTIVE', 2, 2, NOW(), NOW(), NULL),
    (5, 2, 102, 3, '캐주얼 셔츠', 'TWO_LEVEL', 59000.00, 45000.00, 'ACTIVE', 2, 2, NOW(), NOW(), NULL),
    (6, 1, 100, 2, '스트라이프 폴로', 'SINGLE', 39000.00, 29000.00, 'INACTIVE', 1, 1, NOW(), NOW(), NULL),
    (7, 3, 103, 1, '니트 가디건', 'TWO_LEVEL', 79000.00, 65000.00, 'ACTIVE', 3, 3, NOW(), NOW(), NULL),
    (8, 3, 100, 3, '베이직 V넥 티', 'SINGLE', 25000.00, 19000.00, 'ACTIVE', 3, 3, NOW(), NOW(), NULL),
    (9, 1, 101, 2, '와이드 팬츠', 'TWO_LEVEL', 69000.00, 55000.00, 'ACTIVE', 1, 1, NOW(), NOW(), NULL),
    (10, 2, 102, 1, '린넨 블라우스', 'SINGLE', 55000.00, 42000.00, 'ACTIVE', 2, 2, NOW(), NOW(), NULL);

-- Product (SKU) 테스트 데이터 (20개 - 상품그룹당 1~3개)
INSERT INTO products (id, product_group_id, option_type, option1_name, option1_value, option2_name, option2_value, additional_price, sold_out, display_yn, created_at, updated_at, deleted_at)
VALUES
    -- 상품그룹 1 (SINGLE - 1개 SKU)
    (1, 1, 'SINGLE', NULL, NULL, NULL, NULL, 0.00, 0, 1, NOW(), NOW(), NULL),
    -- 상품그룹 2 (TWO_LEVEL - 3개 SKU)
    (2, 2, 'TWO_LEVEL', '사이즈', 'M', '색상', '블랙', 0.00, 0, 1, NOW(), NOW(), NULL),
    (3, 2, 'TWO_LEVEL', '사이즈', 'L', '색상', '블랙', 0.00, 0, 1, NOW(), NOW(), NULL),
    (4, 2, 'TWO_LEVEL', '사이즈', 'XL', '색상', '그레이', 2000.00, 0, 1, NOW(), NOW(), NULL),
    -- 상품그룹 3 (TWO_LEVEL - 2개 SKU)
    (5, 3, 'TWO_LEVEL', '사이즈', '30', '색상', '인디고', 0.00, 0, 1, NOW(), NOW(), NULL),
    (6, 3, 'TWO_LEVEL', '사이즈', '32', '색상', '인디고', 0.00, 1, 1, NOW(), NOW(), NULL),
    -- 상품그룹 4 (SINGLE - 1개 SKU)
    (7, 4, 'SINGLE', NULL, NULL, NULL, NULL, 0.00, 0, 1, NOW(), NOW(), NULL),
    -- 상품그룹 5 (TWO_LEVEL - 3개 SKU)
    (8, 5, 'TWO_LEVEL', '사이즈', 'S', '색상', '화이트', 0.00, 0, 1, NOW(), NOW(), NULL),
    (9, 5, 'TWO_LEVEL', '사이즈', 'M', '색상', '화이트', 0.00, 0, 1, NOW(), NOW(), NULL),
    (10, 5, 'TWO_LEVEL', '사이즈', 'L', '색상', '스카이블루', 0.00, 0, 1, NOW(), NOW(), NULL),
    -- 상품그룹 6 (SINGLE - 1개 SKU)
    (11, 6, 'SINGLE', NULL, NULL, NULL, NULL, 0.00, 0, 1, NOW(), NOW(), NULL),
    -- 상품그룹 7 (TWO_LEVEL - 2개 SKU)
    (12, 7, 'TWO_LEVEL', '사이즈', 'FREE', '색상', '베이지', 0.00, 0, 1, NOW(), NOW(), NULL),
    (13, 7, 'TWO_LEVEL', '사이즈', 'FREE', '색상', '차콜', 0.00, 0, 1, NOW(), NOW(), NULL),
    -- 상품그룹 8 (SINGLE - 1개 SKU)
    (14, 8, 'SINGLE', NULL, NULL, NULL, NULL, 0.00, 0, 1, NOW(), NOW(), NULL),
    -- 상품그룹 9 (TWO_LEVEL - 2개 SKU)
    (15, 9, 'TWO_LEVEL', '사이즈', 'S', '색상', '블랙', 0.00, 0, 1, NOW(), NOW(), NULL),
    (16, 9, 'TWO_LEVEL', '사이즈', 'M', '색상', '블랙', 0.00, 0, 1, NOW(), NOW(), NULL),
    -- 상품그룹 10 (SINGLE - 1개 SKU)
    (17, 10, 'SINGLE', NULL, NULL, NULL, NULL, 0.00, 0, 1, NOW(), NOW(), NULL);

-- ProductImage 테스트 데이터 (상품그룹 1, 2에 대해)
INSERT INTO product_images (id, product_group_id, image_type, origin_url, cdn_url, display_order, created_at, updated_at)
VALUES
    (1, 1, 'MAIN', 'https://origin.example.com/1/main.jpg', 'https://cdn.example.com/1/main.jpg', 1, NOW(), NOW()),
    (2, 1, 'SUB', 'https://origin.example.com/1/sub1.jpg', 'https://cdn.example.com/1/sub1.jpg', 2, NOW(), NOW()),
    (3, 1, 'SUB', 'https://origin.example.com/1/sub2.jpg', 'https://cdn.example.com/1/sub2.jpg', 3, NOW(), NOW()),
    (4, 2, 'MAIN', 'https://origin.example.com/2/main.jpg', 'https://cdn.example.com/2/main.jpg', 1, NOW(), NOW()),
    (5, 2, 'DETAIL', 'https://origin.example.com/2/detail.jpg', 'https://cdn.example.com/2/detail.jpg', 2, NOW(), NOW());

-- ProductDescription 테스트 데이터 (상품그룹 1, 2에 대해)
INSERT INTO product_descriptions (id, product_group_id, html_content, created_at, updated_at)
VALUES
    (1, 1, '<div class="product-desc"><h2>기본 반팔 티셔츠</h2><p>편안한 착용감의 데일리 티셔츠입니다.</p></div>', NOW(), NOW()),
    (2, 2, '<div class="product-desc"><h2>프리미엄 후드티</h2><p>고급 원단을 사용한 후드티입니다.</p></div>', NOW(), NOW());

-- ProductDescriptionImage 테스트 데이터
INSERT INTO product_description_images (id, product_description_id, display_order, origin_url, cdn_url, uploaded_at)
VALUES
    (1, 1, 1, 'https://origin.example.com/desc/1/img1.jpg', 'https://cdn.example.com/desc/1/img1.jpg', NOW()),
    (2, 1, 2, 'https://origin.example.com/desc/1/img2.jpg', 'https://cdn.example.com/desc/1/img2.jpg', NOW());

-- ProductNotice 테스트 데이터 (상품그룹 1, 2에 대해)
INSERT INTO product_notices (id, product_group_id, template_id, created_at, updated_at)
VALUES
    (1, 1, 100, NOW(), NOW()),
    (2, 2, 100, NOW(), NOW());

-- ProductNoticeItem 테스트 데이터
INSERT INTO product_notice_items (id, product_notice_id, field_key, field_value, display_order)
VALUES
    (1, 1, 'material', '면 100%', 1),
    (2, 1, 'country_of_origin', '대한민국', 2),
    (3, 1, 'manufacturer', '(주)의류제조', 3),
    (4, 2, 'material', '면 80%, 폴리에스터 20%', 1),
    (5, 2, 'country_of_origin', '베트남', 2);

-- ProductStock 테스트 데이터 (SKU별 재고)
INSERT INTO product_stocks (id, product_id, quantity, version, created_at, updated_at)
VALUES
    (1, 1, 100, 0, NOW(), NOW()),
    (2, 2, 50, 0, NOW(), NOW()),
    (3, 3, 30, 0, NOW(), NOW()),
    (4, 4, 25, 0, NOW(), NOW()),
    (5, 5, 40, 0, NOW(), NOW()),
    (6, 6, 0, 0, NOW(), NOW()),
    (7, 7, 80, 0, NOW(), NOW()),
    (8, 8, 60, 0, NOW(), NOW()),
    (9, 9, 45, 0, NOW(), NOW()),
    (10, 10, 35, 0, NOW(), NOW());
