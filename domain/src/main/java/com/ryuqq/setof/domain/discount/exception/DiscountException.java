package com.ryuqq.setof.domain.discount.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 할인 도메인 예외. */
public class DiscountException extends DomainException {

    public DiscountException(DiscountErrorCode errorCode) {
        super(errorCode);
    }

    public DiscountException(DiscountErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public DiscountException(DiscountErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
