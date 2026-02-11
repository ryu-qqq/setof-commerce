package com.ryuqq.setof.application.legacy.category.dto.response;

import java.util.List;

/**
 * LegacyTreeCategoryResult - 레거시 트리 카테고리 조회 결과 DTO.
 *
 * <p>트리 구조를 포함하는 결과 DTO입니다.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param displayName 표시명
 * @param categoryDepth 카테고리 depth (1=루트)
 * @param parentCategoryId 부모 카테고리 ID
 * @param children 하위 카테고리 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyTreeCategoryResult(
        long categoryId,
        String categoryName,
        String displayName,
        int categoryDepth,
        long parentCategoryId,
        List<LegacyTreeCategoryResult> children) {

    /**
     * 팩토리 메서드 (children 포함).
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param displayName 표시명
     * @param categoryDepth 카테고리 depth (1=루트)
     * @param parentCategoryId 부모 카테고리 ID
     * @param children 하위 카테고리 목록
     * @return LegacyTreeCategoryResult
     */
    public static LegacyTreeCategoryResult of(
            long categoryId,
            String categoryName,
            String displayName,
            int categoryDepth,
            long parentCategoryId,
            List<LegacyTreeCategoryResult> children) {
        return new LegacyTreeCategoryResult(
                categoryId, categoryName, displayName, categoryDepth, parentCategoryId, children);
    }

    /**
     * 팩토리 메서드 (children 없음, 빈 리스트로 초기화).
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param displayName 표시명
     * @param categoryDepth 카테고리 depth (1=루트)
     * @param parentCategoryId 부모 카테고리 ID
     * @return LegacyTreeCategoryResult
     */
    public static LegacyTreeCategoryResult withEmptyChildren(
            long categoryId,
            String categoryName,
            String displayName,
            int categoryDepth,
            long parentCategoryId) {
        return new LegacyTreeCategoryResult(
                categoryId,
                categoryName,
                displayName,
                categoryDepth,
                parentCategoryId,
                new java.util.ArrayList<>());
    }
}
