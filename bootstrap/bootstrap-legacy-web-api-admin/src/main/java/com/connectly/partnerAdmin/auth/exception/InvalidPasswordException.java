package com.connectly.partnerAdmin.auth.exception;


import com.connectly.partnerAdmin.auth.enums.AuthErrorCode;

public class InvalidPasswordException extends AuthException {

    public InvalidPasswordException() {
        super(AuthErrorCode.AUTH_INVALID_PASSWORD.getCode(), AuthErrorCode.AUTH_INVALID_PASSWORD.getHttpStatus(), AuthErrorConstant.INVALID_PASSWORD);
    }

}
