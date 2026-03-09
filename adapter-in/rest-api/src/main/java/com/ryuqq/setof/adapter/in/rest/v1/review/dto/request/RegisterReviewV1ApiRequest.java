package com.ryuqq.setof.adapter.in.rest.v1.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.Length;

/**
 * RegisterReviewV1ApiRequest - 리뷰 등록 V1 요청 DTO.
 *
 * <p>레거시 CreateReview 호환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "리뷰 등록 요청")
public record RegisterReviewV1ApiRequest(
        @Schema(description = "주문 ID", example = "5000") @NotNull Long orderId,
        @Schema(description = "상품그룹 ID", example = "100") @NotNull Long productGroupId,
        @Schema(description = "평점 (0~5, 소수점 1자리)", example = "4.5") @NotNull Double rating,
        @Schema(description = "리뷰 내용 (최대 500자)", example = "배송도 빠르고 품질이 좋았습니다.") @Length(max = 500)
                String content,
        @Schema(description = "리뷰 이미지 목록 (최대 3장)") @Size(max = 3)
                List<ReviewImageRequest> reviewImages) {

    @Schema(description = "리뷰 이미지 요청")
    public record ReviewImageRequest(
            @Schema(description = "이미지 URL") String imageUrl,
            @Schema(description = "이미지 타입") String reviewImageType) {}
}
