package com.ryuqq.setof.domain.claim.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 클레임 배송 도메인 예외. */
public class ClaimShipmentException extends DomainException {

    public ClaimShipmentException(ClaimShipmentErrorCode errorCode) {
        super(errorCode);
    }

    public ClaimShipmentException(ClaimShipmentErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ClaimShipmentException(ClaimShipmentErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
