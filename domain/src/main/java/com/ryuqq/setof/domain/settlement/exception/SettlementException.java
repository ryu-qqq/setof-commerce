package com.ryuqq.setof.domain.settlement.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 정산 도메인 예외. */
public class SettlementException extends DomainException {

    public SettlementException(SettlementErrorCode errorCode) {
        super(errorCode);
    }

    public SettlementException(SettlementErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public SettlementException(SettlementErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
