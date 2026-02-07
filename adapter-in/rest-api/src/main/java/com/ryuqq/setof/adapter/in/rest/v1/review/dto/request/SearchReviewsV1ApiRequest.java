package com.ryuqq.setof.adapter.in.rest.v1.review.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchReviewsV1ApiRequest - 상품 리뷰 검색 요청 DTO.
 *
 * <p>레거시 ReviewFilter 기반 변환. offset 페이징 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "상품 리뷰 검색 요청")
public record SearchReviewsV1ApiRequest(
        @Parameter(
                        description = "정렬 타입",
                        example = "RECOMMEND",
                        schema = @Schema(allowableValues = {"RECOMMEND", "HIGH_RATING", "RECENT"}))
                String orderType,
        @Parameter(description = "상품그룹 ID", example = "12345") Long productGroupId,
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {

    public int pageOrDefault() {
        return page != null ? page : 0;
    }

    public int sizeOrDefault() {
        return size != null ? size : 20;
    }
}
