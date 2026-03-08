package com.ryuqq.setof.application.review.dto.response;

import java.time.Instant;
import java.util.List;

/**
 * 리뷰 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param reviewId 리뷰 ID
 * @param legacyOrderId 레거시 주문 ID (Long, 레거시 시스템)
 * @param orderId 주문 ID (String, UUIDv7 기반, nullable)
 * @param userName 작성자 이름
 * @param rating 평점
 * @param content 리뷰 내용
 * @param productGroup 상품그룹 정보
 * @param brand 브랜드 정보
 * @param category 카테고리 정보
 * @param reviewImages 리뷰 이미지 목록
 * @param createdAt 작성일시
 * @param updatedAt 수정일시
 * @param paymentDate 결제일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ReviewResult(
        long reviewId,
        Long legacyOrderId,
        String orderId,
        String userName,
        double rating,
        String content,
        ProductGroupResult productGroup,
        BrandResult brand,
        CategoryResult category,
        List<ReviewImageResult> reviewImages,
        Instant createdAt,
        Instant updatedAt,
        Instant paymentDate) {

    public record ProductGroupResult(
            long productGroupId, String name, String imageUrl, String option) {
        public static ProductGroupResult of(
                long productGroupId, String name, String imageUrl, String option) {
            return new ProductGroupResult(productGroupId, name, imageUrl, option);
        }
    }

    public record BrandResult(long brandId, String brandName) {
        public static BrandResult of(long brandId, String brandName) {
            return new BrandResult(brandId, brandName);
        }
    }

    public record CategoryResult(long categoryId, String categoryName) {
        public static CategoryResult of(long categoryId, String categoryName) {
            return new CategoryResult(categoryId, categoryName);
        }
    }

    public record ReviewImageResult(String reviewImageType, String imageUrl) {
        public static ReviewImageResult of(String reviewImageType, String imageUrl) {
            return new ReviewImageResult(reviewImageType, imageUrl);
        }
    }
}
