package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 무료 배송 기준 금액 예외
 *
 * <p>무료 배송 기준 금액이 0 이하인 경우 발생합니다.
 */
public class InvalidFreeShippingThresholdException extends DomainException {

    public InvalidFreeShippingThresholdException(Integer value, String reason) {
        super(
                ShippingPolicyErrorCode.INVALID_FREE_SHIPPING_THRESHOLD,
                String.format("무료 배송 기준 금액이 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
