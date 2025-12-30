-- ============================================================
-- Checkout/Order/Payment/Claim Integration Test Data
-- ============================================================
-- Payment/Order Bounded Context 통합 테스트용 샘플 데이터
-- H2 MySQL Compatibility Mode 사용
--
-- UUID 저장 방식:
-- H2에서 Hibernate 6 UUID-to-BINARY(16) 변환과 호환되도록
-- CAST('uuid-string' AS UUID) 형식 사용
-- ============================================================

-- H2에서 FK 체크 비활성화
SET REFERENTIAL_INTEGRITY FALSE;

-- 기존 데이터 삭제 (테스트 격리)
DELETE FROM claim;
DELETE FROM order_event;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM checkout_items;
DELETE FROM checkouts;
DELETE FROM payments;
-- 참조 데이터 삭제 (의존성 역순)
DELETE FROM product_stocks;
DELETE FROM products;
DELETE FROM product_groups;
DELETE FROM sellers;
DELETE FROM category;
DELETE FROM brand;

-- H2에서 FK 체크 활성화
SET REFERENTIAL_INTEGRITY TRUE;

-- ============================================================
-- 0. 참조 데이터 (Brand, Category, Seller, Product, Stock)
-- ============================================================

-- Brand 데이터
INSERT INTO brand (id, code, name_ko, name_en, logo_url, status, created_at, updated_at)
VALUES (1, 'TEST_BRAND', '테스트브랜드', 'Test Brand', 'https://example.com/brand-logo.jpg', 'ACTIVE', NOW(), NOW());

-- Category 데이터 (최상위 카테고리)
INSERT INTO category (id, code, name_ko, parent_id, depth, path, sort_order, is_leaf, status, created_at, updated_at)
VALUES (1, 'CAT001', '패션의류', NULL, 0, '/1/', 1, TRUE, 'ACTIVE', NOW(), NOW());

-- Seller 데이터
INSERT INTO sellers (
    id, tenant_id, organization_id, seller_name, logo_url, description, approval_status,
    registration_number, sale_report_number, representative,
    business_address_line1, business_address_line2, business_zip_code,
    created_at, updated_at, deleted_at
) VALUES (
    1, 'default', 'default', '테스트스토어', 'https://example.com/seller-logo.jpg', '테스트 판매자입니다', 'APPROVED',
    '123-45-67890', '2024-서울강남-12345', '홍길동',
    '서울시 강남구 테헤란로 123', '12층', '06234',
    NOW(), NOW(), NULL
);

-- Product Group 데이터
INSERT INTO product_groups (
    id, seller_id, category_id, brand_id, name, option_type, regular_price, current_price,
    status, shipping_policy_id, refund_policy_id, created_at, updated_at, deleted_at
) VALUES (
    1, 1, 1, 1, '테스트 상품 그룹', 'SINGLE', 29900.00, 29900.00,
    'ON_SALE', NULL, NULL, NOW(), NOW(), NULL
);

-- Products 데이터 (id: 100-107)
INSERT INTO products (id, product_group_id, option_type, option1_name, option1_value, option2_name, option2_value, additional_price, sold_out, display_yn, created_at, updated_at, deleted_at) VALUES
(100, 1, 'SINGLE', '색상', '블랙', '사이즈', 'XL', 0.00, FALSE, TRUE, NOW(), NOW(), NULL),
(101, 1, 'SINGLE', '색상', '화이트', '사이즈', 'M', 0.00, FALSE, TRUE, NOW(), NOW(), NULL),
(102, 1, 'SINGLE', '색상', '블루', '사이즈', 'L', 0.00, FALSE, TRUE, NOW(), NOW(), NULL),
(103, 1, 'SINGLE', '색상', '레드', '사이즈', 'S', 0.00, FALSE, TRUE, NOW(), NOW(), NULL),
(104, 1, 'SINGLE', '색상', '그린', '사이즈', 'M', 0.00, FALSE, TRUE, NOW(), NOW(), NULL),
(105, 1, 'SINGLE', '색상', '옐로우', '사이즈', 'XS', 0.00, FALSE, TRUE, NOW(), NOW(), NULL),
(106, 1, 'SINGLE', '색상', '퍼플', '사이즈', 'L', 0.00, FALSE, TRUE, NOW(), NOW(), NULL),
(107, 1, 'SINGLE', '색상', '핑크', '사이즈', 'M', 0.00, FALSE, TRUE, NOW(), NOW(), NULL);

