package com.ryuqq.setof.application.category.dto.response;

import java.util.List;

/**
 * 카테고리 경로 응답 DTO (Breadcrumb)
 *
 * <p>카테고리 상위 경로 조회 시 반환되는 응답 DTO입니다. 최상위부터 현재 카테고리까지의 경로를 포함합니다.
 *
 * @param categoryId 현재 카테고리 ID
 * @param breadcrumbs 상위 경로 목록 (최상위 → 현재)
 */
public record CategoryPathResponse(Long categoryId, List<BreadcrumbItem> breadcrumbs) {

    /**
     * 경로 항목 DTO
     *
     * @param id 카테고리 ID
     * @param code 카테고리 코드
     * @param nameKo 한글 카테고리명
     * @param depth 깊이
     */
    public record BreadcrumbItem(Long id, String code, String nameKo, int depth) {

        /**
         * Static Factory Method
         *
         * @param id 카테고리 ID
         * @param code 카테고리 코드
         * @param nameKo 한글 카테고리명
         * @param depth 깊이
         * @return BreadcrumbItem 인스턴스
         */
        public static BreadcrumbItem of(Long id, String code, String nameKo, int depth) {
            return new BreadcrumbItem(id, code, nameKo, depth);
        }
    }

    /**
     * Static Factory Method
     *
     * @param categoryId 현재 카테고리 ID
     * @param breadcrumbs 상위 경로 목록
     * @return CategoryPathResponse 인스턴스
     */
    public static CategoryPathResponse of(Long categoryId, List<BreadcrumbItem> breadcrumbs) {
        return new CategoryPathResponse(categoryId, breadcrumbs);
    }
}
