package com.ryuqq.setof.domain.displaycomponent.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/**
 * DisplayComponentException - 전시 컴포넌트 도메인 예외.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class DisplayComponentException extends DomainException {

    public DisplayComponentException(DisplayComponentErrorCode errorCode) {
        super(errorCode);
    }

    public DisplayComponentException(DisplayComponentErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public DisplayComponentException(DisplayComponentErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
