package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.math.BigDecimal;
import java.util.Map;

/** 할인율이 유효하지 않을 때 발생하는 예외 */
public class InvalidDiscountRateException extends DomainException {

    public InvalidDiscountRateException(BigDecimal value, String reason) {
        super(
                DiscountPolicyErrorCode.INVALID_DISCOUNT_RATE,
                String.format("할인율이 올바르지 않습니다. %s 입력값: %s", reason, value),
                Map.of("value", value != null ? value.toString() : "null", "reason", reason));
    }
}
