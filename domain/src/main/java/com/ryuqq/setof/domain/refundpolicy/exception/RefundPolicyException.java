package com.ryuqq.setof.domain.refundpolicy.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 환불 정책 도메인 예외. */
public class RefundPolicyException extends DomainException {

    public RefundPolicyException(RefundPolicyErrorCode errorCode) {
        super(errorCode);
    }

    public RefundPolicyException(RefundPolicyErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public RefundPolicyException(RefundPolicyErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
