-- ============================================================
-- setof DB PK 동기화: legacy luxurydb PK와 맞추기
-- Brand, Seller(admin 제외), Category
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 1. BRAND: name 기준 매핑 → legacy PK로 변경
-- ============================================================

-- 매핑 테이블 생성
CREATE TEMPORARY TABLE brand_mapping AS
SELECT s.id as new_id, l.brand_id as legacy_id
FROM setof.brand s
JOIN luxurydb.brand l ON s.brand_name COLLATE utf8mb4_general_ci = l.BRAND_NAME COLLATE utf8mb4_general_ci;

-- product_groups.brand_id 업데이트
UPDATE setof.product_groups pg
JOIN brand_mapping bm ON pg.brand_id = bm.new_id
SET pg.brand_id = bm.legacy_id;

-- brand PK 업데이트 (충돌 방지: 먼저 음수로, 그 다음 최종값)
UPDATE setof.brand b
JOIN brand_mapping bm ON b.id = bm.new_id
SET b.id = -bm.legacy_id;

UPDATE setof.brand SET id = -id WHERE id < 0;

-- auto_increment 재설정
SELECT MAX(id) INTO @max_brand FROM setof.brand;
SET @sql = CONCAT('ALTER TABLE setof.brand AUTO_INCREMENT = ', @max_brand + 1);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

DROP TEMPORARY TABLE brand_mapping;

-- ============================================================
-- 2. SELLER: name 기준 매핑 (admin 제외) → legacy PK로 변경
-- ============================================================

CREATE TEMPORARY TABLE seller_mapping AS
SELECT s.id as new_id, l.seller_id as legacy_id
FROM setof.sellers s
JOIN luxurydb.seller l ON s.seller_name COLLATE utf8mb4_general_ci = l.SELLER_NAME COLLATE utf8mb4_general_ci
WHERE l.SELLER_NAME != 'admin';

-- 참조 테이블들 업데이트
UPDATE setof.product_groups pg
JOIN seller_mapping sm ON pg.seller_id = sm.new_id
SET pg.seller_id = sm.legacy_id;

UPDATE setof.discount_policy dp
JOIN seller_mapping sm ON dp.seller_id = sm.new_id
SET dp.seller_id = sm.legacy_id;

UPDATE setof.refund_policies rp
JOIN seller_mapping sm ON rp.seller_id = sm.new_id
SET rp.seller_id = sm.legacy_id;

UPDATE setof.seller_addresses sa
JOIN seller_mapping sm ON sa.seller_id = sm.new_id
SET sa.seller_id = sm.legacy_id;

UPDATE setof.seller_business_infos sb
JOIN seller_mapping sm ON sb.seller_id = sm.new_id
SET sb.seller_id = sm.legacy_id;

UPDATE setof.seller_cs sc
JOIN seller_mapping sm ON sc.seller_id = sm.new_id
SET sc.seller_id = sm.legacy_id;

UPDATE setof.shipping_policies sp
JOIN seller_mapping sm ON sp.seller_id = sm.new_id
SET sp.seller_id = sm.legacy_id;

-- seller PK 업데이트
UPDATE setof.sellers s
JOIN seller_mapping sm ON s.id = sm.new_id
SET s.id = -sm.legacy_id;

UPDATE setof.sellers SET id = -id WHERE id < 0;

SELECT MAX(id) INTO @max_seller FROM setof.sellers;
SET @sql = CONCAT('ALTER TABLE setof.sellers AUTO_INCREMENT = ', @max_seller + 1);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

DROP TEMPORARY TABLE seller_mapping;

-- ============================================================
-- 3. CATEGORY: depth별 재귀 매핑 → legacy PK로 변경
-- ============================================================

-- 매핑 테이블 (persistent, not temporary - depth별로 참조 필요)
CREATE TABLE IF NOT EXISTS setof.category_mapping (
  new_id BIGINT NOT NULL,
  legacy_id BIGINT NOT NULL,
  PRIMARY KEY (new_id)
);
TRUNCATE TABLE setof.category_mapping;

-- depth=1 매핑
INSERT INTO setof.category_mapping (new_id, legacy_id)
SELECT s.id, l.category_id
FROM (
  SELECT id, ROW_NUMBER() OVER (ORDER BY id) as rn
  FROM setof.category WHERE category_depth = 1
) s
JOIN (
  SELECT category_id, ROW_NUMBER() OVER (ORDER BY category_id) as rn
  FROM luxurydb.category WHERE CATEGORY_DEPTH = 1
) l ON s.rn = l.rn;

