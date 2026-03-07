package com.ryuqq.setof.adapter.in.rest.v1.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * TreeCategoryV1ApiResponse - 트리 형태 카테고리 응답 DTO.
 *
 * <p>레거시 CategoryDisplayDto 기반 변환.
 *
 * <p>API-DTO-001: Record 타입 필수.
 *
 * <p>API-DTO-002: DTO 불변성 보장.
 *
 * <p>GET /api/v1/category - 전체 카테고리 트리 조회
 *
 * <p>Response DTO는 Domain 객체 의존 금지 → Result만 의존해야 하며, 변환은 Mapper에서 수행합니다.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명 (노출명)
 * @param categoryDepth 카테고리 depth (1=루트)
 * @param parentCategoryId 부모 카테고리 ID (루트면 0)
 * @param children 하위 카테고리 목록
 * @author ryu-qqq
 * @since 1.0.0
 * @see com.setof.connectly.module.category.dto.CategoryDisplayDto
 */
@Schema(description = "트리 형태 카테고리 응답")
public record TreeCategoryV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") long categoryId,
        @Schema(description = "카테고리명", example = "여성") String categoryName,
        @Schema(description = "카테고리 depth (1=루트)", example = "1") int categoryDepth,
        @Schema(description = "부모 카테고리 ID (루트면 0)", example = "0") long parentCategoryId,
        @Schema(description = "하위 카테고리 목록") List<TreeCategoryV1ApiResponse> children) {

    /**
     * 하위 카테고리 없는 단순 노드 생성.
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param categoryDepth 카테고리 depth
     * @param parentCategoryId 부모 카테고리 ID
     */
    public TreeCategoryV1ApiResponse(
            long categoryId, String categoryName, int categoryDepth, long parentCategoryId) {
        this(categoryId, categoryName, categoryDepth, parentCategoryId, List.of());
    }
}
