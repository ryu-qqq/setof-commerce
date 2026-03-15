package com.ryuqq.setof.application.discount.manager;

import com.ryuqq.setof.application.discount.port.out.command.DiscountPolicyCommandPort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import org.springframework.stereotype.Component;

/**
 * 할인 정책 저장 매니저.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyCommandManager {

    private final DiscountPolicyCommandPort policyCommandPort;

    public DiscountPolicyCommandManager(DiscountPolicyCommandPort policyCommandPort) {
        this.policyCommandPort = policyCommandPort;
    }

    /**
     * 할인 정책 저장.
     *
     * @param policy 저장할 할인 정책
     * @return 저장된 정책 ID
     */
    public long persist(DiscountPolicy policy) {
        return policyCommandPort.persist(policy);
    }
}
