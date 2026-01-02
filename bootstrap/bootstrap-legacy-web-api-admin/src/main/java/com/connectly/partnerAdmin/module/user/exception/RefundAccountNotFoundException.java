package com.connectly.partnerAdmin.module.user.exception;

import com.connectly.partnerAdmin.module.user.enums.UserErrorCode;

public class RefundAccountNotFoundException extends UserException{

    public RefundAccountNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND.getCode(), UserErrorCode.USER_NOT_FOUND.getHttpStatus(), UserErrorConstant.REFUND_ACCOUNT_ADDRESS_NOT_FOUND_MSG);
    }

}
