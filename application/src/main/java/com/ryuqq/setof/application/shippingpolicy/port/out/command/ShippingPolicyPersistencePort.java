package com.ryuqq.setof.application.shippingpolicy.port.out.command;

import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import com.ryuqq.setof.domain.shippingpolicy.vo.ShippingPolicyId;

/**
 * 배송 정책 Persistence Port (Port-Out)
 *
 * <p>배송 정책 Aggregate를 저장/수정하는 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface ShippingPolicyPersistencePort {

    /**
     * 배송 정책 저장 또는 수정
     *
     * @param shippingPolicy 배송 정책 도메인
     * @return 저장된 배송 정책 ID
     */
    ShippingPolicyId persist(ShippingPolicy shippingPolicy);
}
