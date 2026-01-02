package com.ryuqq.setof.adapter.in.rest.v2.review.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 리뷰 수정 V2 API Request
 *
 * <p>리뷰를 수정할 때 사용하는 요청 DTO입니다.
 *
 * <p>수정 가능 항목:
 *
 * <ul>
 *   <li>평점
 *   <li>리뷰 내용
 *   <li>이미지 (기존 이미지 전체 교체)
 * </ul>
 *
 * @param rating 평점 (1-5)
 * @param content 리뷰 내용 (선택)
 * @param imageUrls 리뷰 이미지 URL 목록 (최대 3장)
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "리뷰 수정 요청")
public record UpdateReviewV2ApiRequest(
        @Schema(
                        description = "평점 (1-5)",
                        example = "4",
                        minimum = "1",
                        maximum = "5",
                        requiredMode = Schema.RequiredMode.REQUIRED)
                @NotNull(message = "평점은 필수입니다.")
                @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
                @Max(value = 5, message = "평점은 5 이하여야 합니다.")
                Integer rating,
        @Schema(description = "리뷰 내용 (최대 1000자)", example = "수정된 리뷰입니다.", maxLength = 1000)
                @Size(max = 1000, message = "리뷰 내용은 1000자를 초과할 수 없습니다.")
                String content,
        @Schema(description = "리뷰 이미지 URL 목록 (최대 3장)")
                @Size(max = 3, message = "리뷰 이미지는 최대 3장까지 등록 가능합니다.")
                List<String> imageUrls) {

    /** Compact Constructor - null 처리 */
    public UpdateReviewV2ApiRequest {
        if (imageUrls == null) {
            imageUrls = List.of();
        }
    }
}
