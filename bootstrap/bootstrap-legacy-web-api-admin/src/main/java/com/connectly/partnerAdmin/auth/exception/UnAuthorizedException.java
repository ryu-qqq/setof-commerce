package com.connectly.partnerAdmin.auth.exception;

import com.connectly.partnerAdmin.auth.enums.AuthErrorCode;

public class UnAuthorizedException extends AuthException {

    public UnAuthorizedException() {
        super(AuthErrorCode.AUTH_UNAUTHORIZED_USER.getCode(),
                AuthErrorCode.AUTH_UNAUTHORIZED_USER.getHttpStatus(),
                AuthErrorConstant.UN_AUTHORIZATION_REQUEST);
    }

}
