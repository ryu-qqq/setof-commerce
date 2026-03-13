package com.ryuqq.setof.application.brand.dto.response;

/**
 * 브랜드 노출용 조회 결과.
 *
 * <p>Public API용 간단한 응답 결과입니다.
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param brandNameKo 브랜드 한글명
 * @param brandNameEn 브랜드 영문명
 * @param brandIconUrl 브랜드 아이콘 URL
 * @author ryu-qqq
 * @since 1.0.0
 */
public record BrandDisplayResult(
        Long brandId,
        String brandName,
        String brandNameKo,
        String brandNameEn,
        String brandIconUrl) {

    public static BrandDisplayResult of(
            Long brandId,
            String brandName,
            String brandNameKo,
            String brandNameEn,
            String brandIconUrl) {
        return new BrandDisplayResult(brandId, brandName, brandNameKo, brandNameEn, brandIconUrl);
    }
}
