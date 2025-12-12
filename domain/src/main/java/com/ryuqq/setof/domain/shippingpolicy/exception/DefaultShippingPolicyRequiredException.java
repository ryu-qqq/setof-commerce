package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 기본 배송 정책 필수 예외
 *
 * <p>기본 배송 정책이 반드시 1개 존재해야 하는 규칙을 위반한 경우 발생합니다.
 */
public class DefaultShippingPolicyRequiredException extends DomainException {

    public DefaultShippingPolicyRequiredException(Long sellerId) {
        super(
                ShippingPolicyErrorCode.DEFAULT_SHIPPING_POLICY_REQUIRED,
                String.format("기본 배송 정책이 반드시 1개 존재해야 합니다. sellerId: %d", sellerId),
                Map.of("sellerId", sellerId));
    }
}
