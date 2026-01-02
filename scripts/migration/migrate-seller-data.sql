-- =============================================================================
-- Seller Data Migration Script
-- FROM: luxurydb (SELLER, SELLER_BUSINESS_INFO, SELLER_SHIPPING_INFO, PRODUCT_DELIVERY)
-- TO:   setof (sellers, seller_cs_infos, seller_refund_policies, seller_shipping_policies)
-- =============================================================================
-- 실행 순서: 1 → 2 → 3 → 4
-- 주의: 각 단계별 데이터 검증 후 다음 단계 진행 권장
-- =============================================================================

-- =============================================================================
-- 1. sellers 테이블 마이그레이션
-- FROM: SELLER + SELLER_BUSINESS_INFO
-- =============================================================================
INSERT INTO setof.sellers (
    id,
    tenant_id,
    organization_id,
    seller_name,
    logo_url,
    description,
    approval_status,
    registration_number,
    sale_report_number,
    representative,
    business_address_line1,
    business_address_line2,
    business_zip_code,
    created_at,
    updated_at,
    deleted
)
SELECT
    s.ID AS id,
    'default' AS tenant_id,
    'default' AS organization_id,
    s.SELLER_NAME AS seller_name,
    s.SELLER_LOGO_URL AS logo_url,
    s.SELLER_DESCRIPTION AS description,
    'APPROVED' AS approval_status,
    bi.REGISTRATION_NUMBER AS registration_number,
    bi.SALE_REPORT_NUMBER AS sale_report_number,
    bi.REPRESENTATIVE AS representative,
    bi.BUSINESS_ADDRESS_LINE1 AS business_address_line1,
    bi.BUSINESS_ADDRESS_LINE2 AS business_address_line2,
    bi.BUSINESS_ZIP_CODE AS business_zip_code,
    COALESCE(s.CREATED_AT, NOW()) AS created_at,
    COALESCE(s.UPDATED_AT, NOW()) AS updated_at,
    FALSE AS deleted
FROM luxurydb.SELLER s
LEFT JOIN luxurydb.SELLER_BUSINESS_INFO bi ON s.ID = bi.SELLER_ID
ON DUPLICATE KEY UPDATE
    seller_name = VALUES(seller_name),
    logo_url = VALUES(logo_url),
    description = VALUES(description),
    registration_number = VALUES(registration_number),
    sale_report_number = VALUES(sale_report_number),
    representative = VALUES(representative),
    business_address_line1 = VALUES(business_address_line1),
    business_address_line2 = VALUES(business_address_line2),
    business_zip_code = VALUES(business_zip_code),
    updated_at = NOW();

-- 결과 확인
SELECT COUNT(*) AS migrated_sellers FROM setof.sellers;


-- =============================================================================
-- 2. seller_cs_infos 테이블 마이그레이션
-- FROM: SELLER_BUSINESS_INFO (CS 관련 필드)
-- =============================================================================
INSERT INTO setof.seller_cs_infos (
    seller_id,
    email,
    mobile_phone,
    landline_phone,
    created_at,
    updated_at,
    deleted
)
SELECT
    bi.SELLER_ID AS seller_id,
    bi.CS_EMAIL AS email,
    bi.CS_PHONE_NUMBER AS mobile_phone,
    bi.CS_NUMBER AS landline_phone,
    COALESCE(bi.CREATED_AT, NOW()) AS created_at,
    COALESCE(bi.UPDATED_AT, NOW()) AS updated_at,
    FALSE AS deleted
FROM luxurydb.SELLER_BUSINESS_INFO bi
WHERE bi.SELLER_ID IS NOT NULL
ON DUPLICATE KEY UPDATE
    email = VALUES(email),
    mobile_phone = VALUES(mobile_phone),
    landline_phone = VALUES(landline_phone),
    updated_at = NOW();

-- 결과 확인
SELECT COUNT(*) AS migrated_cs_infos FROM setof.seller_cs_infos;


