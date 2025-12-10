package com.ryuqq.setof.adapter.in.rest.admin.v1.product.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 카테고리 수정 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "카테고리 수정 요청")
public record UpdateCategoryV1ApiRequest(
        @Schema(description = "카테고리 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
                Long categoryId) {}
