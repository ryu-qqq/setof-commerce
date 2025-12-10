package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import java.util.UUID;

/**
 * 환불계좌 미존재 예외
 *
 * <p>요청한 환불계좌 ID 또는 회원 ID에 해당하는 환불계좌가 존재하지 않을 때 발생합니다.
 */
public class RefundAccountNotFoundException extends DomainException {

    public RefundAccountNotFoundException(Long refundAccountId) {
        super(
                RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND,
                String.format("환불계좌를 찾을 수 없습니다. refundAccountId: %d", refundAccountId),
                Map.of("refundAccountId", refundAccountId));
    }

    public RefundAccountNotFoundException(UUID memberId) {
        super(
                RefundAccountErrorCode.REFUND_ACCOUNT_NOT_FOUND,
                String.format("환불계좌를 찾을 수 없습니다. memberId: %s", memberId),
                Map.of("memberId", memberId.toString()));
    }
}
