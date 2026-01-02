-- =============================================================================
-- V5__create_seller_product_tables.sql
-- Seller, Policy, Product 관련 테이블 생성
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. sellers 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE sellers (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 셀러 기본 정보
    seller_name VARCHAR(100) NOT NULL,
    logo_url VARCHAR(500) NULL,
    description TEXT NULL,
    approval_status VARCHAR(20) NOT NULL,

    -- 사업자 정보 (BusinessInfo Embedded)
    registration_number VARCHAR(20) NOT NULL,
    sale_report_number VARCHAR(50) NULL,
    representative VARCHAR(50) NOT NULL,
    business_address_line1 VARCHAR(200) NOT NULL,
    business_address_line2 VARCHAR(100) NULL,
    business_zip_code VARCHAR(10) NOT NULL,

    -- 감사 필드 (SoftDeletableEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- sellers 인덱스
CREATE INDEX idx_sellers_approval_status ON sellers (approval_status);
CREATE INDEX idx_sellers_deleted_at ON sellers (deleted_at);

-- -----------------------------------------------------------------------------
-- 2. seller_cs_infos 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE seller_cs_infos (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 셀러 ID (Long FK)
    seller_id BIGINT NOT NULL,

    -- CS 연락처 정보
    email VARCHAR(100) NULL,
    mobile_phone VARCHAR(20) NULL,
    landline_phone VARCHAR(20) NULL,

    -- 감사 필드 (BaseAuditEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_seller_cs_infos_seller_id UNIQUE (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 3. seller_shipping_policies 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE seller_shipping_policies (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 셀러 ID (Long FK)
    seller_id BIGINT NOT NULL,

    -- 정책 정보
    policy_name VARCHAR(50) NOT NULL,
    default_delivery_cost INT NOT NULL,
    free_shipping_threshold INT NULL,
    delivery_guide TEXT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INT NOT NULL DEFAULT 0,

    -- 감사 필드 (SoftDeletableEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- seller_shipping_policies 인덱스
CREATE INDEX idx_seller_shipping_policies_seller_id ON seller_shipping_policies (seller_id);
CREATE INDEX idx_seller_shipping_policies_seller_default ON seller_shipping_policies (seller_id, is_default);

-- -----------------------------------------------------------------------------
-- 4. seller_refund_policies 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE seller_refund_policies (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 셀러 ID (Long FK)
    seller_id BIGINT NOT NULL,

    -- 정책 정보
    policy_name VARCHAR(50) NOT NULL,
    return_address_line1 VARCHAR(200) NOT NULL,
    return_address_line2 VARCHAR(100) NULL,
    return_zip_code VARCHAR(10) NOT NULL,
    refund_period_days INT NOT NULL,
    refund_delivery_cost INT NOT NULL,
    refund_guide TEXT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INT NOT NULL DEFAULT 0,

    -- 감사 필드 (SoftDeletableEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- seller_refund_policies 인덱스
CREATE INDEX idx_seller_refund_policies_seller_id ON seller_refund_policies (seller_id);
CREATE INDEX idx_seller_refund_policies_seller_default ON seller_refund_policies (seller_id, is_default);

-- -----------------------------------------------------------------------------
-- 5. product_groups 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE product_groups (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- FK 필드 (Long FK 전략)
    seller_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    shipping_policy_id BIGINT NULL,
    refund_policy_id BIGINT NULL,

    -- 상품그룹 정보
    name VARCHAR(200) NOT NULL,
    option_type VARCHAR(20) NOT NULL,
    regular_price DECIMAL(15, 2) NOT NULL,
    current_price DECIMAL(15, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,

    -- 감사 필드 (BaseAuditEntity + Soft Delete)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- product_groups 인덱스
CREATE INDEX idx_product_groups_seller_id ON product_groups (seller_id);
CREATE INDEX idx_product_groups_category_id ON product_groups (category_id);
CREATE INDEX idx_product_groups_brand_id ON product_groups (brand_id);
CREATE INDEX idx_product_groups_status ON product_groups (status);
CREATE INDEX idx_product_groups_created_at ON product_groups (created_at);
CREATE INDEX idx_product_groups_deleted_at ON product_groups (deleted_at);

-- product_groups FULLTEXT 인덱스 (MySQL 5.7+ InnoDB 지원)
-- MATCH AGAINST 검색을 위한 Full-Text 인덱스
-- ngram parser는 한글 검색 지원을 위해 사용 (MySQL 5.7.6+)
ALTER TABLE product_groups ADD FULLTEXT INDEX idx_product_groups_name_ft (name) WITH PARSER ngram;

-- -----------------------------------------------------------------------------
-- 6. products 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE products (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 상품그룹 ID (Long FK)
    product_group_id BIGINT NOT NULL,

    -- 옵션 정보
    option_type VARCHAR(20) NOT NULL,
    option1_name VARCHAR(50) NULL,
    option1_value VARCHAR(100) NULL,
    option2_name VARCHAR(50) NULL,
    option2_value VARCHAR(100) NULL,

    -- 가격 정보
    additional_price DECIMAL(15, 2) NULL,

    -- 상태
    sold_out BOOLEAN NOT NULL DEFAULT FALSE,
    display_yn BOOLEAN NOT NULL DEFAULT TRUE,

    -- 감사 필드 (BaseAuditEntity + Soft Delete)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- products 인덱스
CREATE INDEX idx_products_product_group_id ON products (product_group_id);
CREATE INDEX idx_products_deleted_at ON products (deleted_at);

-- -----------------------------------------------------------------------------
-- 7. product_stocks 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE product_stocks (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 상품 ID (Long FK, UNIQUE - 1:1 관계)
    product_id BIGINT NOT NULL,

    -- 재고 정보
    quantity INT NOT NULL DEFAULT 0,

    -- 낙관적 락 버전
    version BIGINT NOT NULL DEFAULT 0,

    -- 감사 필드 (BaseAuditEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_product_stocks_product_id UNIQUE (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 8. product_images 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE product_images (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 상품그룹 ID (Long FK)
    product_group_id BIGINT NOT NULL,

    -- 이미지 정보
    image_type VARCHAR(20) NOT NULL,
    origin_url VARCHAR(500) NOT NULL,
    cdn_url VARCHAR(500) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,

    -- 감사 필드 (BaseAuditEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- product_images 인덱스
CREATE INDEX idx_product_images_product_group_id ON product_images (product_group_id);
CREATE INDEX idx_product_images_image_type ON product_images (product_group_id, image_type);

-- -----------------------------------------------------------------------------
-- 9. product_descriptions 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE product_descriptions (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 상품그룹 ID (Long FK, UNIQUE - 1:1 관계)
    product_group_id BIGINT NOT NULL,

    -- HTML 컨텐츠
    html_content LONGTEXT NULL,

    -- 감사 필드 (BaseAuditEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_product_descriptions_product_group_id UNIQUE (product_group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 10. product_description_images 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE product_description_images (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 상품설명 ID (Long FK)
    product_description_id BIGINT NOT NULL,

    -- 이미지 정보
    display_order INT NOT NULL DEFAULT 0,
    origin_url VARCHAR(1000) NOT NULL,
    cdn_url VARCHAR(1000) NOT NULL,
    uploaded_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- product_description_images 인덱스
CREATE INDEX idx_product_description_images_desc_id ON product_description_images (product_description_id);

-- -----------------------------------------------------------------------------
-- 11. product_notices 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE product_notices (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 상품그룹 ID (Long FK, UNIQUE - 1:1 관계)
    product_group_id BIGINT NOT NULL,

    -- 템플릿 ID (Long FK)
    template_id BIGINT NOT NULL,

    -- 감사 필드 (BaseAuditEntity)
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    -- 제약 조건
    PRIMARY KEY (id),
    CONSTRAINT uk_product_notices_product_group_id UNIQUE (product_group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------------------------------
-- 12. product_notice_items 테이블 생성
-- -----------------------------------------------------------------------------
CREATE TABLE product_notice_items (
    -- 기본 키 (Auto Increment)
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- 상품고시 ID (Long FK)
    product_notice_id BIGINT NOT NULL,

    -- 고시 항목 정보
    field_key VARCHAR(50) NOT NULL,
    field_value TEXT NULL,
    display_order INT NOT NULL DEFAULT 0,

    -- 제약 조건
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- product_notice_items 인덱스
CREATE INDEX idx_product_notice_items_notice_id ON product_notice_items (product_notice_id);

-- -----------------------------------------------------------------------------
-- 테이블 설명
-- -----------------------------------------------------------------------------
-- sellers 테이블:
-- - 셀러(판매자) 정보를 저장
-- - BusinessInfo (사업자 정보)를 Embedded로 관리
-- - Soft Delete 지원 (deleted_at)
--
-- seller_cs_infos 테이블:
-- - 셀러 고객센터 연락처 정보
-- - sellerId와 1:1 관계 (UNIQUE 제약)
--
-- seller_shipping_policies 테이블:
-- - 셀러별 배송 정책 관리
-- - is_default로 기본 정책 관리
-- - Soft Delete 지원
--
-- seller_refund_policies 테이블:
-- - 셀러별 환불/반품 정책 관리
-- - is_default로 기본 정책 관리
-- - Soft Delete 지원
--
-- product_groups 테이블:
-- - 상품그룹 (SPU - Standard Product Unit)
-- - name 컬럼에 FULLTEXT 인덱스 적용 (MATCH AGAINST 검색용)
-- - Long FK 전략으로 seller, category, brand, policy 참조
--
-- products 테이블:
-- - 개별 상품 (SKU - Stock Keeping Unit)
-- - 옵션 조합별 상품
-- - productGroupId로 상품그룹 참조
--
-- product_stocks 테이블:
-- - 상품 재고 관리
-- - 낙관적 락(version)으로 동시성 제어
-- - productId와 1:1 관계
--
-- product_images 테이블:
-- - 상품그룹 이미지 (MAIN, SUB, DETAIL 타입)
-- - displayOrder로 순서 관리
--
-- product_descriptions 테이블:
-- - 상품 상세 설명 (HTML)
-- - productGroupId와 1:1 관계
--
-- product_description_images 테이블:
-- - 상품 상세 설명 내 이미지
-- - productDescriptionId 참조
--
-- product_notices 테이블:
-- - 상품 정보고시 (전자상거래법 준수)
-- - templateId로 고시 템플릿 참조
-- - productGroupId와 1:1 관계
--
-- product_notice_items 테이블:
-- - 상품 정보고시 항목
-- - productNoticeId 참조
-- =============================================================================
