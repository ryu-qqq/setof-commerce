package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * CategoryV1ApiResponse - 기본 카테고리 응답 DTO.
 *
 * <p>레거시 TreeCategoryContext 기반 변환 (단순 조회 버전).
 *
 * <p>GET /api/v1/category/parents - 다건 카테고리 조회
 *
 * <p>GET /api/v1/category/{categoryId} - 하위 카테고리 조회 (flat list)
 *
 * <p>GET /api/v1/category/parent/{categoryId} - 상위 카테고리 조회 (flat list)
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "카테고리 응답")
public record CategoryV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "100") long categoryId,
        @Schema(description = "카테고리명", example = "티셔츠") String categoryName,
        @Schema(description = "노출명", example = "티셔츠") String displayName,
        @Schema(description = "카테고리 depth", example = "3") int categoryDepth,
        @Schema(description = "부모 카테고리 ID", example = "10") long parentCategoryId) {}
