package com.ryuqq.setof.storage.legacy.composite.web.brand.dto;

/**
 * LegacyWebBrandQueryDto - 레거시 Web 브랜드 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명 (영문 코드)
 * @param korBrandName 브랜드명 (한글)
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebBrandQueryDto(
        long brandId, String brandName, String korBrandName, String brandIconImageUrl) {}
