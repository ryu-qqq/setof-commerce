-- Category 테스트 데이터
-- 사용처: CategoryApiIntegrationTest

-- 1. 테이블 생성 (없으면 생성)
CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name_ko VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    depth INT NOT NULL,
    path VARCHAR(500) NOT NULL,
    sort_order INT NOT NULL,
    is_leaf BOOLEAN NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. 기존 데이터 정리
DELETE FROM category;

-- 2. 테스트 데이터 삽입
-- 계층 구조: 패션(1) > 의류(2) > 상의(3), 하의(4)
--            전자제품(5) > 컴퓨터(6) > 노트북(7)
--            비활성(99)

INSERT INTO category (id, code, name_ko, parent_id, depth, path, sort_order, is_leaf, status, created_at, updated_at)
VALUES
    -- 최상위 카테고리 (depth = 0)
    (1, 'FASHION', '패션', NULL, 0, '/1/', 1, false, 'ACTIVE', NOW(), NOW()),
    (5, 'ELECTRONICS', '전자제품', NULL, 0, '/5/', 2, false, 'ACTIVE', NOW(), NOW()),
    (99, 'INACTIVE', '비활성카테고리', NULL, 0, '/99/', 99, true, 'INACTIVE', NOW(), NOW()),

    -- 중분류 (depth = 1)
    (2, 'CLOTHING', '의류', 1, 1, '/1/2/', 1, false, 'ACTIVE', NOW(), NOW()),
    (6, 'COMPUTERS', '컴퓨터', 5, 1, '/5/6/', 1, false, 'ACTIVE', NOW(), NOW()),
    (10, 'SHOES', '신발', 1, 1, '/1/10/', 2, true, 'ACTIVE', NOW(), NOW()),

    -- 소분류 (depth = 2, leaf)
    (3, 'TOPS', '상의', 2, 2, '/1/2/3/', 1, true, 'ACTIVE', NOW(), NOW()),
    (4, 'BOTTOMS', '하의', 2, 2, '/1/2/4/', 2, true, 'ACTIVE', NOW(), NOW()),
    (7, 'LAPTOP', '노트북', 6, 2, '/5/6/7/', 1, true, 'ACTIVE', NOW(), NOW()),
    (8, 'DESKTOP', '데스크탑', 6, 2, '/5/6/8/', 2, true, 'ACTIVE', NOW(), NOW());
