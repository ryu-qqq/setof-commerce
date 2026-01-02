package com.connectly.partnerAdmin.module.product.exception;

import com.connectly.partnerAdmin.module.product.enums.ProductErrorCode;

public class ProductNotFoundException extends ProductException {

    public ProductNotFoundException(String message) {
        super(ProductErrorCode.PRODUCT_GROUP_NOT_FOUND.getCode(), ProductErrorCode.PRODUCT_GROUP_NOT_FOUND.getHttpStatus(), message);
    }
}
