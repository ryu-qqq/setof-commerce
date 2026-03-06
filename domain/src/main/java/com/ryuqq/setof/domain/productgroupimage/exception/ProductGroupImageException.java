package com.ryuqq.setof.domain.productgroupimage.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/** 상품그룹 이미지 도메인 예외. */
public class ProductGroupImageException extends DomainException {

    public ProductGroupImageException(ProductGroupImageErrorCode errorCode) {
        super(errorCode);
    }

    public ProductGroupImageException(ProductGroupImageErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ProductGroupImageException(
            ProductGroupImageErrorCode errorCode, String customMessage, Map<String, Object> args) {
        super(errorCode, customMessage, args);
    }

    public ProductGroupImageException(ProductGroupImageErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
