package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 배너 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "배너 필터")
public record BannerFilterV1ApiRequest(
        @Schema(description = "배너명", example = "메인 배너") String bannerName,
        @Schema(description = "배너 타입", example = "MAIN") String bannerType,
        @Schema(description = "표시 여부", example = "true") Boolean displayYn) {
}
