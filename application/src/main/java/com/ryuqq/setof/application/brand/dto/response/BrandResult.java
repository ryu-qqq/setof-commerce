package com.ryuqq.setof.application.brand.dto.response;

/**
 * 브랜드 조회 결과.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명 (영문)
 * @param brandNameKo 브랜드명 (한글)
 * @param brandIconUrl 브랜드 아이콘 URL
 * @param displayed 노출 여부
 * @author ryu-qqq
 * @since 1.0.0
 */
public record BrandResult(
        Long brandId,
        String brandName,
        String brandNameKo,
        String brandIconUrl,
        boolean displayed) {

    public static BrandResult of(
            Long brandId,
            String brandName,
            String brandNameKo,
            String brandIconUrl,
            boolean displayed) {
        return new BrandResult(brandId, brandName, brandNameKo, brandIconUrl, displayed);
    }
}
