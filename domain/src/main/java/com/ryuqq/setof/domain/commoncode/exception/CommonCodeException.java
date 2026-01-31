package com.ryuqq.setof.domain.commoncode.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 공통 코드 도메인 예외. */
public class CommonCodeException extends DomainException {

    public CommonCodeException(CommonCodeErrorCode errorCode) {
        super(errorCode);
    }

    public CommonCodeException(CommonCodeErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public CommonCodeException(CommonCodeErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
