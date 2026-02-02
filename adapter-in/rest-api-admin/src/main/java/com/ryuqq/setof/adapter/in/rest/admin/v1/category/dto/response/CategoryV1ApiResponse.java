package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * CategoryV1ApiResponse - 카테고리 V1 응답 DTO.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param parentCategoryId 부모 카테고리 ID
 * @param depth 카테고리 깊이
 * @param displayed 노출 여부
 * @param path 카테고리 경로
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "카테고리 V1 응답")
public record CategoryV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "카테고리명", example = "의류") String categoryName,
        @Schema(description = "부모 카테고리 ID", example = "0") Long parentCategoryId,
        @Schema(description = "카테고리 깊이", example = "1") int depth,
        @Schema(description = "노출 여부", example = "true") boolean displayed,
        @Schema(description = "카테고리 경로", example = "의류 > 상의") String path) {}
