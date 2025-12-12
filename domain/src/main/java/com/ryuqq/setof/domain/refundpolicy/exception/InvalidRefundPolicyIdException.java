package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 잘못된 반품 정책 ID 예외
 *
 * <p>반품 정책 ID가 null이거나 0 이하인 경우 발생합니다.
 */
public class InvalidRefundPolicyIdException extends DomainException {

    public InvalidRefundPolicyIdException(Long value) {
        super(
                RefundPolicyErrorCode.INVALID_REFUND_POLICY_ID,
                String.format("반품 정책 ID는 null이 아닌 양수여야 합니다. value: %s", value),
                Map.of("value", value != null ? value : "null"));
    }
}
