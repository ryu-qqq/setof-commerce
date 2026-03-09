package com.ryuqq.setof.storage.legacy.composite.content.dto;

/**
 * LegacyWebBrandComponentItemQueryDto - 브랜드 컴포넌트 아이템 조회 DTO.
 *
 * @param brandComponentItemId 브랜드 컴포넌트 아이템 ID
 * @param brandComponentId 브랜드 컴포넌트 ID
 * @param brandId 브랜드 ID
 * @param categoryId 카테고리 ID
 * @param brandName 브랜드 이름
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebBrandComponentItemQueryDto(
        long brandComponentItemId,
        long brandComponentId,
        long brandId,
        long categoryId,
        String brandName) {}
