package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 배송비 예외
 *
 * <p>배송비가 null이거나 음수인 경우 발생합니다.
 */
public class InvalidDeliveryCostException extends DomainException {

    public InvalidDeliveryCostException(Integer value, String reason) {
        super(
                ShippingPolicyErrorCode.INVALID_DELIVERY_COST,
                String.format("배송비가 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
