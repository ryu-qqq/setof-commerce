package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;
import java.util.UUID;

/**
 * 환불계좌 중복 등록 예외
 *
 * <p>회원이 이미 환불계좌를 보유하고 있는 상태에서 새로 등록하려는 경우 발생합니다.
 * 회원당 최대 1개의 환불계좌만 등록 가능합니다.
 */
public class RefundAccountAlreadyExistsException extends DomainException {

    public RefundAccountAlreadyExistsException(UUID memberId) {
        super(
                RefundAccountErrorCode.REFUND_ACCOUNT_ALREADY_EXISTS,
                String.format("이미 등록된 환불계좌가 있습니다. memberId: %s", memberId),
                Map.of("memberId", memberId.toString()));
    }
}
