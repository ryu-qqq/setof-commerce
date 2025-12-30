package com.connectly.partnerAdmin.module.discount.exception;

import com.connectly.partnerAdmin.module.discount.enums.DiscountErrorCode;

public class DiscountNotFoundException extends DiscountException{

    public DiscountNotFoundException() {
        super(DiscountErrorCode.DISCOUNT_NOT_FOUND.getCode(), DiscountErrorCode.DISCOUNT_NOT_FOUND.getHttpStatus(), DiscountErrorConstants.DISCOUNT_NOT_FOUND_MSG);
    }

}
