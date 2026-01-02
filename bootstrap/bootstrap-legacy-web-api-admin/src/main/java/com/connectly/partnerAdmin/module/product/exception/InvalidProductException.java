package com.connectly.partnerAdmin.module.product.exception;

import com.connectly.partnerAdmin.module.product.enums.ProductErrorCode;

public class InvalidProductException extends ProductException {

    public InvalidProductException(String message) {
        super(ProductErrorCode.INVALID_PRODUCT_GROUP.getCode(), ProductErrorCode.INVALID_PRODUCT_GROUP.getHttpStatus(), message);
    }

}
