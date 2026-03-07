package com.ryuqq.setof.domain.refundaccount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 환불 계좌 도메인 예외. */
public class RefundAccountException extends DomainException {

    public RefundAccountException(RefundAccountErrorCode errorCode) {
        super(errorCode);
    }

    public RefundAccountException(RefundAccountErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public RefundAccountException(RefundAccountErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
