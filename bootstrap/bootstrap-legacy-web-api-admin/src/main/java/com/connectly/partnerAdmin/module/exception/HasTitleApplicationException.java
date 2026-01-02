package com.connectly.partnerAdmin.module.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Getter
public class HasTitleApplicationException extends ApplicationException{

    protected final String title;

    protected HasTitleApplicationException(String errorCode, HttpStatus httpStatus, String message, String title) {
        super(errorCode, httpStatus, message);
        this.title = title;
    }

    protected HasTitleApplicationException(String errorCode, HttpStatus httpStatus, String message, BindingResult errors, String title) {
        super(errorCode, httpStatus, message, errors);
        this.title = title;
    }
}
