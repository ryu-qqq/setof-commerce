-- =============================================================================
-- V3__create_bank_shipping_refund_tables.sql
-- Banks, Shipping Addresses, Refund Accounts 테이블 생성
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. banks 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE banks (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 은행 코드 (금융결제원 표준 코드, 3자리)
    bank_code VARCHAR(10) NOT NULL,

    -- 은행명
    bank_name VARCHAR(50) NOT NULL,

    -- 활성 상태
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- 노출 순서 (낮을수록 상위)
    display_order INT NOT NULL DEFAULT 0,

    -- 감사 필드
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_banks_bank_code UNIQUE (bank_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 2. shipping_addresses 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE shipping_addresses (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 회원 ID (UUID v7 - FK 없이 String으로 관리)
    member_id VARCHAR(36) NOT NULL,

    -- 배송지명 (예: 집, 회사)
    address_name VARCHAR(50) NOT NULL,

    -- 수령인 정보
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(11) NOT NULL,

    -- 배송 주소 (도로명주소 또는 지번주소 중 하나 필수)
    zip_code VARCHAR(10) NOT NULL,
    road_address VARCHAR(200) NULL,
    jibun_address VARCHAR(200) NULL,
    detail_address VARCHAR(100) NULL,

    -- 배송 요청사항 (최대 200자)
    delivery_request VARCHAR(200) NULL,

    -- 기본 배송지 여부
    is_default BOOLEAN NOT NULL DEFAULT FALSE,

    -- 감사 필드 (SoftDeletableEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- shipping_addresses 인덱스
CREATE INDEX idx_shipping_addresses_member_id ON shipping_addresses (member_id);
CREATE INDEX idx_shipping_addresses_member_default ON shipping_addresses (member_id, is_default);

-- -----------------------------------------------------------------------------
-- 3. refund_accounts 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE refund_accounts (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 회원 ID (UUID v7 - FK 없이 String으로 관리)
    member_id VARCHAR(36) NOT NULL,

    -- 은행 ID (Long FK 전략 - FK 없이 Long으로 관리)
    bank_id BIGINT NOT NULL,

    -- 계좌번호 (마스킹 저장)
    account_number VARCHAR(20) NOT NULL,

    -- 예금주명
    account_holder_name VARCHAR(50) NOT NULL,

    -- 계좌 검증 여부
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,

    -- 계좌 검증 일시
    verified_at DATETIME(6) NULL,

    -- 감사 필드 (SoftDeletableEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_refund_accounts_member_id UNIQUE (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- refund_accounts 인덱스
CREATE INDEX idx_refund_accounts_bank_id ON refund_accounts (bank_id);

-- -----------------------------------------------------------------------------
-- 4. 기본 은행 데이터 삽입
-- -----------------------------------------------------------------------------
INSERT INTO banks (bank_code, bank_name, is_active, display_order, created_at, updated_at) VALUES
('004', 'KB국민은행', TRUE, 1, NOW(6), NOW(6)),
('088', '신한은행', TRUE, 2, NOW(6), NOW(6)),
('020', '우리은행', TRUE, 3, NOW(6), NOW(6)),
('081', '하나은행', TRUE, 4, NOW(6), NOW(6)),
('011', 'NH농협은행', TRUE, 5, NOW(6), NOW(6)),
('003', 'IBK기업은행', TRUE, 6, NOW(6), NOW(6)),
('023', 'SC제일은행', TRUE, 7, NOW(6), NOW(6)),
('039', '경남은행', TRUE, 8, NOW(6), NOW(6)),
('034', '광주은행', TRUE, 9, NOW(6), NOW(6)),
('031', '대구은행', TRUE, 10, NOW(6), NOW(6)),
('032', '부산은행', TRUE, 11, NOW(6), NOW(6)),
('037', '전북은행', TRUE, 12, NOW(6), NOW(6)),
('035', '제주은행', TRUE, 13, NOW(6), NOW(6)),
('090', '카카오뱅크', TRUE, 14, NOW(6), NOW(6)),
('092', '토스뱅크', TRUE, 15, NOW(6), NOW(6)),
('089', '케이뱅크', TRUE, 16, NOW(6), NOW(6)),
('002', 'KDB산업은행', TRUE, 17, NOW(6), NOW(6)),
('071', '우체국', TRUE, 18, NOW(6), NOW(6)),
('007', '수협은행', TRUE, 19, NOW(6), NOW(6)),
('045', '새마을금고', TRUE, 20, NOW(6), NOW(6));

-- -----------------------------------------------------------------------------
-- 5. 테이블 설명
-- -----------------------------------------------------------------------------
-- banks 테이블:
-- - 금융결제원 표준 은행 코드 기반
-- - 환불 계좌 등록 시 은행 선택에 사용
-- - is_active로 노출 여부 제어
-- - display_order로 정렬 순서 제어
--
-- shipping_addresses 테이블:
-- - 회원당 최대 5개 배송지 저장 (Application Layer에서 제어)
-- - is_default로 기본 배송지 관리
-- - Soft Delete 지원 (deleted_at)
-- - Long FK 전략: member_id는 members.id와 FK 없이 관리
--
-- refund_accounts 테이블:
-- - 회원당 1개 환불 계좌만 등록 가능 (UK 제약)
-- - 계좌번호는 마스킹 저장 (예: 1234****5678)
-- - is_verified, verified_at으로 검증 상태 관리
-- - Soft Delete 지원 (deleted_at)
-- - Long FK 전략: member_id, bank_id 모두 FK 없이 관리
-- =============================================================================
