-- ============================================================
-- Category Integration Test Data
-- ============================================================
-- 카테고리 통합 테스트용 샘플 데이터 (계층 구조)
-- ============================================================

-- 기존 데이터 삭제 (테스트 격리 - 하위 카테고리부터 삭제)
DELETE FROM category WHERE id IN (6, 7, 8);
DELETE FROM category WHERE id IN (3, 4, 5);
DELETE FROM category WHERE id IN (1, 2);

-- 대분류 (depth = 0) - Path Enumeration 형식: "id" 또는 "parent_id,id"
INSERT INTO category (id, code, name_ko, parent_id, depth, path, sort_order, is_leaf, status, created_at, updated_at)
VALUES
    (1, 'FASHION', '패션', NULL, 0, '1', 1, false, 'ACTIVE', NOW(), NOW()),
    (2, 'ELECTRONICS', '전자기기', NULL, 0, '2', 2, false, 'ACTIVE', NOW(), NOW());

-- 중분류 (depth = 1)
INSERT INTO category (id, code, name_ko, parent_id, depth, path, sort_order, is_leaf, status, created_at, updated_at)
VALUES
    (3, 'FASHION_MEN', '남성의류', 1, 1, '1,3', 1, false, 'ACTIVE', NOW(), NOW()),
    (4, 'FASHION_WOMEN', '여성의류', 1, 1, '1,4', 2, false, 'ACTIVE', NOW(), NOW()),
    (5, 'ELECTRONICS_MOBILE', '모바일', 2, 1, '2,5', 1, true, 'ACTIVE', NOW(), NOW());

-- 소분류 (depth = 2)
INSERT INTO category (id, code, name_ko, parent_id, depth, path, sort_order, is_leaf, status, created_at, updated_at)
VALUES
    (6, 'FASHION_MEN_TOP', '상의', 3, 2, '1,3,6', 1, true, 'ACTIVE', NOW(), NOW()),
    (7, 'FASHION_MEN_BOTTOM', '하의', 3, 2, '1,3,7', 2, true, 'ACTIVE', NOW(), NOW()),
    (8, 'FASHION_WOMEN_TOP', '상의', 4, 2, '1,4,8', 1, true, 'ACTIVE', NOW(), NOW());
