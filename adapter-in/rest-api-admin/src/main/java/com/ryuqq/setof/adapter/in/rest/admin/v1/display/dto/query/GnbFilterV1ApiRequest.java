package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 GNB 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "GNB 필터")
public record GnbFilterV1ApiRequest(
        @Schema(description = "GNB명", example = "홈") String name,
        @Schema(description = "표시 여부", example = "true") Boolean displayYn) {}
