-- =============================================================================
-- V16__migrate_cms_legacy_data.sql
-- CMS 레거시 데이터 → V2 테이블 마이그레이션
-- =============================================================================
-- 마이그레이션 대상:
--   CONTENT → cms_contents
--   COMPONENT + 서브테이블 → cms_components (JSON 통합)
--   BANNER → cms_banners
--   GNB → cms_gnbs
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. cms_contents 마이그레이션 (CONTENT → cms_contents)
-- -----------------------------------------------------------------------------
-- 필드 매핑:
--   - displayYn (Y/N) → status (ACTIVE/INACTIVE)
--   - displayStartDate/displayEndDate → display_start_date/display_end_date
--   - insertTime/updateTime → created_at/updated_at
-- -----------------------------------------------------------------------------

INSERT INTO cms_contents (
    id,
    title,
    memo,
    image_url,
    status,
    display_start_date,
    display_end_date,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    c.CONTENT_ID,
    c.title,
    c.memo,
    c.image_url,
    CASE
        WHEN c.display_yn = 'Y' THEN 'ACTIVE'
        ELSE 'INACTIVE'
    END AS status,
    c.display_start_date,
    c.display_end_date,
    COALESCE(c.insert_time, NOW()),
    COALESCE(c.update_time, NOW()),
    NULL
FROM CONTENT c
WHERE NOT EXISTS (
    SELECT 1 FROM cms_contents cc WHERE cc.id = c.CONTENT_ID
)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    memo = VALUES(memo),
    image_url = VALUES(image_url),
    status = VALUES(status),
    display_start_date = VALUES(display_start_date),
    display_end_date = VALUES(display_end_date),
    updated_at = VALUES(updated_at);

-- -----------------------------------------------------------------------------
-- 2. cms_components 마이그레이션 (COMPONENT + 서브테이블 → cms_components)
-- -----------------------------------------------------------------------------
-- component_detail JSON 구조:
--   - BLANK: {"height": 100.0, "showLine": true}
--   - TEXT: {"content": "텍스트 내용"}
--   - TITLE: {"title1": "제목1", "title2": "제목2", "subTitle1": "", "subTitle2": ""}
--   - IMAGE: {"imageType": "SINGLE"}
--   - PRODUCT: {"listType": "GRID", "orderType": "LATEST", "badgeType": "NONE", "showFilter": false}
--   - CATEGORY: {"categoryId": 123, "listType": "GRID", "orderType": "LATEST", "badgeType": "NONE", "showFilter": false}
--   - BRAND: {}
--   - TAB: {"stickyYn": true, "tabMovingType": "SCROLL"}
-- -----------------------------------------------------------------------------

INSERT INTO cms_components (
    id,
    content_id,
    component_type,
    component_name,
    display_order,
    status,
    exposed_products,
    display_start_date,
    display_end_date,
    component_detail,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    comp.COMPONENT_ID,
    comp.content_id,
    comp.component_type,
    comp.COMPONENT_NAME,
    comp.DISPLAY_ORDER,
    CASE
        WHEN comp.display_yn = 'Y' THEN 'ACTIVE'
        ELSE 'INACTIVE'
    END AS status,
    COALESCE(comp.EXPOSED_PRODUCTS, 0),
    comp.display_start_date,
    comp.display_end_date,
    -- component_detail JSON 생성 (타입별 서브테이블 JOIN)
    CASE comp.component_type
        WHEN 'BLANK' THEN (
            SELECT JSON_OBJECT(
                'height', COALESCE(bc.height, 0.0),
                'showLine', CASE WHEN bc.line_yn = 'Y' THEN TRUE ELSE FALSE END
            )
            FROM BLANK_COMPONENT bc
            WHERE bc.component_id = comp.COMPONENT_ID
            LIMIT 1
        )
        WHEN 'TEXT' THEN (
            SELECT JSON_OBJECT(
                'content', COALESCE(tc.content, '')
            )
            FROM TEXT_COMPONENT tc
            WHERE tc.component_id = comp.COMPONENT_ID
            LIMIT 1
        )
        WHEN 'TITLE' THEN (
            SELECT JSON_OBJECT(
                'title1', COALESCE(ttc.title1, ''),
                'title2', COALESCE(ttc.title2, ''),
                'subTitle1', COALESCE(ttc.sub_title1, ''),
                'subTitle2', COALESCE(ttc.sub_title2, '')
            )
            FROM TITLE_COMPONENT ttc
            WHERE ttc.component_id = comp.COMPONENT_ID
            LIMIT 1
        )
        WHEN 'IMAGE' THEN (
            SELECT JSON_OBJECT(
                'imageType', COALESCE(ic.image_type, 'SINGLE')
            )
            FROM IMAGE_COMPONENT ic
            WHERE ic.component_id = comp.COMPONENT_ID
            LIMIT 1
        )
        WHEN 'PRODUCT' THEN JSON_OBJECT(
            'listType', COALESCE(comp.list_type, 'GRID'),
            'orderType', COALESCE(comp.order_type, 'LATEST'),
            'badgeType', COALESCE(comp.badge_type, 'NONE'),
            'showFilter', CASE WHEN comp.filter_yn = 'Y' THEN TRUE ELSE FALSE END
        )
        WHEN 'CATEGORY' THEN (
            SELECT JSON_OBJECT(
                'categoryId', COALESCE(cc.category_id, 0),
                'listType', COALESCE(comp.list_type, 'GRID'),
                'orderType', COALESCE(comp.order_type, 'LATEST'),
                'badgeType', COALESCE(comp.badge_type, 'NONE'),
                'showFilter', CASE WHEN comp.filter_yn = 'Y' THEN TRUE ELSE FALSE END
            )
            FROM CATEGORY_COMPONENT cc
            WHERE cc.COMPONENT_ID = comp.COMPONENT_ID
            LIMIT 1
        )
        WHEN 'BRAND' THEN JSON_OBJECT()
        WHEN 'TAB' THEN (
            SELECT JSON_OBJECT(
                'stickyYn', CASE WHEN tbc.sticky_yn = 'Y' THEN TRUE ELSE FALSE END,
                'tabMovingType', COALESCE(tbc.tab_moving_type, 'SCROLL')
            )
            FROM TAB_COMPONENT tbc
            WHERE tbc.COMPONENT_ID = comp.COMPONENT_ID
            LIMIT 1
        )
        ELSE JSON_OBJECT()
    END AS component_detail,
    COALESCE(comp.insert_time, NOW()),
    COALESCE(comp.update_time, NOW()),
    NULL
FROM COMPONENT comp
WHERE NOT EXISTS (
    SELECT 1 FROM cms_components cmc WHERE cmc.id = comp.COMPONENT_ID
)
ON DUPLICATE KEY UPDATE
    content_id = VALUES(content_id),
    component_type = VALUES(component_type),
    component_name = VALUES(component_name),
    display_order = VALUES(display_order),
    status = VALUES(status),
    exposed_products = VALUES(exposed_products),
    display_start_date = VALUES(display_start_date),
    display_end_date = VALUES(display_end_date),
    component_detail = VALUES(component_detail),
    updated_at = VALUES(updated_at);

-- -----------------------------------------------------------------------------
-- 3. cms_banners 마이그레이션 (BANNER → cms_banners)
-- -----------------------------------------------------------------------------
-- 필드 매핑:
--   - displayYn (Y/N) → status (ACTIVE/INACTIVE)
--   - bannerType → banner_type
-- 참고: 레거시 Banner 테이블에 imageUrl, linkUrl이 있지만
--       V2에서는 Banner/BannerItem으로 분리됨 (추후 확장)
-- -----------------------------------------------------------------------------

INSERT INTO cms_banners (
    id,
    title,
    banner_type,
    status,
    display_start_date,
    display_end_date,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    b.BANNER_ID,
    b.TITLE,
    b.banner_type,
    CASE
        WHEN b.display_yn = 'Y' THEN 'ACTIVE'
        ELSE 'INACTIVE'
    END AS status,
    b.display_start_date,
    b.display_end_date,
    COALESCE(b.insert_time, NOW()),
    COALESCE(b.update_time, NOW()),
    NULL
FROM Banner b
WHERE NOT EXISTS (
    SELECT 1 FROM cms_banners cb WHERE cb.id = b.BANNER_ID
)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    banner_type = VALUES(banner_type),
    status = VALUES(status),
    display_start_date = VALUES(display_start_date),
    display_end_date = VALUES(display_end_date),
    updated_at = VALUES(updated_at);

-- -----------------------------------------------------------------------------
-- 4. cms_gnbs 마이그레이션 (GNB → cms_gnbs)
-- -----------------------------------------------------------------------------
-- 필드 매핑:
--   - GnbDetails.displayYn (Y/N) → status (ACTIVE/INACTIVE)
--   - GnbDetails.title → title
--   - GnbDetails.linkUrl → link_url
--   - GnbDetails.displayOrder → display_order
-- -----------------------------------------------------------------------------

INSERT INTO cms_gnbs (
    id,
    title,
    link_url,
    display_order,
    status,
    display_start_date,
    display_end_date,
    created_at,
    updated_at,
    deleted_at
)
SELECT
    g.GNB_ID,
    g.TITLE,
    g.LINK_URL,
    COALESCE(g.DISPLAY_ORDER, 0),
    CASE
        WHEN g.display_yn = 'Y' THEN 'ACTIVE'
        ELSE 'INACTIVE'
    END AS status,
    g.display_start_date,
    g.display_end_date,
    COALESCE(g.insert_time, NOW()),
    COALESCE(g.update_time, NOW()),
    NULL
FROM GNB g
WHERE NOT EXISTS (
    SELECT 1 FROM cms_gnbs cg WHERE cg.id = g.GNB_ID
)
ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    link_url = VALUES(link_url),
    display_order = VALUES(display_order),
    status = VALUES(status),
    display_start_date = VALUES(display_start_date),
    display_end_date = VALUES(display_end_date),
    updated_at = VALUES(updated_at);

-- -----------------------------------------------------------------------------
-- 5. 마이그레이션 검증 쿼리 (주석 처리)
-- -----------------------------------------------------------------------------
-- 마이그레이션 후 아래 쿼리로 데이터 정합성 검증:
--
-- -- Content 마이그레이션 검증
-- SELECT
--     (SELECT COUNT(*) FROM CONTENT) AS legacy_count,
--     (SELECT COUNT(*) FROM cms_contents) AS v2_count;
--
-- -- Component 마이그레이션 검증
-- SELECT
--     (SELECT COUNT(*) FROM COMPONENT) AS legacy_count,
--     (SELECT COUNT(*) FROM cms_components) AS v2_count;
--
-- -- Banner 마이그레이션 검증
-- SELECT
--     (SELECT COUNT(*) FROM Banner) AS legacy_count,
--     (SELECT COUNT(*) FROM cms_banners) AS v2_count;
--
-- -- GNB 마이그레이션 검증
-- SELECT
--     (SELECT COUNT(*) FROM GNB) AS legacy_count,
--     (SELECT COUNT(*) FROM cms_gnbs) AS v2_count;
--
-- -- Component Detail JSON 샘플 확인
-- SELECT
--     id,
--     component_type,
--     component_detail
-- FROM cms_components
-- LIMIT 10;
-- -----------------------------------------------------------------------------
