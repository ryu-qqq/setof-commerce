package com.ryuqq.setof.adapter.in.rest.v2.category.dto.response;

import com.ryuqq.setof.application.category.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Category V2 API Response
 *
 * <p>카테고리 상세 정보 응답 DTO입니다.
 *
 * @param categoryId 카테고리 ID
 * @param code 카테고리 코드
 * @param nameKo 한글 카테고리명
 * @param parentId 부모 카테고리 ID
 * @param depth 깊이
 * @param path 경로 (Path Enumeration)
 * @param sortOrder 정렬 순서
 * @param isLeaf 리프 노드 여부
 * @param status 상태
 */
@Schema(description = "Category V2 응답")
public record CategoryV2ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "카테고리 코드", example = "CAT001") String code,
        @Schema(description = "한글 카테고리명", example = "여성의류") String nameKo,
        @Schema(description = "부모 카테고리 ID", example = "null") Long parentId,
        @Schema(description = "깊이", example = "1") int depth,
        @Schema(description = "경로 (Path Enumeration)", example = "/1/") String path,
        @Schema(description = "정렬 순서", example = "1") int sortOrder,
        @Schema(description = "리프 노드 여부", example = "false") boolean isLeaf,
        @Schema(description = "상태", example = "ACTIVE") String status) {

    /**
     * Application Layer DTO로부터 생성
     *
     * @param response CategoryResponse
     * @return CategoryV2ApiResponse
     */
    public static CategoryV2ApiResponse from(CategoryResponse response) {
        return new CategoryV2ApiResponse(
                response.id(),
                response.code(),
                response.nameKo(),
                response.parentId(),
                response.depth(),
                response.path(),
                response.sortOrder(),
                response.isLeaf(),
                response.status());
    }
}
