package com.ryuqq.setof.adapter.in.rest.v1.review.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * SearchMyReviewsCursorV1ApiRequest - 내가 작성한 리뷰 검색 요청 DTO.
 *
 * <p>레거시 ReviewFilter 기반 변환. 커서 페이징 사용.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Schema(description = "내가 작성한 리뷰 검색 요청 (커서 페이징)")
public record SearchMyReviewsCursorV1ApiRequest(
        @Parameter(description = "마지막 조회 리뷰 ID (커서)", example = "1000") Long lastReviewId,
        @Parameter(description = "페이지 크기 (1~100)", example = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
                Integer size) {

    public int sizeOrDefault() {
        return size != null ? size : 20;
    }
}
