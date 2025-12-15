package com.setof.connectly.module.exception.user;

import com.setof.connectly.module.exception.CheckedApplicationException;
import org.springframework.http.HttpStatus;

public class UserException extends CheckedApplicationException {

    public static final String TITLE = "앗! 요청 정보가 잘못됐어요";

    protected UserException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message, TITLE);
    }

    protected UserException(String errorCode, HttpStatus httpStatus, String message, String title) {
        super(errorCode, httpStatus, message, title);
    }
}
