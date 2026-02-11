package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ProductCategoryV1ApiResponse - 상품 카테고리 응답 DTO.
 *
 * <p>레거시 ProductCategoryContext 기반 변환.
 *
 * <p>GET /api/v1/category/page - 카테고리 페이징 조회
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "상품 카테고리 응답")
public record ProductCategoryV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "100") long categoryId,
        @Schema(description = "카테고리명", example = "티셔츠") String categoryName,
        @Schema(description = "노출명", example = "티셔츠") String displayName,
        @Schema(description = "카테고리 depth (1=대분류, 2=중분류, 3=소분류)", example = "3") int categoryDepth,
        @Schema(description = "카테고리 전체 경로", example = "의류 > 상의 > 티셔츠") String categoryFullPath,
        @Schema(
                        description = "대상 그룹",
                        example = "ALL",
                        allowableValues = {"ALL", "MALE", "FEMALE", "KIDS"})
                String targetGroup) {}
