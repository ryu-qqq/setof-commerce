package com.ryuqq.setof.application.shippingpolicy.manager.command;

import com.ryuqq.setof.application.shippingpolicy.port.out.command.ShippingPolicyPersistencePort;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 배송 정책 Persistence Manager
 *
 * <p>Transaction 경계를 관리하며, Port-Out을 통해 영속성 계층에 접근
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ShippingPolicyPersistenceManager {

    private final ShippingPolicyPersistencePort shippingPolicyPersistencePort;

    public ShippingPolicyPersistenceManager(
            ShippingPolicyPersistencePort shippingPolicyPersistencePort) {
        this.shippingPolicyPersistencePort = shippingPolicyPersistencePort;
    }

    /**
     * 배송 정책 저장
     *
     * @param shippingPolicy 배송 정책 도메인
     * @return 저장된 배송 정책 ID
     */
    @Transactional
    public ShippingPolicyId persist(ShippingPolicy shippingPolicy) {
        return shippingPolicyPersistencePort.persist(shippingPolicy);
    }
}
