package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebCategoryComponentQueryDto - 카테고리 컴포넌트 조회 DTO.
 *
 * @param categoryComponentId 카테고리 컴포넌트 ID
 * @param componentId 컴포넌트 ID
 * @param categoryId 카테고리 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebCategoryComponentQueryDto(
        long categoryComponentId, long componentId, long categoryId) {}
