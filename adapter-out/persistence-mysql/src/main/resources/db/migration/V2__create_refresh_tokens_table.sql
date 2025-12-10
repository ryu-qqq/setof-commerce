-- =============================================================================
-- V2__create_refresh_tokens_table.sql
-- Refresh Tokens 테이블 생성 (RDS 영속 저장소)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. refresh_tokens 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE refresh_tokens (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 회원 ID (UUID v7 - FK 없이 String으로 관리)
    member_id VARCHAR(36) NOT NULL,

    -- Refresh Token 값 (JWT 형식)
    token VARCHAR(512) NOT NULL,

    -- 만료 일시 (UTC 기준)
    expires_at DATETIME(6) NOT NULL,

    -- 감사 필드
    created_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_refresh_tokens_token UNIQUE (token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 2. 인덱스 생성
-- -----------------------------------------------------------------------------

-- 회원 ID 인덱스 (회원별 토큰 조회/삭제용)
CREATE INDEX idx_refresh_tokens_member_id ON refresh_tokens (member_id);

-- 만료 일시 인덱스 (배치 삭제용)
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens (expires_at);

-- -----------------------------------------------------------------------------
-- 3. 테이블 설명
-- -----------------------------------------------------------------------------
-- refresh_tokens 테이블은 Refresh Token을 RDS에 영속 저장합니다.
--
-- 주요 특징:
-- - Redis 캐시의 백업 저장소 역할
-- - Cache-Aside 패턴: Redis 미스 시 RDS 조회 → Redis 복구
-- - 회원 탈퇴/로그아웃 시 일괄 삭제
-- - 배치 작업으로 만료된 토큰 정리
--
-- Long FK 전략:
-- - member_id: members.id와 FK 관계 없이 String UUID로 관리
-- - JPA 관계 어노테이션 사용 금지
--
-- 제약 조건:
-- - token: 고유값 (중복 토큰 방지)
-- - member_id: 인덱스로 빠른 조회 지원
-- =============================================================================
