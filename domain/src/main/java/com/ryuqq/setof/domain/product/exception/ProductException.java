package com.ryuqq.setof.domain.product.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 상품(SKU) 도메인 예외. */
public class ProductException extends DomainException {

    public ProductException(ProductErrorCode errorCode) {
        super(errorCode);
    }

    public ProductException(ProductErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ProductException(ProductErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
