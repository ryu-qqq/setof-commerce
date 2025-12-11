-- =============================================================================
-- RefundAccount 테스트 데이터
-- =============================================================================

-- Bank 테스트 데이터 (외래키 참조용) - IGNORE로 중복 삽입 방지
INSERT IGNORE INTO banks (id, bank_code, bank_name, is_active, display_order, created_at, updated_at)
VALUES
(1, '004', 'KB국민은행', 1, 1, NOW(), NOW()),
(2, '088', '신한은행', 1, 2, NOW(), NOW()),
(3, '020', '우리은행', 1, 3, NOW(), NOW());

-- 테스트 회원 ID (UUID 형식): ApiIntegrationTestSupport.DEFAULT_TEST_MEMBER_ID
INSERT INTO refund_accounts (
    id, member_id, bank_id, account_number, account_holder_name,
    is_verified, verified_at, created_at, updated_at, deleted_at
) VALUES
-- 기본 테스트 회원의 환불계좌
(1, '11111111-1111-1111-1111-111111111111', 1, '1234567890123', '홍길동',
 1, NOW(), NOW(), NOW(), NULL);
