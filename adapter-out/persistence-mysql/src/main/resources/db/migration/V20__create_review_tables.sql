-- V20: Review 도메인 테이블 생성
-- 리뷰, 리뷰 이미지, 상품 평점 통계 테이블

-- 1. reviews 테이블 (리뷰)
CREATE TABLE reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id VARCHAR(36) NOT NULL COMMENT '회원 ID (UUID)',
    order_id BIGINT NOT NULL COMMENT '주문 ID',
    product_group_id BIGINT NOT NULL COMMENT '상품 그룹 ID',
    rating INT NOT NULL COMMENT '평점 (1~5)',
    content TEXT COMMENT '리뷰 내용',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL COMMENT 'Soft Delete 일시',

    INDEX idx_reviews_member_id (member_id),
    INDEX idx_reviews_order_id (order_id),
    INDEX idx_reviews_product_group_id (product_group_id),
    INDEX idx_reviews_rating (rating),
    INDEX idx_reviews_deleted_at (deleted_at),
    UNIQUE INDEX uk_reviews_order_product (order_id, product_group_id, deleted_at) COMMENT '주문당 상품별 1개 리뷰'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='리뷰';

-- 2. review_images 테이블 (리뷰 이미지)
CREATE TABLE review_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT NOT NULL COMMENT '리뷰 ID',
    image_url VARCHAR(500) NOT NULL COMMENT '이미지 URL',
    image_type VARCHAR(20) NOT NULL COMMENT '이미지 타입 (PHOTO)',
    display_order INT NOT NULL DEFAULT 0 COMMENT '표시 순서',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    INDEX idx_review_images_review_id (review_id),
    INDEX idx_review_images_display_order (display_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='리뷰 이미지';

-- 3. product_rating_stats 테이블 (상품 평점 통계)
CREATE TABLE product_rating_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_group_id BIGINT NOT NULL COMMENT '상품 그룹 ID',
    average_rating DECIMAL(3, 2) NOT NULL DEFAULT 0.00 COMMENT '평균 평점',
    review_count BIGINT NOT NULL DEFAULT 0 COMMENT '리뷰 총 개수',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),

    UNIQUE INDEX uk_product_rating_stats_product_group_id (product_group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='상품 평점 통계';
