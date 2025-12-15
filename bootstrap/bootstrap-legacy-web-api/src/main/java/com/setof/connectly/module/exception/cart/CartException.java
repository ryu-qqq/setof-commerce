package com.setof.connectly.module.exception.cart;

import com.setof.connectly.module.exception.CheckedApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class CartException extends CheckedApplicationException {

    public static final String TITLE = "";

    protected CartException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message, TITLE);
    }

    protected CartException(String errorCode, HttpStatus httpStatus, String message, String title) {
        super(errorCode, httpStatus, message, title);
    }

    protected CartException(
            String errorCode,
            HttpStatus httpStatus,
            String message,
            BindingResult errors,
            String title) {
        super(errorCode, httpStatus, message, errors, title);
    }
}
