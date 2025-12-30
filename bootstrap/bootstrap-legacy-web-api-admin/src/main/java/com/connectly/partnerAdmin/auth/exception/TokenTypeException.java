package com.connectly.partnerAdmin.auth.exception;

import com.connectly.partnerAdmin.auth.enums.AuthErrorCode;


public class TokenTypeException extends AuthException {

    public TokenTypeException() {
        super(AuthErrorCode.AUTH_INVALID_TOKEN_TYPE.getCode(), AuthErrorCode.AUTH_INVALID_TOKEN_TYPE.getHttpStatus(), AuthErrorConstant.INVALID_TOKEN_TYPE);
    }
}
