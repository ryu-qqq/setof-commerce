package com.ryuqq.setof.storage.legacy.composite.web.category.dto;

/**
 * LegacyWebTreeCategoryQueryDto - 레거시 Web 트리 카테고리 조회 Projection DTO.
 *
 * <p>트리 구성용 기본 정보만 포함합니다.
 *
 * <p>Projections.constructor()로 매핑.
 *
 * @param categoryId 카테고리 ID
 * @param categoryName 카테고리명
 * @param displayName 표시명
 * @param categoryDepth 카테고리 depth (1=루트)
 * @param parentCategoryId 부모 카테고리 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebTreeCategoryQueryDto(
        long categoryId,
        String categoryName,
        String displayName,
        int categoryDepth,
        long parentCategoryId) {}
