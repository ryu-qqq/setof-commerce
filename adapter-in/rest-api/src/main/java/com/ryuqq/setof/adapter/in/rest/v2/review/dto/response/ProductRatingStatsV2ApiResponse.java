package com.ryuqq.setof.adapter.in.rest.v2.review.dto.response;

import com.ryuqq.setof.application.review.dto.response.ProductRatingStatsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * 상품 평점 통계 V2 API Response
 *
 * <p>상품의 평점 통계 정보를 반환하는 응답 DTO입니다.
 *
 * @param productGroupId 상품 그룹 ID
 * @param averageRating 평균 평점
 * @param reviewCount 리뷰 총 개수
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "상품 평점 통계 응답")
public record ProductRatingStatsV2ApiResponse(
        @Schema(description = "상품 그룹 ID", example = "12345") Long productGroupId,
        @Schema(description = "평균 평점 (소수점 1자리)", example = "4.5") BigDecimal averageRating,
        @Schema(description = "리뷰 총 개수", example = "42") int reviewCount) {

    /**
     * Application Response -> API Response 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static ProductRatingStatsV2ApiResponse from(ProductRatingStatsResponse response) {
        return new ProductRatingStatsV2ApiResponse(
                response.productGroupId(), response.averageRating(), response.reviewCount());
    }
}
