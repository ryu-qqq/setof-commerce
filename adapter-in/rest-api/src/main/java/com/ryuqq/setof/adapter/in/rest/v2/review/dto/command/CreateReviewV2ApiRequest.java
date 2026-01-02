package com.ryuqq.setof.adapter.in.rest.v2.review.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 리뷰 생성 V2 API Request
 *
 * <p>상품 리뷰를 생성할 때 사용하는 요청 DTO입니다.
 *
 * <p>MVP 제약사항:
 *
 * <ul>
 *   <li>주문당 상품별 1개 리뷰만 작성 가능
 *   <li>이미지 최대 3장
 *   <li>평점 1~5점
 * </ul>
 *
 * @param orderId 주문 ID
 * @param productGroupId 상품 그룹 ID
 * @param rating 평점 (1-5)
 * @param content 리뷰 내용 (선택)
 * @param imageUrls 리뷰 이미지 URL 목록 (최대 3장)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "리뷰 생성 요청")
public record CreateReviewV2ApiRequest(
        @Schema(
                        description = "주문 ID",
                        example = "12345",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "주문 ID는 필수입니다.")
                Long orderId,
        @Schema(
                        description = "상품 그룹 ID",
                        example = "12345",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "상품 그룹 ID는 필수입니다.")
                Long productGroupId,
        @Schema(
                        description = "평점 (1-5)",
                        example = "5",
                        minimum = "1",
                        maximum = "5",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "평점은 필수입니다.")
                @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
                @Max(value = 5, message = "평점은 5 이하여야 합니다.")
                Integer rating,
        @Schema(description = "리뷰 내용 (최대 1000자)", example = "상품 품질이 좋습니다!", maxLength = 1000)
                @Size(max = 1000, message = "리뷰 내용은 1000자를 초과할 수 없습니다.")
                String content,
        @Schema(description = "리뷰 이미지 URL 목록 (최대 3장)")
                @Size(max = 3, message = "리뷰 이미지는 최대 3장까지 등록 가능합니다.")
                List<String> imageUrls) {

    /** Compact Constructor - null 처리 */
    public CreateReviewV2ApiRequest {
        if (imageUrls == null) {
            imageUrls = List.of();
        }
    }
}
