package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 기본 반품 정책 필수 예외
 *
 * <p>기본 반품 정책이 반드시 1개 존재해야 하는 규칙을 위반한 경우 발생합니다.
 */
public class DefaultRefundPolicyRequiredException extends DomainException {

    public DefaultRefundPolicyRequiredException(Long sellerId) {
        super(
                RefundPolicyErrorCode.DEFAULT_REFUND_POLICY_REQUIRED,
                String.format("기본 반품 정책이 반드시 1개 존재해야 합니다. sellerId: %d", sellerId),
                Map.of("sellerId", sellerId));
    }
}
