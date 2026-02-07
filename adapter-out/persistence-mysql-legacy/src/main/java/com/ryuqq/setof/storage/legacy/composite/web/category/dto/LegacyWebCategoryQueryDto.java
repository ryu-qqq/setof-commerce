package com.ryuqq.setof.storage.legacy.composite.web.category.dto;

/**
 * LegacyWebCategoryQueryDto - 레거시 Web 카테고리 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param displayName 표시명
 * @param categoryDepth 카테고리 depth (1=루트)
 * @param parentCategoryId 부모 카테고리 ID
 * @param path 카테고리 전체 경로
 * @param targetGroup 대상 그룹 (MALE/FEMALE/KIDS/LIFE)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebCategoryQueryDto(
        long categoryId,
        String categoryName,
        String displayName,
        int categoryDepth,
        long parentCategoryId,
        String path,
        String targetGroup) {}
