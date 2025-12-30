package com.connectly.partnerAdmin.auth.exception;


import com.connectly.partnerAdmin.auth.enums.AuthErrorCode;

public class AuthUserNotFoundException extends AuthException {

    public AuthUserNotFoundException() {
        super(AuthErrorCode.AUTH_USER_NOT_FOUND.getCode(), AuthErrorCode.AUTH_USER_NOT_FOUND.getHttpStatus());
    }

}
