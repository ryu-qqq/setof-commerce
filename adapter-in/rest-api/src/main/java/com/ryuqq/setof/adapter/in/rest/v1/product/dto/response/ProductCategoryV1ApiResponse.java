package com.ryuqq.setof.adapter.in.rest.v1.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * V1 상품 카테고리 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "상품 카테고리 응답")
public record ProductCategoryV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "카테고리명", example = "상의") String categoryName,
        @Schema(description = "표시명", example = "상의 > 티셔츠") String displayName,
        @Schema(description = "부모 카테고리 ID", example = "0") Long parentCategoryId,
        @Schema(description = "카테고리 깊이", example = "1") Integer categoryDepth) {}
