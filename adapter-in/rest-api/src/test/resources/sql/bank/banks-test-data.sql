-- =============================================================================
-- Bank Test Data for REST API Integration Tests
-- =============================================================================

-- 기존 데이터 삭제
DELETE FROM banks;

-- 은행 데이터 삽입 (금융결제원 표준 코드 기준)
INSERT INTO banks (id, bank_code, bank_name, is_active, display_order, created_at, updated_at) VALUES
    (1, '004', 'KB국민은행', true, 1, NOW(6), NOW(6)),
    (2, '088', '신한은행', true, 2, NOW(6), NOW(6)),
    (3, '020', '우리은행', true, 3, NOW(6), NOW(6)),
    (4, '081', '하나은행', true, 4, NOW(6), NOW(6)),
    (5, '011', 'NH농협은행', true, 5, NOW(6), NOW(6)),
    (6, '003', 'IBK기업은행', true, 6, NOW(6), NOW(6)),
    (7, '023', 'SC제일은행', true, 7, NOW(6), NOW(6)),
    (8, '027', '한국씨티은행', true, 8, NOW(6), NOW(6)),
    (9, '039', '경남은행', true, 9, NOW(6), NOW(6)),
    (10, '034', '광주은행', true, 10, NOW(6), NOW(6)),
    (99, '999', '테스트비활성은행', false, 99, NOW(6), NOW(6));