-- =============================================================================
-- 3. seller_refund_policies 테이블 마이그레이션
-- FROM: SELLER_SHIPPING_INFO + PRODUCT_DELIVERY (가장 최신 데이터)
-- =============================================================================
INSERT INTO setof.seller_refund_policies (
    seller_id,
    policy_name,
    return_address_line1,
    return_address_line2,
    return_zip_code,
    refund_period_days,
    refund_delivery_cost,
    refund_guide,
    is_default,
    display_order,
    created_at,
    updated_at,
    deleted
)
SELECT
    si.SELLER_ID AS seller_id,
    '기본 환불 정책' AS policy_name,
    si.RETURN_ADDRESS_LINE1 AS return_address_line1,
    si.RETURN_ADDRESS_LINE2 AS return_address_line2,
    si.RETURN_ZIP_CODE AS return_zip_code,
    7 AS refund_period_days,  -- 기본값 7일
    COALESCE(pd.RETURN_CHARGE_DOMESTIC, 0) AS refund_delivery_cost,
    CONCAT(
        '반품 택배사: ', COALESCE(pd.RETURN_COURIER_DOMESTIC, 'N/A'), '\n',
        '반품 방법: ', COALESCE(pd.RETURN_METHOD_DOMESTIC, 'N/A'), '\n',
        '교환/반품 지역: ', COALESCE(pd.RETURN_EXCHANGE_AREA_DOMESTIC, 'N/A')
    ) AS refund_guide,
    TRUE AS is_default,
    0 AS display_order,
    NOW() AS created_at,
    NOW() AS updated_at,
    FALSE AS deleted
FROM luxurydb.SELLER_SHIPPING_INFO si
LEFT JOIN (
    -- PRODUCT_DELIVERY에서 seller_id별 가장 최신 데이터 선택
    SELECT
        pg.SELLER_ID,
        pd.RETURN_COURIER_DOMESTIC,
        pd.RETURN_METHOD_DOMESTIC,
        pd.RETURN_CHARGE_DOMESTIC,
        pd.RETURN_EXCHANGE_AREA_DOMESTIC
    FROM luxurydb.PRODUCT_DELIVERY pd
    INNER JOIN luxurydb.PRODUCT_GROUP pg ON pd.PRODUCT_GROUP_ID = pg.ID
    INNER JOIN (
        SELECT pg2.SELLER_ID, MAX(pd2.ID) AS max_pd_id
        FROM luxurydb.PRODUCT_DELIVERY pd2
        INNER JOIN luxurydb.PRODUCT_GROUP pg2 ON pd2.PRODUCT_GROUP_ID = pg2.ID
        GROUP BY pg2.SELLER_ID
    ) latest ON pg.SELLER_ID = latest.SELLER_ID AND pd.ID = latest.max_pd_id
) pd ON si.SELLER_ID = pd.SELLER_ID
WHERE si.SELLER_ID IS NOT NULL
ON DUPLICATE KEY UPDATE
    return_address_line1 = VALUES(return_address_line1),
    return_address_line2 = VALUES(return_address_line2),
    return_zip_code = VALUES(return_zip_code),
    refund_delivery_cost = VALUES(refund_delivery_cost),
    refund_guide = VALUES(refund_guide),
    updated_at = NOW();

-- 결과 확인
SELECT COUNT(*) AS migrated_refund_policies FROM setof.seller_refund_policies;


