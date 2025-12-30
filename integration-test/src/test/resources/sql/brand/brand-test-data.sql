-- ============================================================
-- Brand Integration Test Data
-- ============================================================
-- 브랜드 통합 테스트용 샘플 데이터
-- ============================================================

-- 기존 데이터 삭제 (테스트 격리)
DELETE FROM brand WHERE id IN (1, 2, 3, 4, 5);

INSERT INTO brand (id, code, name_ko, name_en, logo_url, status, created_at, updated_at)
VALUES
    (1, 'NIKE001', '나이키', 'Nike', 'https://cdn.example.com/brands/nike.png', 'ACTIVE', NOW(), NOW()),
    (2, 'ADIDAS001', '아디다스', 'Adidas', 'https://cdn.example.com/brands/adidas.png', 'ACTIVE', NOW(), NOW()),
    (3, 'PUMA001', '푸마', 'Puma', 'https://cdn.example.com/brands/puma.png', 'ACTIVE', NOW(), NOW()),
    (4, 'NB001', '뉴발란스', 'New Balance', 'https://cdn.example.com/brands/nb.png', 'ACTIVE', NOW(), NOW()),
    (5, 'REEBOK001', '리복', 'Reebok', 'https://cdn.example.com/brands/reebok.png', 'INACTIVE', NOW(), NOW());
