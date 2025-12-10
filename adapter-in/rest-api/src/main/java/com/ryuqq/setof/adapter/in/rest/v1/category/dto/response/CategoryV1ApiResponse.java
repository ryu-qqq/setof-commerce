package com.ryuqq.setof.adapter.in.rest.v1.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 카테고리 Response
 *
 * <p>카테고리 트리 정보를 반환하는 응답 DTO입니다.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param categoryDepth 깊이 (1: 대분류, 2: 중분류, 3: 소분류)
 * @param parentCategoryId 부모 카테고리 ID
 * @param children 하위 카테고리 목록
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "카테고리 응답")
public record CategoryV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "100") Long categoryId,
        @Schema(description = "카테고리명", example = "상의") String categoryName,
        @Schema(description = "깊이 (1: 대분류, 2: 중분류, 3: 소분류)", example = "1") Integer categoryDepth,
        @Schema(description = "부모 카테고리 ID", example = "null") Long parentCategoryId,
        @Schema(description = "하위 카테고리 목록") List<CategoryV1ApiResponse> children) {}
