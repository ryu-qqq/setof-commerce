package com.ryuqq.setof.application.discount.port.out.command;

import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import com.ryuqq.setof.domain.discount.vo.AppliedDiscount;
import java.time.Instant;
import java.util.List;

/**
 * 새 스키마(setof) product_groups 가격 갱신 + 적용 할인 내역 저장 포트.
 *
 * <p>persistence-mysql에서 구현합니다. sale_price, discount_rate, direct_discount_rate,
 * direct_discount_price 4개 필드를 배치 UPDATE하고, 적용 할인 내역을 product_group_applied_discounts에 저장합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ProductGroupPriceCommandPort {

    /**
     * 여러 상품그룹의 가격 정보를 일괄 갱신.
     *
     * @param updates 갱신 대상 목록
     */
    void persistAll(List<ProductGroupPriceUpdateData> updates);

    /**
     * 상품그룹의 적용 할인 내역을 저장.
     *
     * <p>기존 내역을 삭제하고 새 내역으로 교체합니다 (replace 전략).
     *
     * @param productGroupId 상품그룹 ID
     * @param appliedDiscounts 적용 할인 내역 목록
     * @param appliedAt 할인 적용 시각
     */
    void replaceAppliedDiscounts(
            long productGroupId, List<AppliedDiscount> appliedDiscounts, Instant appliedAt);
}
