package com.setof.connectly.module.exception.event;

import com.setof.connectly.module.exception.CheckedApplicationException;
import org.springframework.http.HttpStatus;

public class EventException extends CheckedApplicationException {

    public static final String TITLE = "";

    protected EventException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message, TITLE);
    }

    protected EventException(
            String errorCode, HttpStatus httpStatus, String message, String title) {
        super(errorCode, httpStatus, message, title);
    }
}
