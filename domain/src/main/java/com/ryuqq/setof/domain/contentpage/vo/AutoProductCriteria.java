package com.ryuqq.setof.domain.contentpage.vo;

import java.util.List;

/**
 * AutoProductCriteria - AUTO 상품 조회 조건 VO.
 *
 * <p>DisplayComponent의 ComponentSpec에서 추출한 AUTO 상품 조회에 필요한 정보를 담는다.
 *
 * @param componentId 컴포넌트 ID
 * @param tabId 탭 ID (0이면 컴포넌트 레벨)
 * @param categoryIds 카테고리 ID 목록 (빈 리스트이면 미지정)
 * @param brandIds 브랜드 ID 목록 (빈 리스트이면 미지정)
 * @param limit 최대 조회 수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record AutoProductCriteria(
        long componentId, long tabId, List<Long> categoryIds, List<Long> brandIds, int limit) {

    public static AutoProductCriteria ofComponent(
            long componentId, long categoryId, List<Long> brandIds, int limit) {
        List<Long> categoryIds = categoryId != 0L ? List.of(categoryId) : List.of();
        return new AutoProductCriteria(componentId, 0L, categoryIds, brandIds, limit);
    }

    public static AutoProductCriteria ofTab(
            long componentId, long tabId, long categoryId, List<Long> brandIds, int limit) {
        List<Long> categoryIds = categoryId != 0L ? List.of(categoryId) : List.of();
        return new AutoProductCriteria(componentId, tabId, categoryIds, brandIds, limit);
    }

    public boolean isTabLevel() {
        return tabId != 0L;
    }

    public boolean hasCategoryFilter() {
        return categoryIds != null && !categoryIds.isEmpty();
    }

    public boolean hasBrandFilter() {
        return brandIds != null && !brandIds.isEmpty();
    }

    /**
     * 카테고리 확장된 새 조건을 반환한다.
     *
     * @param expandedCategoryIds 확장된 카테고리 ID 목록
     * @return 카테고리 ID가 확장된 새 AutoProductCriteria
     */
    public AutoProductCriteria withExpandedCategories(List<Long> expandedCategoryIds) {
        return new AutoProductCriteria(componentId, tabId, expandedCategoryIds, brandIds, limit);
    }
}
