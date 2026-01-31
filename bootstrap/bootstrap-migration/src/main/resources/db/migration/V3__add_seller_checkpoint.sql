-- ============================================
-- Seller 마이그레이션 체크포인트 추가
-- 용도: Seller → SellerApplication 마이그레이션 진행 상태 추적
-- ============================================

INSERT INTO migration_checkpoint (domain_name, status) VALUES
    ('seller', 'PENDING')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;
