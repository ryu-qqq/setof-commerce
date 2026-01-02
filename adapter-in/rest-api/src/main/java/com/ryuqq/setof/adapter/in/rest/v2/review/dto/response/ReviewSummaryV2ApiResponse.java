package com.ryuqq.setof.adapter.in.rest.v2.review.dto.response;

import com.ryuqq.setof.application.review.dto.response.ReviewSummaryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

/**
 * 리뷰 요약 V2 API Response
 *
 * <p>리뷰 목록 조회 시 반환되는 요약 응답 DTO입니다.
 *
 * @param id 리뷰 ID
 * @param memberId 작성자 회원 ID (UUID)
 * @param rating 평점
 * @param content 리뷰 내용
 * @param hasImages 이미지 첨부 여부
 * @param imageCount 이미지 개수
 * @param createdAt 작성일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "리뷰 요약 응답 (목록 조회용)")
public record ReviewSummaryV2ApiResponse(
        @Schema(description = "리뷰 ID", example = "1") Long id,
        @Schema(description = "작성자 회원 ID", example = "01929b9e-0d4f-7ab0-b4d8-1c2d3e4f5a6b")
                UUID memberId,
        @Schema(description = "평점 (1-5)", example = "5") int rating,
        @Schema(description = "리뷰 내용", example = "상품 품질이 좋습니다!") String content,
        @Schema(description = "이미지 첨부 여부", example = "true") boolean hasImages,
        @Schema(description = "이미지 개수", example = "2") int imageCount,
        @Schema(description = "작성일시") Instant createdAt) {

    /**
     * Application Response -> API Response 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static ReviewSummaryV2ApiResponse from(ReviewSummaryResponse response) {
        return new ReviewSummaryV2ApiResponse(
                response.id(),
                response.memberId(),
                response.rating(),
                response.content(),
                response.hasImages(),
                response.imageCount(),
                response.createdAt());
    }
}