-- Product Stocks 데이터 (id: 1001-1008)
INSERT INTO product_stocks (id, product_id, quantity, version, created_at, updated_at) VALUES
(1001, 100, 100, 0, NOW(), NOW()),
(1002, 101, 100, 0, NOW(), NOW()),
(1003, 102, 100, 0, NOW(), NOW()),
(1004, 103, 100, 0, NOW(), NOW()),
(1005, 104, 100, 0, NOW(), NOW()),
(1006, 105, 100, 0, NOW(), NOW()),
(1007, 106, 100, 0, NOW(), NOW()),
(1008, 107, 100, 0, NOW(), NOW());

-- ============================================================
-- 1. PENDING 상태 Checkout (결제 대기 중)
-- ============================================================
INSERT INTO checkouts (
    id, member_id, status,
    receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_amount, discount_amount, final_amount,
    expired_at, completed_at, created_at, updated_at
) VALUES (
    CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID),
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'PENDING',
    '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', '부재 시 경비실에 맡겨주세요',
    59800.00, 0.00, 59800.00,
    TIMESTAMPADD(MINUTE, 30, CURRENT_TIMESTAMP(6)), NULL,
    CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6)
);

INSERT INTO checkout_items (
    id, checkout_id, product_stock_id, product_id, seller_id,
    quantity, unit_price, total_price,
    product_name, product_image, option_name, brand_name, seller_name
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440001' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID),
    1001, 100, 1,
    2, 29900.00, 59800.00,
    '테스트 상품', 'https://example.com/image.jpg', '블랙 / XL', '테스트브랜드', '테스트스토어'
);

INSERT INTO payments (
    id, checkout_id, pg_provider, pg_transaction_id, method, status,
    requested_amount, approved_amount, refunded_amount, currency,
    approved_at, cancelled_at, created_at, updated_at
) VALUES (
    CAST('660e8400-e29b-41d4-a716-446655440001' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440001' AS UUID),
    'TOSS', NULL, 'CARD', 'PENDING',
    59800.00, 0.00, 0.00, 'KRW',
    NULL, NULL, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6)
);

-- ============================================================
-- 2. COMPLETED Checkout + ORDERED Order
-- ============================================================
INSERT INTO checkouts (
    id, member_id, status,
    receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_amount, discount_amount, final_amount,
    expired_at, completed_at, created_at, updated_at
) VALUES (
    CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID),
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'COMPLETED',
    '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', '부재 시 경비실에 맡겨주세요',
    59800.00, 0.00, 59800.00,
    TIMESTAMPADD(MINUTE, 30, CURRENT_TIMESTAMP(6)), CURRENT_TIMESTAMP(6),
    TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP(6)), CURRENT_TIMESTAMP(6)
);

INSERT INTO checkout_items (
    id, checkout_id, product_stock_id, product_id, seller_id,
    quantity, unit_price, total_price,
    product_name, product_image, option_name, brand_name, seller_name
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440002' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID),
    1001, 100, 1,
    2, 29900.00, 59800.00,
    '테스트 상품', 'https://example.com/image.jpg', '블랙 / XL', '테스트브랜드', '테스트스토어'
);

INSERT INTO payments (
    id, checkout_id, pg_provider, pg_transaction_id, method, status,
    requested_amount, approved_amount, refunded_amount, currency,
    approved_at, cancelled_at, created_at, updated_at
) VALUES (
    CAST('660e8400-e29b-41d4-a716-446655440002' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID),
    'TOSS', 'toss_txn_123456789', 'CARD', 'APPROVED',
    59800.00, 59800.00, 0.00, 'KRW',
    CURRENT_TIMESTAMP(6), NULL, TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP(6)), CURRENT_TIMESTAMP(6)
);

