-- V22: CMS Board & FAQ 테이블 생성
-- 게시판, FAQ 카테고리, FAQ 테이블

-- 1. cms_boards 테이블 (게시판)
CREATE TABLE cms_boards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_type VARCHAR(30) NOT NULL COMMENT '게시판 유형 (NOTICE, EVENT, NEWS)',
    title VARCHAR(200) NOT NULL COMMENT '제목',
    content TEXT NOT NULL COMMENT '내용',
    summary VARCHAR(500) NULL COMMENT '요약',
    thumbnail_url VARCHAR(500) NULL COMMENT '썸네일 URL',
    is_pinned BOOLEAN NOT NULL DEFAULT FALSE COMMENT '상단 고정 여부',
    pin_order INT NOT NULL DEFAULT 0 COMMENT '고정 순서',
    display_start_at DATETIME(6) NULL COMMENT '노출 시작일',
    display_end_at DATETIME(6) NULL COMMENT '노출 종료일',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT '상태 (DRAFT, PUBLISHED, HIDDEN)',
    view_count BIGINT NOT NULL DEFAULT 0 COMMENT '조회수',
    created_by BIGINT NULL COMMENT '작성자 ID',
    updated_by BIGINT NULL COMMENT '수정자 ID',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL COMMENT 'Soft Delete 일시',

    INDEX idx_cms_boards_board_type (board_type),
    INDEX idx_cms_boards_status (status),
    INDEX idx_cms_boards_is_pinned (is_pinned),
    INDEX idx_cms_boards_display_start_at (display_start_at),
    INDEX idx_cms_boards_display_end_at (display_end_at),
    INDEX idx_cms_boards_deleted_at (deleted_at),
    INDEX idx_cms_boards_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시판';

-- 2. cms_faq_categories 테이블 (FAQ 카테고리)
CREATE TABLE cms_faq_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL COMMENT '카테고리 코드',
    name VARCHAR(100) NOT NULL COMMENT '카테고리명',
    description VARCHAR(500) NULL COMMENT '설명',
    display_order INT NOT NULL DEFAULT 0 COMMENT '표시 순서',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '상태 (ACTIVE, INACTIVE)',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL COMMENT 'Soft Delete 일시',

    UNIQUE INDEX uk_cms_faq_categories_code (code),
    INDEX idx_cms_faq_categories_status (status),
    INDEX idx_cms_faq_categories_display_order (display_order),
    INDEX idx_cms_faq_categories_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='FAQ 카테고리';

-- 3. cms_faqs 테이블 (FAQ)
CREATE TABLE cms_faqs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_code VARCHAR(50) NOT NULL COMMENT '카테고리 코드',
    question VARCHAR(500) NOT NULL COMMENT '질문',
    answer TEXT NOT NULL COMMENT '답변',
    is_top BOOLEAN NOT NULL DEFAULT FALSE COMMENT '상단 노출 여부',
    top_order INT NOT NULL DEFAULT 0 COMMENT '상단 순서',
    display_order INT NOT NULL DEFAULT 0 COMMENT '표시 순서',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT '상태 (DRAFT, PUBLISHED, HIDDEN)',
    view_count INT NOT NULL DEFAULT 0 COMMENT '조회수',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL COMMENT 'Soft Delete 일시',

    INDEX idx_cms_faqs_category_code (category_code),
    INDEX idx_cms_faqs_status (status),
    INDEX idx_cms_faqs_is_top (is_top),
    INDEX idx_cms_faqs_display_order (display_order),
    INDEX idx_cms_faqs_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='FAQ';
