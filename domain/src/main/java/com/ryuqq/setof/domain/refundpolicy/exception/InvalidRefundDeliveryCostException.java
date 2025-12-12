package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 반품 배송비 예외
 *
 * <p>반품 배송비가 null이거나 음수인 경우 발생합니다.
 */
public class InvalidRefundDeliveryCostException extends DomainException {

    public InvalidRefundDeliveryCostException(Integer value, String reason) {
        super(
                RefundPolicyErrorCode.INVALID_REFUND_DELIVERY_COST,
                String.format("반품 배송비가 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
