package com.ryuqq.setof.domain.exchange.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 교환 도메인 예외. */
public class ExchangeException extends DomainException {

    public ExchangeException(ExchangeErrorCode errorCode) {
        super(errorCode);
    }

    public ExchangeException(ExchangeErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ExchangeException(ExchangeErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
