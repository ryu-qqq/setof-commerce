package com.setof.connectly.module.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Getter
public class CheckedApplicationException extends ApplicationException {

    private final String title;

    protected CheckedApplicationException(
            String errorCode, HttpStatus httpStatus, String message, String title) {
        super(errorCode, httpStatus, message);
        this.title = title;
    }

    protected CheckedApplicationException(
            String errorCode,
            HttpStatus httpStatus,
            String message,
            BindingResult errors,
            String title) {
        super(errorCode, httpStatus, message, errors);
        this.title = title;
    }
}
