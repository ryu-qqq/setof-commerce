package com.ryuqq.setof.domain.legacy.category.dto.query;

/**
 * LegacyCategorySearchCondition - 레거시 카테고리 검색 조건 DTO.
 *
 * <p>Repository 검색 파라미터용 DTO입니다.
 *
 * @param lastCategoryId No-Offset 페이징용 마지막 ID
 * @param categoryId 특정 카테고리 ID
 * @param categoryName 카테고리명 검색어 (displayName LIKE)
 * @param categoryDepth 카테고리 depth 필터
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyCategorySearchCondition(
        Long lastCategoryId, Long categoryId, String categoryName, Integer categoryDepth) {

    /**
     * 전체 조회용 빈 조건.
     *
     * @return LegacyCategorySearchCondition
     */
    public static LegacyCategorySearchCondition empty() {
        return new LegacyCategorySearchCondition(null, null, null, null);
    }

    /**
     * 카테고리 ID로 조회하는 생성자.
     *
     * @param categoryId 카테고리 ID
     * @return LegacyCategorySearchCondition
     */
    public static LegacyCategorySearchCondition ofCategoryId(Long categoryId) {
        return new LegacyCategorySearchCondition(null, categoryId, null, null);
    }

    /**
     * 카테고리명 검색 생성자.
     *
     * @param categoryName 검색어
     * @return LegacyCategorySearchCondition
     */
    public static LegacyCategorySearchCondition ofCategoryName(String categoryName) {
        return new LegacyCategorySearchCondition(null, null, categoryName, null);
    }

    /**
     * No-Offset 페이징 여부.
     *
     * @return lastCategoryId가 있으면 true
     */
    public boolean isNoOffsetFetch() {
        return lastCategoryId != null;
    }

    /**
     * 검색어 존재 여부.
     *
     * @return categoryName이 있으면 true
     */
    public boolean hasCategoryName() {
        return categoryName != null && !categoryName.isBlank();
    }
}
