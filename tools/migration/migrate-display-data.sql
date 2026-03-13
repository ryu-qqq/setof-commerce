-- ============================================================
-- 1회성 마이그레이션: luxurydb → setof (Display 도메인)
-- 대상: banner, gnb, content + components
-- 실행: Stage DB 포트포워딩 후 실행
-- ============================================================
-- 사용법:
--   /opt/homebrew/opt/mysql-client/bin/mysql -h 127.0.0.1 -P 13308 -u admin -p'PASSWORD' < tools/migration/migrate-display-data.sql
-- ============================================================

SET @tz = 'Asia/Seoul';

-- ============================================================
-- 1. BANNER_GROUP (luxurydb.banner → setof.banner_group)
-- ============================================================
INSERT INTO setof.banner_group (id, title, banner_type, display_start_at, display_end_at, active, created_at, updated_at, deleted_at)
SELECT
    b.banner_id,
    b.title,
    b.banner_type,
    CONVERT_TZ(b.display_start_date, @tz, '+00:00'),
    CONVERT_TZ(b.display_end_date, @tz, '+00:00'),
    CASE WHEN b.display_yn = 'Y' THEN 1 ELSE 0 END,
    CONVERT_TZ(b.insert_date, @tz, '+00:00'),
    CONVERT_TZ(b.update_date, @tz, '+00:00'),
    CASE WHEN b.delete_yn = 'Y' THEN CONVERT_TZ(b.update_date, @tz, '+00:00') ELSE NULL END
FROM luxurydb.banner b
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- ============================================================
-- 2. BANNER_SLIDE (luxurydb.banner_item → setof.banner_slide)
-- ============================================================
INSERT INTO setof.banner_slide (id, banner_group_id, title, image_url, link_url, display_order, display_start_at, display_end_at, active, created_at, updated_at, deleted_at)
SELECT
    bi.banner_item_id,
    bi.banner_id,
    bi.title,
    bi.image_url,
    bi.link_url,
    bi.display_order,
    CONVERT_TZ(bi.display_start_date, @tz, '+00:00'),
    CONVERT_TZ(bi.display_end_date, @tz, '+00:00'),
    CASE WHEN bi.display_yn = 'Y' THEN 1 ELSE 0 END,
    CONVERT_TZ(bi.insert_date, @tz, '+00:00'),
    CONVERT_TZ(bi.update_date, @tz, '+00:00'),
    CASE WHEN bi.delete_yn = 'Y' THEN CONVERT_TZ(bi.update_date, @tz, '+00:00') ELSE NULL END
FROM luxurydb.banner_item bi
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- ============================================================
-- 3. NAVIGATION_MENU (luxurydb.gnb → setof.navigation_menu)
-- ============================================================
INSERT INTO setof.navigation_menu (id, title, link_url, display_order, display_start_at, display_end_at, active, created_at, updated_at, deleted_at)
SELECT
    g.gnb_id,
    g.title,
    g.link_url,
    g.display_order,
    CONVERT_TZ(g.display_start_date, @tz, '+00:00'),
    CONVERT_TZ(g.display_end_date, @tz, '+00:00'),
    CASE WHEN g.display_yn = 'Y' THEN 1 ELSE 0 END,
    CONVERT_TZ(g.insert_date, @tz, '+00:00'),
    CONVERT_TZ(g.update_date, @tz, '+00:00'),
    CASE WHEN g.delete_yn = 'Y' THEN CONVERT_TZ(g.update_date, @tz, '+00:00') ELSE NULL END
FROM luxurydb.gnb g
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- ============================================================
-- 4. CONTENT_PAGE (luxurydb.content → setof.content_page)
-- ============================================================
INSERT INTO setof.content_page (id, title, memo, image_url, display_start_at, display_end_at, active, created_at, updated_at, deleted_at)
SELECT
    c.content_id,
    c.title,
    c.memo,
    c.image_url,
    CONVERT_TZ(c.display_start_date, @tz, '+00:00'),
    CONVERT_TZ(c.display_end_date, @tz, '+00:00'),
    CASE WHEN c.display_yn = 'Y' THEN 1 ELSE 0 END,
    CONVERT_TZ(c.insert_date, @tz, '+00:00'),
    CONVERT_TZ(c.update_date, @tz, '+00:00'),
    CASE WHEN c.delete_yn = 'Y' THEN CONVERT_TZ(c.update_date, @tz, '+00:00') ELSE NULL END
