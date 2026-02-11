package com.ryuqq.setof.application.legacy.cart.dto.response;

/**
 * LegacyCartPriceResult - 레거시 장바구니 가격 정보 결과 DTO.
 *
 * <p>APP-DTO-004: Response DTO는 *Result 네이밍.
 *
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param salePrice 할인가
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyCartPriceResult(int regularPrice, int currentPrice, int salePrice) {

    /**
     * 팩토리 메서드.
     *
     * @param regularPrice 정가
     * @param currentPrice 판매가
     * @param salePrice 할인가
     * @return LegacyCartPriceResult
     */
    public static LegacyCartPriceResult of(int regularPrice, int currentPrice, int salePrice) {
        return new LegacyCartPriceResult(regularPrice, currentPrice, salePrice);
    }
}
