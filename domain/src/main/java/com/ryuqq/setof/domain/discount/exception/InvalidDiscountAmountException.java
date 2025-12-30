package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/** 할인 금액이 유효하지 않을 때 발생하는 예외 */
public class InvalidDiscountAmountException extends DomainException {

    public InvalidDiscountAmountException(Long value, String reason) {
        super(
                DiscountPolicyErrorCode.INVALID_DISCOUNT_AMOUNT,
                String.format("할인 금액이 올바르지 않습니다. %s 입력값: %s", reason, value),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
