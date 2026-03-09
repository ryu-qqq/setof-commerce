package com.ryuqq.setof.storage.legacy.productgroup.dto;

/**
 * 상품그룹 가격 업데이트 행.
 *
 * @param productGroupId 상품그룹 ID
 * @param salePrice 최종 판매가
 * @param discountRate 전체 할인율
 * @param directDiscountRate 즉시할인율
 * @param directDiscountPrice 즉시할인가
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyProductGroupPriceUpdateRow(
        long productGroupId,
        int salePrice,
        int discountRate,
        int directDiscountRate,
        int directDiscountPrice) {}
