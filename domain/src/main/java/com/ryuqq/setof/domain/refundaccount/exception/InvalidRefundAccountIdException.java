package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 유효하지 않은 환불계좌 ID 예외
 *
 * <p>환불계좌 ID가 null이거나 0 이하인 경우 발생합니다.
 */
public class InvalidRefundAccountIdException extends DomainException {

    public InvalidRefundAccountIdException(Long refundAccountId) {
        super(
                RefundAccountErrorCode.INVALID_REFUND_ACCOUNT_ID,
                String.format("유효하지 않은 환불계좌 ID: %s", refundAccountId),
                Map.of("refundAccountId", refundAccountId != null ? refundAccountId : "null"));
    }
}
