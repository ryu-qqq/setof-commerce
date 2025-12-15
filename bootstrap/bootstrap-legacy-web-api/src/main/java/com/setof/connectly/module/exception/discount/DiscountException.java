package com.setof.connectly.module.exception.discount;

import com.setof.connectly.module.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class DiscountException extends ApplicationException {

    protected DiscountException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