INSERT INTO orders (
    id, order_number, checkout_id, payment_id, seller_id, member_id,
    status, receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_item_amount, shipping_fee, total_amount,
    ordered_at, confirmed_at, shipped_at, delivered_at, completed_at, cancelled_at,
    created_at, updated_at
) VALUES (
    CAST('770e8400-e29b-41d4-a716-446655440002' AS UUID),
    'ORD-20251223-00000001',
    CAST('550e8400-e29b-41d4-a716-446655440002' AS UUID),
    CAST('660e8400-e29b-41d4-a716-446655440002' AS UUID),
    1, '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'ORDERED', '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', '부재 시 경비실에 맡겨주세요',
    59800.00, 0.00, 59800.00,
    CURRENT_TIMESTAMP(6), NULL, NULL, NULL, NULL, NULL,
    CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6)
);

INSERT INTO order_items (
    id, order_id, product_id, product_stock_id,
    ordered_quantity, cancelled_quantity, refunded_quantity,
    unit_price, total_price, status,
    product_name, product_image, option_name, brand_name, seller_name, original_price
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440002' AS UUID),
    CAST('770e8400-e29b-41d4-a716-446655440002' AS UUID),
    100, 1001,
    2, 0, 0,
    29900.00, 59800.00, 'ORDERED',
    '테스트 상품', 'https://example.com/image.jpg', '블랙 / XL', '테스트브랜드', '테스트스토어', 29900.00
);

-- ============================================================
-- 3. CONFIRMED Order (주문 확정됨)
-- ============================================================
INSERT INTO checkouts (
    id, member_id, status,
    receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_amount, discount_amount, final_amount,
    expired_at, completed_at, created_at, updated_at
) VALUES (
    CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID),
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'COMPLETED',
    '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    89700.00, 0.00, 89700.00,
    TIMESTAMPADD(MINUTE, 30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(HOUR, -3, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6))
);

INSERT INTO payments (
    id, checkout_id, pg_provider, pg_transaction_id, method, status,
    requested_amount, approved_amount, refunded_amount, currency,
    approved_at, cancelled_at, created_at, updated_at
) VALUES (
    CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID),
    'TOSS', 'toss_txn_223456789', 'CARD', 'APPROVED',
    89700.00, 89700.00, 0.00, 'KRW',
    TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6)), NULL,
    TIMESTAMPADD(HOUR, -3, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6))
);

INSERT INTO orders (
    id, order_number, checkout_id, payment_id, seller_id, member_id,
    status, receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_item_amount, shipping_fee, total_amount,
    ordered_at, confirmed_at, shipped_at, delivered_at, completed_at, cancelled_at,
    created_at, updated_at
) VALUES (
    CAST('770e8400-e29b-41d4-a716-446655440003' AS UUID),
    'ORD-20251223-00000002',
    CAST('550e8400-e29b-41d4-a716-446655440003' AS UUID),
    CAST('660e8400-e29b-41d4-a716-446655440003' AS UUID),
    1, '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'CONFIRMED', '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    89700.00, 0.00, 89700.00,
    TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP(6)),
    NULL, NULL, NULL, NULL,
    TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP(6))
);

INSERT INTO order_items (
    id, order_id, product_id, product_stock_id,
    ordered_quantity, cancelled_quantity, refunded_quantity,
    unit_price, total_price, status,
    product_name, product_image, option_name, brand_name, seller_name, original_price
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440003' AS UUID),
    CAST('770e8400-e29b-41d4-a716-446655440003' AS UUID),
    101, 1002,
    3, 0, 0,
    29900.00, 89700.00, 'CONFIRMED',
    '테스트 상품 B', 'https://example.com/image2.jpg', '화이트 / M', '테스트브랜드', '테스트스토어', 29900.00
);

-- ============================================================
-- 4. SHIPPED Order (배송 중)
-- ============================================================
INSERT INTO checkouts (
    id, member_id, status,
    receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_amount, discount_amount, final_amount,
    expired_at, completed_at, created_at, updated_at
) VALUES (
    CAST('550e8400-e29b-41d4-a716-446655440004' AS UUID),
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'COMPLETED',
    '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    49900.00, 0.00, 49900.00,
    TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP(6))
);

INSERT INTO payments (
    id, checkout_id, pg_provider, pg_transaction_id, method, status,
    requested_amount, approved_amount, refunded_amount, currency,
    approved_at, cancelled_at, created_at, updated_at
) VALUES (
    CAST('660e8400-e29b-41d4-a716-446655440004' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440004' AS UUID),
    'KAKAO', 'kakao_txn_323456789', 'KAKAO_PAY', 'APPROVED',
    49900.00, 49900.00, 0.00, 'KRW',
    TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP(6)), NULL,
    TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP(6))
);

