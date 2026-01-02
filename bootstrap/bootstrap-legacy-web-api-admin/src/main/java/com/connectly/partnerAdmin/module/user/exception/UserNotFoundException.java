package com.connectly.partnerAdmin.module.user.exception;

import com.connectly.partnerAdmin.module.user.enums.UserErrorCode;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND.getCode(), UserErrorCode.USER_NOT_FOUND.getHttpStatus(), UserErrorConstant.USER_NOT_FOUND_MSG);
    }
}