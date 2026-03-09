package com.ryuqq.setof.application.discount.port.out.command;

import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;

/**
 * 할인 정책 저장 포트.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface DiscountPolicyCommandPort {

    /**
     * 할인 정책 저장.
     *
     * @param discountPolicy 저장할 할인 정책
     * @return 저장된 정책 ID
     */
    long persist(DiscountPolicy discountPolicy);
}
