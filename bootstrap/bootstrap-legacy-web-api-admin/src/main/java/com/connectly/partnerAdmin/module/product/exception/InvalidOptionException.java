package com.connectly.partnerAdmin.module.product.exception;

import com.connectly.partnerAdmin.module.product.enums.ProductErrorCode;

public class InvalidOptionException extends ProductException {

    public InvalidOptionException(String message) {
        super(ProductErrorCode.INVALID_OPTION.getCode(), ProductErrorCode.INVALID_OPTION.getHttpStatus(), message);
    }
}