-- =============================================================================
-- 4. seller_shipping_policies 테이블 마이그레이션
-- FROM: PRODUCT_DELIVERY (seller_id별 GROUP BY, 가장 최신 데이터)
-- =============================================================================
INSERT INTO setof.seller_shipping_policies (
    seller_id,
    policy_name,
    default_delivery_cost,
    free_shipping_threshold,
    delivery_guide,
    is_default,
    display_order,
    created_at,
    updated_at,
    deleted
)
SELECT
    pg.SELLER_ID AS seller_id,
    '기본 배송 정책' AS policy_name,
    pd.DELIVERY_FEE AS default_delivery_cost,
    0 AS free_shipping_threshold,  -- 기본값: 무료배송 기준 없음
    CONCAT(
        '배송 지역: ', COALESCE(pd.DELIVERY_AREA, '전국'), '\n',
        '평균 배송 기간: ', COALESCE(pd.DELIVERY_PERIOD_AVERAGE, 3), '일'
    ) AS delivery_guide,
    TRUE AS is_default,
    0 AS display_order,
    NOW() AS created_at,
    NOW() AS updated_at,
    FALSE AS deleted
FROM luxurydb.PRODUCT_DELIVERY pd
INNER JOIN luxurydb.PRODUCT_GROUP pg ON pd.PRODUCT_GROUP_ID = pg.ID
INNER JOIN (
    -- seller_id별 가장 최신 PRODUCT_DELIVERY 선택 (ID가 가장 큰 것)
    SELECT pg2.SELLER_ID, MAX(pd2.ID) AS max_pd_id
    FROM luxurydb.PRODUCT_DELIVERY pd2
    INNER JOIN luxurydb.PRODUCT_GROUP pg2 ON pd2.PRODUCT_GROUP_ID = pg2.ID
    WHERE pg2.SELLER_ID IS NOT NULL
    GROUP BY pg2.SELLER_ID
) latest ON pg.SELLER_ID = latest.SELLER_ID AND pd.ID = latest.max_pd_id
WHERE pg.SELLER_ID IS NOT NULL
ON DUPLICATE KEY UPDATE
    default_delivery_cost = VALUES(default_delivery_cost),
    delivery_guide = VALUES(delivery_guide),
    updated_at = NOW();

-- 결과 확인
SELECT COUNT(*) AS migrated_shipping_policies FROM setof.seller_shipping_policies;


-- =============================================================================
-- 마이그레이션 검증 쿼리
-- =============================================================================

-- 전체 마이그레이션 현황
SELECT
    'sellers' AS table_name,
    (SELECT COUNT(*) FROM luxurydb.SELLER) AS legacy_count,
    (SELECT COUNT(*) FROM setof.sellers) AS new_count
UNION ALL
SELECT
    'seller_cs_infos' AS table_name,
    (SELECT COUNT(*) FROM luxurydb.SELLER_BUSINESS_INFO) AS legacy_count,
    (SELECT COUNT(*) FROM setof.seller_cs_infos) AS new_count
UNION ALL
SELECT
    'seller_refund_policies' AS table_name,
    (SELECT COUNT(*) FROM luxurydb.SELLER_SHIPPING_INFO) AS legacy_count,
    (SELECT COUNT(*) FROM setof.seller_refund_policies) AS new_count
UNION ALL
SELECT
    'seller_shipping_policies' AS table_name,
    (SELECT COUNT(DISTINCT pg.SELLER_ID) FROM luxurydb.PRODUCT_DELIVERY pd
     INNER JOIN luxurydb.PRODUCT_GROUP pg ON pd.PRODUCT_GROUP_ID = pg.ID
     WHERE pg.SELLER_ID IS NOT NULL) AS legacy_count,
    (SELECT COUNT(*) FROM setof.seller_shipping_policies) AS new_count;

-- 데이터 샘플 확인
SELECT
    s.id,
    s.seller_name,
    s.approval_status,
    cs.email,
    rp.return_address_line1,
    sp.default_delivery_cost
FROM setof.sellers s
LEFT JOIN setof.seller_cs_infos cs ON s.id = cs.seller_id
LEFT JOIN setof.seller_refund_policies rp ON s.id = rp.seller_id
LEFT JOIN setof.seller_shipping_policies sp ON s.id = sp.seller_id
LIMIT 10;
