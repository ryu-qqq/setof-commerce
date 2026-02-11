package com.ryuqq.setof.application.legacy.brand.dto.response;

/**
 * LegacyBrandResult - 레거시 브랜드 조회 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명 (영문 코드)
 * @param korBrandName 브랜드명 (한글)
 * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyBrandResult(
        long brandId, String brandName, String korBrandName, String brandIconImageUrl) {

    /**
     * 팩토리 메서드.
     *
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명 (영문 코드)
     * @param korBrandName 브랜드명 (한글)
     * @param brandIconImageUrl 브랜드 아이콘 이미지 URL
     * @return LegacyBrandResult
     */
    public static LegacyBrandResult of(
            long brandId, String brandName, String korBrandName, String brandIconImageUrl) {
        return new LegacyBrandResult(brandId, brandName, korBrandName, brandIconImageUrl);
    }
}
