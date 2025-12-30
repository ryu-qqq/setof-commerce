package com.connectly.partnerAdmin.module.notification.exception;

import com.connectly.partnerAdmin.module.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NotificationException extends ApplicationException {

    protected NotificationException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
