package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * TreeCategoryV1ApiResponse - 트리 형태 카테고리 응답 DTO.
 *
 * <p>레거시 TreeCategoryContext 기반 변환.
 *
 * <p>GET /api/v1/category - 전체 카테고리 트리 조회
 *
 * <p>GET /api/v1/category/{categoryId} - 하위 카테고리 조회
 *
 * <p>GET /api/v1/category/parent/{categoryId} - 상위 카테고리 조회
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Schema(description = "트리 형태 카테고리 응답")
public record TreeCategoryV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") long categoryId,
        @Schema(description = "카테고리명", example = "의류") String categoryName,
        @Schema(description = "노출명", example = "의류") String displayName,
        @Schema(description = "카테고리 depth (1=루트)", example = "1") int categoryDepth,
        @Schema(description = "부모 카테고리 ID (루트면 0)", example = "0") long parentCategoryId,
        @Schema(description = "하위 카테고리 목록") @JsonInclude(JsonInclude.Include.NON_EMPTY)
                List<TreeCategoryV1ApiResponse> children) {

    /** 하위 카테고리 없는 단순 노드 생성. */
    public TreeCategoryV1ApiResponse(
            long categoryId,
            String categoryName,
            String displayName,
            int categoryDepth,
            long parentCategoryId) {
        this(categoryId, categoryName, displayName, categoryDepth, parentCategoryId, List.of());
    }
}