-- depth=2 매핑 (parent 매핑 활용)
INSERT INTO setof.category_mapping (new_id, legacy_id)
SELECT s.id, l.category_id
FROM (
  SELECT id, parent_category_id,
    ROW_NUMBER() OVER (PARTITION BY parent_category_id ORDER BY id) as rn
  FROM setof.category WHERE category_depth = 2
) s
JOIN setof.category_mapping pm ON s.parent_category_id = pm.new_id
JOIN (
  SELECT category_id, PARENT_CATEGORY_ID,
    ROW_NUMBER() OVER (PARTITION BY PARENT_CATEGORY_ID ORDER BY category_id) as rn
  FROM luxurydb.category WHERE CATEGORY_DEPTH = 2
) l ON pm.legacy_id = l.PARENT_CATEGORY_ID AND s.rn = l.rn;

-- depth=3 매핑
INSERT INTO setof.category_mapping (new_id, legacy_id)
SELECT s.id, l.category_id
FROM (
  SELECT id, parent_category_id,
    ROW_NUMBER() OVER (PARTITION BY parent_category_id ORDER BY id) as rn
  FROM setof.category WHERE category_depth = 3
) s
JOIN setof.category_mapping pm ON s.parent_category_id = pm.new_id
JOIN (
  SELECT category_id, PARENT_CATEGORY_ID,
    ROW_NUMBER() OVER (PARTITION BY PARENT_CATEGORY_ID ORDER BY category_id) as rn
  FROM luxurydb.category WHERE CATEGORY_DEPTH = 3
) l ON pm.legacy_id = l.PARENT_CATEGORY_ID AND s.rn = l.rn;

-- depth=4 매핑
INSERT INTO setof.category_mapping (new_id, legacy_id)
SELECT s.id, l.category_id
FROM (
  SELECT id, parent_category_id,
    ROW_NUMBER() OVER (PARTITION BY parent_category_id ORDER BY id) as rn
  FROM setof.category WHERE category_depth = 4
) s
JOIN setof.category_mapping pm ON s.parent_category_id = pm.new_id
JOIN (
  SELECT category_id, PARENT_CATEGORY_ID,
    ROW_NUMBER() OVER (PARTITION BY PARENT_CATEGORY_ID ORDER BY category_id) as rn
  FROM luxurydb.category WHERE CATEGORY_DEPTH = 4
) l ON pm.legacy_id = l.PARENT_CATEGORY_ID AND s.rn = l.rn;

-- 매핑 검증
SELECT COUNT(*) as mapped_count,
       (SELECT COUNT(*) FROM setof.category) as total_count
FROM setof.category_mapping;

-- product_groups.category_id 업데이트
UPDATE setof.product_groups pg
JOIN setof.category_mapping cm ON pg.category_id = cm.new_id
SET pg.category_id = cm.legacy_id;

-- parent_category_id 업데이트 (자기참조)
UPDATE setof.category c
JOIN setof.category_mapping cm ON c.parent_category_id = cm.new_id
SET c.parent_category_id = -cm.legacy_id;

UPDATE setof.category SET parent_category_id = -parent_category_id
WHERE parent_category_id < 0;

-- category PK 업데이트
UPDATE setof.category c
JOIN setof.category_mapping cm ON c.id = cm.new_id
SET c.id = -cm.legacy_id;

UPDATE setof.category SET id = -id WHERE id < 0;

-- path 컬럼도 업데이트 필요 (legacy path 가져오기)
UPDATE setof.category c
JOIN luxurydb.category l ON c.id = l.category_id
SET c.path = l.PATH;

-- target_group도 legacy 값으로 동기화
UPDATE setof.category c
JOIN luxurydb.category l ON c.id = l.category_id
SET c.target_group = l.TARGET_GROUP;

SELECT MAX(id) INTO @max_cat FROM setof.category;
SET @sql = CONCAT('ALTER TABLE setof.category AUTO_INCREMENT = ', @max_cat + 1);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 정리
DROP TABLE IF EXISTS setof.category_mapping;

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'PK sync completed successfully' as result;
