-- =============================================================================
-- Cleanup Script for REST API Admin Integration Tests
-- 테스트 종료 후 데이터 정리용 스크립트
-- =============================================================================

-- 외래 키 의존성 순서대로 삭제
DELETE FROM refund_policies;
DELETE FROM shipping_policies;
DELETE FROM seller_cs_infos;
DELETE FROM seller_business_infos;
DELETE FROM sellers;
