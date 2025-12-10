package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 브랜드 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "브랜드 필터")
public record BrandFilterV1ApiRequest(
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "100") Long lastDomainId,
        @Schema(description = "사이트 ID", example = "1") Long siteId,
        @Schema(description = "브랜드명", example = "Nike") String brandName,
        @Schema(description = "브랜드명 목록",
                example = "[\"Nike\", \"Adidas\"]") List<String> brandNames,
        @Schema(description = "메인 표시명 타입", example = "KOREAN") String mainDisplayType) {
}
