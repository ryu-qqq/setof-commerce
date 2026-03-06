package com.ryuqq.setof.domain.productnotice.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/** 상품고시 도메인 예외. */
public class ProductNoticeException extends DomainException {

    public ProductNoticeException(ProductNoticeErrorCode errorCode) {
        super(errorCode);
    }

    public ProductNoticeException(ProductNoticeErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ProductNoticeException(
            ProductNoticeErrorCode errorCode, String customMessage, Map<String, Object> args) {
        super(errorCode, customMessage, args);
    }

    public ProductNoticeException(ProductNoticeErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
