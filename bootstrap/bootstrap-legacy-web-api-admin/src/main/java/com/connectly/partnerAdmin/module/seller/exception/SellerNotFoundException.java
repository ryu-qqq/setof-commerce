package com.connectly.partnerAdmin.module.seller.exception;

import com.connectly.partnerAdmin.module.seller.enums.SellerErrorCode;

public class SellerNotFoundException extends SellerException {

    public SellerNotFoundException() {
        super(SellerErrorCode.SELLER_NOT_FOUND.getCode(), SellerErrorCode.SELLER_NOT_FOUND.getHttpStatus());
    }
}
