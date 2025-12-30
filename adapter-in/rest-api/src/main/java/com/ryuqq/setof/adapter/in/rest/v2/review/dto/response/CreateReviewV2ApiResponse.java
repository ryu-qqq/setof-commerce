package com.ryuqq.setof.adapter.in.rest.v2.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 리뷰 생성 V2 API Response
 *
 * <p>리뷰 생성 후 반환되는 응답 DTO입니다.
 *
 * @param reviewId 생성된 리뷰 ID
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "리뷰 생성 응답")
public record CreateReviewV2ApiResponse(
        @Schema(description = "생성된 리뷰 ID", example = "1") Long reviewId) {

    /**
     * Static Factory Method
     *
     * @param reviewId 리뷰 ID
     * @return API 응답
     */
    public static CreateReviewV2ApiResponse of(Long reviewId) {
        return new CreateReviewV2ApiResponse(reviewId);
    }
}
