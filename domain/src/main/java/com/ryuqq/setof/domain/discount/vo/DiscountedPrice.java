package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.common.vo.Money;
import java.util.Collections;
import java.util.List;

/**
 * 할인 계산 결과 Value Object.
 *
 * <p>할인이 적용된 최종 가격과 적용된 할인 내역을 포함합니다.
 *
 * @param salePrice 할인 적용 후 가격
 * @param totalDiscountRate 전체 할인율 (정가 대비, 0~100)
 * @param appliedDiscounts 적용된 할인 내역 목록
 */
public record DiscountedPrice(
        Money salePrice, int totalDiscountRate, List<AppliedDiscount> appliedDiscounts) {

    public DiscountedPrice {
        if (salePrice == null) {
            throw new IllegalArgumentException("할인 가격은 필수입니다");
        }
        if (totalDiscountRate < 0 || totalDiscountRate > 100) {
            throw new IllegalArgumentException("할인율은 0~100 사이여야 합니다: " + totalDiscountRate);
        }
        appliedDiscounts =
                appliedDiscounts != null
                        ? Collections.unmodifiableList(appliedDiscounts)
                        : Collections.emptyList();
    }

    public static DiscountedPrice of(
            Money salePrice, int totalDiscountRate, List<AppliedDiscount> appliedDiscounts) {
        return new DiscountedPrice(salePrice, totalDiscountRate, appliedDiscounts);
    }

    /** 할인이 적용되지 않은 결과 생성 */
    public static DiscountedPrice noDiscount(Money originalPrice) {
        return new DiscountedPrice(originalPrice, 0, Collections.emptyList());
    }

    /** 할인이 적용되었는지 확인 */
    public boolean hasDiscount() {
        return !appliedDiscounts.isEmpty();
    }

    /** 총 할인 금액 */
    public Money totalDiscountAmount() {
        return appliedDiscounts.stream()
                .map(AppliedDiscount::amount)
                .reduce(Money.zero(), Money::add);
    }
}
