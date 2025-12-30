package com.connectly.partnerAdmin.module.order.exception;

import com.connectly.partnerAdmin.module.exception.HasTitleApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class OrderException extends HasTitleApplicationException {


    protected OrderException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message, OrderErrorConstant.TITLE);
    }

    protected OrderException(String errorCode, HttpStatus httpStatus, String message, BindingResult errors, String title) {
        super(errorCode, httpStatus, message, errors, title);
    }
}
