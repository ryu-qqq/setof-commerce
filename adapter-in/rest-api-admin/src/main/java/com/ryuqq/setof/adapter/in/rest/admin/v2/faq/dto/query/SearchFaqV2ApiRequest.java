package com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * FAQ 검색 요청 DTO
 *
 * @author development-team
 * @since 2.0.0
 */
@Schema(description = "FAQ 검색 요청")
public record SearchFaqV2ApiRequest(
        @Schema(description = "카테고리 코드", example = "ORDER") String categoryCode,
        @Schema(description = "상태 (DRAFT, PUBLISHED, HIDDEN)", example = "PUBLISHED") String status,
        @Schema(description = "상단 노출 여부", example = "true") Boolean isTop,
        @Schema(description = "키워드 검색", example = "주문") String keyword,
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0") @Min(0)
                Integer page,
        @Schema(description = "페이지 크기", example = "20", defaultValue = "20") @Min(1) @Max(100)
                Integer size) {

    public SearchFaqV2ApiRequest {
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
