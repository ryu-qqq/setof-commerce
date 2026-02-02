package com.ryuqq.setof.application.category.dto.response;

import java.util.List;

/**
 * 카테고리 노출용 조회 결과.
 *
 * <p>Public API용 간단한 응답 결과입니다.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param parentCategoryId 부모 카테고리 ID
 * @param depth 카테고리 깊이
 * @param children 자식 카테고리 목록
 * @author ryu-qqq
 * @since 1.0.0
 */
public record CategoryDisplayResult(
        Long categoryId,
        String categoryName,
        Long parentCategoryId,
        int depth,
        List<CategoryDisplayResult> children) {

    public static CategoryDisplayResult of(
            Long categoryId,
            String categoryName,
            Long parentCategoryId,
            int depth,
            List<CategoryDisplayResult> children) {
        return new CategoryDisplayResult(
                categoryId, categoryName, parentCategoryId, depth, children);
    }

    public static CategoryDisplayResult leaf(
            Long categoryId, String categoryName, Long parentCategoryId, int depth) {
        return new CategoryDisplayResult(
                categoryId, categoryName, parentCategoryId, depth, List.of());
    }
}
