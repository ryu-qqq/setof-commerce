-- Brand 테스트 데이터
-- 사용처: BrandApiIntegrationTest

-- 1. 기존 데이터 정리
DELETE FROM brand;

-- 2. 테스트 데이터 삽입
INSERT INTO brand (id, code, name_ko, name_en, logo_url, status, created_at, updated_at)
VALUES
    (1, 'NIKE', '나이키', 'Nike', 'https://cdn.example.com/brand/nike.png', 'ACTIVE', NOW(), NOW()),
    (2, 'ADIDAS', '아디다스', 'Adidas', 'https://cdn.example.com/brand/adidas.png', 'ACTIVE', NOW(), NOW()),
    (3, 'PUMA', '푸마', 'Puma', 'https://cdn.example.com/brand/puma.png', 'ACTIVE', NOW(), NOW()),
    (4, 'REEBOK', '리복', 'Reebok', 'https://cdn.example.com/brand/reebok.png', 'ACTIVE', NOW(), NOW()),
    (5, 'NEWBALANCE', '뉴발란스', 'New Balance', 'https://cdn.example.com/brand/newbalance.png', 'ACTIVE', NOW(), NOW()),
    (6, 'CONVERSE', '컨버스', 'Converse', 'https://cdn.example.com/brand/converse.png', 'ACTIVE', NOW(), NOW()),
    (7, 'VANS', '반스', 'Vans', 'https://cdn.example.com/brand/vans.png', 'ACTIVE', NOW(), NOW()),
    (8, 'FILA', '휠라', 'Fila', 'https://cdn.example.com/brand/fila.png', 'ACTIVE', NOW(), NOW()),
    (9, 'UNDERARMOUR', '언더아머', 'Under Armour', 'https://cdn.example.com/brand/underarmour.png', 'ACTIVE', NOW(), NOW()),
    (10, 'JORDAN', '조던', 'Jordan', 'https://cdn.example.com/brand/jordan.png', 'ACTIVE', NOW(), NOW()),
    (99, 'INACTIVE_BRAND', '비활성브랜드', 'InactiveBrand', NULL, 'INACTIVE', NOW(), NOW());
