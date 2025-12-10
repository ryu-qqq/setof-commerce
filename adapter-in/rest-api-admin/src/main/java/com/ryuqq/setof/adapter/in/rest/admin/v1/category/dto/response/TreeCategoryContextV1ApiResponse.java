package com.ryuqq.setof.adapter.in.rest.admin.v1.category.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * V1 트리 카테고리 컨텍스트 Response
 *
 * @author development-team
 * @since 1.0.0
 */
@Schema(description = "트리 카테고리 컨텍스트 응답")
public record TreeCategoryContextV1ApiResponse(
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
        @Schema(description = "카테고리명", example = "상의") String categoryName,
        @Schema(description = "표시명", example = "상의 > 티셔츠") String displayName,
        @Schema(description = "카테고리 깊이", example = "1") Integer categoryDepth,
        @Schema(description = "부모 카테고리 ID", example = "0") Long parentCategoryId,
        @Schema(description = "하위 카테고리 목록") @JsonInclude(JsonInclude.Include.NON_EMPTY) List<TreeCategoryContextV1ApiResponse> children) {
}
