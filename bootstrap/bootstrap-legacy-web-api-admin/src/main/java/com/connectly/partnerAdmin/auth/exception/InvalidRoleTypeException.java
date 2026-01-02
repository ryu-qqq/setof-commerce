package com.connectly.partnerAdmin.auth.exception;


import com.connectly.partnerAdmin.auth.enums.AuthErrorCode;

public class InvalidRoleTypeException extends AuthException {

    public InvalidRoleTypeException() {
        super(AuthErrorCode.AUTH_INVALID_ROLE_TYPE.getCode(), AuthErrorCode.AUTH_INVALID_ROLE_TYPE.getHttpStatus(), AuthErrorConstant.INVALID_ROLE_TYPE);
    }

}
