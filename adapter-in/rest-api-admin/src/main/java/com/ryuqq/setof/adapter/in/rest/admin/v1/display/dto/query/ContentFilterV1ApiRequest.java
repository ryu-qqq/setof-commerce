package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 컨텐츠 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "컨텐츠 필터")
public record ContentFilterV1ApiRequest(
        @Schema(description = "컨텐츠 제목", example = "신상품") String title,
        @Schema(description = "표시 여부", example = "true") Boolean displayYn) {
}
