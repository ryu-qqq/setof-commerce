-- =====================================================
-- V5: sellers 테이블에 인증 관련 컬럼 추가
-- =====================================================
-- JPA 엔티티: SellerJpaEntity
-- - auth_tenant_id: AuthHub 테넌트 ID
-- - auth_organization_id: AuthHub 조직 ID
-- =====================================================

ALTER TABLE sellers
    ADD COLUMN auth_tenant_id VARCHAR(100) NULL COMMENT 'AuthHub 테넌트 ID' AFTER is_active,
    ADD COLUMN auth_organization_id VARCHAR(100) NULL COMMENT 'AuthHub 조직 ID' AFTER auth_tenant_id;

-- 인덱스 추가 (조회 최적화)
ALTER TABLE sellers
    ADD INDEX idx_sellers_auth_tenant (auth_tenant_id),
    ADD INDEX idx_sellers_auth_organization (auth_organization_id);
