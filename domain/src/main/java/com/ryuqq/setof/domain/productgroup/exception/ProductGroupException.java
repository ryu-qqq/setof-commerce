package com.ryuqq.setof.domain.productgroup.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;

/** 상품그룹 도메인 예외. */
public class ProductGroupException extends DomainException {

    public ProductGroupException(ProductGroupErrorCode errorCode) {
        super(errorCode);
    }

    public ProductGroupException(ProductGroupErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ProductGroupException(ProductGroupErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
