package com.ryuqq.setof.domain.navigation.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 네비게이션 도메인 예외. */
public class NavigationException extends DomainException {

    public NavigationException(NavigationErrorCode errorCode) {
        super(errorCode);
    }

    public NavigationException(NavigationErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public NavigationException(NavigationErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
