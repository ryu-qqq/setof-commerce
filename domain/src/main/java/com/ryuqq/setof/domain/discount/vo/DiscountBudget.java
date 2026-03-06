package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.common.vo.Money;

/**
 * 할인 예산 Value Object.
 *
 * <p>정책별 총 예산과 사용된 예산을 관리합니다. usedBudget ≤ totalBudget 불변식을 보장합니다.
 *
 * @param totalBudget 총 예산
 * @param usedBudget 사용된 예산
 */
public record DiscountBudget(Money totalBudget, Money usedBudget) {

    public DiscountBudget {
        if (totalBudget == null) {
            throw new IllegalArgumentException("총 예산은 필수입니다");
        }
        if (usedBudget == null) {
            usedBudget = Money.zero();
        }
        if (usedBudget.isGreaterThan(totalBudget)) {
            throw new IllegalArgumentException("사용 예산이 총 예산을 초과할 수 없습니다");
        }
    }

    public static DiscountBudget of(Money totalBudget) {
        return new DiscountBudget(totalBudget, Money.zero());
    }

    public static DiscountBudget of(Money totalBudget, Money usedBudget) {
        return new DiscountBudget(totalBudget, usedBudget);
    }

    /** 예산 소진 후 새 DiscountBudget 반환 */
    public DiscountBudget consume(Money amount) {
        Money newUsed = usedBudget.add(amount);
        if (newUsed.isGreaterThan(totalBudget)) {
            throw new IllegalArgumentException("예산이 부족합니다");
        }
        return new DiscountBudget(totalBudget, newUsed);
    }

    /** 잔여 예산 */
    public Money remaining() {
        return totalBudget.subtract(usedBudget);
    }

    /** 주어진 금액만큼 예산이 남아있는지 확인 */
    public boolean hasSufficient(Money amount) {
        return remaining().isGreaterThanOrEqual(amount);
    }

    /** 예산이 소진되었는지 확인 */
    public boolean isExhausted() {
        return usedBudget.isGreaterThanOrEqual(totalBudget);
    }
}
