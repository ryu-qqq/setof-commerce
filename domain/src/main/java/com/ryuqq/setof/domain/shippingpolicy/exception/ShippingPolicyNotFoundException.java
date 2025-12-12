package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 배송 정책 미존재 예외
 *
 * <p>요청한 배송 정책 ID에 해당하는 정책이 존재하지 않을 때 발생합니다.
 */
public class ShippingPolicyNotFoundException extends DomainException {

    public ShippingPolicyNotFoundException(Long policyId) {
        super(
                ShippingPolicyErrorCode.SHIPPING_POLICY_NOT_FOUND,
                String.format("배송 정책을 찾을 수 없습니다. policyId: %d", policyId),
                Map.of("policyId", policyId != null ? policyId : "null"));
    }
}