FROM luxurydb.content c
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- ============================================================
-- 5. DISPLAY_COMPONENT (luxurydb.component + view_extension + spec → setof.display_component)
-- ============================================================
-- 임시 테이블: component + view_extension JOIN
DROP TEMPORARY TABLE IF EXISTS tmp_component;
CREATE TEMPORARY TABLE tmp_component AS
SELECT
    cp.component_id,
    cp.content_id,
    cp.component_name,
    cp.component_type,
    cp.list_type,
    cp.order_type,
    cp.badge_type,
    CASE WHEN cp.filter_yn = 'Y' THEN 1 ELSE 0 END AS filter_enabled,
    IFNULL(cp.exposed_products, 0) AS exposed_products,
    cp.display_order,
    CONVERT_TZ(cp.display_start_date, @tz, '+00:00') AS display_start_at,
    CONVERT_TZ(cp.display_end_date, @tz, '+00:00') AS display_end_at,
    CASE WHEN cp.display_yn = 'Y' THEN 1 ELSE 0 END AS active,
    -- view extension 인라인
    ve.view_extension_type,
    ve.link_url AS ve_link_url,
    ve.button_name AS ve_button_name,
    ve.product_count_per_click AS ve_product_count_per_click,
    ve.max_click_count AS ve_max_click_count,
    ve.after_max_action_type AS ve_after_max_action_type,
    ve.after_max_action_link_url AS ve_after_max_action_link_url,
    -- audit
    CONVERT_TZ(cp.insert_date, @tz, '+00:00') AS created_at,
    CONVERT_TZ(cp.update_date, @tz, '+00:00') AS updated_at,
    CASE WHEN cp.delete_yn = 'Y' THEN CONVERT_TZ(cp.update_date, @tz, '+00:00') ELSE NULL END AS deleted_at
FROM luxurydb.component cp
LEFT JOIN luxurydb.view_extension ve ON cp.view_extension_id = ve.view_extension_id;

-- spec_data JSON 생성을 위한 임시 테이블
DROP TEMPORARY TABLE IF EXISTS tmp_spec_data;
CREATE TEMPORARY TABLE tmp_spec_data (
    component_id BIGINT NOT NULL PRIMARY KEY,
    spec_data JSON NOT NULL
);

-- TEXT spec
INSERT INTO tmp_spec_data (component_id, spec_data)
SELECT tc.component_id, JSON_OBJECT('content', tc.content)
FROM luxurydb.text_component tc
WHERE tc.delete_yn = 'N'
ON DUPLICATE KEY UPDATE spec_data = VALUES(spec_data);

-- TITLE spec
INSERT INTO tmp_spec_data (component_id, spec_data)
SELECT tc.component_id, JSON_OBJECT('title1', tc.title1, 'title2', tc.title2, 'subTitle1', tc.sub_title1, 'subTitle2', tc.sub_title2)
FROM luxurydb.title_component tc
WHERE tc.delete_yn = 'N'
ON DUPLICATE KEY UPDATE spec_data = VALUES(spec_data);

-- BLANK spec
INSERT INTO tmp_spec_data (component_id, spec_data)
SELECT bc.component_id, JSON_OBJECT('height', bc.height, 'showLine', CASE WHEN bc.line_yn = 'Y' THEN TRUE ELSE FALSE END)
FROM luxurydb.blank_component bc
WHERE bc.delete_yn = 'N'
ON DUPLICATE KEY UPDATE spec_data = VALUES(spec_data);

-- IMAGE spec (image_component + items → JSON)
INSERT INTO tmp_spec_data (component_id, spec_data)
SELECT
    ic.component_id,
    JSON_OBJECT(
        'imageType', ic.image_type,
        'slides', IFNULL((
            SELECT JSON_ARRAYAGG(
                JSON_OBJECT(
                    'displayOrder', ici.display_order,
                    'imageUrl', ici.image_url,
                    'linkUrl', ici.link_url
                )
            )
            FROM luxurydb.image_component_item ici
            WHERE ici.image_component_id = ic.image_component_id
              AND ici.delete_yn = 'N'
        ), JSON_ARRAY())
    )
FROM luxurydb.image_component ic
WHERE ic.delete_yn = 'N'
ON DUPLICATE KEY UPDATE spec_data = VALUES(spec_data);

-- CATEGORY spec
INSERT INTO tmp_spec_data (component_id, spec_data)
SELECT cc.component_id, JSON_OBJECT('categoryId', cc.category_id)
FROM luxurydb.category_component cc
ON DUPLICATE KEY UPDATE spec_data = VALUES(spec_data);

-- PRODUCT spec (빈 JSON - 상품은 component_fixed_product로 분리)
INSERT INTO tmp_spec_data (component_id, spec_data)
SELECT pc.component_id, CAST('{}' AS JSON)
FROM luxurydb.product_component pc
ON DUPLICATE KEY UPDATE spec_data = VALUES(spec_data);

-- BRAND spec (brand_component + brand_component_item → brandFilters JSON)
INSERT INTO tmp_spec_data (component_id, spec_data)
SELECT
    bc.component_id,
    JSON_OBJECT(
        'categoryId', IFNULL((SELECT bci.category_id FROM luxurydb.brand_component_item bci WHERE bci.brand_component_id = bc.brand_component_id AND bci.delete_yn = 'N' LIMIT 1), 0),
        'brandFilters', IFNULL((
            SELECT JSON_ARRAYAGG(
                JSON_OBJECT('brandId', bci.brand_id, 'brandName', IFNULL(b.brand_name, ''))
            )
            FROM luxurydb.brand_component_item bci
            LEFT JOIN luxurydb.brand b ON b.brand_id = bci.brand_id
            WHERE bci.brand_component_id = bc.brand_component_id
              AND bci.delete_yn = 'N'
        ), JSON_ARRAY())
    )
