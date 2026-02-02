package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * TreeCategoryV1ApiResponse - 트리 구조 카테고리 V1 응답 DTO.
 *
 * <p>레거시 TreeCategoryContext와 동일한 필드 구조입니다.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param displayName 표시명
 * @param categoryDepth 카테고리 깊이
 * @param parentCategoryId 부모 카테고리 ID
 * @param children 자식 카테고리 목록
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "트리 구조 카테고리 V1 응답 (레거시 호환)")
public record TreeCategoryV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") long categoryId,
        @Schema(description = "카테고리명", example = "의류") String categoryName,
        @Schema(description = "표시명", example = "의류") String displayName,
        @Schema(description = "카테고리 깊이", example = "1") int categoryDepth,
        @Schema(description = "부모 카테고리 ID", example = "0") long parentCategoryId,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) @Schema(description = "자식 카테고리 목록")
                List<TreeCategoryV1ApiResponse> children) {}
