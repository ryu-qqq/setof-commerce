package com.connectly.partnerAdmin.module.external.exception;

import com.connectly.partnerAdmin.module.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class ExternalServerException extends ApplicationException {

    protected ExternalServerException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }

    protected ExternalServerException(String errorCode, HttpStatus httpStatus, String message, BindingResult errors) {
        super(errorCode, httpStatus, message, errors);
    }
}
