package com.ryuqq.setof.adapter.in.rest.v2.brand.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Brand V2 검색 조건 API Request
 *
 * <p>브랜드 목록 조회 시 사용되는 검색 조건입니다.
 *
 * <p>변환 로직은 {@link
 * com.ryuqq.setof.adapter.in.rest.v2.brand.mapper.BrandV2ApiMapper#toSearchCondition} 참조
 *
 * @param keyword 검색 키워드 (한글/영문 브랜드명 검색)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 */
@Schema(description = "Brand V2 검색 조건")
public record BrandV2SearchApiRequest(
        @Schema(description = "검색 키워드 (브랜드명)", example = "나이키") String keyword,
        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다")
                Integer page,
        @Schema(description = "페이지 크기", example = "20", defaultValue = "20")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다")
                @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다")
                Integer size) {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;

    /** Compact Constructor - 기본값 설정 */
    public BrandV2SearchApiRequest {
        page = page != null ? page : DEFAULT_PAGE;
        size = size != null ? size : DEFAULT_SIZE;
    }
}
