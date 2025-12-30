-- =============================================================================
-- V17__create_cms_item_tables.sql
-- CMS Component Items 및 Banner Items 테이블 생성
-- =============================================================================
-- 목적:
--   - cms_component_items: 컴포넌트(PRODUCT/BRAND/TAB 등)에 연결된 상품/브랜드 아이템
--   - cms_banner_items: 배너에 연결된 이미지 아이템 (슬라이드 배너 등)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. cms_component_items 테이블
-- -----------------------------------------------------------------------------
-- Legacy COMPONENT_TARGET + COMPONENT_ITEM → V2 cms_component_items
-- 용도: PRODUCT 타입 컴포넌트의 개별 상품 목록, BRAND 컴포넌트의 브랜드 목록 등
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cms_component_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    component_id BIGINT NOT NULL,
    item_type VARCHAR(30) NOT NULL COMMENT 'PRODUCT, BRAND, IMAGE, TAB 등',
    reference_id BIGINT COMMENT '참조 ID (productGroupId, brandId, categoryId 등)',
    title VARCHAR(200) COMMENT '표시 제목 (오버라이드용)',
    image_url VARCHAR(500) COMMENT '표시 이미지 URL (오버라이드용)',
    link_url VARCHAR(500) COMMENT '링크 URL',
    display_order INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    sort_type VARCHAR(30) COMMENT 'LATEST, POPULAR, SALES 등',
    extra_data TEXT COMMENT '추가 데이터 (JSON)',
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_cms_component_items_component_id (component_id),
    INDEX idx_cms_component_items_item_type (item_type),
    INDEX idx_cms_component_items_reference_id (reference_id),
    INDEX idx_cms_component_items_status (status),
    INDEX idx_cms_component_items_display_order (display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 2. cms_banner_items 테이블
-- -----------------------------------------------------------------------------
-- Legacy BANNER_ITEM → V2 cms_banner_items
-- 용도: 배너에 포함된 개별 이미지 슬라이드 아이템
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cms_banner_items (
    id BIGINT NOT NULL AUTO_INCREMENT,
    banner_id BIGINT NOT NULL,
    title VARCHAR(200),
    image_url VARCHAR(500) NOT NULL,
    link_url VARCHAR(500),
    display_order INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    display_start_date TIMESTAMP(6),
    display_end_date TIMESTAMP(6),
    image_width INT COMMENT '이미지 너비 (px)',
    image_height INT COMMENT '이미지 높이 (px)',
    alt_text VARCHAR(200) COMMENT '이미지 대체 텍스트',
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_cms_banner_items_banner_id (banner_id),
    INDEX idx_cms_banner_items_status (status),
    INDEX idx_cms_banner_items_display_order (display_order),
    INDEX idx_cms_banner_items_display_dates (display_start_date, display_end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 3. cms_component_items 레거시 데이터 마이그레이션
-- -----------------------------------------------------------------------------
-- COMPONENT_TARGET + COMPONENT_ITEM → cms_component_items
-- -----------------------------------------------------------------------------
INSERT INTO cms_component_items (
    id,
    component_id,
    item_type,
    reference_id,
    title,
    image_url,
    link_url,
    display_order,
    status,
    sort_type,
    extra_data,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    ci.COMPONENT_ITEM_ID,
    ct.COMPONENT_ID,
    'PRODUCT' AS item_type,
    ci.PRODUCT_GROUP_ID AS reference_id,
    ci.PRODUCT_DISPLAY_NAME AS title,
    ci.PRODUCT_DISPLAY_IMAGE AS image_url,
    NULL AS link_url,
    ci.DISPLAY_ORDER,
    CASE
        WHEN ci.DELETE_YN = 'Y' THEN 'DELETED'
        ELSE 'ACTIVE'
    END AS status,
    ct.SORT_TYPE,
    NULL AS extra_data,
    COALESCE(ci.INSERT_TIME, NOW()),
    COALESCE(ci.UPDATE_TIME, NOW()),
    CASE WHEN ci.DELETE_YN = 'Y' THEN NOW() ELSE NULL END
FROM COMPONENT_ITEM ci
INNER JOIN COMPONENT_TARGET ct ON ci.COMPONENT_TARGET_ID = ct.COMPONENT_TARGET_ID
WHERE NOT EXISTS (
    SELECT 1 FROM cms_component_items cci WHERE cci.id = ci.COMPONENT_ITEM_ID
)
ON DUPLICATE KEY UPDATE
    component_id = VALUES(component_id),
    item_type = VALUES(item_type),
    reference_id = VALUES(reference_id),
    title = VALUES(title),
    image_url = VALUES(image_url),
    display_order = VALUES(display_order),
    status = VALUES(status),
    sort_type = VALUES(sort_type),
    updated_at = VALUES(updated_at);

-- -----------------------------------------------------------------------------
-- 4. cms_banner_items 레거시 데이터 마이그레이션
-- -----------------------------------------------------------------------------
-- BANNER_ITEM → cms_banner_items
-- -----------------------------------------------------------------------------
INSERT INTO cms_banner_items (
    id,
    banner_id,
    title,
    image_url,
    link_url,
    display_order,
    status,
    display_start_date,
    display_end_date,
    image_width,
    image_height,
    alt_text,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    bi.BANNER_ITEM_ID,
    bi.BANNER_ID,
    bi.TITLE,
    bi.IMAGE_URL,
    bi.LINK_URL,
    bi.DISPLAY_ORDER,
    CASE
        WHEN bi.DELETE_YN = 'Y' THEN 'DELETED'
        WHEN bi.DISPLAY_YN = 'N' THEN 'INACTIVE'
        ELSE 'ACTIVE'
    END AS status,
    bi.DISPLAY_START_DATE,
    bi.DISPLAY_END_DATE,
    bi.IMAGE_WIDTH,
    bi.IMAGE_HEIGHT,
    bi.TITLE AS alt_text,
    COALESCE(bi.INSERT_TIME, NOW()),
    COALESCE(bi.UPDATE_TIME, NOW()),
    CASE WHEN bi.DELETE_YN = 'Y' THEN NOW() ELSE NULL END
FROM BANNER_ITEM bi
WHERE NOT EXISTS (
    SELECT 1 FROM cms_banner_items cbi WHERE cbi.id = bi.BANNER_ITEM_ID
)
ON DUPLICATE KEY UPDATE
    banner_id = VALUES(banner_id),
    title = VALUES(title),
    image_url = VALUES(image_url),
    link_url = VALUES(link_url),
    display_order = VALUES(display_order),
    status = VALUES(status),
    display_start_date = VALUES(display_start_date),
    display_end_date = VALUES(display_end_date),
    image_width = VALUES(image_width),
    image_height = VALUES(image_height),
    updated_at = VALUES(updated_at);

-- -----------------------------------------------------------------------------
-- 5. 마이그레이션 검증 쿼리 (주석 처리)
-- -----------------------------------------------------------------------------
-- 마이그레이션 후 아래 쿼리로 데이터 정합성 검증:
--
-- -- ComponentItem 마이그레이션 검증
-- SELECT
--     (SELECT COUNT(*) FROM COMPONENT_ITEM) AS legacy_count,
--     (SELECT COUNT(*) FROM cms_component_items) AS v2_count;
--
-- -- BannerItem 마이그레이션 검증
-- SELECT
--     (SELECT COUNT(*) FROM BANNER_ITEM) AS legacy_count,
--     (SELECT COUNT(*) FROM cms_banner_items) AS v2_count;
--
-- -- ComponentItem 샘플 확인
-- SELECT * FROM cms_component_items LIMIT 10;
--
-- -- BannerItem 샘플 확인
-- SELECT * FROM cms_banner_items LIMIT 10;
-- -----------------------------------------------------------------------------