INSERT INTO orders (
    id, order_number, checkout_id, payment_id, seller_id, member_id,
    status, receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_item_amount, shipping_fee, total_amount,
    ordered_at, confirmed_at, shipped_at, delivered_at, completed_at, cancelled_at,
    created_at, updated_at
) VALUES (
    CAST('770e8400-e29b-41d4-a716-446655440004' AS UUID),
    'ORD-20251223-00000003',
    CAST('550e8400-e29b-41d4-a716-446655440004' AS UUID),
    CAST('660e8400-e29b-41d4-a716-446655440004' AS UUID),
    1, '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'SHIPPED', '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    49900.00, 0.00, 49900.00,
    TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP(6)), NULL, NULL, NULL,
    TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP(6))
);

INSERT INTO order_items (
    id, order_id, product_id, product_stock_id,
    ordered_quantity, cancelled_quantity, refunded_quantity,
    unit_price, total_price, status,
    product_name, product_image, option_name, brand_name, seller_name, original_price
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440004' AS UUID),
    CAST('770e8400-e29b-41d4-a716-446655440004' AS UUID),
    102, 1003,
    1, 0, 0,
    49900.00, 49900.00, 'SHIPPED',
    '테스트 상품 C', 'https://example.com/image3.jpg', '블루 / L', '테스트브랜드', '테스트스토어', 49900.00
);

-- ============================================================
-- 5. DELIVERED Order (배송 완료)
-- ============================================================
INSERT INTO checkouts (
    id, member_id, status,
    receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_amount, discount_amount, final_amount,
    expired_at, completed_at, created_at, updated_at
) VALUES (
    CAST('550e8400-e29b-41d4-a716-446655440005' AS UUID),
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'COMPLETED',
    '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    39900.00, 0.00, 39900.00,
    TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -6, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6))
);

INSERT INTO payments (
    id, checkout_id, pg_provider, pg_transaction_id, method, status,
    requested_amount, approved_amount, refunded_amount, currency,
    approved_at, cancelled_at, created_at, updated_at
) VALUES (
    CAST('660e8400-e29b-41d4-a716-446655440005' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440005' AS UUID),
    'TOSS', 'toss_txn_423456789', 'CARD', 'APPROVED',
    39900.00, 39900.00, 0.00, 'KRW',
    TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6)), NULL,
    TIMESTAMPADD(DAY, -6, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6))
);

INSERT INTO orders (
    id, order_number, checkout_id, payment_id, seller_id, member_id,
    status, receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_item_amount, shipping_fee, total_amount,
    ordered_at, confirmed_at, shipped_at, delivered_at, completed_at, cancelled_at,
    created_at, updated_at
) VALUES (
    CAST('770e8400-e29b-41d4-a716-446655440005' AS UUID),
    'ORD-20251223-00000004',
    CAST('550e8400-e29b-41d4-a716-446655440005' AS UUID),
    CAST('660e8400-e29b-41d4-a716-446655440005' AS UUID),
    1, '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'DELIVERED', '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    39900.00, 0.00, 39900.00,
    TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -4, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP(6)),
    NULL, NULL,
    TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP(6))
);

INSERT INTO order_items (
    id, order_id, product_id, product_stock_id,
    ordered_quantity, cancelled_quantity, refunded_quantity,
    unit_price, total_price, status,
    product_name, product_image, option_name, brand_name, seller_name, original_price
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440005' AS UUID),
    CAST('770e8400-e29b-41d4-a716-446655440005' AS UUID),
    103, 1004,
    1, 0, 0,
    39900.00, 39900.00, 'DELIVERED',
    '테스트 상품 D', 'https://example.com/image4.jpg', '레드 / S', '테스트브랜드', '테스트스토어', 39900.00
);

