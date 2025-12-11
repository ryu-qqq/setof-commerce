package com.ryuqq.setof.adapter.in.rest.v2.category.dto.response;

import com.ryuqq.setof.application.category.dto.response.CategoryPathResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Category Path V2 API Response (Breadcrumb)
 *
 * <p>카테고리 상위 경로 응답 DTO입니다. 최상위부터 현재 카테고리까지의 경로를 포함합니다.
 *
 * @param categoryId 현재 카테고리 ID
 * @param breadcrumbs 상위 경로 목록 (최상위 -> 현재)
 */
@Schema(description = "Category Path V2 응답 (Breadcrumb)")
public record CategoryPathV2ApiResponse(
        @Schema(description = "현재 카테고리 ID", example = "10") Long categoryId,
        @Schema(description = "상위 경로 목록") List<BreadcrumbItemV2ApiResponse> breadcrumbs) {

    /**
     * Breadcrumb 항목 응답 DTO
     *
     * @param categoryId 카테고리 ID
     * @param code 카테고리 코드
     * @param nameKo 한글 카테고리명
     * @param depth 깊이
     */
    @Schema(description = "Breadcrumb 항목")
    public record BreadcrumbItemV2ApiResponse(
            @Schema(description = "카테고리 ID", example = "1") Long categoryId,
            @Schema(description = "카테고리 코드", example = "CAT001") String code,
            @Schema(description = "한글 카테고리명", example = "여성의류") String nameKo,
            @Schema(description = "깊이", example = "1") int depth) {

        /**
         * Application Layer DTO로부터 생성
         *
         * @param item BreadcrumbItem
         * @return BreadcrumbItemV2ApiResponse
         */
        public static BreadcrumbItemV2ApiResponse from(CategoryPathResponse.BreadcrumbItem item) {
            return new BreadcrumbItemV2ApiResponse(
                    item.id(), item.code(), item.nameKo(), item.depth());
        }
    }

    /**
     * Application Layer DTO로부터 생성
     *
     * @param response CategoryPathResponse
     * @return CategoryPathV2ApiResponse
     */
    public static CategoryPathV2ApiResponse from(CategoryPathResponse response) {
        List<BreadcrumbItemV2ApiResponse> breadcrumbItems =
                response.breadcrumbs() != null
                        ? response.breadcrumbs().stream()
                                .map(BreadcrumbItemV2ApiResponse::from)
                                .toList()
                        : List.of();

        return new CategoryPathV2ApiResponse(response.categoryId(), breadcrumbItems);
    }
}
