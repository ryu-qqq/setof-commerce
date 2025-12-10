package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 상품 카테고리 컨텍스트 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 카테고리 컨텍스트 응답")
public record ProductCategoryContextV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "카테고리명", example = "상의") String categoryName,
        @Schema(description = "표시명", example = "상의 > 티셔츠") String displayName,
        @Schema(description = "카테고리 깊이", example = "1") Integer categoryDepth,
        @Schema(description = "카테고리 전체 경로", example = "상의 > 티셔츠 > 반팔") String categoryFullPath,
        @Schema(description = "대상 그룹", example = "PRODUCT") String targetGroup) {}