-- ============================================================
-- 6. COMPLETED Order (구매 확정)
-- ============================================================
INSERT INTO checkouts (
    id, member_id, status,
    receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_amount, discount_amount, final_amount,
    expired_at, completed_at, created_at, updated_at
) VALUES (
    CAST('550e8400-e29b-41d4-a716-446655440006' AS UUID),
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'COMPLETED',
    '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    79800.00, 0.00, 79800.00,
    TIMESTAMPADD(DAY, -15, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -15, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -16, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -15, CURRENT_TIMESTAMP(6))
);

INSERT INTO payments (
    id, checkout_id, pg_provider, pg_transaction_id, method, status,
    requested_amount, approved_amount, refunded_amount, currency,
    approved_at, cancelled_at, created_at, updated_at
) VALUES (
    CAST('660e8400-e29b-41d4-a716-446655440006' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440006' AS UUID),
    'TOSS', 'toss_txn_523456789', 'CARD', 'APPROVED',
    79800.00, 79800.00, 0.00, 'KRW',
    TIMESTAMPADD(DAY, -15, CURRENT_TIMESTAMP(6)), NULL,
    TIMESTAMPADD(DAY, -16, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -15, CURRENT_TIMESTAMP(6))
);

INSERT INTO orders (
    id, order_number, checkout_id, payment_id, seller_id, member_id,
    status, receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_item_amount, shipping_fee, total_amount,
    ordered_at, confirmed_at, shipped_at, delivered_at, completed_at, cancelled_at,
    created_at, updated_at
) VALUES (
    CAST('770e8400-e29b-41d4-a716-446655440006' AS UUID),
    'ORD-20251223-00000005',
    CAST('550e8400-e29b-41d4-a716-446655440006' AS UUID),
    CAST('660e8400-e29b-41d4-a716-446655440006' AS UUID),
    1, '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'COMPLETED', '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    79800.00, 0.00, 79800.00,
    TIMESTAMPADD(DAY, -15, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -14, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -13, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -7, CURRENT_TIMESTAMP(6)), NULL,
    TIMESTAMPADD(DAY, -15, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -7, CURRENT_TIMESTAMP(6))
);

INSERT INTO order_items (
    id, order_id, product_id, product_stock_id,
    ordered_quantity, cancelled_quantity, refunded_quantity,
    unit_price, total_price, status,
    product_name, product_image, option_name, brand_name, seller_name, original_price
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440006' AS UUID),
    CAST('770e8400-e29b-41d4-a716-446655440006' AS UUID),
    104, 1005,
    2, 0, 0,
    39900.00, 79800.00, 'COMPLETED',
    '테스트 상품 E', 'https://example.com/image5.jpg', '그린 / M', '테스트브랜드', '테스트스토어', 39900.00
);

-- ============================================================
-- 7. CANCELLED Order (취소된 주문)
-- ============================================================
INSERT INTO checkouts (
    id, member_id, status,
    receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_amount, discount_amount, final_amount,
    expired_at, completed_at, created_at, updated_at
) VALUES (
    CAST('550e8400-e29b-41d4-a716-446655440007' AS UUID),
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'COMPLETED',
    '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    29900.00, 0.00, 29900.00,
    TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -11, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6))
);

INSERT INTO payments (
    id, checkout_id, pg_provider, pg_transaction_id, method, status,
    requested_amount, approved_amount, refunded_amount, currency,
    approved_at, cancelled_at, created_at, updated_at
) VALUES (
    CAST('660e8400-e29b-41d4-a716-446655440007' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440007' AS UUID),
    'TOSS', 'toss_txn_623456789', 'CARD', 'CANCELLED',
    29900.00, 29900.00, 29900.00, 'KRW',
    TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -9, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -11, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -9, CURRENT_TIMESTAMP(6))
);

INSERT INTO orders (
    id, order_number, checkout_id, payment_id, seller_id, member_id,
    status, receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_item_amount, shipping_fee, total_amount,
    ordered_at, confirmed_at, shipped_at, delivered_at, completed_at, cancelled_at,
    created_at, updated_at
) VALUES (
    CAST('770e8400-e29b-41d4-a716-446655440007' AS UUID),
    'ORD-20251223-00000006',
    CAST('550e8400-e29b-41d4-a716-446655440007' AS UUID),
    CAST('660e8400-e29b-41d4-a716-446655440007' AS UUID),
    1, '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'CANCELLED', '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    29900.00, 0.00, 29900.00,
    TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6)), NULL,
    NULL, NULL, NULL, TIMESTAMPADD(DAY, -9, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -9, CURRENT_TIMESTAMP(6))
);

