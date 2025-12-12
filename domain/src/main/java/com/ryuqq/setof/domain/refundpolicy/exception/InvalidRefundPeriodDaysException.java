package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 반품 기간 예외
 *
 * <p>반품 기간이 null이거나 유효하지 않은 경우 발생합니다.
 */
public class InvalidRefundPeriodDaysException extends DomainException {

    public InvalidRefundPeriodDaysException(Integer value, String reason) {
        super(
                RefundPolicyErrorCode.INVALID_REFUND_PERIOD_DAYS,
                String.format("반품 기간이 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
