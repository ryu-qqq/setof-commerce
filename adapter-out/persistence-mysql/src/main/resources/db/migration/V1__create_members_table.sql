-- =============================================================================
-- V1__create_members_table.sql
-- Members 테이블 생성 (UUID v7 기반 PK)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. members 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE members (
    -- 기본 키 (UUID v7 - Domain MemberId)
    -- 보안성: 순차적 ID(1,2,3)는 예측 가능하여 보안 취약점
    -- UUID v7: 시간 기반 정렬 가능 + 예측 불가능
    id VARCHAR(36) NOT NULL,

    -- 휴대폰 번호 (010으로 시작하는 11자리)
    phone_number VARCHAR(11) NOT NULL,

    -- 이메일 주소 (RFC 5322 형식, nullable)
    email VARCHAR(100) NULL,

    -- 비밀번호 해시 (BCrypt, 60자, nullable - 소셜 로그인 회원)
    password_hash VARCHAR(60) NULL,

    -- 회원명
    name VARCHAR(50) NOT NULL,

    -- 생년월일 (nullable - 소셜 로그인 회원)
    date_of_birth DATE NULL,

    -- 성별 (MALE, FEMALE, OTHER, nullable)
    gender VARCHAR(10) NULL,

    -- 인증 제공자 (LOCAL, KAKAO)
    provider VARCHAR(20) NOT NULL,

    -- 소셜 ID (카카오 등, nullable)
    social_id VARCHAR(100) NULL,

    -- 회원 상태 (ACTIVE, INACTIVE, WITHDRAWN)
    status VARCHAR(20) NOT NULL,

    -- 개인정보 수집 동의 (필수)
    privacy_consent BOOLEAN NOT NULL DEFAULT FALSE,

    -- 서비스 이용약관 동의 (필수)
    service_terms_consent BOOLEAN NOT NULL DEFAULT FALSE,

    -- 광고 수신 동의 (선택)
    ad_consent BOOLEAN NOT NULL DEFAULT FALSE,

    -- 탈퇴 사유 (nullable)
    withdrawal_reason VARCHAR(50) NULL,

    -- 탈퇴 일시 (nullable)
    withdrawn_at DATETIME(6) NULL,

    -- 감사 필드 (SoftDeletableEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_members_phone_number UNIQUE (phone_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 2. 인덱스 생성
-- -----------------------------------------------------------------------------

-- 소셜 ID 인덱스 (카카오 로그인 조회용)
CREATE INDEX idx_members_social_id ON members (social_id);

-- 이메일 인덱스 (이메일 로그인 조회용, nullable이므로 unique 아님)
CREATE INDEX idx_members_email ON members (email);

-- 상태 인덱스 (활성 회원 조회용)
CREATE INDEX idx_members_status ON members (status);

-- 삭제 여부 인덱스 (소프트 딜리트 조회용)
CREATE INDEX idx_members_deleted_at ON members (deleted_at);

-- 생성일시 인덱스 (최신 가입 회원 조회용)
CREATE INDEX idx_members_created_at ON members (created_at);

-- -----------------------------------------------------------------------------
-- 3. 테이블 설명
-- -----------------------------------------------------------------------------
-- members 테이블은 회원 정보를 저장합니다.
--
-- 주요 특징:
-- - UUID v7 PK: id 컬럼에 Domain에서 생성한 UUID v7 저장 (보안성 확보)
--   - AUTO_INCREMENT 대신 UUID v7 사용
--   - 순차적 ID 예측 공격 방지
--   - 시간 기반 정렬 가능 (인덱스 친화적)
-- - Long FK 전략: JPA 관계 어노테이션 없이 FK 직접 관리
-- - Soft Delete: deleted_at 컬럼으로 논리 삭제 처리
-- - 소셜 로그인: provider, social_id로 카카오 등 소셜 로그인 지원
-- - BCrypt: password_hash에 60자 BCrypt 해시 저장
--
-- 제약 조건:
-- - id: UUID v7 형식 (36자), PK
-- - phone_number: 010 시작 11자리, 고유값
-- - email: RFC 5322 형식 (애플리케이션에서 검증)
-- - status: ACTIVE, INACTIVE, WITHDRAWN 중 하나
-- - provider: LOCAL, KAKAO 중 하나
-- =============================================================================
