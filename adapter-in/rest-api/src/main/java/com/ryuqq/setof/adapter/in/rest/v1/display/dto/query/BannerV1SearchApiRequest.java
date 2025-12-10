package com.ryuqq.setof.adapter.in.rest.v1.display.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Banner 조회 필터")
public record BannerV1SearchApiRequest(
        @Schema(description = "Banner Type", example = "CATEGORY") String bannerType) {}
