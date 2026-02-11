package com.ryuqq.setof.application.legacy.order.dto.response;

/**
 * LegacyBrandResult - 레거시 브랜드 결과 DTO (주문 내 중첩 객체).
 *
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyBrandResult(long brandId, String brandName) {

    /**
     * 정적 팩토리 메서드.
     *
     * @param brandId 브랜드 ID
     * @param brandName 브랜드명
     * @return LegacyBrandResult
     */
    public static LegacyBrandResult of(long brandId, String brandName) {
        return new LegacyBrandResult(brandId, brandName);
    }
}
