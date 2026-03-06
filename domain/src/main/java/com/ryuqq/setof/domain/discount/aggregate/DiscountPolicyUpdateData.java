package com.ryuqq.setof.domain.discount.aggregate;

import com.ryuqq.setof.domain.common.vo.Money;
import com.ryuqq.setof.domain.discount.vo.DiscountBudget;
import com.ryuqq.setof.domain.discount.vo.DiscountMethod;
import com.ryuqq.setof.domain.discount.vo.DiscountPeriod;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyName;
import com.ryuqq.setof.domain.discount.vo.DiscountRate;
import com.ryuqq.setof.domain.discount.vo.Priority;

/**
 * 할인 정책 수정 데이터 Value Object.
 *
 * <p>Aggregate의 update() 메서드에 전달할 수정 데이터를 묶는 역할을 합니다.
 */
public record DiscountPolicyUpdateData(
        DiscountPolicyName name,
        String description,
        DiscountMethod discountMethod,
        DiscountRate discountRate,
        Money discountAmount,
        Money maxDiscountAmount,
        boolean discountCapped,
        Money minimumOrderAmount,
        Priority priority,
        DiscountPeriod period,
        DiscountBudget budget) {

    public static DiscountPolicyUpdateData of(
            DiscountPolicyName name,
            String description,
            DiscountMethod discountMethod,
            DiscountRate discountRate,
            Money discountAmount,
            Money maxDiscountAmount,
            boolean discountCapped,
            Money minimumOrderAmount,
            Priority priority,
            DiscountPeriod period,
            DiscountBudget budget) {
        return new DiscountPolicyUpdateData(
                name,
                description,
                discountMethod,
                discountRate,
                discountAmount,
                maxDiscountAmount,
                discountCapped,
                minimumOrderAmount,
                priority,
                period,
                budget);
    }
}
