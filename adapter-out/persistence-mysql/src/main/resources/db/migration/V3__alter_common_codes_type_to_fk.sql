-- =====================================================
-- V2: common_codes 테이블 type → common_code_type_id 변경
-- =====================================================
-- JPA 엔티티와 스키마 불일치 해결
-- - 기존: type VARCHAR(50)
-- - 변경: common_code_type_id BIGINT (FK to common_code_types)
-- =====================================================

-- 1. common_code_type_id 컬럼 추가
ALTER TABLE common_codes
    ADD COLUMN common_code_type_id BIGINT NOT NULL DEFAULT 0 AFTER id;

-- 2. 기존 type 값을 common_code_types의 ID로 매핑 (데이터가 있는 경우)
-- 참고: Stage 환경은 데이터가 없거나 적을 것으로 예상
UPDATE common_codes cc
INNER JOIN common_code_types cct ON cct.code = cc.type
SET cc.common_code_type_id = cct.id
WHERE cc.common_code_type_id = 0;

-- 3. 기존 인덱스 삭제
ALTER TABLE common_codes
    DROP INDEX uk_common_codes_type_code,
    DROP INDEX idx_common_codes_type_active_order;

-- 4. type 컬럼 삭제
ALTER TABLE common_codes
    DROP COLUMN type;

-- 5. 새 인덱스 생성 (common_code_type_id 기반)
ALTER TABLE common_codes
    ADD UNIQUE KEY uk_common_codes_type_code (common_code_type_id, code),
    ADD INDEX idx_common_codes_type_active_order (common_code_type_id, is_active, display_order);

-- 6. DEFAULT 제약 제거 (데이터 마이그레이션 완료 후)
ALTER TABLE common_codes
    ALTER COLUMN common_code_type_id DROP DEFAULT;
