package com.ryuqq.setof.application.discount.manager.command;

import com.ryuqq.setof.application.discount.port.out.command.DiscountPolicyPersistencePort;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import com.ryuqq.setof.domain.discount.vo.DiscountPolicyId;
import org.springframework.stereotype.Component;

/**
 * 할인 정책 Persistence Manager
 *
 * <p>Port-Out과 Domain 사이의 영속성 관련 로직을 조율
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountPolicyPersistenceManager {

    private final DiscountPolicyPersistencePort discountPolicyPersistencePort;

    public DiscountPolicyPersistenceManager(
            DiscountPolicyPersistencePort discountPolicyPersistencePort) {
        this.discountPolicyPersistencePort = discountPolicyPersistencePort;
    }

    /**
     * 할인 정책 저장 (신규 생성 또는 수정)
     *
     * @param discountPolicy 저장할 할인 정책
     * @return 저장된 할인 정책 ID
     */
    public DiscountPolicyId persist(DiscountPolicy discountPolicy) {
        return discountPolicyPersistencePort.persist(discountPolicy);
    }
}
