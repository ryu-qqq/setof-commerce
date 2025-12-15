package com.setof.connectly.module.exception.order;

import com.setof.connectly.module.exception.CheckedApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public class OrderException extends CheckedApplicationException {

    public static final String TITLE = "앗! 요청 정보가 잘못됐어요";

    protected OrderException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message, TITLE);
    }

    protected OrderException(
            String errorCode,
            HttpStatus httpStatus,
            String message,
            BindingResult errors,
            String title) {
        super(errorCode, httpStatus, message, errors, title);
    }
}
