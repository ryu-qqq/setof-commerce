package com.ryuqq.setof.application.review.dto.response;

import java.math.BigDecimal;

/**
 * Product Rating Stats Response
 *
 * <p>상품 평점 통계 응답 DTO
 *
 * @param productGroupId 상품 그룹 ID
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 총 개수
 * @author development-team
 * @since 1.0.0
 */
public record ProductRatingStatsResponse(
        Long productGroupId, BigDecimal averageRating, int reviewCount) {

    /**
     * Static Factory Method
     *
     * @param productGroupId 상품 그룹 ID
     * @param averageRating 평균 평점
     * @param reviewCount 리뷰 총 개수
     * @return ProductRatingStatsResponse 인스턴스
     */
    public static ProductRatingStatsResponse of(
            Long productGroupId, BigDecimal averageRating, int reviewCount) {
        return new ProductRatingStatsResponse(productGroupId, averageRating, reviewCount);
    }

    /**
     * 리뷰가 없는 경우의 빈 통계 생성
     *
     * @param productGroupId 상품 그룹 ID
     * @return 빈 ProductRatingStatsResponse 인스턴스
     */
    public static ProductRatingStatsResponse empty(Long productGroupId) {
        return new ProductRatingStatsResponse(productGroupId, BigDecimal.ZERO, 0);
    }
}
