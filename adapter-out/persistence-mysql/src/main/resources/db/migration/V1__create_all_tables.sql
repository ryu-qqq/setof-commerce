-- =====================================================
-- V1: 전체 테이블 생성 (CommonCode + Seller 도메인)
-- =====================================================

-- =====================================================
-- 1. Common Code 테이블
-- =====================================================

-- Common Code Type 테이블
CREATE TABLE common_code_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    UNIQUE KEY uk_common_code_types_code (code),
    INDEX idx_common_code_types_active_order (is_active, display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Common Code 테이블
CREATE TABLE common_codes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    UNIQUE KEY uk_common_codes_type_code (type, code),
    INDEX idx_common_codes_type_active_order (type, is_active, display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. Seller 도메인 테이블
-- =====================================================

-- 셀러 기본 정보 테이블
CREATE TABLE sellers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_name VARCHAR(100) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    logo_url VARCHAR(500),
    description VARCHAR(2000),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    UNIQUE KEY uk_sellers_seller_name (seller_name),
    INDEX idx_sellers_active (is_active),
    INDEX idx_sellers_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 셀러 주소 테이블
CREATE TABLE seller_addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    address_type VARCHAR(20) NOT NULL COMMENT 'SHIPPING, RETURN',
    address_name VARCHAR(50) NOT NULL,
    zipcode VARCHAR(10),
    address VARCHAR(200),
    address_detail VARCHAR(200),
    contact_name VARCHAR(50),
    contact_phone VARCHAR(20),
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    INDEX idx_seller_addresses_seller_id (seller_id),
    INDEX idx_seller_addresses_seller_type (seller_id, address_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 셀러 사업자 정보 테이블
CREATE TABLE seller_business_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    registration_number VARCHAR(20) NOT NULL COMMENT '사업자등록번호',
    company_name VARCHAR(100) NOT NULL,
    representative VARCHAR(50) NOT NULL COMMENT '대표자명',
    sale_report_number VARCHAR(50) COMMENT '통신판매업신고번호',
    business_zipcode VARCHAR(10),
    business_address VARCHAR(200),
    business_address_detail VARCHAR(200),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    UNIQUE KEY uk_seller_business_infos_registration (registration_number),
    INDEX idx_seller_business_infos_seller_id (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 셀러 CS 정보 테이블
CREATE TABLE seller_cs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    cs_phone VARCHAR(20) NOT NULL COMMENT 'CS 전화번호',
    cs_mobile VARCHAR(20) COMMENT 'CS 휴대폰번호',
    cs_email VARCHAR(100) NOT NULL COMMENT 'CS 이메일',
    operating_start_time TIME COMMENT '운영 시작 시간',
    operating_end_time TIME COMMENT '운영 종료 시간',
    operating_days VARCHAR(50) COMMENT '운영 요일 (MON,TUE,WED,THU,FRI)',
    kakao_channel_url VARCHAR(500) COMMENT '카카오톡 채널 URL',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    INDEX idx_seller_cs_seller_id (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 셀러 계약 정보 테이블
CREATE TABLE seller_contracts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    commission_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '수수료율 (%)',
    contract_start_date DATE NOT NULL COMMENT '계약 시작일',
    contract_end_date DATE COMMENT '계약 종료일 (NULL=무기한)',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, EXPIRED, TERMINATED',
    special_terms TEXT COMMENT '특약사항',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    INDEX idx_seller_contracts_seller_id (seller_id),
    INDEX idx_seller_contracts_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 셀러 정산 정보 테이블
CREATE TABLE seller_settlements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    bank_code VARCHAR(10) COMMENT '은행 코드',
    bank_name VARCHAR(50) COMMENT '은행명',
    account_number VARCHAR(30) COMMENT '계좌번호',
    account_holder_name VARCHAR(50) COMMENT '예금주',
    settlement_cycle VARCHAR(20) NOT NULL DEFAULT 'MONTHLY' COMMENT 'WEEKLY, BIWEEKLY, MONTHLY',
    settlement_day INT NOT NULL DEFAULT 1 COMMENT '정산일 (1-31)',
    is_verified BOOLEAN NOT NULL DEFAULT FALSE COMMENT '계좌 인증 여부',
    verified_at DATETIME(6) COMMENT '계좌 인증 일시',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,
    INDEX idx_seller_settlements_seller_id (seller_id),
    INDEX idx_seller_settlements_verified (is_verified)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. Seller Application 도메인 테이블
-- =====================================================

-- 셀러 입점 신청 테이블
CREATE TABLE seller_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- 신청자 기본 정보
    seller_name VARCHAR(100) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    logo_url VARCHAR(500),
    description VARCHAR(2000),
    -- 사업자 정보
    registration_number VARCHAR(20) NOT NULL COMMENT '사업자등록번호',
    company_name VARCHAR(100) NOT NULL,
    representative VARCHAR(50) NOT NULL COMMENT '대표자명',
    sale_report_number VARCHAR(50) COMMENT '통신판매업신고번호',
    business_zip_code VARCHAR(10) NOT NULL,
    business_base_address VARCHAR(200) NOT NULL,
    business_detail_address VARCHAR(200),
    -- CS 정보
    cs_phone_number VARCHAR(20) NOT NULL COMMENT 'CS 전화번호',
    cs_email VARCHAR(100) NOT NULL COMMENT 'CS 이메일',
    -- 주소 정보 (출고지/반품지)
    address_type VARCHAR(20) NOT NULL COMMENT 'SHIPPING, RETURN',
    address_name VARCHAR(50) NOT NULL,
    address_zip_code VARCHAR(10) NOT NULL,
    address_base_address VARCHAR(200) NOT NULL,
    address_detail_address VARCHAR(200),
    contact_name VARCHAR(50) NOT NULL,
    contact_phone_number VARCHAR(20) NOT NULL,
    -- 동의 정보
    agreed_at DATETIME(6) NOT NULL COMMENT '약관 동의 일시',
    -- 상태 관리
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING, APPROVED, REJECTED',
    applied_at DATETIME(6) NOT NULL COMMENT '신청 일시',
    processed_at DATETIME(6) COMMENT '처리 일시',
    processed_by VARCHAR(100) COMMENT '처리자',
    rejection_reason VARCHAR(500) COMMENT '거절 사유',
    -- 승인 후 생성된 셀러 ID
    approved_seller_id BIGINT COMMENT '승인된 셀러 ID',
    -- Audit
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    INDEX idx_seller_applications_status (status),
    INDEX idx_seller_applications_applied_at (applied_at),
    INDEX idx_seller_applications_registration (registration_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
