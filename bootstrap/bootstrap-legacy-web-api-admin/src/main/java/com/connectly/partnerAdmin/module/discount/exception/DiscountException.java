package com.connectly.partnerAdmin.module.discount.exception;

import com.connectly.partnerAdmin.module.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class DiscountException extends ApplicationException {

    protected DiscountException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