INSERT INTO order_items (
    id, order_id, product_id, product_stock_id,
    ordered_quantity, cancelled_quantity, refunded_quantity,
    unit_price, total_price, status,
    product_name, product_image, option_name, brand_name, seller_name, original_price
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440007' AS UUID),
    CAST('770e8400-e29b-41d4-a716-446655440007' AS UUID),
    105, 1006,
    1, 1, 0,
    29900.00, 29900.00, 'CANCELLED',
    '테스트 상품 F', 'https://example.com/image6.jpg', '옐로우 / XS', '테스트브랜드', '테스트스토어', 29900.00
);

-- ============================================================
-- 8. EXPIRED Checkout (만료된 체크아웃)
-- ============================================================
INSERT INTO checkouts (
    id, member_id, status,
    receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_amount, discount_amount, final_amount,
    expired_at, completed_at, created_at, updated_at
) VALUES (
    CAST('550e8400-e29b-41d4-a716-446655440008' AS UUID),
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'EXPIRED',
    '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    19900.00, 0.00, 19900.00,
    TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP(6)), NULL,
    TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP(6))
);

INSERT INTO checkout_items (
    id, checkout_id, product_stock_id, product_id, seller_id,
    quantity, unit_price, total_price,
    product_name, product_image, option_name, brand_name, seller_name
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440008' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440008' AS UUID),
    1007, 106, 1,
    1, 19900.00, 19900.00,
    '테스트 상품 G', 'https://example.com/image7.jpg', '퍼플 / L', '테스트브랜드', '테스트스토어'
);

INSERT INTO payments (
    id, checkout_id, pg_provider, pg_transaction_id, method, status,
    requested_amount, approved_amount, refunded_amount, currency,
    approved_at, cancelled_at, created_at, updated_at
) VALUES (
    CAST('660e8400-e29b-41d4-a716-446655440008' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440008' AS UUID),
    'TOSS', NULL, 'CARD', 'PENDING',
    19900.00, 0.00, 0.00, 'KRW',
    NULL, NULL, TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP(6))
);

-- ============================================================
-- 9. Claim Data - REQUESTED 상태 (반품 요청됨)
-- ============================================================
INSERT INTO claim (
    claim_id, claim_number, order_id, order_item_id,
    claim_type, claim_reason, claim_reason_detail, quantity, refund_amount,
    status, processed_by, processed_at, reject_reason,
    return_tracking_number, return_carrier,
    return_shipping_method, return_shipping_status, return_pickup_scheduled_at, return_pickup_address, return_customer_phone, return_received_at,
    inspection_result, inspection_note,
    exchange_tracking_number, exchange_carrier, exchange_shipped_at, exchange_delivered_at,
    created_at, updated_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440001',
    'CLM-20251223-00000001',
    '770e8400-e29b-41d4-a716-446655440005', -- DELIVERED order
    '880e8400-e29b-41d4-a716-446655440005',
    'RETURN', 'WRONG_SIZE', '사이즈가 맞지 않습니다', 1, 39900.00,
    'REQUESTED', NULL, NULL, NULL,
    NULL, NULL,
    NULL, NULL, NULL, NULL, NULL, NULL,
    NULL, NULL,
    NULL, NULL, NULL, NULL,
    CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6)
);

-- ============================================================
-- 10. Claim Data - APPROVED 상태 (반품 승인됨)
-- ============================================================
INSERT INTO claim (
    claim_id, claim_number, order_id, order_item_id,
    claim_type, claim_reason, claim_reason_detail, quantity, refund_amount,
    status, processed_by, processed_at, reject_reason,
    return_tracking_number, return_carrier,
    return_shipping_method, return_shipping_status, return_pickup_scheduled_at, return_pickup_address, return_customer_phone, return_received_at,
    inspection_result, inspection_note,
    exchange_tracking_number, exchange_carrier, exchange_shipped_at, exchange_delivered_at,
    created_at, updated_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440002',
    'CLM-20251223-00000002',
    '770e8400-e29b-41d4-a716-446655440005', -- DELIVERED order
    '880e8400-e29b-41d4-a716-446655440005',
    'RETURN', 'DEFECTIVE_PRODUCT', '상품에 불량이 있습니다', 1, 39900.00,
    'APPROVED', 'admin-001', TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6)), NULL,
    NULL, NULL,
    'SELLER_PICKUP', 'PENDING', NULL, '서울시 강남구 테헤란로 123동 456호', '010-1234-5678', NULL,
    NULL, NULL,
    NULL, NULL, NULL, NULL,
    TIMESTAMPADD(HOUR, -3, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP(6))
);

