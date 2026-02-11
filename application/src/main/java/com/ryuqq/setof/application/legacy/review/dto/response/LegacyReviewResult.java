package com.ryuqq.setof.application.legacy.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 레거시 Review 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param reviewId 리뷰 ID
 * @param orderId 주문 ID
 * @param userName 작성자 이름
 * @param rating 평점
 * @param content 리뷰 내용
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param productGroupImageUrl 상품그룹 메인 이미지 URL
 * @param brand 브랜드 정보
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param reviewImages 리뷰 이미지 목록
 * @param insertDate 작성일시
 * @param paymentDate 결제일시
 * @param option 옵션 문자열
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyReviewResult(
        long reviewId,
        long orderId,
        String userName,
        double rating,
        String content,
        long productGroupId,
        String productGroupName,
        String productGroupImageUrl,
        BrandResult brand,
        long categoryId,
        String categoryName,
        List<ReviewImageResult> reviewImages,
        LocalDateTime insertDate,
        LocalDateTime paymentDate,
        String option) {

    public record BrandResult(long brandId, String brandName) {
        public static BrandResult of(long brandId, String brandName) {
            return new BrandResult(brandId, brandName);
        }
    }

    public record ReviewImageResult(String reviewImageType, String imageUrl) {
        public static ReviewImageResult of(String reviewImageType, String imageUrl) {
            return new ReviewImageResult(reviewImageType, imageUrl);
        }
    }
}
