package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 배송 정책 ID 예외
 *
 * <p>배송 정책 ID가 null이거나 0 이하인 경우 발생합니다.
 */
public class InvalidShippingPolicyIdException extends DomainException {

    public InvalidShippingPolicyIdException(Long value) {
        super(
                ShippingPolicyErrorCode.INVALID_SHIPPING_POLICY_ID,
                String.format("배송 정책 ID는 null이 아닌 양수여야 합니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
