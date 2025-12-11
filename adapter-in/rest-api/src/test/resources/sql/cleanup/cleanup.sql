-- =============================================================================
-- 테스트 데이터 정리 스크립트
-- =============================================================================

-- 테이블 데이터 삭제 (역순으로 FK 제약 고려)
DELETE FROM refund_accounts;
DELETE FROM shipping_addresses;
DELETE FROM banks;
DELETE FROM brand;
DELETE FROM category;