-- ============================================================
-- 11. Claim Data - COMPLETED 상태 (환불 완료)
-- ============================================================
INSERT INTO checkouts (
    id, member_id, status,
    receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_amount, discount_amount, final_amount,
    expired_at, completed_at, created_at, updated_at
) VALUES (
    CAST('550e8400-e29b-41d4-a716-446655440009' AS UUID),
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'COMPLETED',
    '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    59900.00, 0.00, 59900.00,
    TIMESTAMPADD(DAY, -20, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -20, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -21, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -20, CURRENT_TIMESTAMP(6))
);

INSERT INTO payments (
    id, checkout_id, pg_provider, pg_transaction_id, method, status,
    requested_amount, approved_amount, refunded_amount, currency,
    approved_at, cancelled_at, created_at, updated_at
) VALUES (
    CAST('660e8400-e29b-41d4-a716-446655440009' AS UUID),
    CAST('550e8400-e29b-41d4-a716-446655440009' AS UUID),
    'TOSS', 'toss_txn_723456789', 'CARD', 'REFUNDED',
    59900.00, 59900.00, 59900.00, 'KRW',
    TIMESTAMPADD(DAY, -20, CURRENT_TIMESTAMP(6)), NULL,
    TIMESTAMPADD(DAY, -21, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6))
);

INSERT INTO orders (
    id, order_number, checkout_id, payment_id, seller_id, member_id,
    status, receiver_name, receiver_phone, address, address_detail, zip_code, memo,
    total_item_amount, shipping_fee, total_amount,
    ordered_at, confirmed_at, shipped_at, delivered_at, completed_at, cancelled_at,
    created_at, updated_at
) VALUES (
    CAST('770e8400-e29b-41d4-a716-446655440009' AS UUID),
    'ORD-20251223-00000007',
    CAST('550e8400-e29b-41d4-a716-446655440009' AS UUID),
    CAST('660e8400-e29b-41d4-a716-446655440009' AS UUID),
    1, '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'DELIVERED', '홍길동', '010-1234-5678', '서울시 강남구 테헤란로', '123동 456호', '06234', NULL,
    59900.00, 0.00, 59900.00,
    TIMESTAMPADD(DAY, -20, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -19, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -18, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -15, CURRENT_TIMESTAMP(6)),
    NULL, NULL,
    TIMESTAMPADD(DAY, -20, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6))
);

INSERT INTO order_items (
    id, order_id, product_id, product_stock_id,
    ordered_quantity, cancelled_quantity, refunded_quantity,
    unit_price, total_price, status,
    product_name, product_image, option_name, brand_name, seller_name, original_price
) VALUES (
    CAST('880e8400-e29b-41d4-a716-446655440009' AS UUID),
    CAST('770e8400-e29b-41d4-a716-446655440009' AS UUID),
    107, 1008,
    1, 0, 1,
    59900.00, 59900.00, 'REFUNDED',
    '테스트 상품 H', 'https://example.com/image8.jpg', '핑크 / M', '테스트브랜드', '테스트스토어', 59900.00
);

