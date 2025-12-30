-- =============================================
-- V6: sellers 테이블에 tenant_id, organization_id 컬럼 추가
-- 멀티테넌트 접근 제어를 위한 필드 추가
-- =============================================

-- 테넌트 ID 컬럼 추가 (기본값 'default' - 기존 데이터 호환)
ALTER TABLE sellers
    ADD COLUMN tenant_id VARCHAR(50) NOT NULL DEFAULT 'default' AFTER id;

-- 조직 ID 컬럼 추가 (기본값 'default' - 기존 데이터 호환)
ALTER TABLE sellers
    ADD COLUMN organization_id VARCHAR(50) NOT NULL DEFAULT 'default' AFTER tenant_id;

-- 테넌트/조직별 조회를 위한 인덱스 추가
CREATE INDEX idx_sellers_tenant_id ON sellers (tenant_id);
CREATE INDEX idx_sellers_organization_id ON sellers (organization_id);
CREATE INDEX idx_sellers_tenant_org ON sellers (tenant_id, organization_id);

-- 기본값 제거 (신규 데이터는 반드시 tenant_id, organization_id 지정 필요)
ALTER TABLE sellers
    ALTER COLUMN tenant_id DROP DEFAULT;

ALTER TABLE sellers
    ALTER COLUMN organization_id DROP DEFAULT;
