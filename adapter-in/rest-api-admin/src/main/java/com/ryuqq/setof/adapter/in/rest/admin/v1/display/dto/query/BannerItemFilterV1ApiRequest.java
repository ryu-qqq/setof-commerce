package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 배너 아이템 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배너 아이템 필터")
public record BannerItemFilterV1ApiRequest(
        @Schema(description = "표시 여부", example = "true") Boolean displayYn) {
}
