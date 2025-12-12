package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 반품 정책 미존재 예외
 *
 * <p>요청한 반품 정책 ID에 해당하는 정책이 존재하지 않을 때 발생합니다.
 */
public class RefundPolicyNotFoundException extends DomainException {

    public RefundPolicyNotFoundException(Long policyId) {
        super(
                RefundPolicyErrorCode.REFUND_POLICY_NOT_FOUND,
                String.format("반품 정책을 찾을 수 없습니다. policyId: %d", policyId),
                Map.of("policyId", policyId != null ? policyId : "null"));
    }
}
