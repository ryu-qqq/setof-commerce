package com.ryuqq.setof.application.cart.dto.response;

import java.util.Set;

/**
 * 장바구니 조회 결과 DTO.
 *
 * @param cartId 장바구니 ID
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param sellerId 판매자 ID
 * @param sellerName 판매자명
 * @param productId 상품(SKU) ID
 * @param price 가격 정보
 * @param quantity 장바구니 수량
 * @param stockQuantity 재고 수량
 * @param optionValue 옵션 값 (조합된 문자열)
 * @param options 옵션 목록
 * @param imageUrl 대표 이미지 URL
 * @param productStatus 상품 상태 (ON_SALE, SOLD_OUT 등)
 * @param categoryPath 카테고리 경로
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartItemResult(
        long cartId,
        long brandId,
        String brandName,
        long productGroupId,
        String productGroupName,
        long sellerId,
        String sellerName,
        long productId,
        CartPriceResult price,
        int quantity,
        int stockQuantity,
        String optionValue,
        Set<CartOptionResult> options,
        String imageUrl,
        String productStatus,
        String categoryPath) {

    public static CartItemResult of(
            long cartId,
            long brandId,
            String brandName,
            long productGroupId,
            String productGroupName,
            long sellerId,
            String sellerName,
            long productId,
            CartPriceResult price,
            int quantity,
            int stockQuantity,
            String optionValue,
            Set<CartOptionResult> options,
            String imageUrl,
            String productStatus,
            String categoryPath) {
        return new CartItemResult(
                cartId,
                brandId,
                brandName,
                productGroupId,
                productGroupName,
                sellerId,
                sellerName,
                productId,
                price,
                quantity,
                stockQuantity,
                optionValue,
                options,
                imageUrl,
                productStatus,
                categoryPath);
    }
}
