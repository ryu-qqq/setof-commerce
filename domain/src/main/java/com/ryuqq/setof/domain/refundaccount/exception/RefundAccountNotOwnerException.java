package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import java.util.UUID;

/**
 * 환불계좌 소유권 없음 예외
 *
 * <p>다른 회원의 환불계좌에 접근하려는 경우 발생합니다.
 */
public class RefundAccountNotOwnerException extends DomainException {

    public RefundAccountNotOwnerException(Long refundAccountId, UUID requestMemberId) {
        super(
                RefundAccountErrorCode.NOT_OWNER,
                String.format("해당 환불계좌에 대한 권한이 없습니다. refundAccountId: %d, memberId: %s",
                        refundAccountId, requestMemberId),
                Map.of("refundAccountId", refundAccountId,
                        "requestMemberId", requestMemberId.toString()));
    }
}
