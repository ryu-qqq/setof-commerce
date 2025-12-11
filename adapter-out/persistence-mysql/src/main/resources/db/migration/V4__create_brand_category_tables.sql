-- =============================================================================
-- V4__create_brand_category_tables.sql
-- Brands, Categories 테이블 생성 (V2 Query API용)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. brands 테이블 생성
-- MarketPlace에서 배치로 동기화되는 읽기 전용 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE brand (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 브랜드 코드 (MarketPlace와 동일 값 사용)
    code VARCHAR(100) NOT NULL,

    -- 한글 브랜드명
    name_ko VARCHAR(255) NOT NULL,

    -- 영문 브랜드명 (선택)
    name_en VARCHAR(255) NULL,

    -- 로고 이미지 URL
    logo_url VARCHAR(500) NULL,

    -- 상태 (ACTIVE, INACTIVE)
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    -- 감사 필드
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_brand_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- brand 인덱스
CREATE INDEX idx_brand_status ON brand (status);
CREATE INDEX idx_brand_name_ko ON brand (name_ko);

-- -----------------------------------------------------------------------------
-- 2. categories 테이블 생성
-- Path Enumeration 패턴 적용된 계층형 카테고리 테이블
-- MarketPlace에서 배치로 동기화되는 읽기 전용 테이블
-- -----------------------------------------------------------------------------
CREATE TABLE category (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 카테고리 코드
    code VARCHAR(100) NOT NULL,

    -- 한글 카테고리명
    name_ko VARCHAR(255) NOT NULL,

    -- 부모 카테고리 ID (null이면 최상위)
    parent_id BIGINT NULL,

    -- 깊이 (0=최상위, 1=중분류, 2=소분류...)
    depth TINYINT UNSIGNED NOT NULL DEFAULT 0,

    -- Path Enumeration (예: "/1/5/23/")
    path VARCHAR(500) NOT NULL,

    -- 정렬 순서 (낮을수록 상위)
    sort_order INT NOT NULL DEFAULT 0,

    -- 리프 노드 여부 (하위 카테고리 없음)
    is_leaf TINYINT(1) NOT NULL DEFAULT 1,

    -- 상태 (ACTIVE, INACTIVE)
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    -- 감사 필드
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_category_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- category 인덱스
CREATE INDEX idx_category_parent ON category (parent_id);
CREATE INDEX idx_category_path ON category (path);
CREATE INDEX idx_category_status ON category (status);
CREATE INDEX idx_category_depth ON category (depth);
CREATE INDEX idx_category_parent_sort ON category (parent_id, sort_order);

-- -----------------------------------------------------------------------------
-- 3. 테이블 설명
-- -----------------------------------------------------------------------------
-- brand 테이블:
-- - MarketPlace에서 배치로 동기화되는 읽기 전용 데이터
-- - code는 MarketPlace와 동일한 값 사용
-- - setof-commerce에서는 조회만 수행 (CUD 없음)
-- - status로 활성/비활성 상태 관리
--
-- category 테이블:
-- - Path Enumeration 패턴으로 계층 구조 표현
-- - path 필드: "/{id}/{parent_id}/..." 형식으로 조상 경로 저장
-- - depth: 0=대분류, 1=중분류, 2=소분류
-- - is_leaf: 하위 카테고리 존재 여부 (true면 리프 노드)
-- - MarketPlace에서 배치로 동기화되는 읽기 전용 데이터
-- =============================================================================
