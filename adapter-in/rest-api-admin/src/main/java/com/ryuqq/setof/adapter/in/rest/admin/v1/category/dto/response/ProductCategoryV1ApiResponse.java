package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ProductCategoryV1ApiResponse - 상품 카테고리 V1 응답 DTO.
 *
 * <p>레거시 ProductCategoryContext와 동일한 필드 구조입니다.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param displayName 표시명
 * @param categoryDepth 카테고리 깊이
 * @param targetGroup 타겟 그룹 (MEN, WOMEN, KIDS 등)
 * @param categoryFullPath 카테고리 전체 경로
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "상품 카테고리 V1 응답 (레거시 호환)")
public record ProductCategoryV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") long categoryId,
        @Schema(description = "카테고리명", example = "상의") String categoryName,
        @Schema(description = "표시명", example = "상의") String displayName,
        @Schema(description = "카테고리 깊이", example = "2") int categoryDepth,
        @Schema(description = "타겟 그룹", example = "MEN") String targetGroup,
        @Schema(description = "카테고리 전체 경로", example = "의류 > 상의") String categoryFullPath) {}
