package com.ryuqq.setof.domain.productdescription.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/** 상품그룹 상세설명 도메인 예외. */
public class ProductDescriptionException extends DomainException {

    public ProductDescriptionException(ProductDescriptionErrorCode errorCode) {
        super(errorCode);
    }

    public ProductDescriptionException(
            ProductDescriptionErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ProductDescriptionException(
            ProductDescriptionErrorCode errorCode, String customMessage, Map<String, Object> args) {
        super(errorCode, customMessage, args);
    }

    public ProductDescriptionException(ProductDescriptionErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
