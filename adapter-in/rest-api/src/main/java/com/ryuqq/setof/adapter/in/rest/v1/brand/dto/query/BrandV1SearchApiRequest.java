package com.ryuqq.setof.adapter.in.rest.v1.brand.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 브랜드 검색 필터 Request
 *
 * <p>브랜드 목록을 검색할 때 사용하는 필터 조건입니다. 검색어를 통해 브랜드명을 검색할 수 있습니다.
 *
 * @param searchWord 검색어 (브랜드명)
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "브랜드 검색 필터")
public record BrandV1SearchApiRequest(
    @Schema(description = "검색어 (브랜드명)", example = "나이키") String searchWord
) {
}
