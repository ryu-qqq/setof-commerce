package com.connectly.partnerAdmin.auth.exception;

import com.connectly.partnerAdmin.auth.enums.AuthErrorCode;

public class ExpiredJwtTokenException extends AuthException{

    public ExpiredJwtTokenException() {
        super(AuthErrorCode.TOKEN_EXPIRED.getCode(), AuthErrorCode.TOKEN_EXPIRED.getHttpStatus());
    }

}
