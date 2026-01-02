-- ============================================================
-- Discount Integration Test Data
-- ============================================================
-- Discount Bounded Context 통합 테스트용 샘플 데이터
-- H2 MySQL Compatibility Mode 사용
-- ============================================================

-- H2에서 FK 체크 비활성화
SET REFERENTIAL_INTEGRITY FALSE;

-- 기존 데이터 삭제 (테스트 격리)
DELETE FROM discount_usage_histories;
DELETE FROM discount_policies;
DELETE FROM sellers WHERE id IN (1, 2);

-- H2에서 FK 체크 활성화
SET REFERENTIAL_INTEGRITY TRUE;

-- ============================================================
-- 0. 참조 데이터 (Seller)
-- ============================================================

-- Seller 1 (테스트용 기본 셀러)
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

-- Seller 2 (권한 테스트용)
INSERT INTO sellers (
    id, tenant_id, organization_id, seller_name, logo_url, description, approval_status,
    registration_number, sale_report_number, representative,
    business_address_line1, business_address_line2, business_zip_code,
    created_at, updated_at, deleted_at
) VALUES (
    2, 'default', 'default', '다른스토어', 'https://example.com/seller2-logo.jpg', '다른 판매자입니다', 'APPROVED',
    '987-65-43210', '2024-서울강남-54321', '김철수',
    '서울시 강남구 역삼로 456', '5층', '06235',
    NOW(), NOW(), NULL
);

-- ============================================================
-- 1. 정률 할인 정책 - 활성 상태 (ID: 1)
-- ============================================================
INSERT INTO discount_policies (
    id, seller_id, policy_name, discount_group, discount_type, target_type, target_ids,
    discount_rate, discount_amount, maximum_discount_amount, minimum_order_amount,
    valid_start_at, valid_end_at, max_usage_per_customer, max_total_usage,
    platform_cost_share_ratio, seller_cost_share_ratio, priority, is_active,
    created_at, updated_at, deleted_at
) VALUES (
    1, 1, '여름 시즌 10% 할인', 'PRODUCT', 'RATE', 'ALL', NULL,
    10.00, NULL, 10000, 30000,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, 30, CURRENT_TIMESTAMP(6)),
    1, 1000,
    50.00, 50.00, 100, TRUE,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), NULL
);

-- ============================================================
-- 2. 정액 할인 정책 - 활성 상태 (ID: 2)
-- ============================================================
INSERT INTO discount_policies (
    id, seller_id, policy_name, discount_group, discount_type, target_type, target_ids,
    discount_rate, discount_amount, maximum_discount_amount, minimum_order_amount,
    valid_start_at, valid_end_at, max_usage_per_customer, max_total_usage,
    platform_cost_share_ratio, seller_cost_share_ratio, priority, is_active,
    created_at, updated_at, deleted_at
) VALUES (
    2, 1, '5000원 즉시 할인', 'PRODUCT', 'FIXED_PRICE', 'ALL', NULL,
    NULL, 5000, NULL, 30000,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, 30, CURRENT_TIMESTAMP(6)),
    1, 500,
    50.00, 50.00, 200, TRUE,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), NULL
);

-- ============================================================
-- 3. 비활성 할인 정책 (ID: 3)
-- ============================================================
INSERT INTO discount_policies (
    id, seller_id, policy_name, discount_group, discount_type, target_type, target_ids,
    discount_rate, discount_amount, maximum_discount_amount, minimum_order_amount,
    valid_start_at, valid_end_at, max_usage_per_customer, max_total_usage,
    platform_cost_share_ratio, seller_cost_share_ratio, priority, is_active,
    created_at, updated_at, deleted_at
) VALUES (
    3, 1, '비활성 할인', 'PRODUCT', 'RATE', 'ALL', NULL,
    20.00, NULL, 20000, 50000,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, 30, CURRENT_TIMESTAMP(6)),
    2, 100,
    70.00, 30.00, 300, FALSE,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), NULL
);

