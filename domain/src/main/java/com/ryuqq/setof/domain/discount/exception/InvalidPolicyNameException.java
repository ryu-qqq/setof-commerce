package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/** 정책명이 유효하지 않을 때 발생하는 예외 */
public class InvalidPolicyNameException extends DomainException {

    public InvalidPolicyNameException(String value, String reason) {
        super(
                DiscountPolicyErrorCode.INVALID_POLICY_NAME,
                String.format("정책명이 올바르지 않습니다. %s 입력값: %s", reason, value),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
