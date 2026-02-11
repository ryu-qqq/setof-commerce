package com.ryuqq.setof.application.legacy.category.dto.response;

/**
 * LegacyCategoryResult - 레거시 카테고리 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param displayName 표시명
 * @param categoryDepth 카테고리 depth (1=루트)
 * @param parentCategoryId 부모 카테고리 ID
 * @param categoryFullPath 카테고리 전체 경로
 * @param targetGroup 대상 그룹 (MALE/FEMALE/KIDS/LIFE)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyCategoryResult(
        long categoryId,
        String categoryName,
        String displayName,
        int categoryDepth,
        long parentCategoryId,
        String categoryFullPath,
        String targetGroup) {

    /**
     * 팩토리 메서드.
     *
     * @param categoryId 카테고리 ID
     * @param categoryName 카테고리명
     * @param displayName 표시명
     * @param categoryDepth 카테고리 depth (1=루트)
     * @param parentCategoryId 부모 카테고리 ID
     * @param categoryFullPath 카테고리 전체 경로
     * @param targetGroup 대상 그룹
     * @return LegacyCategoryResult
     */
    public static LegacyCategoryResult of(
            long categoryId,
            String categoryName,
            String displayName,
            int categoryDepth,
            long parentCategoryId,
            String categoryFullPath,
            String targetGroup) {
        return new LegacyCategoryResult(
                categoryId,
                categoryName,
                displayName,
                categoryDepth,
                parentCategoryId,
                categoryFullPath,
                targetGroup);
    }
}
