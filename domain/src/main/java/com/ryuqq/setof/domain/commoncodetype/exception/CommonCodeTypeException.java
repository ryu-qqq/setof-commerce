package com.ryuqq.setof.domain.commoncodetype.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 공통 코드 타입 도메인 예외. */
public class CommonCodeTypeException extends DomainException {

    public CommonCodeTypeException(CommonCodeTypeErrorCode errorCode) {
        super(errorCode);
    }

    public CommonCodeTypeException(CommonCodeTypeErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public CommonCodeTypeException(CommonCodeTypeErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
