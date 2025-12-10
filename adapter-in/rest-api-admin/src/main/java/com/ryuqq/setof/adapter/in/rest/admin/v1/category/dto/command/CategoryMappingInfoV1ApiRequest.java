package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 카테고리 매핑 정보 Request
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "카테고리 매핑 정보 요청")
public record CategoryMappingInfoV1ApiRequest(
        @Schema(description = "매핑 카테고리 ID", example = "mapping_123") String categoryMappingId,
        @Schema(description = "카테고리명", example = "상의") String categoryName,
        @Schema(description = "카테고리 ID", example = "1") Long categoryId) {}
