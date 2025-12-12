package com.ryuqq.setof.domain.shippingpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 표시 순서 예외
 *
 * <p>표시 순서가 null이거나 음수인 경우 발생합니다.
 */
public class InvalidDisplayOrderException extends DomainException {

    public InvalidDisplayOrderException(Integer value, String reason) {
        super(
                ShippingPolicyErrorCode.INVALID_DISPLAY_ORDER,
                String.format("표시 순서가 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
