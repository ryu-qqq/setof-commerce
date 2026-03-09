package com.ryuqq.setof.domain.discount.dto;

/**
 * 상품그룹 가격 갱신 데이터.
 *
 * @param productGroupId 상품그룹 ID
 * @param salePrice 최종 판매가
 * @param discountRate 전체 할인율 (정가 대비)
 * @param directDiscountRate 즉시할인율 (현재가 대비)
 * @param directDiscountPrice 즉시할인가 (현재가 - 판매가)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupPriceUpdateData(
        long productGroupId,
        int salePrice,
        int discountRate,
        int directDiscountRate,
        int directDiscountPrice) {}
