package com.connectly.partnerAdmin.auth.exception;

import com.connectly.partnerAdmin.module.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AuthException extends ApplicationException {

    protected AuthException(String errorCode, HttpStatus httpStatus) {
        super(errorCode, httpStatus, errorCode);
    }

    protected AuthException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }

}
