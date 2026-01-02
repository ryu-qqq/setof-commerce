package com.connectly.partnerAdmin.module.payment.exception;

import com.connectly.partnerAdmin.module.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class PaymentException extends ApplicationException {
    protected PaymentException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }

    protected PaymentException(String errorCode, HttpStatus httpStatus, String message, BindingResult errors) {
        super(errorCode, httpStatus, message, errors);
    }

}
