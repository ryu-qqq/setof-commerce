package com.setof.connectly.module.exception.notification;

import com.setof.connectly.module.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NotificationException extends ApplicationException {

    protected NotificationException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
