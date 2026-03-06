package com.ryuqq.setof.domain.refund.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 반품 도메인 예외. */
public class RefundException extends DomainException {

    public RefundException(RefundErrorCode errorCode) {
        super(errorCode);
    }

    public RefundException(RefundErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public RefundException(RefundErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
