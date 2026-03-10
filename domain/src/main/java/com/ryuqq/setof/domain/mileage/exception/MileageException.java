package com.ryuqq.setof.domain.mileage.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 마일리지 도메인 예외. */
public class MileageException extends DomainException {

    public MileageException(MileageErrorCode errorCode) {
        super(errorCode);
    }

    public MileageException(MileageErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public MileageException(MileageErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
