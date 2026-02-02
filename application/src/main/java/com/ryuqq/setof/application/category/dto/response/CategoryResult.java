package com.ryuqq.setof.application.category.dto.response;

/**
 * 카테고리 조회 결과.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param parentCategoryId 부모 카테고리 ID
 * @param depth 카테고리 깊이
 * @param displayed 노출 여부
 * @param targetUrl 타겟 URL
 * @author ryu-qqq
 * @since 1.0.0
 */
public record CategoryResult(
        Long categoryId,
        String categoryName,
        Long parentCategoryId,
        Integer depth,
        Boolean displayed,
        String targetUrl) {

    public static CategoryResult of(
            Long categoryId,
            String categoryName,
            Long parentCategoryId,
            Integer depth,
            Boolean displayed,
            String targetUrl) {
        return new CategoryResult(
                categoryId, categoryName, parentCategoryId, depth, displayed, targetUrl);
    }
}