FROM luxurydb.brand_component bc
ON DUPLICATE KEY UPDATE spec_data = VALUES(spec_data);

-- TAB spec (tab_component → sticky/movingType JSON)
INSERT INTO tmp_spec_data (component_id, spec_data)
SELECT
    tc.component_id,
    JSON_OBJECT(
        'sticky', CASE WHEN tc.sticky_yn = 'Y' THEN TRUE ELSE FALSE END,
        'movingType', IFNULL(tc.tab_moving_type, 'SCROLL')
    )
FROM luxurydb.tab_component tc
ON DUPLICATE KEY UPDATE spec_data = VALUES(spec_data);

-- display_component INSERT (component + spec_data 결합)
INSERT INTO setof.display_component (
    id, content_page_id, name, component_type, display_order,
    list_type, order_type, badge_type, filter_enabled, exposed_products,
    display_start_at, display_end_at, active,
    view_extension_type, view_extension_link_url, view_extension_button_name,
    view_extension_product_count_per_click, view_extension_max_click_count,
    view_extension_after_max_action_type, view_extension_after_max_action_link_url,
    spec_data,
    created_at, updated_at, deleted_at
)
SELECT
    t.component_id,
    t.content_id,
    t.component_name,
    t.component_type,
    t.display_order,
    IFNULL(t.list_type, 'NONE'),
    IFNULL(t.order_type, 'NONE'),
    IFNULL(t.badge_type, 'NONE'),
    t.filter_enabled,
    t.exposed_products,
    t.display_start_at,
    t.display_end_at,
    t.active,
    t.view_extension_type,
    t.ve_link_url,
    t.ve_button_name,
    t.ve_product_count_per_click,
    t.ve_max_click_count,
    t.ve_after_max_action_type,
    t.ve_after_max_action_link_url,
    IFNULL(sd.spec_data, CAST('{}' AS JSON)),
    t.created_at,
    t.updated_at,
    t.deleted_at
FROM tmp_component t
LEFT JOIN tmp_spec_data sd ON sd.component_id = t.component_id
ON DUPLICATE KEY UPDATE name = VALUES(name);

DROP TEMPORARY TABLE IF EXISTS tmp_component;
DROP TEMPORARY TABLE IF EXISTS tmp_spec_data;

-- ============================================================
-- 6. DISPLAY_TAB (luxurydb.tab → setof.display_tab)
-- ============================================================
INSERT INTO setof.display_tab (id, component_id, tab_name, display_order, created_at, deleted_at)
SELECT
    t.tab_id,
    tc.component_id,
    t.tab_name,
    t.display_order,
    NOW(6),
    CASE WHEN t.delete_yn = 'Y' THEN NOW(6) ELSE NULL END
FROM luxurydb.tab t
INNER JOIN luxurydb.tab_component tc ON tc.tab_component_id = t.tab_component_id
ON DUPLICATE KEY UPDATE tab_name = VALUES(tab_name);

-- ============================================================
-- 7. COMPONENT_FIXED_PRODUCT (luxurydb.component_target + component_item → setof.component_fixed_product)
-- ============================================================
-- component_target.tab_id = 0 → tab_id NULL (컴포넌트 레벨)
-- component_target.tab_id != 0 → tab_id = tab_id (탭 레벨)
INSERT INTO setof.component_fixed_product (component_id, tab_id, product_group_id, display_order, deleted_at)
SELECT
    ct.component_id,
    CASE WHEN ct.tab_id = 0 OR ct.tab_id IS NULL THEN NULL ELSE ct.tab_id END,
    ci.product_group_id,
    ci.display_order,
    CASE WHEN ci.delete_yn = 'Y' OR ct.delete_yn = 'Y' THEN NOW(6) ELSE NULL END
FROM luxurydb.component_target ct
INNER JOIN luxurydb.component_item ci ON ci.component_target_id = ct.component_target_id
ON DUPLICATE KEY UPDATE display_order = VALUES(display_order);

-- ============================================================
-- 검증 쿼리
-- ============================================================
SELECT 'banner_group' AS tbl, COUNT(*) AS cnt FROM setof.banner_group
UNION ALL SELECT 'banner_slide', COUNT(*) FROM setof.banner_slide
UNION ALL SELECT 'navigation_menu', COUNT(*) FROM setof.navigation_menu
UNION ALL SELECT 'content_page', COUNT(*) FROM setof.content_page
UNION ALL SELECT 'display_component', COUNT(*) FROM setof.display_component
UNION ALL SELECT 'display_tab', COUNT(*) FROM setof.display_tab
UNION ALL SELECT 'component_fixed_product', COUNT(*) FROM setof.component_fixed_product;
