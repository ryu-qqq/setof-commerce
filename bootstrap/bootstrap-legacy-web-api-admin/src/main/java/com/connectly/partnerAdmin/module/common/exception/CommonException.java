package com.connectly.partnerAdmin.module.common.exception;

import com.connectly.partnerAdmin.module.exception.HasTitleApplicationException;
import org.springframework.http.HttpStatus;

public class CommonException extends HasTitleApplicationException {

    public static final String TITLE = "앗! 잠깐만요";

    protected CommonException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message, TITLE);
    }
}
