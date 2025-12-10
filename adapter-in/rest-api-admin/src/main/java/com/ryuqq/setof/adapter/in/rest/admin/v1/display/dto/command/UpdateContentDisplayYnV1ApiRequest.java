package com.ryuqq.setof.adapter.in.rest.admin.v1.display.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * V1 컨텐츠 표시 상태 변경 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "컨텐츠 표시 상태 변경 요청")
public record UpdateContentDisplayYnV1ApiRequest(
        @Schema(description = "표시 여부", example = "true") @NotNull Boolean displayYn) {}
