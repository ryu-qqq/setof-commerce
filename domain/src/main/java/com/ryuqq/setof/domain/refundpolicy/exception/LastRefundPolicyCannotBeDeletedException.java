package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 마지막 반품 정책 삭제 불가 예외
 *
 * <p>셀러의 마지막 반품 정책은 삭제할 수 없는 규칙을 위반한 경우 발생합니다.
 */
public class LastRefundPolicyCannotBeDeletedException extends DomainException {

    public LastRefundPolicyCannotBeDeletedException(Long sellerId, Long policyId) {
        super(
                RefundPolicyErrorCode.LAST_REFUND_POLICY_CANNOT_BE_DELETED,
                String.format(
                        "마지막 반품 정책은 삭제할 수 없습니다. sellerId: %d, policyId: %d", sellerId, policyId),
                Map.of("sellerId", sellerId, "policyId", policyId));
    }
}