INSERT INTO claim (
    claim_id, claim_number, order_id, order_item_id,
    claim_type, claim_reason, claim_reason_detail, quantity, refund_amount,
    status, processed_by, processed_at, reject_reason,
    return_tracking_number, return_carrier,
    return_shipping_method, return_shipping_status, return_pickup_scheduled_at, return_pickup_address, return_customer_phone, return_received_at,
    inspection_result, inspection_note,
    exchange_tracking_number, exchange_carrier, exchange_shipped_at, exchange_delivered_at,
    created_at, updated_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440003',
    'CLM-20251223-00000003',
    '770e8400-e29b-41d4-a716-446655440009',
    '880e8400-e29b-41d4-a716-446655440009',
    'RETURN', 'DAMAGED_DURING_DELIVERY', '배송 중 파손되었습니다', 1, 59900.00,
    'COMPLETED', 'admin-001', TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6)), NULL,
    '1234567890', 'CJ',
    'SELLER_PICKUP', 'RECEIVED', TIMESTAMPADD(DAY, -12, CURRENT_TIMESTAMP(6)), '서울시 강남구 테헤란로 123동 456호', '010-1234-5678', TIMESTAMPADD(DAY, -11, CURRENT_TIMESTAMP(6)),
    'PASS', '상품 검수 완료',
    NULL, NULL, NULL, NULL,
    TIMESTAMPADD(DAY, -14, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6))
);

-- ============================================================
-- 12. Order Event Data (주문 이벤트 타임라인)
-- ============================================================
-- ORDERED 주문의 이벤트 (주문 생성)
INSERT INTO order_event (
    order_id, event_type, event_source, source_id,
    previous_status, current_status,
    actor_type, actor_id, description, metadata, created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440002',
    'ORDER_PLACED', 'CHECKOUT', '550e8400-e29b-41d4-a716-446655440002',
    NULL, 'ORDERED',
    'MEMBER', '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    '주문이 생성되었습니다',
    '{"checkoutId": "550e8400-e29b-41d4-a716-446655440002", "totalAmount": 59800}',
    CURRENT_TIMESTAMP(6)
);

-- CONFIRMED 주문의 이벤트 (주문 확정)
INSERT INTO order_event (
    order_id, event_type, event_source, source_id,
    previous_status, current_status,
    actor_type, actor_id, description, metadata, created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440003',
    'ORDER_CONFIRMED', 'ORDER', '770e8400-e29b-41d4-a716-446655440003',
    'ORDERED', 'CONFIRMED',
    'SELLER', '1',
    '주문이 확정되었습니다',
    '{"confirmedBy": "seller-1"}',
    TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP(6))
);

-- SHIPPED 주문의 이벤트 (배송 시작)
INSERT INTO order_event (
    order_id, event_type, event_source, source_id,
    previous_status, current_status,
    actor_type, actor_id, description, metadata, created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440004',
    'SHIPMENT_STARTED', 'ORDER', '770e8400-e29b-41d4-a716-446655440004',
    'CONFIRMED', 'SHIPPED',
    'SELLER', '1',
    '배송이 시작되었습니다',
    '{"trackingNumber": "1234567890", "carrier": "CJ"}',
    TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP(6))
);

-- DELIVERED 주문의 이벤트 (배송 완료)
INSERT INTO order_event (
    order_id, event_type, event_source, source_id,
    previous_status, current_status,
    actor_type, actor_id, description, metadata, created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440005',
    'DELIVERY_COMPLETED', 'ORDER', '770e8400-e29b-41d4-a716-446655440005',
    'SHIPPED', 'DELIVERED',
    'SYSTEM', NULL,
    '배송이 완료되었습니다',
    '{"deliveredAt": "2025-12-20T10:00:00Z"}',
    TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP(6))
);

-- CANCELLED 주문의 이벤트 (주문 취소)
INSERT INTO order_event (
    order_id, event_type, event_source, source_id,
    previous_status, current_status,
    actor_type, actor_id, description, metadata, created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440007',
    'ORDER_CANCELLED', 'CLAIM', NULL,
    'ORDERED', 'CANCELLED',
    'MEMBER', '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    '고객 요청으로 주문이 취소되었습니다',
    '{"cancelReason": "SIMPLE_CHANGE_OF_MIND"}',
    TIMESTAMPADD(DAY, -9, CURRENT_TIMESTAMP(6))
);

-- Claim에 의한 환불 이벤트
INSERT INTO order_event (
    order_id, event_type, event_source, source_id,
    previous_status, current_status,
    actor_type, actor_id, description, metadata, created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440009',
    'REFUND_COMPLETED', 'CLAIM', '990e8400-e29b-41d4-a716-446655440003',
    'DELIVERED', 'DELIVERED',
    'SYSTEM', NULL,
    '환불이 완료되었습니다',
    '{"claimId": "990e8400-e29b-41d4-a716-446655440003", "refundAmount": 59900}',
    TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6))
);
