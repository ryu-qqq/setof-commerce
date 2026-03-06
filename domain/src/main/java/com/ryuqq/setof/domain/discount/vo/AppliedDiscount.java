package com.ryuqq.setof.domain.discount.vo;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.id.DiscountPolicyId;

/**
 * 개별 할인 적용 내역 Value Object.
 *
 * <p>어떤 정책이 얼마의 할인을 제공했는지 기록합니다.
 *
 * @param discountPolicyId 적용된 할인 정책 ID
 * @param stackingGroup 스태킹 그룹
 * @param amount 할인 금액
 * @param shareRatio 전체 할인 중 이 정책의 비율 (0.0~1.0)
 */
public record AppliedDiscount(
        DiscountPolicyId discountPolicyId,
        StackingGroup stackingGroup,
        Money amount,
        double shareRatio) {

    public AppliedDiscount {
        if (discountPolicyId == null) {
            throw new IllegalArgumentException("할인 정책 ID는 필수입니다");
        }
        if (stackingGroup == null) {
            throw new IllegalArgumentException("스태킹 그룹은 필수입니다");
        }
        if (amount == null) {
            throw new IllegalArgumentException("할인 금액은 필수입니다");
        }
        if (shareRatio < 0.0 || shareRatio > 1.0) {
            throw new IllegalArgumentException("할인 비율은 0.0~1.0 사이여야 합니다: " + shareRatio);
        }
    }

    public static AppliedDiscount of(
            DiscountPolicyId discountPolicyId,
            StackingGroup stackingGroup,
            Money amount,
            double shareRatio) {
        return new AppliedDiscount(discountPolicyId, stackingGroup, amount, shareRatio);
    }
}
