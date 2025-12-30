package com.ryuqq.setof.adapter.in.rest.v2.review.dto.response;

import com.ryuqq.setof.application.review.dto.response.ReviewImageResponse;
import com.ryuqq.setof.application.review.dto.response.ReviewResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * 리뷰 상세 V2 API Response
 *
 * <p>리뷰 상세 조회 시 반환되는 응답 DTO입니다.
 *
 * @param id 리뷰 ID
 * @param memberId 작성자 회원 ID (UUID)
 * @param orderId 주문 ID
 * @param productGroupId 상품 그룹 ID
 * @param rating 평점
 * @param content 리뷰 내용
 * @param images 이미지 목록
 * @param createdAt 작성일시
 * @param updatedAt 수정일시
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "리뷰 상세 응답")
public record ReviewV2ApiResponse(
        @Schema(description = "리뷰 ID", example = "1") Long id,
        @Schema(description = "작성자 회원 ID", example = "01929b9e-0d4f-7ab0-b4d8-1c2d3e4f5a6b")
                UUID memberId,
        @Schema(description = "주문 ID", example = "12345") Long orderId,
        @Schema(description = "상품 그룹 ID", example = "67890") Long productGroupId,
        @Schema(description = "평점 (1-5)", example = "5") int rating,
        @Schema(description = "리뷰 내용", example = "상품 품질이 좋습니다!") String content,
        @Schema(description = "이미지 목록") List<ReviewImageV2ApiResponse> images,
        @Schema(description = "작성일시") Instant createdAt,
        @Schema(description = "수정일시") Instant updatedAt) {

    /**
     * Application Response -> API Response 변환
     *
     * @param response Application Layer 응답
     * @return API 응답
     */
    public static ReviewV2ApiResponse from(ReviewResponse response) {
        List<ReviewImageV2ApiResponse> imageResponses =
                response.images().stream().map(ReviewImageV2ApiResponse::from).toList();

        return new ReviewV2ApiResponse(
                response.id(),
                response.memberId(),
                response.orderId(),
                response.productGroupId(),
                response.rating(),
                response.content(),
                imageResponses,
                response.createdAt(),
                response.updatedAt());
    }

    /** 리뷰 이미지 응답 */
    @Schema(description = "리뷰 이미지")
    public record ReviewImageV2ApiResponse(
            @Schema(description = "이미지 URL") String imageUrl,
            @Schema(description = "표시 순서") int displayOrder) {

        /**
         * Application Response -> API Response 변환
         *
         * @param response Application Layer 응답
         * @return API 응답
         */
        public static ReviewImageV2ApiResponse from(ReviewImageResponse response) {
            return new ReviewImageV2ApiResponse(response.imageUrl(), response.displayOrder());
        }
    }
}