-- ============================================================
-- 4. 삭제된 할인 정책 - Soft Delete (ID: 4)
-- ============================================================
INSERT INTO discount_policies (
    id, seller_id, policy_name, discount_group, discount_type, target_type, target_ids,
    discount_rate, discount_amount, maximum_discount_amount, minimum_order_amount,
    valid_start_at, valid_end_at, max_usage_per_customer, max_total_usage,
    platform_cost_share_ratio, seller_cost_share_ratio, priority, is_active,
    created_at, updated_at, deleted_at
) VALUES (
    4, 1, '삭제된 할인', 'PRODUCT', 'RATE', 'ALL', NULL,
    15.00, NULL, 15000, 40000,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, 30, CURRENT_TIMESTAMP(6)),
    1, 200,
    50.00, 50.00, 400, TRUE,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -10, CURRENT_TIMESTAMP(6))  -- Soft Delete
);

-- ============================================================
-- 5. 기간 만료된 할인 정책 (ID: 5)
-- ============================================================
INSERT INTO discount_policies (
    id, seller_id, policy_name, discount_group, discount_type, target_type, target_ids,
    discount_rate, discount_amount, maximum_discount_amount, minimum_order_amount,
    valid_start_at, valid_end_at, max_usage_per_customer, max_total_usage,
    platform_cost_share_ratio, seller_cost_share_ratio, priority, is_active,
    created_at, updated_at, deleted_at
) VALUES (
    5, 1, '기간 만료된 할인', 'PRODUCT', 'RATE', 'ALL', NULL,
    10.00, NULL, 10000, 30000,
    TIMESTAMPADD(DAY, -60, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)),  -- 이미 만료됨
    1, 1000,
    50.00, 50.00, 500, TRUE,
    TIMESTAMPADD(DAY, -60, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -60, CURRENT_TIMESTAMP(6)), NULL
);

-- ============================================================
-- 6. 다른 셀러의 할인 정책 (권한 테스트용) (ID: 6)
-- ============================================================
INSERT INTO discount_policies (
    id, seller_id, policy_name, discount_group, discount_type, target_type, target_ids,
    discount_rate, discount_amount, maximum_discount_amount, minimum_order_amount,
    valid_start_at, valid_end_at, max_usage_per_customer, max_total_usage,
    platform_cost_share_ratio, seller_cost_share_ratio, priority, is_active,
    created_at, updated_at, deleted_at
) VALUES (
    6, 2, '다른 셀러 할인', 'PRODUCT', 'RATE', 'ALL', NULL,
    15.00, NULL, 15000, 25000,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, 30, CURRENT_TIMESTAMP(6)),
    1, 500,
    60.00, 40.00, 100, TRUE,
    TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP(6)), NULL
);

-- ============================================================
-- 7. 할인 사용 이력 데이터 (할인 적용 검증용)
-- ============================================================
INSERT INTO discount_usage_histories (
    id, discount_policy_id, member_id, checkout_id, order_id,
    applied_amount, original_amount,
    platform_ratio, seller_ratio, platform_cost, seller_cost,
    used_at, created_at, updated_at
) VALUES (
    1, 1, '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    '550e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001',
    3000, 30000,
    50.00, 50.00, 1500, 1500,
    TIMESTAMPADD(DAY, -7, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -7, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -7, CURRENT_TIMESTAMP(6))
);

INSERT INTO discount_usage_histories (
    id, discount_policy_id, member_id, checkout_id, order_id,
    applied_amount, original_amount,
    platform_ratio, seller_ratio, platform_cost, seller_cost,
    used_at, created_at, updated_at
) VALUES (
    2, 2, '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    '550e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440002',
    5000, 50000,
    50.00, 50.00, 2500, 2500,
    TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6)),
    TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6)), TIMESTAMPADD(DAY, -5, CURRENT_TIMESTAMP(6))
);
