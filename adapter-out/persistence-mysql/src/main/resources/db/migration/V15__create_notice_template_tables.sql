-- =============================================================================
-- V15__create_notice_template_tables.sql
-- 상품고시 템플릿 관련 테이블 생성
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. notice_templates 테이블 생성
-- 카테고리별 상품고시 템플릿 정의
-- -----------------------------------------------------------------------------
CREATE TABLE notice_templates (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 카테고리 ID (Long FK)
    category_id BIGINT NOT NULL,

    -- 템플릿 정보
    template_name VARCHAR(100) NOT NULL,

    -- 감사 필드 (BaseAuditEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_notice_templates_category_id UNIQUE (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- notice_templates 인덱스
CREATE INDEX idx_notice_templates_template_name ON notice_templates (template_name);

-- -----------------------------------------------------------------------------
-- 2. notice_template_fields 테이블 생성
-- 템플릿별 필드 정의 (필수/선택)
-- -----------------------------------------------------------------------------
CREATE TABLE notice_template_fields (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 템플릿 ID (Long FK)
    template_id BIGINT NOT NULL,

    -- 필드 정보
    field_key VARCHAR(50) NOT NULL,
    description VARCHAR(200) NULL,
    required BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INT NOT NULL DEFAULT 0,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- notice_template_fields 인덱스
CREATE INDEX idx_notice_template_fields_template_id ON notice_template_fields (template_id);
CREATE UNIQUE INDEX uk_notice_template_fields_template_key ON notice_template_fields (template_id, field_key);

-- -----------------------------------------------------------------------------
-- 3. V1 호환용 기본 의류 템플릿 데이터 삽입
-- 레거시 9개 필드 (material, color, size, maker, origin, washingMethod, yearMonth, assuranceStandard, asPhone)
-- -----------------------------------------------------------------------------
INSERT INTO notice_templates (id, category_id, template_name, created_at, updated_at)
VALUES (1, 1, '의류', NOW(6), NOW(6));

INSERT INTO notice_template_fields (template_id, field_key, description, required, display_order)
VALUES
    (1, 'material', '소재', TRUE, 1),
    (1, 'color', '색상', TRUE, 2),
    (1, 'size', '사이즈', TRUE, 3),
    (1, 'maker', '제조사', TRUE, 4),
    (1, 'origin', '원산지', TRUE, 5),
    (1, 'washingMethod', '세탁 방법', TRUE, 6),
    (1, 'yearMonth', '제조년월', TRUE, 7),
    (1, 'assuranceStandard', '품질보증기준', TRUE, 8),
    (1, 'asPhone', 'A/S 전화번호', TRUE, 9);

-- -----------------------------------------------------------------------------
-- 테이블 설명
-- -----------------------------------------------------------------------------
-- notice_templates 테이블:
-- - 카테고리별 상품고시 템플릿 정의
-- - categoryId와 1:1 관계 (UNIQUE 제약)
-- - V1 호환을 위해 의류 템플릿(id=1) 기본 생성
--
-- notice_template_fields 테이블:
-- - 템플릿별 필드 정의 (필수/선택)
-- - templateId + fieldKey 복합 유니크
-- - required 플래그로 필수/선택 구분
-- - displayOrder로 표시 순서 관리
-- =============================================================================
