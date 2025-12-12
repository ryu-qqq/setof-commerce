package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 반품 정책 소유권 없음 예외
 *
 * <p>다른 셀러의 반품 정책에 접근하려는 경우 발생합니다.
 */
public class RefundPolicyNotOwnerException extends DomainException {

    public RefundPolicyNotOwnerException(Long refundPolicyId, Long requestSellerId) {
        super(
                RefundPolicyErrorCode.NOT_OWNER,
                String.format(
                        "해당 반품 정책에 대한 권한이 없습니다. refundPolicyId: %d, sellerId: %d",
                        refundPolicyId, requestSellerId),
                Map.of("refundPolicyId", refundPolicyId, "requestSellerId", requestSellerId));
    }
}
