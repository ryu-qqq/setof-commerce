package com.ryuqq.setof.application.cart.dto.response;

/**
 * 장바구니 가격 정보 결과 DTO.
 *
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param salePrice 할인가
 * @param directDiscountRate 직접 할인율
 * @param directDiscountPrice 직접 할인가
 * @param discountRate 전체 할인율
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartPriceResult(
        int regularPrice,
        int currentPrice,
        int salePrice,
        int directDiscountRate,
        int directDiscountPrice,
        int discountRate) {

    public static CartPriceResult of(int regularPrice, int currentPrice, int salePrice) {
        return new CartPriceResult(regularPrice, currentPrice, salePrice, 0, 0, 0);
    }

    public static CartPriceResult of(
            int regularPrice,
            int currentPrice,
            int salePrice,
            int directDiscountRate,
            int directDiscountPrice,
            int discountRate) {
        return new CartPriceResult(
                regularPrice,
                currentPrice,
                salePrice,
                directDiscountRate,
                directDiscountPrice,
                discountRate);
    }
}
