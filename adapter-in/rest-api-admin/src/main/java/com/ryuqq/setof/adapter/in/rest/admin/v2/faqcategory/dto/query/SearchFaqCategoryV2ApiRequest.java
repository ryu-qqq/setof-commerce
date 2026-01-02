package com.ryuqq.setof.adapter.in.rest.admin.v2.faqcategory.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * FAQ 카테고리 검색 요청 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "FAQ 카테고리 검색 요청")
public record SearchFaqCategoryV2ApiRequest(
        @Schema(description = "상태 (ACTIVE, INACTIVE, DELETED)", example = "ACTIVE") String status,
        @Schema(description = "키워드 검색", example = "주문") String keyword,
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0") @Min(0)
                Integer page,
        @Schema(description = "페이지 크기", example = "20", defaultValue = "20") @Min(1) @Max(100)
                Integer size) {

    public SearchFaqCategoryV2ApiRequest {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
    }

    /**
     * 페이지 번호 반환
     *
     * @return 페이지 번호
     */
    public int getPage() {
        return page;
    }

    /**
     * 페이지 크기 반환
     *
     * @return 페이지 크기
     */
    public int getSize() {
        return size;
    }
}
