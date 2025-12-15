package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 정책명 예외
 *
 * <p>정책명이 null이거나 빈 문자열이거나 최대 길이를 초과한 경우 발생합니다.
 */
public class InvalidPolicyNameException extends DomainException {

    public InvalidPolicyNameException(String value, String reason) {
        super(
                RefundPolicyErrorCode.INVALID_POLICY_NAME,
                String.format("정책명이 올바르지 않습니다. value: %s, reason: %s", value, reason),
                Map.of("value", value != null ? value : "null", "reason", reason));
    }
}
