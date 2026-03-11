package com.ryuqq.setof.domain.discount.dto;

import com.ryuqq.setof.domain.discount.vo.AppliedDiscount;
import java.util.List;

/**
 * 상품그룹 가격 갱신 데이터.
 *
 * @param productGroupId 상품그룹 ID
 * @param salePrice 최종 판매가
 * @param discountRate 전체 할인율 (정가 대비)
 * @param directDiscountRate 즉시할인율 (현재가 대비)
 * @param directDiscountPrice 즉시할인가 (현재가 - 판매가)
 * @param appliedDiscounts 적용된 할인 내역 목록 (정산/감사용)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ProductGroupPriceUpdateData(
        long productGroupId,
        int salePrice,
        int discountRate,
        int directDiscountRate,
        int directDiscountPrice,
        List<AppliedDiscount> appliedDiscounts) {

    public ProductGroupPriceUpdateData {
        if (appliedDiscounts == null) {
            appliedDiscounts = List.of();
        }
    }
}
