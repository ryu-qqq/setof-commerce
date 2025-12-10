package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 카테고리 필터 Query
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "카테고리 필터")
public record CategoryFilterV1ApiRequest(
        @Schema(description = "마지막 조회한 도메인 ID (커서 기반 페이징)", example = "100") Long lastDomainId,
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "카테고리명", example = "상의") String categoryName,
        @Schema(description = "카테고리 깊이", example = "1") Integer categoryDepth) {}
