package com.connectly.partnerAdmin.module.product.exception;

import com.connectly.partnerAdmin.module.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class ProductException extends ApplicationException {

    protected ProductException(String errorCode, HttpStatus httpStatus) {
        super(errorCode, httpStatus, errorCode);
    }

    protected ProductException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }

    protected ProductException(String errorCode, HttpStatus httpStatus, String message, BindingResult errors) {
        super(errorCode, httpStatus, message, errors);
    }

}

