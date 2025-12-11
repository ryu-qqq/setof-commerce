package com.ryuqq.setof.adapter.in.rest.v2.category.dto.response;

import com.ryuqq.setof.application.category.dto.response.CategoryTreeResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Category Tree V2 API Response
 *
 * <p>카테고리 트리 구조 응답 DTO입니다. 재귀적 구조로 하위 카테고리를 포함합니다.
 *
 * @param categoryId 카테고리 ID
 * @param code 카테고리 코드
 * @param nameKo 한글 카테고리명
 * @param depth 깊이
 * @param sortOrder 정렬 순서
 * @param isLeaf 리프 노드 여부
 * @param children 하위 카테고리 목록
 */
@Schema(description = "Category Tree V2 응답")
public record CategoryTreeV2ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "카테고리 코드", example = "CAT001") String code,
        @Schema(description = "한글 카테고리명", example = "여성의류") String nameKo,
        @Schema(description = "깊이", example = "1") int depth,
        @Schema(description = "정렬 순서", example = "1") int sortOrder,
        @Schema(description = "리프 노드 여부", example = "false") boolean isLeaf,
        @Schema(description = "하위 카테고리 목록") List<CategoryTreeV2ApiResponse> children) {

    /**
     * Application Layer DTO로부터 생성
     *
     * @param response CategoryTreeResponse
     * @return CategoryTreeV2ApiResponse
     */
    public static CategoryTreeV2ApiResponse from(CategoryTreeResponse response) {
        List<CategoryTreeV2ApiResponse> childrenResponse =
                response.children() != null
                        ? response.children().stream().map(CategoryTreeV2ApiResponse::from).toList()
                        : List.of();

        return new CategoryTreeV2ApiResponse(
                response.id(),
                response.code(),
                response.nameKo(),
                response.depth(),
                response.sortOrder(),
                response.isLeaf(),
                childrenResponse);
    }
}
