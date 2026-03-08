package com.ryuqq.setof.domain.review.vo;

import java.time.Instant;
import java.util.List;

/**
 * 작성된 리뷰 정보를 담는 도메인 VO.
 *
 * <p>내 리뷰 / 상품그룹 리뷰 조회 결과로 Persistence Layer에서 반환됩니다.
 *
 * @param reviewId 리뷰 ID
 * @param legacyOrderId 레거시 주문 ID (Long, 레거시 시스템)
 * @param orderId 주문 ID (String, UUIDv7 기반, nullable)
 * @param userName 작성자 이름
 * @param rating 평점
 * @param content 리뷰 내용
 * @param productGroup 상품그룹 스냅샷
 * @param brand 브랜드 스냅샷
 * @param category 카테고리 스냅샷
 * @param images 리뷰 이미지 목록
 * @param createdAt 작성일시
 * @param updatedAt 수정일시
 * @param paymentDate 결제일시
 * @author ryu-qqq
 * @since 1.1.0
 */
public record WrittenReview(
        long reviewId,
        Long legacyOrderId,
        String orderId,
        String userName,
        double rating,
        String content,
        ProductGroupSnapshot productGroup,
        BrandSnapshot brand,
        CategorySnapshot category,
        List<WrittenReviewImage> images,
        Instant createdAt,
        Instant updatedAt,
        Instant paymentDate) {

    public record ProductGroupSnapshot(
            long productGroupId, String name, String imageUrl, String option) {}

    public record BrandSnapshot(long brandId, String name) {}

    public record CategorySnapshot(long categoryId, String name) {}

    public record WrittenReviewImage(String reviewImageType, String imageUrl) {}
}
