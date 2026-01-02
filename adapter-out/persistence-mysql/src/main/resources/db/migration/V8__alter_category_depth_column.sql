-- =============================================================================
-- V8__alter_category_depth_column.sql
-- Category 테이블 depth 컬럼 타입 변경 (JPA Entity 호환성)
-- =============================================================================
-- 문제: JPA Entity에서 Integer로 매핑된 depth 컬럼이 DB에서는 TINYINT UNSIGNED
-- Hibernate validate 모드에서 타입 불일치 오류 발생
-- 해결: TINYINT UNSIGNED → INT로 변경
-- =============================================================================

ALTER TABLE category MODIFY COLUMN depth INT NOT NULL DEFAULT 0;
